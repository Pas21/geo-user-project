package server.web.resources.json;

import java.util.StringTokenizer;

import org.restlet.data.Status;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;

import commons.ErrorCodes;
import commons.InvalidUsernameException;
import commons.User;
import server.backend.wrapper.UserRegistryAPI;
import server.web.frontend.UserRegistryWebApplication;

public class UserAuthJSON extends ServerResource{
	@Post
	public String verifyUser(String payload){
		Gson gson=new Gson();
		UserRegistryAPI urapi=UserRegistryAPI.instance();
		
		try{
			String data=gson.fromJson(payload, String.class);
			StringTokenizer st=new StringTokenizer(data,"&");
			String username=st.nextToken();
			String password=st.nextToken();
			if(urapi.verifyUser(username, password)){
				UserRegistryWebApplication.verifier.getLocalSecrets().put(username, password.toCharArray());
				return gson.toJson("authenticated",String.class);
			}
			return gson.toJson("notAuthenticated",String.class);
		}catch(InvalidUsernameException e){
			Status s=new Status(ErrorCodes.INVALID_USERNAME_CODE);
			setStatus(s);
			return gson.toJson(e,InvalidUsernameException.class);
			
		}	
	}
	
	
	@Put
	public String addUser(String payload){
		Gson gson=new Gson();
		UserRegistryAPI urapi=UserRegistryAPI.instance();
		User u=gson.fromJson(payload, User.class);
		try{
			urapi.add(u);
			UserRegistryWebApplication.verifier.getLocalSecrets().put(u.getUsername(), u.getPassword().toCharArray());
			return gson.toJson("user added: "+u.getUsername(),String.class);
		}catch(InvalidUsernameException e){
			Status s=new Status(ErrorCodes.INVALID_USERNAME_CODE);
			setStatus(s);
			return gson.toJson(e, InvalidUsernameException.class);
		}
	}

}
