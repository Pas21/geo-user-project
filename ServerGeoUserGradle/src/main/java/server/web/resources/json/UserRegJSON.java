package server.web.resources.json;

import java.util.StringTokenizer;

import org.restlet.data.Status;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import org.restlet.security.Verifier;

import com.google.gson.Gson;

import commons.ErrorCodes;
import commons.InvalidUsernameException;
import commons.Utente;
import server.backend.wrapper.UserRegistryAPI;
import server.web.frontend.UserRegistryWebApplication;

public class UserRegJSON extends ServerResource{
	

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
		}
	}
	
	//Metodo per l'autenticazione dell'utente
	@Put
	public String checkUser(String payload){
		Gson gson = new Gson();
		StringTokenizer st = new StringTokenizer(payload,";");	    	
		if(UserRegistryWebApplication.verifier.verify(st.nextToken(), st.nextToken().toCharArray())==Verifier.RESULT_VALID)
			return gson.toJson(true, Boolean.class);
		return gson.toJson(false, Boolean.class);
	}

}
