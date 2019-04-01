package ud.group9.moviemanager.api;

import ud.group9.moviemanager.api.exceptions.SignupException;
import ud.group9.moviemanager.data.User;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;

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
    	ClientConfig config = new ClientConfig();

        Client client = ClientBuilder.newClient(config);
        WebTarget webTarget = client.target(addr()).path("signup/").matrixParam("username", "prueba").matrixParam("password", "prueba");
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        //User user = new User("user", null);
        //Response response = invocationBuilder.post(Entity.entity(user, MediaType.APPLICATION_XML));
        //System.out.println(response);
    }

	public static void main(String[] args) {    	
		MovieManagerClient mmc = new MovieManagerClient("127.0.0.1", 8000);
		System.out.println("MovieManager Client");
		try {
			mmc.SignUp("user", "user");
		} catch (SignupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
