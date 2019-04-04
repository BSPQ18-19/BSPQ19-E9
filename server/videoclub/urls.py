from django.urls import path

from . import views

urlpatterns = [
    path('login/', views.login, name='login'),  # log user in
    path('movie/<str:movie_id>/', views.movie, name='movie'),  # signup user
    path('populate/', views.populate_db_movies, name='populate'),  # populate DB
    path('search/', views.search, name="search"),  # search for movies
    path('signup/', views.signup, name='signup'),  # sign user up
    path('watched/', views.watched_movies, name="watched"),  # mark movie as watched
]
