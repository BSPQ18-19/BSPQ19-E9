from datetime import datetime, timedelta
from logging import getLogger, INFO
from threading import Thread
from time import sleep
from uuid import uuid4

logger = getLogger(__name__)
SESSION_HANDLER = None


def gen_token():
    """
    gen token generates a unique token for a session
    :return: string
    """
    return uuid4().__str__()


def is_valid(token):
    """
    is_valid checks if the session token is present in SESSION_HANDLER
    :param token: session token
    :return: True if present, False if not
    """
    return SESSION_HANDLER.get(token) is not None


def set_session_handler(session_handler):
    """
    this function is called ONCE at the beginning of the program
    :param session_handler: session handler
    :return: None
    """
    global SESSION_HANDLER
    SESSION_HANDLER = session_handler


class Session:
    """
    A session contains a user, a renewal time and a timeout
    if (renewal_time + timeout) < current_time : session has expired
    """
    def __init__(self, user, renewal_time, timeout=timedelta(minutes=15)):
        self.user = user
        self.renewal_time = renewal_time
        self.timeout = timeout

    def expired(self):
        """
        expired returns True if the session has expired
        :return:
        """
        return datetime.now() > self.renewal_time + self.timeout


class SessionHandler(Thread):
    def __init__(self, sleep_time=30):
        Thread.__init__(self)
        self.daemon = True
        self.sessions = {}
        self.sleep_time = sleep_time

    def add_session(self, token, user):
        """
        add_session adds a new session with the specified user and token
        :param token: session token
        :param user: user that created the session
        :return: None
        """
        self.sessions[token] = Session(user, datetime.now())

    def get(self, token):
        """
        get is used to find a session in self.sessions
        :param token: session token
        :return: session if the token is valid, None if not
        """
        return self.sessions.get(token)

    def renew_session(self, token):
        """
        renew_session resets the renewal time of a session to the current time
        :param token: session token
        :return: None
        :raises: Exception
        """
        if token in self.sessions.keys():
            session = self.sessions.get(token)
            session.renewal_time = datetime.now()
        else:
            raise Exception("No session open for token '{}'".format(token))

    def remove_expired_sessions(self):
        """
        remove_session iterates self.sessions and removes the expired ones
        :return: None
        """
        for token, session in self.sessions.copy().items():
            if session.expired():
                self.remove_session(token)
                logger.log(INFO, "removed session '{}' due to timeout".format(token))

    def remove_session(self, token):
        """
        remove_session deletes the token and session from self.session
        :param token: token of the session to be removed
        :return: None
        :raises: Exception
        """
        if token in self.sessions.keys():
            logger.log(INFO, "removed session '{}'".format(token))
            self.sessions.pop(token)
        else:
            raise Exception("No session open for token '{}'".format(token))

    def run(self):
        # (remove expired sessions and sleep) forever
        while 1:
            self.remove_expired_sessions()
            sleep(self.sleep_time)
