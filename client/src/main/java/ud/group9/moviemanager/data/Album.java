package ud.group9.moviemanager.data;

import org.json.JSONObject;

import java.util.ArrayList;

public class Album {
    private String  albumID;
    private String  title;
    private String    owner;
    private ArrayList<Movie> movies;

    public Album(String albumID, String title, String owner, ArrayList<Movie>  movies) {
        this.albumID = albumID;
        this.title   = title;
        this.owner   = owner;
        this.movies  = movies;
    }
    
    public Album(String albumID, String title) {
        this.albumID = albumID;
        this.title   = title;
    }

    public static Album fromJSONComplete(JSONObject album) {
        String albumID = album.getString("album_id");
        String title = album.getString("title");
        String owner = album.getString("owner");
        ArrayList<Movie> movies = new ArrayList<>();

        for (Object movie: album.getJSONArray("movies")) {
            movies.add(Movie.fromJSON((JSONObject) movie));
        }

        return new Album(albumID, title, owner, movies);
    }
    
    public static Album fromJSONSimple(JSONObject album) {
        String albumID = album.getString("album_id");
        String title = album.getString("title");
        return new Album(albumID, title);
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public ArrayList<Movie>  getMovies() {
        return movies;
    }

    public void setMovies(ArrayList<Movie>  movies) {
        this.movies = movies;
    }

	@Override
	public String toString() {
		return "Album [albumID=" + albumID + ", title=" + title + ", owner=" + owner + ", movies=" + movies + "]";
	}
    
}
