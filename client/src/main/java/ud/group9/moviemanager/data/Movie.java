package ud.group9.moviemanager.data;

import org.json.JSONObject;

public class Movie {
    private String movieID;
    private String title;
    private int    year;
    private String type;
    private String poster;

    public Movie(String movieID, String title, int year, String type,
                 String poster) {
        this.movieID   = movieID;
        this.title     = title;
        this.year      = year;
        this.type      = type;
        this.poster    = poster;
    }

    public static Movie fromJSON(JSONObject movie)  {
        String movieID = movie.getString("imdbID");
        String title = movie.getString("Title");
        int year = movie.getInt("Year");
        String type = movie.getString("Type");
        String poster = movie.getString("Poster");
        return new Movie(movieID, title, year, type, poster);
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPoster() {
		return poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}

	@Override
	public String toString() {
		return "Movie [movieID=" + movieID + ", title=" + title + ", year=" + year + ", type=" + type + ", poster="
				+ poster + "]";
	}

}
