package ud.group9.moviemanager.api;

import ud.group9.moviemanager.api.exceptions.SearchMovieException;
import ud.group9.moviemanager.api.exceptions.SignupException;
import ud.group9.moviemanager.data.Album;
import ud.group9.moviemanager.data.Movie;
import ud.group9.moviemanager.gui.SignupGUI;
import ud.group9.moviemanager.gui.UserAlbumsGUI;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import ud.group9.moviemanager.utils.Hash;

/**
 * The MovieManagerClient class manage all the client side connection with the server side
 *
 */
public enum MovieManagerClient {
	INSTANCE;
	private static Client client = Client.create();
	private static String protocol = "http";
	private static String host = "127.0.0.1";
	private static int port = 8000;
	private static String basepath = "moviemanager";

	private static ResourceBundle bundle = ResourceBundle.getBundle("SystemMessages_es");
	private static String sessionToken = null;
	private static ArrayList<String> watchedIDs = null;
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	/**
	 * Empty contructor for MovieManagerClient
	 */
	private MovieManagerClient(){
		try {
            LoggerMaster.setup();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Problems with creating the log files");
        }
	}
	
	/**
	 * Sets the parameters for the connection to the server
	 * @param host The IP direction where the server is hosted
	 * @param port The Port where the server is waiting for connections
	 */
	public void setConnection(String host, int port) {
		MovieManagerClient.host = host;
		MovieManagerClient.port = port;
	}
	
