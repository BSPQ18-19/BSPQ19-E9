package ud.group9.moviemanager.data;

/**
 * The Rating class represents how a Rating object is going to be
 */
public class Rating {
    private String movieID;
    private String userID;
    private int    score;

    /**
     * Constructor for Rating type object
     * @param movieID Unique identifier of a Movie
     * @param userID Unique identifier of a User
     * @param score Score of a Movie
     */
    public Rating(String movieID, String userID, int score) {
        this.movieID = movieID;
        this.userID  = userID;
        this.score   = score;
    }

    /**
     * Gets the MovieID of a Rating object
     * @return String Returns the MovieID of a Rating object
     */
    public String getMovieID() {
        return movieID;
    }

    /**
     * Sets the MovieID to a Rating object
     * @param movieID New unique identifier for movieID of a Rating object
     */
    public void setMovieID(String movieID) {
        this.movieID = movieID;
    }

    /**
     * Gets the UserID of a Rating object
     * @return String Returns the UserID of a Rating object
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Sets the UserID to a Rating object
     * @param movieID New unique identifier for userID of a Rating object
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * Gets the Score of a Rating object
     * @return String Returns the Score of a Rating object
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets the Score to a Rating object
     * @param movieID New Score of a Rating object
     */
    public void setScore(int score) {
        this.score = score;
    }
}
