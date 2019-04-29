import json
import random
import string

from locust import HttpLocust, TaskSet, task

REGISTERED = False


class UserBehaviour(TaskSet):

    username = ''.join(random.choice(
        string.ascii_uppercase + string.digits
    ) for _ in range(6))
    password = ''.join(random.choice(
        string.ascii_uppercase + string.digits
    ) for _ in range(6))
    token = None

    def on_start(self):
        global REGISTERED
        if not REGISTERED:
            REGISTERED = True
            self.signup()
        self.login()

    def on_stop(self):
        pass

    def signup(self):
        self.client.post("/moviemanager/signup/", {
            "username": self.username,
            "password": self.password
        })

    def login(self):
        response = self.client.get(
            "/moviemanager/login/?username={}&password={}"
            .format(self.username, self.password),
            name="/moviemanager/login/")
        self.token = json.loads(response.content).get("token")

    @task(1)
    def get_watched(self):
        self.client.get(f"/moviemanager/watched/?token={self.token}",
                        name="/moviemanager/watched/")

    @task(1)
    def get_albums(self):
        self.client.get(f"/moviemanager/user/albums/?token={self.token}",
                        name="/moviemanager/watched")


class WebsiteUser(HttpLocust):
    task_set = UserBehaviour
    min_wait = 5000
    max_wait = 20000
