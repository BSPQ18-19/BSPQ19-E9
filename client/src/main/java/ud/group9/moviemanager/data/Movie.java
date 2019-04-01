package ud.group9.moviemanager.data;

import org.json.JSONObject;

public class Movie {
    private String movieID;
    private String title;
    private int    year;
    private int  avgRating;
    private String plot;
    private String posterUrl;
    private String genre;

    public Movie(String movieID, String title, int year, int avgRating,
                 String plot, String posterUrl, String genre) {
        this.movieID   = movieID;
        this.title     = title;
        this.year      = year;
        this.avgRating = avgRating;
        this.plot      = plot;
        this.posterUrl = posterUrl;
        this.genre     = genre;
    }

    public static Movie fromJSON(JSONObject movie)  {
        String movieID = movie.getString("movie_id");
        String title = movie.getString("title");
        int year = movie.getInt("year");
        int avgRating = movie.getInt("avg_rating");
        String plot = movie.getString("plot");
        String posterUrl = movie.getString("poster_url");
        String genre = movie.getString("genre");
        return new Movie(movieID, title, year, avgRating, plot, posterUrl, genre);
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

    public int getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(int avgRating) {
        this.avgRating = avgRating;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
