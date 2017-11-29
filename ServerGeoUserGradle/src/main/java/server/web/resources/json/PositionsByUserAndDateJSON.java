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

public class PositionsByUserAndDateJSON extends ServerResource{
	
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

	//Inner class per la gestione della serializzazione degli oggetti in JSON
	static class MyTypeToken {
		static <T> TypeToken<Set<T>> listType() {
			return new TypeToken<Set<T>>() {};
		}
	}
}
