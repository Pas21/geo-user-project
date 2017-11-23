package server.web.resources.json;

import org.restlet.data.Status;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;

import commons.ErrorCodes;
import commons.InvalidPositionException;
import commons.InvalidUsernameException;
import commons.Posizione;
import server.backend.wrapper.UserRegistryAPI;

public class PositionRegJSON extends ServerResource{
	@Post
	public String addPosizione(String payload) {
		Gson gson = new Gson();
		UserRegistryAPI userregAPI = UserRegistryAPI.instance();
		
		Posizione newPosizione = gson.fromJson(payload, Posizione.class);
			try {
				userregAPI.addPosizione(newPosizione);
				return gson.toJson("La posizione con id " + newPosizione.getIdPosizione().toString() + " è stata aggiunta!",String.class);
			} catch (InvalidUsernameException e) {
				Status s = new Status(ErrorCodes.INVALID_USERNAME_CODE);
				setStatus(s);
				return gson.toJson(e, InvalidUsernameException.class);
			} catch (InvalidPositionException e) {
				Status s = new Status(ErrorCodes.INVALID_POSITION_CODE);
				setStatus(s);
				return gson.toJson(e, InvalidPositionException.class);
			}
	}
}
