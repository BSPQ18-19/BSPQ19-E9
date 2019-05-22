"""
omdb_gw.py is a gateways file to the OMDB API,
used to retrieve information about movies from IMDB
"""
from os.path import dirname, join, realpath

from . import OMDB_CLI

_MOVIE_LIST_PATH = join(dirname(realpath(__file__)),
                        "data", "movies.csv")


def build_movie(movie_model, omdb_json):
    """
    build_movie instances and configures models.Movie given an OMDB response
    :param movie_model: models.Movie
    :param omdb_json: json returned by OMDB
    :return: movie object
    """
    movie = movie_model()
    movie.movie_id = omdb_json["imdbID"]
    movie.title = omdb_json["Title"]
    movie.year = omdb_json["Year"]
    movie.genre = omdb_json["Genre"]
    movie.avg_rating = int(float(omdb_json["imdbRating"]) * 10)
    movie.plot = omdb_json["Plot"]
    movie.poster_url = omdb_json["Poster"]
    return movie

## @brief Details of a movie
# movie_details requests information about a movie to IMDB
# given the imdbID of the movie
# @param movie_id imdbID of the movie
# @return (status_code, response)
#

def movie_details(movie_id, raw=False):
    """
    movie_details requests information about a movie to IMDB
    given the imdbID of the movie
    :param movie_id: imdbID of the movie
    :return: (status_code, response)
    """
    res = OMDB_CLI.request(i=movie_id, r="json")
    info = res.json()

    if "Error" in info.keys():
        return 404, info["Error"]

    if raw:
        return 200, info
    return 200, {
        "movie_id": info["imdbID"],
        "title": info["Title"],
        "year": info["Year"],
        "genre": info["Genre"],
        "avg_rating": int(float(info["Ratings"][0]["Value"]) * 10),
        "plot": info["Plot"],
        "poster_url": info["Poster"]
    }

## @brief Search for movirs
# search_movies sends a search request to IMDB and returns
# the processes result
# @param kwargs title=<movie title>, year(optional)=<release year>
# @return {"movies":[<list of movies>]}
#

def search_movies(**kwargs):
    """
    search_movies sends a search request to IMDB and returns
    the processes result
    :param kwargs: title=<movie title>, year(optional)=<release year>
    :return: {"movies":[<list of movies>]}
    """
    # criteria will store the query parameters required by the OMDB API
    criteria = {}
    if "title" not in kwargs.keys():
        return 400, "Missing movie title"

    criteria["s"] = kwargs["title"]
    if "year" in kwargs.keys():
        criteria["y"] = kwargs["year"]

    # response format and search type are always "json" and "movie" respectively
    criteria["r"] = "json"
    criteria["type"] = "movie"

    # send search query to the OMDB API
    res = OMDB_CLI.request(**criteria).json()

    # incorrect serach
    if "Response" in res.keys() and "Error" in res.keys():
        return 404, res["Error"]
    from json import dumps
    for m in res["Search"]:
        print(dumps(m))
    # adapt response to the format expected by the client
    movies = {
        "movies": [
            {
                "movie_id": m["imdbID"],
                "title": m["Title"],
                "year": m["Year"],
                "poster_url": m["Poster"],
            } for m in res["Search"]
        ]
    }

    return 200, movies
