from django.http import HttpResponse, JsonResponse, QueryDict
from django.views.decorators.csrf import csrf_exempt
from django.views.decorators.http import require_http_methods

from videoclub.lib.session import SESSION_HANDLER, gen_token, is_valid
from . import models
from .utils import check_params, compare_password, hash_password


@csrf_exempt
@require_http_methods(["POST"])
def signup(request):
    # get and check parameters
    query = QueryDict(request.META.get("QUERY_STRING"))
    params = ["username", "password"]
    error_response = check_params(query, params)
    if error_response:
        return error_response

    username = query.get("username")
    password = query.get("password")
    # hash password
    password = hash_password(password)

    try:
        models.User.objects.get(username=username)
        return HttpResponse("username {} is already taken".format(username),
                            status=400)
    except models.User.DoesNotExist:
        pass

    user = models.User(username=username, password=password)
    user.save()
    print("Created user {}".format(user.__str__()))

    return HttpResponse("created user {}".format(username))


@csrf_exempt
@require_http_methods(["GET"])
def login(request):
    # get and check parameters
    query = QueryDict(request.META.get("QUERY_STRING"))
    params = ["username", "password"]
    error_response = check_params(query, params)
    if error_response:
        return error_response

    username = query.get("username")
    password = query.get("password")
    try:
        user = models.User.objects.get(username=username)
    except models.User.DoesNotExist:
        # user not found
        return HttpResponse("no user with username {}".format(username),
                            status=403)
    # password doesn't match
    if not compare_password(password, user.password):
        return HttpResponse("Invalid username or password", status=403)

    token = gen_token()
    SESSION_HANDLER.add_session(token, user)

    print("log user {} with token {}".format(user.__str__(), token))
    return JsonResponse({"token": token})


@csrf_exempt
@require_http_methods(["DELETE", "GET", "POST"])
def watched_movies(request):
    query = QueryDict(request.META.get("QUERY_STRING"))
    params = ["token"] if request.method == "GET" else ["token", "movie_id"]
    error_response = check_params(query, params)
    if error_response:
        return error_response

    token = query.get("token")
    if not is_valid(token):
        return HttpResponse("Invalid token '{}'".format(token), status=403)

    user = SESSION_HANDLER.get(token).user
    if request.method == "GET":
        return JsonResponse({"watched": user.json().get("watched")})

    movie_id = query.get("movie_id")
    try:
        movie = models.Movie.objects.get(movie_id=movie_id)
    except models.Movie.DoesNotExist:
        return HttpResponse("Movie '{}' not found".format(movie_id),
                            status=404)

    if request.method == "POST":
        if movie not in user.watched.all():
            user.watched.add(movie)
            user.save()
        print("add movie '{}' to watched list of user '{}'".format(
            movie.title, user.__str__()
        ))

    elif request.method == "DELETE":
        if movie in user.watched.all():
            user.watched.remove(movie)
            user.save()
        print("remove movie '{}' from watched list of user '{}'".format(
            movie.title, user.__str__()
        ))

    return HttpResponse("OK")