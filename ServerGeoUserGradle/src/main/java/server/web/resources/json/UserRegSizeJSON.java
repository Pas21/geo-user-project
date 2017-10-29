package server.web.resources.json;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;

import server.backend.wrapper.UserRegistryAPI;

public class UserRegSizeJSON extends ServerResource{
	@Get
	public String getSize(){
		Gson gson=new Gson();
		UserRegistryAPI urapi=UserRegistryAPI.instance();
		return gson.toJson(new Integer(urapi.size()),Integer.class);
	}
	
}
