package server.web.resources.json;


import java.util.Map;
import java.util.TreeMap;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;

import commons.Utente;
import server.backend.wrapper.UserRegistryAPI;

public class UserAuthJSON extends ServerResource{

	//Metodo per l'ottenimento di tutti gli username degli utenti
	@Get
	public String getUsers(){
		Gson gson=new Gson();
		UserRegistryAPI urapi=UserRegistryAPI.instance();
		TreeMap<String,Utente> utenti = urapi.getUtenti();
		String[] usernameUtenti = new String[urapi.getUtenti().size()];
		int i=0;	
		for(Map.Entry<String, Utente> entry : utenti.entrySet()) {			
			usernameUtenti[i++] = entry.getKey();
		}
		return gson.toJson(usernameUtenti,String[].class);
	}
	
}
