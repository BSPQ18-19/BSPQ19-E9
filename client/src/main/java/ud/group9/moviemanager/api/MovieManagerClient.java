package ud.group9.moviemanager.api;

import ud.group9.moviemanager.api.exceptions.SignupException;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.Client;
import ud.group9.moviemanager.utils.Hash;

public class MovieManagerClient {
	private static Client client;
    private String protocol = "http";
    private String host = "127.0.0.1";
    private int port = 8000;
    private String basepath = "videoclub";

    public MovieManagerClient(String host, int port) {
        this.host = host;
        this.port = port;
        client = Client.create();
        // Open GUI
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
    
    public boolean LogIn( String username, String password) throws SignupException {
    	// Hash password so no plaintext password is sent through the network
    	String hashedPassword = Hash.encodeHash(Hash.sha256Hash(password));

 	    WebResource webResource = client.resource(addr()).path("login/");
    	    ClientResponse response = webResource
    	            .queryParam("username", username)
    	            .queryParam("password", hashedPassword)
    	            .get(ClientResponse.class);
    	    response.close();
    	    return (response.getStatus() == 200);
    }
    
    public void closeClient(){
    	client.destroy();
    }

	public static void main(String[] args) {    	
		MovieManagerClient mmc = new MovieManagerClient("127.0.0.1", 8000);
		System.out.println("MovieManager Client");
		try {
	    	
			System.out.println(mmc.SignUp("user1", "user"));
			System.out.println(mmc.SignUp("user2", "user"));
			System.out.println(mmc.LogIn("user1", "user"));
			Thread.sleep(10000);
			mmc.closeClient();
		} catch (SignupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
