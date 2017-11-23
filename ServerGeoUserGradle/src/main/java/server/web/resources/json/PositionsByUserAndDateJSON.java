package server.web.resources.json;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;

import commons.Posizione;
import server.backend.wrapper.UserRegistryAPI;

public class PositionsByUserAndDateJSON extends ServerResource{
	@Get
	public String getPosizioniByData() {
		Gson gson = new Gson();
		UserRegistryAPI userregAPI = UserRegistryAPI.instance();
		String username = getAttribute("username");
		Timestamp from = Timestamp.valueOf(getAttribute("fromdata").replace('_', ' '));
		Timestamp to = Timestamp.valueOf(getAttribute("todata").replace('_', ' '));
		
		Set<Posizione> posizioniFiltrate = userregAPI.getPosizioniUtenteByData(username, from, to);
		if(!posizioniFiltrate.isEmpty()) {
			return gson.toJson(posizioniFiltrate, HashSet.class);
		}
		else {
			return gson.toJson("Non ci sono posizioni dell'utente con username " + username + "tra le date indicate!", String.class);
		}
		
	}
}
