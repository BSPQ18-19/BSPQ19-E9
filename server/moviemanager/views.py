from logging import getLogger, INFO
from uuid import uuid4

from django.http import HttpResponse, JsonResponse, QueryDict
from django.views.decorators.csrf import csrf_exempt
from django.views.decorators.http import require_http_methods
from silk.profiling.profiler import silk_profile

import moviemanager.internals.album as libalbum
from moviemanager.gateways import omdb_gw
from moviemanager.internals.session import SESSION_HANDLER, gen_token, validate
from . import models
from .utils import check_params, compare_password, hash_password, search_movie_by_id

logger = getLogger(__name__)


def get_query(request):
    if request.GET:
        query = request.GET
    elif request.POST:
        query = request.POST
    elif request.PUT:
        query = request.PUT
    else:
        query = QueryDict(request.META.get("QUERY_STRING"))
    return query


@csrf_exempt
@require_http_methods(["DELETE", "GET", "POST", "PUT"])
@silk_profile(name='Handle user albums by title')
def handle_album_by_title(request):
    """
    create_album creates an album with the specified title associated to the
    user
    :param request: HTTP REST request with the title of the album and the user
    token
    :return: JSON containing album_id if all went well, error otherwise
    """
    query = get_query(request)
    # check that are required parameters are present
    params = ["token", "title"]
    if request.method == "POST":
        params.append("movie_id")
    error_response = check_params(query, params)
    if error_response:
        return error_response

    token = query.get("token")
    title = query.get("title")
    if not validate(token):
        return HttpResponse("Invalid token '{}'".format(token), status=403)
    session = SESSION_HANDLER.get(token)

    # GET, POST or DELETE

    if request.method in {"DELETE", "GET", "POST"}:
        try:
            album = models.Album.objects.get(title=title, owner=session.user)
            return handle_album(request, album.album_id)
        except models.Album.DoesNotExist:
            return HttpResponse(404, "No album '{}' for user '{}'"
                                .format(title, session.user.username))

    # PUT

    album = models.Album()
    # if album with the same name already exists: error
    try:
        models.Album.objects.get(owner=session.user, title=title)
        return HttpResponse(
            "There is already an album titled '{}' for user '{}'".format(
                title, session.user.username
            ))
    except models.Album.DoesNotExist:
        pass

    album.owner = session.user
    album.album_id = uuid4().__str__()
    album.title = title
    album.save()
    logger.info("create album {}".format(album.json()))
    return JsonResponse({"album_id": album.album_id})


@csrf_exempt
@require_http_methods(["DELETE", "GET", "POST"])
@silk_profile(name='Handle user albums by ID')
def handle_album(request, album_id):
    """
    handle_album is used to:
        delete an album
        delete a movie from an album
        retrieve an album
        add a movie to an album
    :param request: DELETE, GET or POST request
    :param album_id: ID of the album to be deleted
    :return: HTTP response
    """
    query = get_query(request)
    params = ["token", "movie_id"] if request.method == "POST" else ["token"]
    error_response = check_params(query, params)
    if error_response:
        return error_response

    # verify token
    token = query.get("token")
    if not validate(token):
        return HttpResponse("Invalid token '{}'".format(token), status=403)

    # verify that album exists
    try:
        album = models.Album.objects.get(album_id=album_id)
    except models.Album.DoesNotExist:
        return HttpResponse("Album '{}' not found".format(album_id), status=404)

    # verify that the album belongs to the user
    session = SESSION_HANDLER.get(token)
    if album.owner != session.user:
        return HttpResponse("Forbidden", status=405)

    if request.method == "GET":
        return libalbum.get(album)

    elif request.method == "POST":
        # check that movie exists
        movie_id = query.get("movie_id")
        return libalbum.post(album, movie_id)

    elif request.method == "DELETE":
        # check that movie exists
        movie_id = query.get("movie_id")

        # delete album
        if movie_id is None:
            try:
                return libalbum.delete_album(album)
            # ValueError is raised by django after deleting the album as the
            # reference is now invalid
            except ValueError:
                pass
        # delete movie from album
        else:
            libalbum.delete_movie_from_album(album, movie_id)

    return HttpResponse("OK")


