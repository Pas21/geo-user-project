package server.web.resources.json;

import org.restlet.data.Status;
import org.restlet.resource.Delete;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;

import commons.ErrorCodes;
import commons.InvalidUsernameException;
import server.web.frontend.UserRegistryWebApplication;

public class UserLogOutJSON extends ServerResource{
	@Delete
	public String removeUserCredential(){
		String username=getAttribute("username");
		Gson gson=new Gson();
		try{
			UserRegistryWebApplication.verifier.getLocalSecrets().remove(username);
			return gson.toJson("User credential revocated");
		}catch(Exception e){
			Status s=new Status(ErrorCodes.INVALID_USERNAME_CODE);
			setStatus(s);
			return gson.toJson(e, InvalidUsernameException.class);
		}
	}

	
	
}
