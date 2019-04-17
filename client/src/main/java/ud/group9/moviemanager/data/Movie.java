package ud.group9.moviemanager.data;

import org.json.JSONObject;


public class Movie {
	private String movieID;
	private String title;
	private int    year;
	private String poster;

	public Movie(String movieID, String title, int year, 
			String poster) {
		this.movieID   = movieID;
		this.title     = title;
		this.year      = year;
		this.poster    = poster;
	}

	public static Movie fromJSON(JSONObject movie)  {
		String movieID = movie.getString("movie_id");
		String title = movie.getString("title");
		int year = movie.getInt("year");
		String poster = movie.getString("poster_url");
		return new Movie(movieID, title, year, poster);
	}

	public String getMovieID() {
		return movieID;
	}

	public void setMovieID(String movieID) {
		this.movieID = movieID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getPoster() {
		return poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}

	@Override
	public String toString() {
		return "Movie [movieID=" + movieID + ", title=" + title + ", year=" + year + ", poster="
				+ poster + "]";
	}

}
