package ud.group9.moviemanager.api;

import ud.group9.moviemanager.api.exceptions.*;
import ud.group9.moviemanager.data.Movie;
import ud.group9.moviemanager.gui.LoginGUI;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import java.util.ArrayList;
import java.util.ResourceBundle;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import ud.group9.moviemanager.utils.Hash;

public class MovieManagerClient {
	private static Client client;
	private String protocol = "http";
	private String host = "127.0.0.1";
	private int port = 8000;
	private String basepath = "videoclub";

	private ResourceBundle bundle = ResourceBundle.getBundle("SystemMessages_en");
	private String sessionToken = null;

	public MovieManagerClient(String host, int port) {
		this.host = host;
		this.port = port;
		client = Client.create();
		// Open GUI
		new LoginGUI(this);
	}

	private String addr() {
		return this.protocol + "://" + this.host + ":" + this.port + "/" + this.basepath;
	}

	public String SignUp( String username, String password) throws SignupException {
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

	public boolean LogIn( String username, String password ) throws SignupException {
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

	public ArrayList<Movie> searchForMovie( String title, String year ) throws SearchMovieException {

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

	public boolean addToWatched( String movieID ) throws SearchMovieException {

		WebResource webResource = client.resource(addr()).path("watched/");
		ClientResponse response = webResource
				.queryParam("token", sessionToken)
				.queryParam("movie_id", movieID)
				.get(ClientResponse.class);
		response.close();
		return (response.getStatus() == 200);
	}

	public void closeClient(){
		client.destroy();
	}

	public ResourceBundle getBundle() {
		return bundle;
	}

	public void setBundle(ResourceBundle bundle) {
		this.bundle = bundle;
	}

	public static void main(String[] args) { 
//		MovieManagerClient mmc = new MovieManagerClient("127.0.0.1", 8000);
		new MovieManagerClient(args[0], Integer.parseInt(args[1]));
		//		try {

		//			System.out.println(mmc.SignUp("user1", "user"));
		//			System.out.println(mmc.SignUp("user2", "user"));
		//			System.out.println(mmc.LogIn("test_user", "test_password"));
		//			System.out.println(mmc.searchForMovie("Scott Pilgrim", "2010"));
		//			System.out.println(mmc.addToWatched(mmc.searchForMovie("Scott Pilgrim", "2010").get(0).getMovieID()));
		//			Thread.sleep(10000);
		// mmc.closeClient();
		//		} catch (SignupException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		} catch (InterruptedException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		} catch (SearchMovieException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
	}
}
