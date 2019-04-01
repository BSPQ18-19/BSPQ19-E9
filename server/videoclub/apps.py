from django.apps import AppConfig

from videoclub.lib import session, vcomdb


class VideoclubConfig(AppConfig):
    name = 'videoclub'

    def ready(self):
        # session handler
        session.set_session_handler(session.SessionHandler())
        session.SESSION_HANDLER.start()

        # omdb client
        vcomdb.init_client()

        # FIXME: setup breaks if import is made outside of ready()
        from . import models
        vcomdb.load_movies(models.Movie)
