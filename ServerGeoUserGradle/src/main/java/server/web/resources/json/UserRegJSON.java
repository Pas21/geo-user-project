package server.web.resources.json;

import java.util.StringTokenizer;

import org.restlet.data.Status;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import org.restlet.security.MapVerifier;

import com.google.gson.Gson;

import commons.ErrorCodes;
import commons.InvalidEmailException;
import commons.InvalidUsernameException;
import commons.Utente;
import server.backend.wrapper.UserRegistryAPI;
import server.web.frontend.UserRegistryWebApplication;

// TODO: Auto-generated Javadoc
/**
 * The Class UserRegJSON defines Web resources to sign a new user (HTTP method POST) and to authenticate a user (HTTP method PUT).
 */
public class UserRegJSON extends ServerResource{
	

	/**
	 * Signs a new user.
	 *
	 * @param payload the user to sign in JSON
	 * @return the string with the result of the HTTP request
	 */
	//Metodo per l'aggiunta di un utente
	@Post
	public String addUser(String payload){
		Gson gson=new Gson();
		UserRegistryAPI urapi=UserRegistryAPI.instance();
		Utente u=gson.fromJson(payload, Utente.class);
		try{
			urapi.addUtente(u);
			UserRegistryWebApplication.verifier.getLocalSecrets().put(u.getUsername(), u.getPassword().toCharArray());
			return gson.toJson("L'utente: '"+u.getUsername()+"' e' stato aggiunto!",String.class);
		}catch(InvalidUsernameException e){
			Status s=new Status(ErrorCodes.INVALID_USERNAME_CODE);
			setStatus(s);
			return gson.toJson(e, InvalidUsernameException.class);
		}catch(InvalidEmailException e){
			Status s=new Status(ErrorCodes.INVALID_EMAIL_CODE);
			setStatus(s);
		return gson.toJson(e, InvalidEmailException.class);
		}
	}
	
	/**
	 * Authenticates an user checking his credentials on the MapVerifier.
	 *
	 * @param payload the credentials of the user in JSON
	 * @return True, if authenticated and False otherwise, both in JSON
	 */
	//Metodo per l'autenticazione dell'utente
	@Put
	public String checkUser(String payload){
		Gson gson = new Gson();
		String response=gson.fromJson(payload, String.class);
		StringTokenizer st = new StringTokenizer(response,";");	
		String username = st.nextToken();
		String password = st.nextToken();
		if(UserRegistryWebApplication.verifier.verify(username, password.toCharArray())==MapVerifier.RESULT_VALID) 
			return gson.toJson(true,Boolean.class);
		return gson.toJson(false, Boolean.class);
		
	}

}
