package server.backend.wrapper;

import java.sql.Timestamp;
import java.util.Set;
import java.util.TreeMap;

import commons.IdPosizione;
import commons.InvalidDateException;
import commons.InvalidEmailException;
import commons.InvalidPositionException;
import commons.InvalidUsernameException;
import commons.Posizione;
import commons.Utente;
import server.backend.UserRegistry;

/**
 * The Class UserRegistryAPI is a wrapper class of the User Registry class and defines an API to interact with the server backend.
 */
public class UserRegistryAPI {
	
	/**
	 * Instantiates a new user registry API and indirectly instantiates 
	 * a new user registry that creates an instance of the GestoriDatiPersistenti class 
	 * and takes care of loading the data from the database.
	 */
	private UserRegistryAPI(){
		this.userreg=new UserRegistry();
	}
	
	/**
	 * Singleton instance of the UserRegistryAPI class.
	 *
	 * @return instance the singleton instance of the UserRegistryAPI class
	 */
	public static synchronized UserRegistryAPI instance(){
		if(instance==null)
			instance=new UserRegistryAPI();
		return instance;
	}
	
	/**
	 * Adds an user to set of registered users and inserts him to the database.
	 *
	 * @param newUtente the new user to add
	 * @throws InvalidUsernameException the invalid username exception, username of the new user already exists
	 * @throws InvalidEmailException the invalid email exception, email of the new user already exists
	 */
	//Metodo per l'aggiunta di un nuovo utente
	public synchronized void addUtente(Utente newUtente) throws InvalidUsernameException, InvalidEmailException{
		this.userreg.addUtente(newUtente);
	}
	
	/**
	 * Removes an user, specified by his username, from set of registered users and deletes him from the database.
	 *
	 * @param username the username of the user to remove 
	 * @return true, if successful
	 * @throws InvalidUsernameException the invalid username exception, username doesn't exist in the set of registered usernames
	 */
	//Metodo per la rimozione di un utente
	public synchronized boolean removeUtente(String username) throws InvalidUsernameException{
		return this.userreg.removeUtente(username);
	}
		
	/**
	 * Adds a user position to set of positions of its user and inserts it to the database.*
	 * 
	 * @param newPosizione the new position to add
	 * @throws InvalidUsernameException the invalid username exception, username doesn't exist
	 * @throws InvalidPositionException the invalid position exception, position already exists
	 */
	//Metodo per l'aggiunta di una posizione
	public synchronized void addPosizione(Posizione newPosizione) throws InvalidUsernameException, InvalidPositionException{
		this.userreg.addPosizione(newPosizione);
	}
	
	/**
	 * Removes a user position, specified by its id, from set of positions of its user and deletes him from the database.
	 *
	 * @param idPosizione the id posizione
	 * @return true, if successful
	 * @throws InvalidPositionException the invalid position exception, position doesn't exist
	 */
	//Metodo per la rimozione di una posizione
	public synchronized boolean removePosizione(IdPosizione idPosizione) throws InvalidPositionException {
		return this.userreg.removePosizione(idPosizione);
	}
	
	/**
	 * Removes all positions of an user specified by his username.
	 *
	 * @param username the username of the user to remove
	 * @return true, if successful
	 * @throws InvalidUsernameException the invalid username exception, username doesn't exist
	 */
	//Metodo per la rimozione di tutte le posizioni di un determinato utente
	public synchronized boolean removePosizioni(String username) throws InvalidUsernameException {
		return this.userreg.removePosizioni(username);
	}
	
	/**
	 * Gets all positions of an user specified by his username.
	 *
	 * @param username the username of the user
	 * @return posizioni all position of the user
	 * @throws InvalidUsernameException the invalid username exception, username doesn't exists
	 */
	//Metodo per l'ottenimento di tutte le posizioni di un determinato utente
	public synchronized Set<Posizione> getPosizioniByUtente(String username) throws InvalidUsernameException {
		return this.userreg.getPosizioniByUtente(username);
	}
	
	/**
	 * Gets all positions of an user, specified by his username, added in a range defined by an initial date and a final date.
	 *
	 * @param username the username of the user
	 * @param from initial date of the range 
	 * @param to final date of the range 
	 * @return posizioni all position of the user in the specified range
	 * @throws InvalidUsernameException the invalid username exception, username doesn't exist
	 * @throws InvalidDateException the invalid date exception, incorrect values of the dates
	 */
	//Metodo per l'ottenimento di tutte le posizioni di un utente che ricadono entro un certo intervallo di tempo
	public synchronized Set<Posizione> getPosizioniUtenteByData(String username, Timestamp from, Timestamp to) throws InvalidUsernameException, InvalidDateException {
		return this.userreg.getPosizioniUtenteByData(username, from, to);
	}
	
	/**
	 * Gets all registered users.
	 *
	 * @return utenti all registered users
	 */
	//Metodo per l'ottenimento di tutti gli utenti
	public synchronized TreeMap<String, Utente> getUtenti() {
		return this.userreg.getUtenti();
	}
	
	/**
	 * Gets all registered positions.
	 *
	 * @return posizioni all registered positions
	 */
	public TreeMap<IdPosizione, Posizione> getPosizioni() {
		return this.userreg.getPosizioni();
	}
	
	/** The singleton instance of UserRegistryAPI class. */
	private static UserRegistryAPI instance;
	
	/** The instance of UserRegistry class. */
	private UserRegistry userreg;
	

}
