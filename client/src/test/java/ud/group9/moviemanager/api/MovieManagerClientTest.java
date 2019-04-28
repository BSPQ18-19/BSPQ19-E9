package ud.group9.moviemanager.api;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ud.group9.moviemanager.api.exceptions.SearchMovieException;
import ud.group9.moviemanager.api.exceptions.SignupException;
import ud.group9.moviemanager.data.Movie;

import java.util.ArrayList;

import static org.junit.Assert.fail;

public class MovieManagerClientTest {
    private static String username;
    private static String password;

    @BeforeClass
    public static void setUp() {
        // Generate random username and password
        username = Long.toHexString(Double.doubleToLongBits(Math.random()));
        password = Long.toHexString(Double.doubleToLongBits(Math.random()));
        System.out.println("Generated username: " + username);
        System.out.println("Generated password: " + password);
        System.out.print("Signing user up...");
        testSignUp();
        System.out.println(" done");
    }

    public static void testSignUp() {
        try {
            // correct signup
            String response = MovieManagerClient.SignUp(username, password);
            String expected = MovieManagerClient.getBundle().getString("newuserstored");
            Assert.assertEquals("Failed to sign up", response, expected);

            // duplicated signup
            response = MovieManagerClient.SignUp(username, password);
            expected = MovieManagerClient.getBundle().getString("generalerror");
            Assert.assertEquals("Unexpected successful signup", response, expected);
        } catch (SignupException e) {
            fail("Unexpected exception: " + e.toString());
        }
    }

    @Test
    public void testLogIn() {
        try {
            String previousToken = MovieManagerClient.getSessionToken();
            boolean logged = MovieManagerClient.LogIn(username, password);
            Assert.assertTrue("Failed to log in", logged);
            String currentToken = MovieManagerClient.getSessionToken();
            Assert.assertNotEquals("LogIn didn't update the token", currentToken, previousToken);

            logged = MovieManagerClient.LogIn(username, "");
            Assert.assertFalse("Unexpected login", logged);
        } catch (SignupException e) {
            fail("Unexpected exception: " + e.toString());
        }
    }

    @Test
    public void testSearchForMovie() {
        try {
            ArrayList<Movie> movies = MovieManagerClient.searchForMovie("Scott", "2010");
            Assert.assertNotEquals("Empty movie list", 0, movies.size());
            for (Movie movie: movies) {
                Assert.assertNotNull("Null movie", movie);
            }
        } catch (SearchMovieException e) {
            fail("Unexpected exception: " + e.toString());
        }
    }

    @Test
    public void testGetMovie() {
        String testMovieID = "tt0446029";
        Movie movie = MovieManagerClient.getMovie(testMovieID);
        Movie expectedMovie = new Movie(
                testMovieID,
                "Scott Pilgrim vs. the World",
                2010,
                "https://m.media-amazon.com/images/M/MV5BMTkwNTczNTMyOF5BMl5BanBnXkFtZTcwNzUxOTUyMw@@._V1_SX300.jpg"
        );
        // compare actual to expected attributes
        Object[][] attributes = {
                {movie.getTitle(), expectedMovie.getTitle()},
                {movie.getYear(), expectedMovie.getYear()},
                {movie.getMovieID(), expectedMovie.getMovieID()},
                {movie.getPoster(), expectedMovie.getPoster()}
        };
        for (Object[] attr: attributes) {
            Assert.assertEquals("Mismatched movies", attr[0], attr[1]);
        }
    }
}
