package server.web.resources.json;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import commons.Posizione;
import commons.Utente;
import server.backend.wrapper.UserRegistryAPI;

/**
 * The Class LastPositionAllUsersJSON defines Web resource to get last position of each user (HTTP method GET).
 */
public class LastPositionAllUsersJSON extends ServerResource{

	private Map<String,Posizione> ultimaPosizioneUtenti;

	/**
	 * Gets map with last position of each registered user.
	 *
	 * @return ultimaPosizioneUtenti map with username of the user as key and his last position as value
	 */
	//Metodo per l'ottenimento dell'ultima posizione di ogni utente
	@Get
	public String getUltimaPosizioneOgniUtente() {
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		UserRegistryAPI userregAPI = UserRegistryAPI.instance();
		Timestamp dataMin=new Timestamp(0);
		ultimaPosizioneUtenti = new HashMap<String,Posizione>();
		TreeMap<String,Utente> utenti;
		Posizione posizione;
		
		utenti=userregAPI.getUtenti();
		for(Map.Entry<String, Utente> entry : utenti.entrySet()){
			posizione=null;
			dataMin.setTime(0);
			for(Posizione p : utenti.get(entry.getKey()).getPosizioni()){
				if(p.getIdPosizione().getTimestamp().after(dataMin)){
					posizione=p;
					dataMin=p.getIdPosizione().getTimestamp();
				}
			}
			if(posizione!=null){
				ultimaPosizioneUtenti.put(entry.getKey(), posizione);				
			}
		}
		System.out.println(ultimaPosizioneUtenti);
		return gson.toJson(ultimaPosizioneUtenti, MyTypeToken.listType().getType());
	}
	
	
	/**
	 * The Class MyTypeToken is an inner class for managing the serialization of Map with type of key as String and type of value as Posizione objects in JSON.
	 */
	static class MyTypeToken {
		
		/**
		 * List type.
		 *
		 * @return the type token
		 */
		static TypeToken<HashMap<String,Posizione>> listType() {
			return new TypeToken<HashMap<String,Posizione>>() {};
		}
	}
}
