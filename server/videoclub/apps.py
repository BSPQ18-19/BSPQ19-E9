from django.apps import AppConfig
from videoclub.lib import session


class VideoclubConfig(AppConfig):
    name = 'videoclub'

    def ready(self):
        # session handler
        session.set_session_handler(session.SessionHandler())
        session.SESSION_HANDLER.start()
