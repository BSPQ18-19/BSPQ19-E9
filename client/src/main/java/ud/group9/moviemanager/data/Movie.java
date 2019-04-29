package ud.group9.moviemanager.data;

import org.json.JSONObject;

/**
 * The Movie class represents how a Movie object is going to be
 */
public class Movie {
	private String movieID;
	private String title;
	private int    year;
	private String poster;

	/**
	 * Constructor for Movie type object
	 * @param movieID Unique identifier for the Movie
	 * @param title The name of the Movie
	 * @param year The year in which the movie was published
	 * @param poster The url where the movie poster is located
	 */
	public Movie(String movieID, String title, int year, 
			String poster) {
		this.movieID   = movieID;
		this.title     = title;
		this.year      = year;
		this.poster    = poster;
	}
	
	/**
	 * Creates a new Movie with the values  passed from a JSONObject
	 * @param movie A JSONObject with all the necessary information
	 * @return Movie This returns a Movie object
	 */
	public static Movie fromJSON(JSONObject movie)  {
		String movieID = movie.getString("movie_id");
		String title = movie.getString("title");
		int year = movie.getInt("year");
		String poster = movie.getString("poster_url");
		return new Movie(movieID, title, year, poster);
	}
	
	/**
     * Gets the MovieID of a Movie object
     * @return String Returns the MovieID of a Movie object
     */
	public String getMovieID() {
		return movieID;
	}
	 
    /**
     * Sets the MovieID to a Movie object
     * @param movieID New unique identifier for the Movie
     */
	public void setMovieID(String movieID) {
		this.movieID = movieID;
	}

	/**
     * Gets the Title of a Movie object
     * @return String Returns the Title of a Movie object
     */
	public String getTitle() {
		return title;
	}
	 
    /**
     * Sets the Title to a Movie object
     * @param title New Title for the Movie
     */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
     * Gets the Year of a Movie object
     * @return String Returns the Year of a Movie object
     */
	public int getYear() {
		return year;
	}
	 
    /**
     * Sets the Year to a Movie object
     * @param year New Year for the Movie
     */
	public void setYear(int year) {
		this.year = year;
	}

	/**
     * Gets the Poster of a Movie object
     * @return String Returns the Poster of a Movie object
     */
	public String getPoster() {
		return poster;
	}
	 
    /**
     * Sets the Poster to a Movie object
     * @param poster New Poster for the Movie
     */
	public void setPoster(String poster) {
		this.poster = poster;
	}

	@Override
	public String toString() {
		return "Movie [movieID=" + movieID + ", title=" + title + ", year=" + year + ", poster="
				+ poster + "]";
	}

}
