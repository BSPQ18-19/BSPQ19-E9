from logging import getLogger, INFO

from django.apps import AppConfig
from videoclub.internals import session

logger = getLogger(__name__)


class VideoclubConfig(AppConfig):
    name = 'videoclub'

    def ready(self):
        logger.log(INFO, "Initialize session handler")
        # session handler
        session.set_session_handler(session.SessionHandler())
        session.SESSION_HANDLER.start()