@csrf_exempt
@require_http_methods(["GET"])
@silk_profile(name='Log In')
def login(request):
    """
    login assigns a token to a user if verification is correct
    :param request: GET request containing username and password
    :return: JsonResponse with token if ok, HttpResponse otherwise
    """
    # check that are required parameters are present
    query = get_query(request)
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
@silk_profile(name='View details of a movie')
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
        if not movie.plot:
            raise models.Movie.DoesNotExist
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
@silk_profile(name='Search for a movie')
def search(request):
    """
    search sends a search request to IMDB and returns
    the processes result
    :param request: GET request containing movie title and optionally year
    :return: JsonResponse with movies if ok, HttpResponse otherwise
    """
    query = get_query(request)
    # check that are required parameters are present
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
@silk_profile(name='Sign Up')
def signup(request):
    """
    signup creates a new user and stores it in the database
    :param request: POST request containing username and password
    :return: HttpResponse
    """

    query = get_query(request)
    # check that are required parameters are present
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
@require_http_methods(["GET"])
@silk_profile(name='List user albums')
def user_albums(request):
    """
    user_albums retrieves a list of all the albums of a user
    :param request: GET request with the user token as a REST param and
    detailed=true as an optional parameter
    :return: JSOn with the list of the user
    """
    # check that are required parameters are present
    query = get_query(request)
    params = ["token"] if request.method == "GET" else ["token"]
    error_response = check_params(query, params)
    if error_response:
        return error_response

    # check that the token is valid
    token = query.get("token")
    if not validate(token):
        return HttpResponse("Invalid token '{}'".format(token), status=403)
    user = SESSION_HANDLER.get(token).user
    albums = models.Album.objects.filter(owner=user)

    # if detailed return entire json, if not only album_id and title
    detailed = query.get("detailed") == "true"
    return JsonResponse({
        "albums": [
            album.json() if detailed else {
                "album_id": album.json()["album_id"],
                "title": album.json()["title"]
            } for album in albums
        ]
    })


@csrf_exempt
@require_http_methods(["DELETE", "GET", "POST"])
@silk_profile(name='Handle watched movies')
def watched_movies(request):
    """
    watched_movies returns the list of watched movies of a user
    :param request: token to identify the user with
    :return: list of movies if token was valid
    """
    # check that are required parameters are present
    query = get_query(request)
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
    found, movie = search_movie_by_id(movie_id)
    if not found:
        return HttpResponse("Movie '{}' not found".format(movie_id),
                            status=404)

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


@csrf_exempt
@require_http_methods(["DELETE", "GET", "POST", "PUT"])
def handle_rating(request):
    # check that are required parameters are present
    query = get_query(request)
    params = ["token", "movie_id"] if request.method in {
        "GET", "DELETE"} else ["token", "movie_id", "score"]
    error_response = check_params(query, params)
    if error_response:
        return error_response

    # check that the token is valid
    token = query.get("token")
    if not validate(token):
        return HttpResponse("Invalid token '{}'".format(token), status=403)

    user = SESSION_HANDLER.get(token).user
    movie_id = query.get("movie_id")

    try:
        movie = models.Movie.objects.get(movie_id=movie_id)
    except models.Movie.DoesNotExist:
        return HttpResponse("Movie '{}' not found".format(movie_id), status=404)

    try:
        rating = models.Rating.objects.get(user_id=user, movie_id=movie)
    except models.Rating.DoesNotExist:
        #
        # PUT
        #
        if request.method == "PUT":
            rating = models.Rating()
            score = query.get("score")
            rating.user_id = SESSION_HANDLER.get(token).user
            rating.movie_id = movie
            try:
                rating.score = int(score)
            except ValueError:
                return HttpResponse("Invalid score '{}'".format(score), status=400)
            rating.save()
            logger.log(INFO, "create rating {}".format(rating.json()))
            return HttpResponse("OK")
        else:
            return HttpResponse("No rating found for user '{}' movie '{}'".format(
                user.username, movie_id
            ), status=404)
    except models.Rating.MultipleObjectsReturned:
        ratings = models.Rating.objects.filter(user_id=user)
        for rating in ratings:
            rating.delete()
        return HttpResponse(
            "Found multiple albums. Deleted them because testing is enabled.",
            status=500)

    #
    # GET
    #
    if request.method == "GET":
        return JsonResponse(rating.json())

    #
    # POST
    #
    elif request.method == "POST":
        score = query.get("score")
        try:
            rating.score = int(score)
        except ValueError:
            return HttpResponse("Invalid score '{}'".format(score), status=400)
        models.Rating.objects.update(user_id=rating.user_id,
                                     movie_id=rating.movie_id, score=score)

    #
    # DELETE
    #
    elif request.method == "DELETE":
        rating.delete()

    return HttpResponse("OK")


@csrf_exempt
@require_http_methods(["GET"])
def in_album(request):
    query = get_query(request)
    # check that are required parameters are present
    params = ["token", "movie_id"]
    error_response = check_params(query, params)
    if error_response:
        return error_response

    # check that the token is valid
    token = query.get("token")
    if not validate(token):
        return HttpResponse("Invalid token '{}'".format(token), status=403)

    user = SESSION_HANDLER.get(token).user
    movie_id = query.get("movie_id")

    try:
        movie = models.Movie.objects.get(movie_id=movie_id)
    except models.Movie.DoesNotExist:
        return JsonResponse({"albums": []})

    albums = models.Album.objects.filter(owner=user,
                                         movies=movie)

    return JsonResponse({
        "albums": [
            a.title for a in albums
        ]
    })
