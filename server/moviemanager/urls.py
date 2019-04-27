from django.urls import path

from . import views

urlpatterns = [
    path('album/', views.create_album, name="create album or album by title"),  # create album or manage album by title
    path('album/<str:album_id>/', views.handle_album, name="handle album"), # manage album by ID
    path('login/', views.login, name='login'),  # log user in
    path('movie/<str:movie_id>/', views.movie_details, name='movie'),  # signup user
    path('search/', views.search, name="search"),  # search for movies
    path('signup/', views.signup, name='signup'),  # sign user up
    path('user/albums/', views.user_albums, name="user_albums"),  # albums of user
    path('watched/', views.watched_movies, name="watched"),  # manage watched list
]
