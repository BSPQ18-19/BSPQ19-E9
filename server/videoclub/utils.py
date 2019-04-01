from bcrypt import checkpw, gensalt, hashpw
from django.http import HttpResponse

_encoding = "utf-8"


def check_params(query, params):
    """
    check_params creates an HTTP response if any param is missing in query
    :param query: query to check
    :param params: params to look for
    :return: None if all are present, HttpResponse otherwise
    """
    valid, missing = _params_present(query, params)
    if not valid:
        response = "Missing parameters: {}".format(', '.join(missing))
        return HttpResponse(response, status=400)
    return None


def _params_present(query, params):
    """
    param_present checks that all parameters are present in a query
    :param query: query to check
    :param params: params to look for
    :return: True if all are present, false otherwise
    """
    missing = []
    for param in params:
        if param not in query:
            missing.append(param)

    return len(missing) == 0, missing


def hash_password(password):
    return hashpw(password.encode(_encoding), gensalt()).decode(_encoding)


def compare_password(unhashed, hashed):
    return checkpw(unhashed.encode(_encoding), hashed.encode(_encoding))
