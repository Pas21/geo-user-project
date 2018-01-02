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

/**
 * The Class PositionsByUserJSON defines Web resources to get positions by user (HTTP method GET) and to add a position  (HTTP method POST).
 */
public class PositionsByUserJSON extends ServerResource{
	
	/**
	 * Gets all positions of an user specified by his username in the URI.
	 *
	 * @return posizioni all position of the user
	 */
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
		
		/**
		 * Adds a user position to set of positions of its user and inserts it to the database.*
		 *
		 * @param payload the position to add in JSON
		 * @return the string with the result of the HTTP request
		 */
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
		
		/**
		 * The Class MyTypeToken is an inner class for managing the serialization of objects in JSON.
		 */
		//Inner class per la gestione della serializzazione degli oggetti in JSON
		static class MyTypeToken {
			
			/**
			 * List type.
			 *
			 * @param <T> the generic type
			 * @return the type token
			 */
			static <T> TypeToken<Set<T>> listType() {
				return new TypeToken<Set<T>>() {};
			}
		}
}
