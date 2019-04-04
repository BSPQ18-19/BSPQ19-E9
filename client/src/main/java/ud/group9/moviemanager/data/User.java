package ud.group9.moviemanager.data;

import org.json.JSONObject;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
@XmlRootElement(name = "user")
@XmlAccessorType (XmlAccessType.FIELD)
public class User {
    private String  username;
    private ArrayList<Movie> watched;

    public User(String username, ArrayList<Movie>  watched) {
        this.username = username;
        this.watched  = watched;
    }

    public static User fromJSON(JSONObject user) {
        String username = user.getString("username");
        ArrayList<Movie> watched = new ArrayList<>();

        for (Object movie: user.getJSONArray("watched")) {
            watched.add(Movie.fromJSON((JSONObject) movie));
        }

        return new User(username, watched);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<Movie> getWatched() {
        return watched;
    }

    public void setWatched(ArrayList<Movie> watched) {
        this.watched = watched;
    }
}
