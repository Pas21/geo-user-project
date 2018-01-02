package server.web.resources.json;

import java.sql.Timestamp;
import java.util.Set;

import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import commons.ErrorCodes;
import commons.InvalidDateException;
import commons.InvalidUsernameException;
import commons.Posizione;
import server.backend.wrapper.UserRegistryAPI;

// TODO: Auto-generated Javadoc
/**
 * The Class PositionsByUserAndDateJSON defines Web resource to get all positions 
 * of an user, specified by his username in the URI, added in a range defined by an initial date and a final date (HTTP method GET).
 */
public class PositionsByUserAndDateJSON extends ServerResource{
	
	/**
	 * Gets all positions of an user, specified by his username in the URI, added in a range defined by an initial date and a final date in the URI.
	 *
	 * @return posizioni all position of the user in the specified range
	 */
	//Metodo per l'ottenimento delle posizioni di un determinato utente che ricadono entro un certo intervallo temporale
	@Get
	public String getPosizioniByData() {
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		UserRegistryAPI userregAPI = UserRegistryAPI.instance();
		String username = getAttribute("username");
		String fromS=getAttribute("fromdata");
		String toS=getAttribute("todata");
		Timestamp from,to;
		
		try {
			if(getAttribute("fromdata").equalsIgnoreCase("null") && !getAttribute("todata").equalsIgnoreCase("null")){
				from=null;
				to = Timestamp.valueOf(toS.replace('_', ' '));
			}
			else if(!getAttribute("fromdata").equalsIgnoreCase("null") && getAttribute("todata").equalsIgnoreCase("null")){
				from = Timestamp.valueOf(fromS.replace('_', ' '));
				to=null;
			}
			else if(getAttribute("todata").equalsIgnoreCase("null") && getAttribute("fromdata").equalsIgnoreCase("null")){
				from=null;
				to=null;
			}
			else{
				from = Timestamp.valueOf(fromS.replace('_', ' '));
				to = Timestamp.valueOf(toS.replace('_', ' '));
			}
			Set<Posizione> posizioniFiltrate;
			posizioniFiltrate = userregAPI.getPosizioniUtenteByData(username, from, to);
			return gson.toJson(posizioniFiltrate, MyTypeToken.<Posizione>listType().getType());
		} catch (InvalidUsernameException e) {
			Status s = new Status(ErrorCodes.INVALID_USERNAME_CODE);
			setStatus(s);
			return gson.toJson(e, InvalidUsernameException.class);
		} catch (InvalidDateException | IllegalArgumentException e) {
			Status s = new Status(ErrorCodes.INVALID_DATE_CODE);
			setStatus(s);
			return gson.toJson(e, InvalidDateException.class);
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
