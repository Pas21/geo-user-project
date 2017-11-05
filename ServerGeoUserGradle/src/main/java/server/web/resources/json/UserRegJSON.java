package server.web.resources.json;

import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;

import commons.InvalidUsernameException;
import commons.Utente;
import server.backend.wrapper.UserRegistryAPI;

public class UserRegJSON extends ServerResource{
	@Get
	public String getUsers(){
		Gson gson=new Gson();
		UserRegistryAPI urapi=UserRegistryAPI.instance();
		return gson.toJson(urapi.getAllUsers(),String[].class);
	}
	
	
	
	@Put
	public String updateUser(String payload){
		Gson gson=new Gson();
		UserRegistryAPI urapi=UserRegistryAPI.instance();
		Utente u=gson.fromJson(payload, Utente.class);
		urapi.update(u);
		return gson.toJson("User updated:"+ u.getUsername(),String.class);	
	}
	
	@Delete
	public String deleteAllUser() throws InvalidUsernameException{
		Gson gson=new Gson();
		UserRegistryAPI urapi=UserRegistryAPI.instance();
		for(String username:urapi.getAllUsers())
			urapi.remove(username);
		return gson.toJson("All User are deleted ",String.class);
		
	}

}
