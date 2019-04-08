from django.db import models

# Create your models here.


class Movie(models.Model):
    movie_id = models.CharField(primary_key=True, max_length=30)
    title = models.CharField(max_length=255)
    year = models.SmallIntegerField()
    avg_rating = models.SmallIntegerField()
    plot = models.CharField(max_length=255)
    poster_url = models.CharField(max_length=300)
    genre = models.CharField(max_length=255)

    def __str__(self):
        return "{} ({})".format(self.title, self.year)

    def json(self):
        return {
            "movie_id": self.movie_id,
            "title": self.title,
            "year": self.year,
            "avg_rating": self.avg_rating,
            "plot": self.plot,
            "poster_url": self.poster_url,
            "genre": self.genre
        }


class User(models.Model):
    username = models.CharField(primary_key=True, max_length=30)
    password = models.CharField(max_length=255)
    watched = models.ManyToManyField(Movie)

    def __str__(self):
        return str(self.username)

    def json(self):
        return {
            "username": self.username,
            "watched": [movie.json() for movie in self.watched.all()]
        }


class Album(models.Model):
    managed = True
    album_id = models.CharField(max_length=255, blank=True, null=True)
    title = models.CharField(max_length=255)
    owner = models.ForeignKey(User, on_delete=models.CASCADE)
    movies = models.ManyToManyField(Movie)

    def json(self):
        return {
            "album_id": self.album_id,
            "title": self.title,
            "owner": self.owner.username,
            "movies": [movie.json() for movie in self.movies.all()]
        }


class Rating(models.Model):
    movie_id = models.ForeignKey(Movie, on_delete=models.CASCADE)
    user_id = models.ForeignKey(User, on_delete=models.CASCADE)
    score = models.IntegerField()

    def json(self):
        return {
            "user": self.user_id.username,
            "movie": self.movie_id.json(),
            "score": self.score
        }
