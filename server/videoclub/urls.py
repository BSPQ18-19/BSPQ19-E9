from django.urls import path

from . import views

urlpatterns = [
    path('login/', views.login, name='login'),
    path('signup/', views.signup, name='signup'),
    path('watched/', views.watched_movies, name="watched"),
    # download info about movies
    path('populate/', views.populate_db_movies, name='populate'),
]