	/**
	 * Opens the interface for the Initial connection where the user will need to LogIn or SignUp
	 */
	public static void start(){
		try {
			new UserAlbumsGUI();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates the complete path to establish connection with the server
	 * @return String Returns the path for the server connection
	 */
	private static String addr() {
		return protocol + "://" + host + ":" + port + "/" + basepath;
	}

	/**
	 * The user registers with the values that he has provided
	 * @param username The name by which the user will be recognized
	 * @param password The password that the user will use to access
	 * @return String Returns a message with the result of the SignUp process
	 * @throws SignupException A exception if something goes wrong
	 */
	public static String SignUp( String username, String password) throws SignupException {
		// Hash password so no plaintext password is sent through the network
		String hashedPassword = Hash.encodeHash(Hash.sha256Hash(password));

		String mensaje = "";
		WebResource webResource = client.resource(addr()).path("signup/");
		ClientResponse response = webResource
				.queryParam("username", username)
				.queryParam("password", hashedPassword)
				.post(ClientResponse.class);
		switch(response.getStatus()){
		case 200:
			mensaje = MovieManagerClient.getBundle().getString("newuserstored");
			LOGGER.log(Level.INFO, "New user Stored.");
			break;
		case 401:
			mensaje = MovieManagerClient.getBundle().getString("usernametaken");
			LOGGER.log(Level.INFO, "The Username is already taken.");
			break;
		default:
			mensaje = MovieManagerClient.getBundle().getString("generalerror");
			LOGGER.log(Level.INFO, "Error while SignUp.");
			break;
		}
		response.close();
		return mensaje;
	}

	/**
	 * The user LogIn with the values that he has provided
	 * @param username The username with which the user SignUp
	 * @param password The password with which the user SignUp
	 * @return boolean Returns a Boolean depending on whether everything went well or not 
	 * @throws SignupException A exception if something goes wrong
	 */
	public static boolean LogIn( String username, String password ) throws SignupException {
		// Hash password so no plaintext password is sent through the network 
		String hashedPassword = Hash.encodeHash(Hash.sha256Hash(password));

		WebResource webResource = client.resource(addr()).path("login/");
		ClientResponse response = webResource
				.queryParam("username", username)
				.queryParam("password", hashedPassword)
				.get(ClientResponse.class);
		if (response.getStatus() == 200){
			JSONObject jo = new JSONObject(response.getEntity(String.class));
			sessionToken = jo.get("token").toString();
			watchedIDs = MovieManagerClient.getWatchedIDs();
			LOGGER.log(Level.INFO, "LogIn Succesessful");
		}
		response.close();
		return (response.getStatus() == 200);
	}

	/**
	 * Search for a movie based on the parameters entered by the user
	 * @param title Title of the movie that the user wants to search
	 * @param year Year of the premiere of the movie that the user wants to search
	 * @return ArrayList<Movie> Returns a list of movies that match the search parameters entered by the user
	 * @throws SearchMovieException A exception if something goes wrong
	 */
	public static ArrayList<Movie> searchForMovie( String title, String year ) throws SearchMovieException {

		ArrayList<Movie> moviesSearched = new ArrayList<>();

		WebResource webResource = client.resource(addr()).path("search/");
		ClientResponse response = webResource
				.queryParam("title", title)
				.queryParam("year", year)
				.get(ClientResponse.class);
		JSONObject jo = new JSONObject(response.getEntity(String.class));
		JSONArray joa = jo.getJSONArray("movies");
		for (int i = 0; i < joa.length(); i++){
			moviesSearched.add(Movie.fromJSON(joa.getJSONObject(i)));
		}
		LOGGER.log(Level.INFO, "Search Movie Successful");
		response.close();
		return moviesSearched;
	}

	/**
	 * Get a Specific Movie based on the provided parameter
	 * @param movieID Unique identifier for a Movie
	 * @return Movie Returns the Movie object with the identifier passed as a parameter
	 */
	public static Movie getMovie( String movieID ){
		Movie movie = null;
		WebResource webResource = client.resource(addr()).path("movie/" + movieID + "/");
		ClientResponse response = webResource
				.get(ClientResponse.class);
		movie = Movie.fromJSON( new JSONObject(response.getEntity(String.class)));
		LOGGER.log(Level.INFO, "Movie retrived successfully");
		response.close();
		return movie;
	}
	
	/**
	 * Get a list of movies that the user has seen
	 * @return ArrayList<Movie> Returns a List of Movies that the user has seen
	 * @throws SearchMovieException A exception if something goes wrong
	 */
	public static ArrayList<Movie> getWatched() throws SearchMovieException {
		
		ArrayList<Movie> movies = new ArrayList<>();

		WebResource webResource = client.resource(addr()).path("watched/");
		ClientResponse response = webResource
				.queryParam("token", sessionToken)
				.get(ClientResponse.class);
		JSONObject jo = new JSONObject(response.getEntity(String.class));
		JSONArray joa = jo.getJSONArray("watched");
		for (int i = 0; i < joa.length(); i++){
			movies.add(Movie.fromJSON(joa.getJSONObject(i)));
		}

		LOGGER.log(Level.INFO, "Watched Movies retrieved successfully");
		response.close();
		return movies;
	}
	
	/**
	 * Get a list of movieID of the Movies that the user has seen
	 * @return ArrayList<String> Returns a List of MovieID of Movies that the user has seen
	 */
	public static ArrayList<String> getWatchedIDs() {
		ArrayList<String> watched = new ArrayList<>();
		try {
			for (Movie m: MovieManagerClient.getWatched()){
				watched.add(m.getMovieID());
			}
			LOGGER.log(Level.INFO, "Watched Movies Id retrieved successfully");
		} catch (SearchMovieException e) {
			LOGGER.log(Level.SEVERE, LoggerMaster.getStackTrace(e));
		}
		return watched;
	}
	
	/**
	 * Add a Movie to Watched list
	 * @param movieID Unique identifier of the Movie to add
	 * @return boolean Returns a Boolean depending on whether everything went well or not 
	 * @throws SearchMovieException A exception if something goes wrong
	 */
	public static boolean addToWatched( String movieID ) throws SearchMovieException {

		WebResource webResource = client.resource(addr()).path("watched/");
		ClientResponse response = webResource
				.queryParam("token", sessionToken)
				.queryParam("movie_id", movieID)
				.post(ClientResponse.class);
		response.close();
		watchedIDs.add(movieID);
		if(response.getStatus() == 200)
			LOGGER.log(Level.INFO, "Movie successfully added to Watched list");
		return (response.getStatus() == 200);
	}
	
	/**
	 * Remove a Movie to Watched list
	 * @param movieID
	 * @return boolean Returns a Boolean depending on whether everything went well or not 
	 * @throws SearchMovieException A exception if something goes wrong
	 */
	public static boolean deleteFromWatched( String movieID ) throws SearchMovieException {

		WebResource webResource = client.resource(addr()).path("watched/");
		ClientResponse response = webResource
				.queryParam("token", sessionToken)
				.queryParam("movie_id", movieID)
				.delete(ClientResponse.class);
		response.close();
		watchedIDs.remove(movieID);
		if(response.getStatus() == 200)
			LOGGER.log(Level.INFO, "Movie successfully deleted from Watched list");
		return (response.getStatus() == 200);
	}
	
	/**
	 * Create a new Album
	 * @param title The name that the user wants to put on the album
	 * @return int Return a status code of the process
	 */
	public static int createAlbum( String title ){
		WebResource webResource = client.resource(addr()).path("album/");
		ClientResponse response = webResource
				.queryParam("token", sessionToken)
				.queryParam("title", title)
				.put(ClientResponse.class);
		response.close();
		if(response.getStatus() == 200)
			LOGGER.log(Level.INFO, "Album created successfully");
		return response.getStatus();
	}

	/**
	 * Get all User Albums
	 * @return ArrayList<Album> Returns a list of User Albums
	 */
	public static ArrayList<Album> getAlbums(){
		ArrayList<Album> albums = new ArrayList<>();

		WebResource webResource = client.resource(addr()).path("user/albums/");
		ClientResponse response = webResource
				.queryParam("token", sessionToken)
				.get(ClientResponse.class);
		JSONObject jo = new JSONObject(response.getEntity(String.class));
		JSONArray joa = jo.getJSONArray("albums");
		for (int i = 0; i < joa.length(); i++){
			albums.add(Album.fromJSONSimple(joa.getJSONObject(i)));
		}
		response.close();
		if(response.getStatus() == 200)
			LOGGER.log(Level.INFO, "Albums retrieved Successfully");
		return albums;
	}

	/**
	 * Get a specific album by ID
	 * @param albumID Unique identifier of the album that the user has requested
	 * @return Album Returns a complete Album object
	 */
	public static Album getAlbum(String albumID){
		WebResource webResource = client.resource(addr()).path("album/" + albumID + "/");
		ClientResponse response = webResource
				.queryParam("token", sessionToken)
				.get(ClientResponse.class);
		JSONObject jo = new JSONObject(response.getEntity(String.class));
		response.close();
		if(response.getStatus() == 200)
			LOGGER.log(Level.INFO, "Album retrieved Successfully");
		return Album.fromJSONComplete(jo);
	}
	
	/**
	 * Get a specific album by Title
	 * @param albumTitle Title of the album that the user has requested
	 * @return Album Returns a complete Album object
	 */
	public static Album getAlbumByTitle(String albumTitle){
		WebResource webResource = client.resource(addr()).path("album/");
		ClientResponse response = webResource
				.queryParam("token", sessionToken)
				.queryParam("title", albumTitle)
				.get(ClientResponse.class);
		JSONObject jo = new JSONObject(response.getEntity(String.class));
		response.close();
		if(response.getStatus() == 200)
			LOGGER.log(Level.INFO, "Album by Title retrieved Successfully");
		return Album.fromJSONComplete(jo);
	}

	/**
	 * Remove a User Album by ID
	 * @param albumID Unique identifier of the album that the user has requested to remove
	 * @return int Return a status code of the process
	 */
	public static int deleteAlbum(String albumID){
		WebResource webResource = client.resource(addr()).path("album/" + albumID + "/");
		ClientResponse response = webResource
				.queryParam("token", sessionToken)
				.delete(ClientResponse.class);
		response.close();
		if(response.getStatus() == 200)
			LOGGER.log(Level.INFO, "Album deleted Successfully");
		return response.getStatus();
	}

	/**
	 * Remove a User Album by Title
	 * @param albumTitle Title of the album that the user has requested
	 * @return int Return a status code of the process
	 */
	public static int deleteAlbumByTitle(String albumTitle){
		WebResource webResource = client.resource(addr()).path("album/");
		ClientResponse response = webResource
				.queryParam("token", sessionToken)
				.queryParam("title", albumTitle)
				.delete(ClientResponse.class);
		response.close();
		if(response.getStatus() == 200)
			LOGGER.log(Level.INFO, "Album deleted by Title Successfully");
		return response.getStatus();
	}
	
	/**
	 * Add a Movie to a User Album by ID
	 * @param albumID Unique identifier of the album that the user has requested to add
	 * @param movieID Unique identifier of the movie that the user has requested to add
	 * @return int Return a status code of the process
	 */
	public static int addMovieToAlbum( String albumID, String movieID ){
		WebResource webResource = client.resource(addr()).path("album/" + albumID + "/");
		ClientResponse response = webResource
				.queryParam("token", sessionToken)
				.queryParam("movie_id", movieID)
				.post(ClientResponse.class);
		response.close();
		if(response.getStatus() == 200)
			LOGGER.log(Level.INFO, "Movie added Successfully to the Album");
		return response.getStatus();
	}
	
	/**
	 * Add a Movie to a User Album by Title
	 * @param albumTitle Unique identifier of the album that the user has requested to add
	 * @param movieID Unique identifier of the movie that the user has requested to add
	 * @return int Return a status code of the process
	 */
	public static int addMovieToAlbumByTitle( String albumTitle, String movieID ){
		WebResource webResource = client.resource(addr()).path("album/");
		ClientResponse response = webResource
				.queryParam("token", sessionToken)
				.queryParam("title", albumTitle)
				.queryParam("movie_id", movieID)
				.post(ClientResponse.class);
		response.close();
		if(response.getStatus() == 200)
			LOGGER.log(Level.INFO, "Movie added Successfully to the Album by Title");
		return response.getStatus();
	}
	
	/**
	 * Remove a Movie from a User Album
	 * @param albumID Unique identifier of the album that the user has requested to remove
	 * @param movieID Unique identifier of the movie that the user has requested to remove
	 * @return int Return a status code of the process
	 */
	public static int deleteMovieFromAlbum( String albumID, String movieID ){
		WebResource webResource = client.resource(addr()).path("album/" + albumID + "/");
		ClientResponse response = webResource
				.queryParam("token", sessionToken)
				.queryParam("movie_id", movieID)
				.delete(ClientResponse.class);
		response.close();
		if(response.getStatus() == 200)
			LOGGER.log(Level.INFO, "Movie deleted Successfully from the Album");
		return response.getStatus();
	}
	
	/**
	 * Check if a movie has been watched by the User
	 * @param movieID Unique identifier of the movie that has requested to check
	 * @return Boolean Returns a Boolean depending on whether the Movie is watched or not 
	 */
	public static boolean isWatched(String movieID){
		return watchedIDs.contains(movieID);
	}

	/**
	 * Close the Client
	 */
	public static void closeClient(){
		client.destroy();
		LOGGER.log(Level.INFO, "Client closed");
	}

	/**
	 * Get the Client Bundle
	 * @return ResourceBundle Returns the Client Bundle
	 */
	public static ResourceBundle getBundle() {
		return bundle;
	}

	/**
	 * Set the Bundle for the Client
	 * @param bundle The ResourceBundle object to set
	 */
	public static void setBundle(ResourceBundle bundle) {
		MovieManagerClient.bundle = bundle;
	}

	/**
	 * Get actual session Token
	 * @return String Returns the actual session token
	 */
	public static String getSessionToken() {
		return sessionToken;
	}

	public static void main(String[] args) {
		//		new MovieManagerClient(args[0], Integer.parseInt(args[1]));
		try {
			MovieManagerClient.start();
			//			System.out.println(mmc.SignUp("user", "test_password"));
//			MovieManagerClient.LogIn("user", "test_password");
			//			mmc.createAlbum("albumToDelete");
			//			mmc.deleteAlbum(mmc.getAlbums().get(0).getAlbumID());
//			System.out.println(mmc.getAlbums());
//			System.out.println(mmc.searchForMovie("Scott Pilgrim", "2010").toString());
//			System.out.println(MovieManagerClient.getWatched());
//			System.out.println(MovieManagerClient.getMovie(MovieManagerClient.searchForMovie("Scott Pilgrim", "2010").get(0).getMovieID()));
//			System.out.println(MovieManagerClient.addToWatched( MovieManagerClient.searchForMovie("Scott Pilgrim", "2010").get(0).getMovieID()));
//			System.out.println(MovieManagerClient.addToWatched( MovieManagerClient.searchForMovie("Scott Pilgrim", "2010").get(1).getMovieID()));
//			System.out.println(MovieManagerClient.getWatched());
//			System.out.println(MovieManagerClient.deleteFromWatched( MovieManagerClient.searchForMovie("Scott Pilgrim", "2010").get(1).getMovieID()));
//			System.out.println(MovieManagerClient.getWatched());
//			System.out.println(mmc.addMovieToAlbum(mmc.getAlbums().get(0).getAlbumID(), mmc.searchForMovie("Scott Pilgrim", "2010").get(1).getMovieID()));
//			System.out.println(mmc.getAlbums());
//			System.out.println(mmc.deleteMovieFromAlbum(mmc.getAlbums().get(0).getAlbumID(), mmc.searchForMovie("Scott Pilgrim", "2010").get(1).getMovieID()));
//			System.out.println(mmc.getAlbums());
			//			System.out.println(mmc.addToWatched(mmc.searchForMovie("Scott Pilgrim", "2010").get(0).getMovieID()));
			Thread.sleep(10000);
			// mmc.closeClient();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
