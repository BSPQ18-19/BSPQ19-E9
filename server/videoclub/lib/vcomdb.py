from csv import reader
from os.path import dirname, join, realpath

from omdb import OMDBClient

# Do not touch these
_OMDB_KEY = "e02fa7ac"
_MOVIE_LIST_PATH = join(dirname(realpath(__file__)),
                        "data", "movies.csv")

# OMDB client instance
OMDB_CLI = None


def init_client(key=_OMDB_KEY):
    """
    Call this once at the beginning
    :param key: API key for OMDB
    :return: None
    """
    global OMDB_CLI
    OMDB_CLI = OMDBClient(apikey=key)


def movie_details(movie_id):
    res = OMDB_CLI.request(i=movie_id, r="json")
    info = res.json()

    if "Error" in info.keys():
        return 404, info["Error"]

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
    init_client()
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

    return 200, {"movies": res["Search"]}


def load_movies(movie_model, movie_list_path=_MOVIE_LIST_PATH):
    init_client()
    with open(movie_list_path, 'r') as mlf:
        ml = reader(mlf, delimiter=',')
        for rmovie in ml:
            # if movie is already stored in DB, skip
            try:
                movie_model.objects.get(title=rmovie[0])
                continue
            except movie_model.DoesNotExist:
                pass
            res = OMDB_CLI.request(t=rmovie[0], y=rmovie[1], r="json")
            info = res.json()
            movie = movie_model()
            if "Response" in info.keys() and info["Response"] == "False":
                print("Failed to add movie '{}', error: {}".format(rmovie[0], info["Error"]))
                continue
            movie.movie_id = info["imdbID"]
            movie.title = info["Title"]
            movie.year = info["Year"]
            movie.genre = info["Genre"]
            movie.avg_rating = int(float(info["imdbRating"]) * 10)
            movie.plot = info["Plot"]
            movie.poster_url = info["Poster"]
            movie.save()
            print("stored movie {}".format(movie.__str__()))
