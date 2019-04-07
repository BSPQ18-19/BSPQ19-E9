from csv import reader
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
        "avg_rating": int(float(info["imdbRating"]) * 10),
        "plot": info["Plot"],
        "poster_url": info["Poster"]
    }


def search_movies(**kwargs):
    """
    search_movies sends a search request to IMDB and returns
    the processes result
    :param kwargs: title=<movie title>, year(optional)=<release year>
    :return: {"movies":[<list of movies>]}
    """
    criteria = {}
    if "title" not in kwargs.keys():
        return 400, "Missing movie title"

    criteria["s"] = kwargs["title"]
    if "year" in kwargs.keys():
        criteria["y"] = kwargs["year"]

    criteria["r"] = "json"
    criteria["type"] = "movie"
    res = OMDB_CLI.request(**criteria).json()

    if "Response" in res.keys() and "Error" in res.keys():
        return 404, res["Error"]

    movies = {
        "movies": [
            {
                "movie_id": m["imdbID"],
                "title": m["Title"],
                "year": m["Year"],
                "poster_url": m["Poster"]
            } for m in res["Search"]
        ]
    }

    return 200, movies


@DeprecationWarning
def load_movies(movie_model, movie_list_path=_MOVIE_LIST_PATH):
    """
    load_movies requests movie information to OMDB and stores it in storage engine
    :param movie_model: model of a movie: models.Movie
    :param movie_list_path: path to csv containing movie titles and years
    :return: None
    """
    with open(movie_list_path, 'r') as mlf:
        movie_list = reader(mlf, delimiter=',')
        for m in movie_list:
            # if movie is already stored in DB, skip
            try:
                movie_model.objects.get(title=m[0])
                continue
            except movie_model.DoesNotExist:
                pass
            res = OMDB_CLI.request(t=m[0], y=m[1], r="json")
            info = res.json()
            if "Response" in info.keys() and info["Response"] == "False":
                print("Failed to add movie '{}', error: {}".format(m[0], info["Error"]))
                continue
            movie = build_movie(movie_model, info)
            movie.save()
            print("stored movie {}".format(movie.__str__()))
