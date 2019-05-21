from django.test import Client, TestCase
from json import loads
# Create your tests here.


class ViewsTestCase(TestCase):
    def setUp(self):
        # create user
        self.username = "test_user"
        self.password = "test_pass"
        self.test_movie_id = "tt0446029"
        self._test_signup_and_login()
        # create test movie

    def _create_test_movie(self):
        """
        will query a movie that will be retrieved from IMDB and stored in local
        database
        :return: None
        """
        title = "Scott"
        year = 2010
        self.client.get("/moviemanager/search/?title={}&year={}"
                        .format(title, year))

    def _test_signup_and_login(self):
        """
        will be run by self.setUp()
        :return: None
        """
        r = self.client.post("/moviemanager/signup/?username={}&password={}"
                             .format("test_user", "test_password"))
        # self.assertEqual(200, r.status_code)
        r = self.client.get("/moviemanager/login/?username={}&password={}"
                            .format("test_user", "test_password"))
        self.assertEqual(200, r.status_code)
        self.token = loads(r.content).get("token")
        self.assertIsNotNone(self.token)

    def test_album_views(self):
        # create album
        r = self.client.put("/moviemanager/album/?token={}&title={}"
                            .format(self.token, "test_album_title"))
        self.assertEqual(200, r.status_code)
        album_id = loads(r.content).get("album_id")
        self.assertIsNotNone(album_id)

        # add movie to album
        r = self.client.post("/moviemanager/album/{}/?token={}&movie_id={}"
                             .format(album_id, self.token, self.test_movie_id))
        self.assertEqual(200, r.status_code)

        # get album
        r = self.client.get("/moviemanager/album/{}/?token={}"
                            .format(album_id, self.token))
        movies = loads(r.content).get("movies")
        self.assertEqual(1, len(movies))

        # list albums of user
        r = self.client.get("/moviemanager/user/albums/?token={}"
                            .format(self.token))
        albums = loads(r.content).get("albums")
        self.assertEqual(1, len(albums))

        # remove movie from album & get album
        self.client.delete("/moviemanager/album/{}/?token={}&movie_id={}"
                           .format(album_id, self.token, self.test_movie_id))
        r = self.client.get("/moviemanager/album/{}/?token={}"
                            .format(album_id, self.token))
        movies = loads(r.content).get("movies")
        self.assertEqual(0, len(movies))

        # remove album
        self.client.delete("/moviemanager/album/{}/?token={}"
                           .format(album_id, self.token, self.test_movie_id))
        r = self.client.get("/moviemanager/album/{}/?token={}"
                            .format(album_id, self.token))
        self.assertEqual(404, r.status_code)

    def test_movie_views(self):
        # search movie
        r = self.client.get("/moviemanager/search/?title={}&year={}"
                            .format("Scott", 2010))
        movies = loads(r.content).get("movies")
        self.assertNotEqual(0, len(movies))

        # check that a random movie has the expected fields
        self.assertSetEqual(set(movies[0].keys()),
                            {"movie_id", "title", "year", "poster_url"})

        # get details of a movie
        r = self.client.get("/moviemanager/movie/{}/".format(self.test_movie_id))
        details = loads(r.content)
        # check that all fields are present
        for key in ["movie_id", "title", "year", "avg_rating",
                    "plot", "poster_url", "genre"]:
            self.assertIsNotNone(details.get(key))

    def test_user_views(self):
        # create user and login as user are already tested at
        # _test_signup_and_login

        # add movie to watched
        r = self.client.post("/moviemanager/watched/?token={}&movie_id={}"
                             .format(self.token, self.test_movie_id))
        self.assertEqual(200, r.status_code)

        # get watched list
        r = self.client.get("/moviemanager/watched/?token={}"
                            .format(self.token))
        watched = loads(r.content).get("watched")
        self.assertIsNotNone(watched)

        # delete movie from watched
        r = self.client.delete("/moviemanager/watched/?token={}&movie_id={}"
                               .format(self.token, self.test_movie_id))
        self.assertEqual(200, r.status_code)
