package ud.group9.moviemanager.data;

import org.json.JSONObject;

import java.util.ArrayList;

public class Album {
    private String  albumID;
    private String  title;
    private User    owner;
    private ArrayList<Movie> movies;

    public Album(String albumID, String title, User owner, ArrayList<Movie>  movies) {
        this.albumID = albumID;
        this.title   = title;
        this.owner   = owner;
        this.movies  = movies;
    }

    public static Album fromJSON(JSONObject album) {
        String albumID = album.getString("album_id");
        String title = album.getString("title");
        User owner = User.fromJSON(album.getJSONObject("owner"));
        ArrayList<Movie> movies = new ArrayList<>();

        for (Object movie: album.getJSONArray("movies")) {
            movies.add(Movie.fromJSON((JSONObject) movie));
        }

        return new Album(albumID, title, owner, movies);
    }

    public String getAlbumID() {
        return albumID;
    }

    public void setAlbumID(String albumID) {
        this.albumID = albumID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public ArrayList<Movie>  getMovies() {
        return movies;
    }

    public void setMovies(ArrayList<Movie>  movies) {
        this.movies = movies;
    }
}
