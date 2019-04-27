package ud.group9.moviemanager.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ud.group9.moviemanager.api.exceptions.SearchMovieException;
import ud.group9.moviemanager.api.exceptions.SignupException;
import ud.group9.moviemanager.data.Movie;

import java.util.ArrayList;

import static org.junit.Assert.fail;

public class MovieManagerClientTest {
    private static MovieManagerClient client = MovieManagerClient.INSTANCE;
    private String username;
    private String password;


    @Before
    public void setUp() {
        // Generate random username and password
        this.username = Long.toHexString(Double.doubleToLongBits(Math.random()));
        this.password = Long.toHexString(Double.doubleToLongBits(Math.random()));
        System.out.println("Generated username: " + this.username);
        System.out.println("Generated password: " + this.password);
        System.out.print("Signing user up...");
        testSignUp();
        System.out.println(" done");
    }

    public void testSignUp() {
        try {
            // correct signup
            String response = client.SignUp(username, password);
            String expected = client.getBundle().getString("newuserstored");
            Assert.assertTrue("Failed to sign up", response.equals(expected));

            // duplicated signup
            response = client.SignUp(username, password);
            expected = client.getBundle().getString("generalerror");
            Assert.assertTrue("Unexpected successful signup", response.equals(expected));
        } catch (SignupException e) {
            fail("Unexpected exception: " + e.toString());
        }
    }

    @Test
    public void testLogIn() {
        try {
            String previousToken = client.getSessionToken();
            boolean logged = client.LogIn(username, password);
            Assert.assertTrue("Failed to log in", logged);
            String currentToken = client.getSessionToken();
            Assert.assertFalse("LogIn didn't update the token",
                    currentToken.equals(previousToken));

            logged = client.LogIn(username, "");
            Assert.assertFalse("Unexpected login", logged);
        } catch (SignupException e) {
            fail("Unexpected exception: " + e.toString());
        }
    }

    @Test
    public void searchForMovie() {
        try {
            ArrayList<Movie> movies = client.searchForMovie("Scott", "2010");
            Assert.assertFalse("Empty movie list", movies.size() == 0);
            for (Movie movie: movies) {
                Assert.assertFalse("Null movie", movie == null);
            }
        } catch (SearchMovieException e) {
            fail("Unexpected exception: " + e.toString());
        }
    }
}
