package ud.group9.moviemanager.api;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ud.group9.moviemanager.api.exceptions.SearchMovieException;
import ud.group9.moviemanager.api.exceptions.SignupException;
import ud.group9.moviemanager.data.Album;
import ud.group9.moviemanager.data.Movie;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class MovieManagerClientTest {
    private static String username;
    private static String password;

    @BeforeClass
    public static void setUp() {
        // Generate random username and password
        username = Long.toHexString(Double.doubleToLongBits(Math.random()));
        password = Long.toHexString(Double.doubleToLongBits(Math.random()));
        System.out.println("Generated username: " + username + ".");
        System.out.println("Generated password: " + password + ".");
        System.out.print("Signing user up...");
        testSignUp();
        System.out.println(" done.");
        System.out.print("Logging user in...");
        testLogIn();
        System.out.println(" done with token: " + MovieManagerClient.getSessionToken() + ".");
    }

    @SuppressWarnings("WeakerAccess")
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

    @SuppressWarnings("WeakerAccess")
    public static void testLogIn() {
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

    @Test
    public void testWatchList() {
        String testMovieID = "tt0446029";
        try {
            // Add watched
            boolean ok = MovieManagerClient.addToWatched(testMovieID);
            Assert.assertTrue("Failed to add watched movie", ok);
            // Get watched
            ArrayList<Movie> movies = MovieManagerClient.getWatched();
            Assert.assertEquals("Mismatched watched list length", 1, movies.size());
            Assert.assertEquals("Mismatched movies", testMovieID, movies.get(0).getMovieID());
            // Get watched IDs
            ArrayList<String> movieIDs = MovieManagerClient.getWatchedIDs();
            Assert.assertEquals("Mismatched watched list length", 1, movieIDs.size());
            Assert.assertEquals("Mismatched movie IDs", testMovieID, movieIDs.get(0));
            // Remove watched
            ok = MovieManagerClient.deleteFromWatched(testMovieID);
            Assert.assertTrue("Failed to add watched movie", ok);
            // Get watched
            movies = MovieManagerClient.getWatched();
            Assert.assertEquals("Mismatched watched list length", 0, movies.size());
        } catch (SearchMovieException e) {
            fail("Unexpected exception: " + e.toString());
        }
    }

    @Test
    public void testAlbum() {
        String testMovieID = "tt0446029";
        String testAlbumTitle = "TestAlbum";
        Album testAlbum = null;
        
        // Create album
        int status = MovieManagerClient.createAlbum(testAlbumTitle);
        Assert.assertEquals("Failed to create album", 200, status);

        // Get album for comparisons
        try {
            testAlbum = MovieManagerClient.getAlbumByTitle(testAlbumTitle);
            assertEquals("Mismatched titles", testAlbumTitle, testAlbum.getTitle());
        } catch (IndexOutOfBoundsException e) {
            fail("Unexpected exception, failed to retrieve album: " + e.toString());
        }
        // Add movie to album
        status = MovieManagerClient.addMovieToAlbum(testAlbum.getAlbumID(), testMovieID);
        Assert.assertEquals("Failed to add movie to album", 200, status);

        // Get album by:
        //// id
        Album albumByID = MovieManagerClient.getAlbum(testAlbum.getAlbumID());
        //// title
        Album albumByTitle = MovieManagerClient.getAlbumByTitle(testAlbum.getTitle());
        // compare both
        Assert.assertEquals("Wrong album", albumByID.getAlbumID(), testAlbum.getAlbumID());
        Assert.assertEquals("Mismatched IDs", albumByID.getAlbumID(), albumByID.getAlbumID());
        Assert.assertEquals("Mismatched titles", albumByID.getTitle(), albumByID.getTitle());
        Assert.assertEquals("Mismatched movies",
                albumByID.getMovies().size(), albumByTitle.getMovies().size());

        // Compare with full list of albums
        ArrayList<Album> albums = MovieManagerClient.getAlbums();
        Assert.assertEquals("Unexpected amount of albums", 1, albums.size());
        Assert.assertEquals("Mismatched IDs", testAlbum.getAlbumID(), albums.get(0).getAlbumID());

        // Remove movie from album
        status = MovieManagerClient.deleteMovieFromAlbum(testAlbum.getAlbumID(), testMovieID);
        Assert.assertEquals("Failed to remove movie from album", 200, status);
        Album emptyAlbum = MovieManagerClient.getAlbum(testAlbum.getAlbumID());
        Assert.assertEquals("Unexpected amount of movies", 0, emptyAlbum.getMovies().size());

        // Remove album by Id
        status = MovieManagerClient.deleteAlbum(testAlbum.getAlbumID());
        Assert.assertEquals("Failed to delete album", 200, status);
        try {
            MovieManagerClient.getAlbum(testAlbum.getAlbumID());
            // TODO: catch this exception at .getAlbum()
            fail("Expected JSONException not thrown");
        } catch (JSONException e) {}

        // Create album and delete it by title
        MovieManagerClient.createAlbum(testAlbumTitle);
        status = MovieManagerClient.deleteAlbumByTitle(testAlbumTitle);
        Assert.assertEquals("Failed to delete album by title", 200, status);
        try {
            MovieManagerClient.getAlbumByTitle(testAlbumTitle);
            fail("Expected JSONException not thrown");
        } catch (JSONException | IllegalArgumentException e) {}
    }
}
