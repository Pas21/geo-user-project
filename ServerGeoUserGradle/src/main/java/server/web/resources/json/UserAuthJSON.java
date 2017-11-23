package server.web.resources.json;


import org.restlet.data.Status;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;

import commons.ErrorCodes;
import commons.InvalidUsernameException;
import commons.Utente;
import server.backend.wrapper.UserRegistryAPI;
import server.web.frontend.UserRegistryWebApplication;

public class UserAuthJSON extends ServerResource{
	
	//Metodo per l'aggiunta di un utente
	@Post
	public String addUser(String payload){
		Gson gson=new Gson();
		UserRegistryAPI urapi=UserRegistryAPI.instance();
		Utente u=gson.fromJson(payload, Utente.class);		try{
			urapi.addUtente(u);
			UserRegistryWebApplication.verifier.getLocalSecrets().put(u.getUsername(), u.getPassword().toCharArray());
			return gson.toJson("L'utente: '"+u.getUsername()+"' e' stato aggiunto!",String.class);
		}catch(InvalidUsernameException e){
			Status s=new Status(ErrorCodes.INVALID_USERNAME_CODE);
			setStatus(s);
			return gson.toJson(e, InvalidUsernameException.class);
		}
	}
	
}
