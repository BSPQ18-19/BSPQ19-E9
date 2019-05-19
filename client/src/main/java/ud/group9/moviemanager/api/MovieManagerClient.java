package ud.group9.moviemanager.api;

import ud.group9.moviemanager.api.exceptions.SearchMovieException;
import ud.group9.moviemanager.api.exceptions.SignupException;
import ud.group9.moviemanager.data.Album;
import ud.group9.moviemanager.data.Movie;
import ud.group9.moviemanager.gui.UserAlbumsGUI;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import ud.group9.moviemanager.utils.Hash;

/**
 * @brief MovieManagerClient class
 * 
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
	private final static Logger LOGGER = LogManager.getRootLogger();
	
	/**
	 * @brief Empty contructor for MovieManagerClient
	 */
	private MovieManagerClient(){
	}
	
	/**
	 * @brief Set connection parameters 
	 * 
	 * Sets the parameters for the connection to the server
	 * @param host The IP direction where the server is hosted
	 * @param port The Port where the server is waiting for connections
	 */
	public void setConnection(String host, int port) {
		MovieManagerClient.host = host;
		MovieManagerClient.port = port;
	}
	
	/**
	 * @brief Start initial interface
	 * 
	 * Opens the interface for the Initial connection where the user will need to LogIn or SignUp
	 */
	public static void start(){
		try {
			new UserAlbumsGUI();
		} catch (MalformedURLException e) {
			LOGGER.info(e.toString());
		} catch (IOException e) {
			LOGGER.info(e.toString());
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
	 * @brief SignUp process
	 * 
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
			LOGGER.info("New user Stored.");
			break;
		case 401:
			mensaje = MovieManagerClient.getBundle().getString("usernametaken");
			LOGGER.info("The Username is already taken.");
			break;
		default:
			mensaje = MovieManagerClient.getBundle().getString("generalerror");
			LOGGER.info("Error while SignUp.");
			break;
		}
		response.close();
		return mensaje;
	}

	/**
	 * @brief LogIn process 
	 * 
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
			LOGGER.info("LogIn Succesessful");
		}
		response.close();
		return (response.getStatus() == 200);
	}

	/**
	 * @brief Search movie by title and year
	 * 
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
		LOGGER.info("Search Movie Successful");
		response.close();
		return moviesSearched;
	}

	/**
	 * @brief Get a movie by Id
	 * 
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
		LOGGER.info("Movie retrived successfully");
		response.close();
		return movie;
	}
	
	/**
	 * @brief Watched movies by user
	 * 
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

		LOGGER.info("Watched Movies retrieved successfully");
		response.close();
		return movies;
	}
	
	/**
	 * @brief Id of all watched movies
	 * 
	 * Get a list of movieID of the Movies that the user has seen
	 * @return ArrayList<String> Returns a List of MovieID of Movies that the user has seen
	 */
	public static ArrayList<String> getWatchedIDs() {
		ArrayList<String> watched = new ArrayList<>();
		try {
			for (Movie m: MovieManagerClient.getWatched()){
				watched.add(m.getMovieID());
			}
			LOGGER.info("Watched Movies Id retrieved successfully");
		} catch (SearchMovieException e) {
			LOGGER.warn(e.toString());
		}
		return watched;
	}
	
	/**
	 * @brief Add movie to Watched list
	 * 
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
			LOGGER.info("Movie successfully added to Watched list");
		return (response.getStatus() == 200);
	}
	
	/**
	 * @brief Remove a movie from watched list
	 *  
	 * Remove a Movie from Watched list
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
			LOGGER.info("Movie successfully deleted from Watched list");
		return (response.getStatus() == 200);
	}
	
	/**
	 * @brief Create an Album
	 * 
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
			LOGGER.info("Album created successfully");
		return response.getStatus();
	}

	/**
	 * @brief Get all ALbums
	 * 
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
			LOGGER.info("Albums retrieved Successfully");
		return albums;
	}

	/**
	 * @brief Get album by ID
	 * 
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
			LOGGER.info("Album retrieved Successfully");
		return Album.fromJSONComplete(jo);
	}
	
	/**
	 * @brief Get album by Title
	 * 
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
			LOGGER.info("Album by Title retrieved Successfully");
		return Album.fromJSONComplete(jo);
	}

	/**
	 * @brief Remove Album by id
	 * 
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
			LOGGER.info("Album deleted Successfully");
		return response.getStatus();
	}

	/**
	 * @brief Remove Album by Title
	 * 
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
			LOGGER.info("Album deleted by Title Successfully");
		return response.getStatus();
	}
	
	/**
	 * @brief Add movie to album by Id
	 * 
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
			LOGGER.info("Movie added Successfully to the Album");
		return response.getStatus();
	}
	
	/**
	 * @brief Add movie to an Album by title
	 * 
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
			LOGGER.info("Movie added Successfully to the Album by Title");
		return response.getStatus();
	}
	
	/**
	 * @brief Remove Movie from album
	 * 
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
			LOGGER.info("Movie deleted Successfully from the Album");
		return response.getStatus();
	}
	
	/**
	 * @brief Create Rating for a movie
	 *  
	 * Create rating for movie with given score
	 * @param movieID Unique identifier of the movie that the user has requested to give a rating to
	 * @param score Number from 0 to 100 to score the movie with
	 * @return int Return a status code of the process
	 */
	public static int createRating( String movieID, int score ){
		WebResource webResource = client.resource(addr()).path("rating/");
		ClientResponse response = webResource
				.queryParam("token", sessionToken)
				.queryParam("movie_id", movieID)
				.queryParam("score", Integer.toString(score))
				.put(ClientResponse.class);
		response.close();
		if(response.getStatus() == 200)
			LOGGER.info("Rating created successfully");
		return response.getStatus();
	}
	
	/**
	 * @brief Update rating of a movie
	 * 
	 * Update rating for movie with given score
	 * @param movieID Unique identifier of the movie that the user has requested to update rating
	 * @param score Number from 0 to 100 to score the movie with
	 * @return int Return a status code of the process
	 */
	public static int updateRating( String movieID, int score ){
		WebResource webResource = client.resource(addr()).path("rating/");
		ClientResponse response = webResource
				.queryParam("token", sessionToken)
				.queryParam("movie_id", movieID)
				.queryParam("score", Integer.toString(score))
				.post(ClientResponse.class);
		response.close();
		if(response.getStatus() == 200)
			LOGGER.info("Rating updated successfully");
		return response.getStatus();
	}
	
	/**
	 * @brief Get rating of a Movie
	 * 
	 * Get a rating for a movie of passed id 
	 * @param movieID Unique identifier of the movie that the user has requested to update rating
	 * @return int Return the Movie Rating
	 */
	public static int getRating( String movieID ){
		WebResource webResource = client.resource(addr()).path("rating/");
		ClientResponse response = webResource
				.queryParam("token", sessionToken)
				.queryParam("movie_id", movieID)
				.get(ClientResponse.class);
		if(response.getStatus() == 200){
			LOGGER.info("Rating obtained from server");
			JSONObject jo = new JSONObject(response.getEntity(String.class));
			response.close();
			return jo.getInt("score");
		}
		else
			response.close();
			return -1;
	}
	
	/**
	 * @brief Delete rating of a Movie
	 * 
	 * Delete rating of a movie of passed id
	 * @param movieID Unique identifier of the movie that the user has requested to update rating
	 * @return int Return a status code of the process
	 */
	public static int deleteRating( String movieID ){
		System.out.println(movieID);
		WebResource webResource = client.resource(addr()).path("rating/");
		ClientResponse response = webResource
				.queryParam("token", sessionToken)
				.queryParam("movie_id", movieID)
				.delete(ClientResponse.class);
		response.close();
		if(response.getStatus() == 200)
			LOGGER.info("Rating deleted from server");
		return response.getStatus();
	}
	
	/**
	 * @brief Check if a movie Has been watched
	 * 
	 * Check if a movie has been watched by the User
	 * @param movieID Unique identifier of the movie that has requested to check
	 * @return Boolean Returns a Boolean depending on whether the Movie is watched or not 
	 */
	public static boolean isWatched(String movieID){
		return watchedIDs.contains(movieID);
	}

	/**
	 * @brief Close the client
	 * 
	 * Close the Client interface
	 */
	public static void closeClient(){
		client.destroy();
		LOGGER.info("Client closed");
	}

	/**
	 * @brief Get the Client Bundle
	 * 
	 * Get the Client Bundle
	 * @return ResourceBundle Returns the Client Bundle
	 */
	public static ResourceBundle getBundle() {
		return bundle;
	}

	/**
	 * @brief Set the Bundle
	 * 
	 * Set the Bundle for the Client
	 * @param bundle The ResourceBundle object to set
	 */
	public static void setBundle( String language ) {
		ResourceBundle bundle = ResourceBundle.getBundle("SystemMessages_" + language);
		MovieManagerClient.bundle = bundle;
	}

	/**
	 * @brief Get session Token
	 * 
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
			Thread.sleep(10000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
