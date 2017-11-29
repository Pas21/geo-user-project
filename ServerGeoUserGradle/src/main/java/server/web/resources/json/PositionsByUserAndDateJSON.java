package server.web.resources.json;

import java.sql.Timestamp;
import java.util.Set;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import commons.Posizione;
import server.backend.wrapper.UserRegistryAPI;

public class PositionsByUserAndDateJSON extends ServerResource{
	
	//Metodo per l'ottenimento delle posizioni di un determinato utente che ricadono entro un certo intervallo temporale
	@Get
	public String getPosizioniByData() {
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		UserRegistryAPI userregAPI = UserRegistryAPI.instance();
		String username = getAttribute("username");
		Timestamp from = Timestamp.valueOf(getAttribute("fromdata").replace('_', ' '));
		Timestamp to = Timestamp.valueOf(getAttribute("todata").replace('_', ' '));
		
		Set<Posizione> posizioniFiltrate = userregAPI.getPosizioniUtenteByData(username, from, to);
		return gson.toJson(posizioniFiltrate, MyTypeToken.<Posizione>listType().getType());
	}
	
	//Inner class per la gestione della serializzazione degli oggetti in JSON
	static class MyTypeToken {
		static <T> TypeToken<Set<T>> listType() {
			return new TypeToken<Set<T>>() {};
		}
	}
}
