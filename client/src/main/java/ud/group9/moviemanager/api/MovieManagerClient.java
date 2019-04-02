package ud.group9.moviemanager.api;

import ud.group9.moviemanager.api.exceptions.SignupException;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.Client;

public class MovieManagerClient {
    private String protocol = "http";
    private String host = "127.0.0.1";
    private int port = 8000;
    private String basepath = "videoclub";

    public MovieManagerClient(String host, int port) {
        this.host = host;
        this.port = port;
        // Open GUI
    }

    private String addr() {
        return this.protocol + "://" + this.host + ":" + this.port + "/" + this.basepath;
    }
    public void SignUp(String username, String password) throws SignupException {
    
    	 Client client = Client.create();
    	    WebResource webResource = client.resource(addr()).path("signup/");
    	    ClientResponse response = webResource
    	            .queryParam("username", username)
    	            .queryParam("password", password)
    	            .post(ClientResponse.class);
    	    System.out.println(response);
    	
    }

	public static void main(String[] args) {    	
		MovieManagerClient mmc = new MovieManagerClient("127.0.0.1", 8000);
		System.out.println("MovieManager Client");
		try {
			mmc.SignUp("user37894", "user");
		} catch (SignupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
