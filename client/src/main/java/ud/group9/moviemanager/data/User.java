package ud.group9.moviemanager.data;

import org.json.JSONObject;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
@XmlRootElement(name = "user")
@XmlAccessorType (XmlAccessType.FIELD)

/**
 * @brief User class
 * 
 * The User class represents how a User object is going to be
 */
public class User {
    private String  username;
    private ArrayList<Movie> watched;

    /**
     * @brief User Constructor
     * 
     * Constructor for User type object
     * @param username The name of the User
     * @param watched A list of movies that the user has seen
     */
    public User(String username, ArrayList<Movie>  watched) {
        this.username = username;
        this.watched  = watched;
    }

    /**
     * @brief Create a new User
     * 
     * Creates a new User with the values  passed from a JSONObject
     * @param user A JSONObject with all the necessary information
     * @return Returns a new User object
     */
    public static User fromJSON(JSONObject user) {
        String username = user.getString("username");
        ArrayList<Movie> watched = new ArrayList<>();

        for (Object movie: user.getJSONArray("watched")) {
            watched.add(Movie.fromJSON((JSONObject) movie));
        }

        return new User(username, watched);
    }

    /**
     * @brief Get the User Username
     * 
     * Gets the Username of a User object
     * @return String Returns the Username of a User object
     */
    public String getUsername() {
        return username;
    }

    /**
     * @brief Set the User Username
     * 
     * Sets the Username to a User object
     * @param username New Username for the User
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @brief Get the list of watched movies
     * 
     * Gets the list of watched Movies of a User object
     * @return ArrayList<Movie> Returns the list of watched Movies of a User object
     */
    public ArrayList<Movie> getWatched() {
        return watched;
    }

    /**
     * @brief Set the list of watched Movies
     * 
     * Sets the list of watched Movies of a User object
     * @param watched New list of watched Movies of a User object
     */
    public void setWatched(ArrayList<Movie> watched) {
        this.watched = watched;
    }
}
