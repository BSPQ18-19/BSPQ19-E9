package ud.group9.moviemanager.data;

public class Rating {
    private String movieID;
    private String userID;
    private int    score;

    public Rating(String movieID, String userID, int score) {
        this.movieID = movieID;
        this.userID  = userID;
        this.score   = score;
    }

    public String getMovieID() {
        return movieID;
    }

    public void setMovieID(String movieID) {
        this.movieID = movieID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
