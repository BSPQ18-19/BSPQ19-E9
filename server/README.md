# MovieManager

## Server

The _MovieManager_ server is written in [Python](https://www.python.org/)
using the [Django framework](https://www.djangoproject.com/). The methods are
accesses using a REST API and are language-agnostic.

### Package structure

* [`moviemanager/`](https://github.com/BSPQ18-19/BSPQ19-E9/tree/master/server/moviemanager)
:django application for moviemanager
    * [`gateways/`](https://github.com/BSPQ18-19/BSPQ19-E9/tree/master/server/moviemanager/gateways)
    :  gateways used to access external services such as [OMDB](https://www.omdbapi.com/)
    * [`internals/`](https://github.com/BSPQ18-19/BSPQ19-E9/tree/master/server/moviemanager/internals)
    : files used by the application for specific actions such as session management
    * [`migrations/`](https://github.com/BSPQ18-19/BSPQ19-E9/tree/master/server/moviemanager/migrations)
    : _ignore this package_
* [`spq_server/`](https://github.com/BSPQ18-19/BSPQ19-E9/tree/master/server/spq_server)
: general Django app
* [`manage.py`](https://github.com/BSPQ18-19/BSPQ19-E9/blob/master/server/manage.py)
: file used to manage the Django app