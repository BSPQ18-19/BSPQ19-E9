package ud.group9.moviemanager.api;

import ud.group9.moviemanager.api.exceptions.SearchMovieException;
import ud.group9.moviemanager.api.exceptions.SignupException;
import ud.group9.moviemanager.data.Album;
import ud.group9.moviemanager.data.Movie;
import ud.group9.moviemanager.gui.LoginGUI;
import ud.group9.moviemanager.gui.SignupGUI;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import java.util.ArrayList;
import java.util.ResourceBundle;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import ud.group9.moviemanager.utils.Hash;

public enum MovieManagerClient {
	INSTANCE;
	private static Client client = Client.create();
	private static String protocol = "http";
	private static String host = "127.0.0.1";
	private static int port = 8000;
	private static String basepath = "moviemanager";

	private static ResourceBundle bundle = ResourceBundle.getBundle("SystemMessages_en");
	private static String sessionToken = null;
	
	private MovieManagerClient(){
	}
	
	public void setConnection(String host, int port) {
		MovieManagerClient.host = host;
		MovieManagerClient.port = port;
	}
	public static void start(){
		new SignupGUI();
	}
	private static String addr() {
		return protocol + "://" + host + ":" + port + "/" + basepath;
	}

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
			mensaje = "200 - New user stored properly";
			break;
		case 401:
			mensaje = "401 - Username already taken";
			break;
		default:
			mensaje = "400 - Error, please contact the administrator";
			break;
		}
		response.close();
		return mensaje;
	}

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
		}
		response.close();
		return (response.getStatus() == 200);
	}

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
		response.close();
		return moviesSearched;
	}

	public static Movie getMovie( String movieID ){
		Movie movie = null;
		WebResource webResource = client.resource(addr()).path("movie/" + movieID + "/");
		ClientResponse response = webResource
				.get(ClientResponse.class);
		movie = Movie.fromJSON( new JSONObject(response.getEntity(String.class)));
		response.close();
		return movie;
	}
	
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
		response.close();
		return movies;
	}
	
	public static boolean addToWatched( String movieID ) throws SearchMovieException {

		WebResource webResource = client.resource(addr()).path("watched/");
		ClientResponse response = webResource
				.queryParam("token", sessionToken)
				.queryParam("movie_id", movieID)
				.post(ClientResponse.class);
		response.close();
		return (response.getStatus() == 200);
	}
	
	public static boolean deleteFromWatched( String movieID ) throws SearchMovieException {

		WebResource webResource = client.resource(addr()).path("watched/");
		ClientResponse response = webResource
				.queryParam("token", sessionToken)
				.queryParam("movie_id", movieID)
				.delete(ClientResponse.class);
		response.close();
		return (response.getStatus() == 200);
	}
	public static int createAlbum( String title ){
		WebResource webResource = client.resource(addr()).path("album/");
		ClientResponse response = webResource
				.queryParam("token", sessionToken)
				.queryParam("title", title)
				.put(ClientResponse.class);
		response.close();
		return response.getStatus();
	}

	public static ArrayList<Album> getAlbums(){
		ArrayList<Album> albums = new ArrayList<>();

		WebResource webResource = client.resource(addr()).path("user/albums/");
		ClientResponse response = webResource
				.queryParam("token", sessionToken)
				.get(ClientResponse.class);
		JSONObject jo = new JSONObject(response.getEntity(String.class));
		JSONArray joa = jo.getJSONArray("albums");
		for (int i = 0; i < joa.length(); i++){
			albums.add(Album.fromJSON(joa.getJSONObject(i)));
		}
		response.close();
		return albums;
	}

	public static Album getAlbum(String albumID){
		WebResource webResource = client.resource(addr()).path("album/" + albumID + "/");
		ClientResponse response = webResource
				.queryParam("token", sessionToken)
				.get(ClientResponse.class);
		JSONObject jo = new JSONObject(response.getEntity(String.class));
		response.close();
		return Album.fromJSON(jo);
	}

	public static int deleteAlbum(String albumID){
		WebResource webResource = client.resource(addr()).path("album/" + albumID + "/");
		ClientResponse response = webResource
				.queryParam("token", sessionToken)
				.delete(ClientResponse.class);
		response.close();
		return response.getStatus();
	}

	public static int addMovieToAlbum( String albumID, String movieID ){
		WebResource webResource = client.resource(addr()).path("album/" + albumID + "/");
		ClientResponse response = webResource
				.queryParam("token", sessionToken)
				.queryParam("movie_id", movieID)
				.post(ClientResponse.class);
		response.close();
		return response.getStatus();
	}
	
	public static int deleteMovieFromAlbum( String albumID, String movieID ){
		WebResource webResource = client.resource(addr()).path("album/" + albumID + "/");
		ClientResponse response = webResource
				.queryParam("token", sessionToken)
				.queryParam("movie_id", movieID)
				.delete(ClientResponse.class);
		response.close();
		return response.getStatus();
	}

	public static void closeClient(){
		client.destroy();
	}

	public static ResourceBundle getBundle() {
		return bundle;
	}

	public static void setBundle(ResourceBundle bundle) {
		MovieManagerClient.bundle = bundle;
	}

	public static void main(String[] args) { 
		//		new MovieManagerClient(args[0], Integer.parseInt(args[1]));
		try {
			MovieManagerClient.start();
			//			System.out.println(mmc.SignUp("user", "test_password"));
			MovieManagerClient.LogIn("user", "test_password");
			//			mmc.createAlbum("albumToDelete");
			//			mmc.deleteAlbum(mmc.getAlbums().get(0).getAlbumID());
//			System.out.println(mmc.getAlbums());
//			System.out.println(mmc.searchForMovie("Scott Pilgrim", "2010").toString());
			System.out.println(MovieManagerClient.getWatched());
			System.out.println(MovieManagerClient.getMovie(MovieManagerClient.searchForMovie("Scott Pilgrim", "2010").get(0).getMovieID()));
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
