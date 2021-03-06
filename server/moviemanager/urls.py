from django.urls import path

from . import views

urlpatterns = [
    path('album/', views.handle_album_by_title, name="create album or album by title"),  # create album or manage album by title
    path('album/<str:album_id>/', views.handle_album, name="handle album"), # manage album by ID
    path('in_album/', views.in_album, name="in album"),  # albums of user with movie in them
    path('login/', views.login, name='login'),  # log user in
    path('movie/<str:movie_id>/', views.movie_details, name='movie'),  # signup user
    path('rating/', views.handle_rating, name="handle rating"),  # handle ratings
    path('search/', views.search, name="search"),  # search for movies
    path('signup/', views.signup, name='signup'),  # sign user up
    path('user/albums/', views.user_albums, name="user_albums"),  # albums of user
    path('watched/', views.watched_movies, name="watched"),  # manage watched list
]
