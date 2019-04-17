from django.http import HttpResponse, JsonResponse
from logging import getLogger, INFO

from .. import models, utils


logger = getLogger(__name__)


def get(album):
    return JsonResponse(album.json())


def post(album, movie_id):
    found, movie = utils.search_movie_by_id(movie_id)
    if not found:
        return HttpResponse("Movie '{}' not found".format(movie_id))
    album.movies.add(movie)
    album.save()
    logger.log(INFO, "add movie {} to album {}".format(movie.title, album.title))
    return HttpResponse("OK")


def delete_album(album):
    album.delete()
    logger.log(INFO, "delete album {}".format(album.json()))
    return HttpResponse("OK")


def delete_movie_from_album(album, movie_id):
    found, movie = utils.search_movie_by_id(movie_id)
    if not found:
        return HttpResponse("Movie '{}' not found".format(movie_id))
    if movie not in album.movies.all():
        return HttpResponse("Movie '{}' not in album '{}'".format(
            movie_id, album.album_id))
    album.movies.remove(movie)
    album.save()
    logger.log(INFO, "remove movie {} from album {}".format(movie.title, album.title))
    return HttpResponse("OK")
