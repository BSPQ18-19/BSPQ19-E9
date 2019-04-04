from omdb import OMDBClient

# Do not touch these
_OMDB_KEY = "e02fa7ac"
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


init_client()
