package ud.group9.moviemanager.data;

import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import ud.group9.moviemanager.data.MovieTest;

public class UserTest {
    @SuppressWarnings("Duplicates")
    @Test
    public void fromJSON() {
        // read json example from file
        JSONObject userJSON;
        String userPath = "src/test/resources/data/user.json";
        try {
            String raw = FileUtils.readFileToString(new File(userPath));
            userJSON = new JSONObject(raw);
        } catch (IOException | JSONException e) {
            fail(e.getMessage());
            return;
        }
        User user = User.fromJSON(userJSON);
        assertUser(userJSON, user);
    }

    protected static void assertUser(JSONObject expected, User actual) {
        assertEquals(expected.getString("username"), actual.getUsername());
        JSONArray expectedMovies = expected.getJSONArray("watched");
        ArrayList<Movie> actualMovies = actual.getWatched();
        TestCase.assertEquals(expectedMovies.length(), actualMovies.size());

        for (int i=0; i<actualMovies.size(); i++) {
            MovieTest.assertMovie((JSONObject) expectedMovies.get(i), actualMovies.get(i));
        }
    }
}
