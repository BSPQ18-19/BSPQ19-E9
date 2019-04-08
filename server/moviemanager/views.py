from logging import getLogger, INFO

from django.http import HttpResponse, JsonResponse, QueryDict
from django.views.decorators.csrf import csrf_exempt
from django.views.decorators.http import require_http_methods

from moviemanager.gateways import omdb_gw
from moviemanager.internals.session import SESSION_HANDLER, gen_token, validate
from . import models
from .utils import check_params, compare_password, hash_password

logger = getLogger(__name__)


@csrf_exempt
@require_http_methods(["GET"])
def login(request):
    """
    login assigns a token to a user if verification is correct
    :param request: GET request containing username and password
    :return: JsonResponse with token if ok, HttpResponse otherwise
    """
    # check that are required parameters are present
    query = QueryDict(request.META.get("QUERY_STRING"))
    params = ["username", "password"]
    error_response = check_params(query, params)
    if error_response:
        return error_response

    # assign parameters to variables
    username = query.get("username")
    password = query.get("password")

    # find user
    try:
        user = models.User.objects.get(username=username)
    except models.User.DoesNotExist:
        # user not found
        return HttpResponse("no user with username {}".format(username),
                            status=403)
    # match passwords
    if not compare_password(password, user.password):
        return HttpResponse("Invalid username or password", status=403)

    # generate a token for the user
    token = gen_token()
    # create a session with the token and the user
    SESSION_HANDLER.add_session(token, user)
    logger.log(INFO, "log user {} with token {}".format(user.__str__(), token))
    # return token to user
    return JsonResponse({"token": token})


@csrf_exempt
@require_http_methods(["GET"])
def movie_details(request, movie_id):
    """        return HttpResponse(status=code, content=result)

    movie returns detailed information about a movie from IMDB
    :param request: GET request
    :param movie_id: imdbID of the movie
    :return: JsonResponse with details if ok, HttpResponse otherwise
    """
    # if movie is stored locally in the DB, return it to the user
    try:
        movie = models.Movie.objects.get(movie_id=movie_id)
        return JsonResponse(movie.json())
    # if it is not in the database, look for it using the OMDB gateways
    except models.Movie.DoesNotExist:
        code, result = omdb_gw.movie_details(movie_id, True)
        if code == 200:
            # store movie in local DB
            movie = omdb_gw.build_movie(models.Movie, result)
            logger.log(INFO, "store movie {}".format(movie.__str__()))
            movie.save()
            # return movie info to the user
            return JsonResponse(movie.json())
        # return error returned by the gateways
        return HttpResponse(status=code, content=result)


@csrf_exempt
@require_http_methods(["GET"])
def search(request):
    """
    search sends a search request to IMDB and returns
    the processes result
    :param request: GET request containing movie title and optionally year
    :return: JsonResponse with movies if ok, HttpResponse otherwise
    """

    query = QueryDict(request.META.get("QUERY_STRING"))
    params = ["title"]
    error_response = check_params(query, params)
    if error_response:
        return error_response

    # delegate movie search to the OMDB gateways
    code, result = omdb_gw.search_movies(**query)
    # movies found
    if code == 200:
        return JsonResponse(result)
    # movies not found
    return HttpResponse(status=code, content=result)


@csrf_exempt
@require_http_methods(["POST"])
def signup(request):
    """
    signup creates a new user and stores it in the database
    :param request: POST request containing username and password
    :return: HttpResponse
    """
    # check that are required parameters are present
    query = QueryDict(request.META.get("QUERY_STRING"))
    params = ["username", "password"]
    error_response = check_params(query, params)
    if error_response:
        return error_response

    # assign query parameters to variables
    username = query.get("username")
    password = query.get("password")

    # hash password
    password = hash_password(password)

    # check if username is taken
    try:
        models.User.objects.get(username=username)
        return HttpResponse("username {} is already taken".format(username),
                            status=400)
    except models.User.DoesNotExist:
        pass

    # create user and store it in DB
    user = models.User(username=username, password=password)
    user.save()
    logger.log(INFO, "Created user {}".format(user.__str__()))

    return HttpResponse("created user {}".format(username))


@csrf_exempt
@require_http_methods(["DELETE", "GET", "POST"])
def watched_movies(request):
    """
    watched_movies returns the list of watched movies of a user
    :param request: token to identify the user with
    :return: list of movies if token was valid
    """
    # check that are required parameters are present
    query = QueryDict(request.META.get("QUERY_STRING"))
    params = ["token"] if request.method == "GET" else ["token", "movie_id"]
    error_response = check_params(query, params)
    if error_response:
        return error_response

    # check that the token is valid
    token = query.get("token")
    if not validate(token):
        return HttpResponse("Invalid token '{}'".format(token), status=403)

    # get user the token was assigned to
    user = SESSION_HANDLER.get(token).user

    #
    # GET: the user requested their list of watched movies
    #
    if request.method == "GET":
        return JsonResponse({"watched": user.json().get("watched")})

    # get referenced movie
    movie_id = query.get("movie_id")
    # check if movie is stored in local DB
    try:
        movie = models.Movie.objects.get(movie_id=movie_id)
    # if not request it to the gateways
    except models.Movie.DoesNotExist:
        # if movie does not exist, try to look for it and store it
        status, movie_data = omdb_gw.movie_details(movie_id, True)
        if status != 200:
            return HttpResponse("Movie '{}' not found".format(movie_id),
                                status=404)
        # store movie in database
        movie = omdb_gw.build_movie(models.Movie, movie_data)
        logger.log(INFO, "store movie {}".format(movie.__str__()))
        movie.save()

    #
    # POST: the user wants to add a movie to their watched list
    #
    if request.method == "POST":
        if movie not in user.watched.all():
            user.watched.add(movie)
            user.save()
        logger.log(INFO, "add movie '{}' to watched list of user '{}'".format(
            movie.title, user.__str__()
        ))

    #
    # DELETE: the user wants to delete a movie from their watched list
    #
    elif request.method == "DELETE":
        if movie in user.watched.all():
            user.watched.remove(movie)
            user.save()
        logger.log(INFO, "remove movie '{}' from watched list of user '{}'".format(
            movie.title, user.__str__()
        ))

    return HttpResponse("OK")
