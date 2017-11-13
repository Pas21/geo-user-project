package server.backend.wrapper;


import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

import commons.IdPosizione;
import commons.InvalidPositionException;
import commons.InvalidUsernameException;
import commons.Posizione;
import commons.Utente;
import server.backend.UserRegistry;

public class UserRegistryAPI {
	
	protected UserRegistryAPI(){
		this.userreg=new UserRegistry();
	}
	
	public static synchronized UserRegistryAPI instance(){
		if(instance==null)
			instance=new UserRegistryAPI();
		return instance;
	}
	
	//Metodo per l'aggiunta di un nuovo utente
	public synchronized void addUtente(Utente newUtente) throws InvalidUsernameException{
		this.userreg.addUtente(newUtente);
	}
	
	//Metodo per la rimozione di un utente
	public synchronized void removeUtente(String username) throws InvalidUsernameException{
		this.userreg.removeUtente(username);
	}
		
	//Metodo per l'aggiunta di una posizione
	public synchronized void addPosizione(Posizione newPosizione) throws InvalidUsernameException, InvalidPositionException{
		this.userreg.addPosizione(newPosizione);
	}
	
	//Metodo per la rimozione di una posizione
	public synchronized void removePosizione(IdPosizione idPosizione) throws InvalidPositionException {
		this.userreg.removePosizione(idPosizione);
	}
	
	//Metodo per la rimozione di tutte le posizioni di un determinato utente
	public synchronized void removePosizioni(String username) throws InvalidUsernameException {
		this.userreg.removePosizioni(username);
	}
	
	//Metodo per l'ottenimento di tutte le posizioni di un determinato utente
	public synchronized Set<Posizione> getPosizioniByUtente(String username) throws InvalidUsernameException {
		return this.userreg.getPosizioniByUtente(username);
	}
	
	//Metodo per l'ottenimento di tutte gli utenti
	public synchronized TreeMap<String, Utente> getUtenti() {
		return this.userreg.getUtenti();
	}
	
	private static UserRegistryAPI instance;
	private UserRegistry userreg;
	

}
