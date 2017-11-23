package server.web.resources.json;

import java.util.HashSet;
import java.util.Set;

import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;

import commons.ErrorCodes;
import commons.InvalidUsernameException;
import commons.Posizione;
import server.backend.wrapper.UserRegistryAPI;

public class PositionsByUserJSON extends ServerResource{
	@Get
	public String getPosizioniByUtente() {
		Gson gson = new Gson();
		UserRegistryAPI userregAPI = UserRegistryAPI.instance();
		String username = getAttribute("username");
		
		try {
			Set<Posizione> posizioniFiltrate = userregAPI.getPosizioniByUtente(username);
			return gson.toJson(posizioniFiltrate, HashSet.class);
		} catch (InvalidUsernameException e) {
			Status s = new Status(ErrorCodes.INVALID_USERNAME_CODE);
			setStatus(s);
			return gson.toJson(e, InvalidUsernameException.class);
		}
	}
}
