package server.web.resources.json;

import java.util.Set;

import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import commons.ErrorCodes;
import commons.InvalidPositionException;
import commons.InvalidUsernameException;
import commons.Posizione;
import server.backend.wrapper.UserRegistryAPI;

public class PositionsByUserJSON extends ServerResource{
	
	//Metodo per l'ottenimento delle posizioni di un utente
		@Get
		public String getPosizioniByUtente() {
			Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
			UserRegistryAPI userregAPI = UserRegistryAPI.instance();
			String username = getAttribute("username");
			
			try {
				Set<Posizione> posizioniFiltrate = userregAPI.getPosizioniByUtente(username);
				return gson.toJson(posizioniFiltrate, MyTypeToken.<Posizione>listType().getType());
			} catch (InvalidUsernameException e) {
				Status s = new Status(ErrorCodes.INVALID_USERNAME_CODE);
				setStatus(s);
				return gson.toJson(e, InvalidUsernameException.class);
			}
		}
		
		//Metodo per l'aggiunta di una nuova posizione ad un determinato utente
		@Post
		public String addPosizione(String payload) {
			Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
			UserRegistryAPI userregAPI = UserRegistryAPI.instance();
			String username = getAttribute("username");
			Posizione newPosizione = gson.fromJson(payload, Posizione.class);
			newPosizione.setUtente(userregAPI.getUtenti().get(username));
				try {
					userregAPI.addPosizione(newPosizione);
					return gson.toJson("La posizione con id " + newPosizione.getIdPosizione().toString() + " e' stata aggiunta!",String.class);
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
		
		//Inner class per la gestione della serializzazione degli oggetti in JSON
		static class MyTypeToken {
			static <T> TypeToken<Set<T>> listType() {
				return new TypeToken<Set<T>>() {};
			}
		}
}
