package ud.group9.moviemanager.data;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * The Album class represents how an Album object is going to be
 */

public class Album {
    private String  albumID;
    private String  title;
    private String  owner;
    private ArrayList<Movie> movies;
    
    /**
     * Constructor for complete Album type object
     * @param albumID Unique identifier for the Album
     * @param title The name of the Album
     * @param owner The person name who creates the Album
     * @param movies List of movies that the album contains
     */
    public Album(String albumID, String title, String owner, ArrayList<Movie>  movies) {
        this.albumID = albumID;
        this.title   = title;
        this.owner   = owner;
        this.movies  = movies;
    }
    
    /**
     * Constructor for simple Album type object
     * @param albumID Unique identifier for the Album
     * @param title he name of the Album
     */
    public Album(String albumID, String title) {
        this.albumID = albumID;
        this.title   = title;
    }
    
    /**
     * Creates a new complete Album with the values  passed from a JSONObject
     * @param album A JSONObject with all the necessary information
     * @return Album This returns a complete Album object
     */
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
    
    /**
     * Creates a new simple Album with the values  passed from a JSONObject
     * @param album A JSONObject with all the necessary information
     * @return Album This returns a simple Album object
     */
    public static Album fromJSONSimple(JSONObject album) {
        String albumID = album.getString("album_id");
        String title = album.getString("title");
        return new Album(albumID, title);
    }

    /**
     * Gets the AlbumID of an Album object
     * @return String Returns the AlbumID of an Album object
     */
    public String getAlbumID() {
        return albumID;
    }
    
    /**
     * Sets the AlbumID to an Album object
     * @param albumID New unique identifier for the Album
     */
    public void setAlbumID(String albumID) {
        this.albumID = albumID;
    }

    /**
     * Gets the Title of an Album object
     * @return String Returns the Title of an Album object
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the Title to an Album object
     * @param title New Title for the Album
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the Owner of an Album object
     * @return String Returns the Owner of an Album object
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Sets the Owner to an Album object
     * @param owner New Owner for the Album
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }
    
    /**
     * Gets the Movies of an Album object
     * @return ArrayList<Movie> Returns a list of all the Movies in an Album object
     */
    public ArrayList<Movie>  getMovies() {
        return movies;
    }

    /**
     * Sets the Movies list to an Album object
     * @param movies New Movies list for the Album
     */
    public void setMovies(ArrayList<Movie>  movies) {
        this.movies = movies;
    }

   
	@Override
	public String toString() {
		return "Album [albumID=" + albumID + ", title=" + title + ", owner=" + owner + ", movies=" + movies + "]";
	}
    
}
