package ud.group9.moviemanager.data;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

public class MovieTest {

    @Test
    public void fromJSON() {
        // read json example from file
        JSONObject movieJSON;
        String moviePath = "src/test/resources/data/movie.json";
        try {
            String raw = FileUtils.readFileToString(new File(moviePath));
            movieJSON = new JSONObject(raw);
        } catch (IOException | JSONException e) {
            fail(e.getMessage());
            return;
        }
        Movie movie = Movie.fromJSON(movieJSON);
        assertMovie(movieJSON, movie);
    }

    protected static void assertMovie(JSONObject expected, Movie actual) {
        assertEquals(expected.getString("movie_id"), actual.getMovieID());
        assertEquals(expected.getString("title"), actual.getTitle());
        assertEquals(expected.getInt("year"), actual.getYear());
        assertEquals(expected.getInt("avg_rating"), actual.getAvgRating());
        assertEquals(expected.getString("plot"), actual.getPlot());
        assertEquals(expected.getString("poster_url"), actual.getPosterUrl());
    }
}
