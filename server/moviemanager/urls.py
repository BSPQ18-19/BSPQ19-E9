from django.urls import path

from . import views

urlpatterns = [
    path('login/', views.login, name='login'),  # log user in
    path('movie/<str:movie_id>/', views.movie_details, name='movie'),  # signup user
    path('search/', views.search, name="search"),  # search for movies
    path('signup/', views.signup, name='signup'),  # sign user up
    path('watched/', views.watched_movies, name="watched"),  # manage watched list
]
