package server.web.resources.json;

import org.restlet.data.Status;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;

import commons.ErrorCodes;
import commons.InvalidUsernameException;
import commons.Posizione;
import commons.Utente;
import server.backend.wrapper.UserRegistryAPI;
import server.web.frontend.UserRegistryWebApplication;

public class UserJSON extends ServerResource{
	
	@Get
	public String getUser(){
		Gson gson=new Gson();
		UserRegistryAPI urapi=UserRegistryAPI.instance();
		return null;
		/*try{
			Utente u=urapi.get(getAttribute("username"));
			return gson.toJson(u,Utente.class);
		}catch(InvalidUsernameException e){
			Status s=new Status(ErrorCodes.INVALID_USERNAME_CODE);
			setStatus(s);
			return gson.toJson(e, InvalidUsernameException.class);
		}*/
		
	}
	
	
	@Post
	
	public String addPosition(String payload){
		Gson gson=new Gson();
		UserRegistryAPI urapi=UserRegistryAPI.instance();
		return null;
		/*try{
			
			Posizione p=gson.fromJson(payload, Posizione.class);
			urapi.addUserPosition(getAttribute("username"), p);
			return gson.toJson("Position addet to "+getAttribute("username"),String.class);
		}catch(InvalidUsernameException e){
			Status s=new Status(ErrorCodes.INVALID_USERNAME_CODE);
			setStatus(s);
			return gson.toJson(e, InvalidUsernameException.class);
		}*/
		
		
	}
	
	@Delete
	public String deleteUser(){
		Gson gson=new Gson();
		UserRegistryAPI urapi=UserRegistryAPI.instance();
		return null;
		/*try{
			urapi.remove(getAttribute("username"));
			UserRegistryWebApplication.verifier.getLocalSecrets().remove(getAttribute("username"));
			return gson.toJson("User eliminated: "+getAttribute("username"),String.class);
		}catch(InvalidUsernameException e){
			Status s=new Status(ErrorCodes.INVALID_USERNAME_CODE);
			setStatus(s);
			return gson.toJson(e, InvalidUsernameException.class);
		}*/
		
	}

}
