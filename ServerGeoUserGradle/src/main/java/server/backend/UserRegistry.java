package server.backend;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import commons.IdPosizione;
import commons.InvalidDateException;
import commons.InvalidEmailException;
import commons.InvalidPositionException;
import commons.InvalidUsernameException;
import commons.Utente;
import commons.Posizione;

/**
 * The Class UserRegistry is a back-end class for managing data on the server.
 */
public class UserRegistry {
	
	/**
	 * Instantiates a new user registry that creates an instance of the GestoriDatiPersistenti class and takes care of loading the data from the database.
	 */
	public UserRegistry(){
		//Ottenimento di un'istanza del gestore del database
		this.gestoreDB = GestoreDatiPersistenti.getInstance();
		
		//Caricamento degli utenti e delle posizioni dal database
		this.utenti = this.gestoreDB.getUtenti();
		this.posizioni = this.gestoreDB.getPosizioni();
		
		//Aggiornamento set di posizioni degli utenti
		Set<Posizione> posizioniUtente = new HashSet<Posizione>(0);
		String usernameUtente=null;
		for(Entry<String, Utente> utente : this.utenti.entrySet()) {
			usernameUtente=utente.getValue().getUsername();
			for(Entry<IdPosizione, Posizione> idPos : this.posizioni.entrySet()) {
				Posizione posizione = idPos.getValue();
				if(posizione.getUtente().getUsername().equals(usernameUtente)) {
					posizioniUtente.add(posizione);
				}
			}
			this.utenti.get(usernameUtente).setPosizioni(posizioniUtente);
			posizioniUtente = new HashSet<Posizione>(0);
		}
		
		//Aggiornamento utente delle posizioni
		//Questo aggiornamento puo' essere incluso nel precedente(aggiornamento set di posizioni)
		//E' stato separato solo per motivi di leggibilita' del codice e, inoltre, non inficia molto sulle performance visto che viene eseguito solo una volta all'avvio del server
		for(Entry<String, Utente> utente : this.utenti.entrySet()) {
			for(Entry<IdPosizione, Posizione> idPos : this.posizioni.entrySet()) {
				Posizione posizione = idPos.getValue();
				if(posizione.getUtente().getUsername().equals(utente.getValue().getUsername())) {
					posizione.setUtente(utente.getValue());
					this.posizioni.put(idPos.getKey(), posizione);
				}
			}
		}
	}

	/**
	 * Adds an user to set of registered users and inserts him to the database.
	 *
	 * @param newUtente the new user to add
	 * @throws InvalidUsernameException the invalid username exception, username of the new user already exists
	 * @throws InvalidEmailException the invalid email exception, email of the new user already exists
	 */
	//Metodo per l'aggiunta di un nuovo utente
	public void addUtente(Utente newUtente) throws InvalidUsernameException, InvalidEmailException{
		try{
			//Verifica presenza utente
			if(this.utenti.containsKey(newUtente.getUsername()))
				throw new InvalidUsernameException("L'utente con username " + newUtente.getUsername() + " gia' esiste!");
		
			//Verifica duplicati email
			for(Map.Entry<String, Utente> entry : utenti.entrySet()) {
				if(entry.getValue().getEmail().equals(newUtente.getEmail()))
					throw new InvalidEmailException("L'email '" + newUtente.getEmail() + "' gia' esiste!");
			}
		    
			//Aggiunta utente al database
			if(!this.gestoreDB.addUtente(newUtente)) {
				System.err.println("Errore aggiunta utente al database con username " + newUtente.getUsername() + "!");
			}
			else {
				//Aggiunta utente alla lista degli utenti caricata in memoria
				this.utenti.put(newUtente.getUsername(), newUtente);	
			}
		} catch(NullPointerException e) {
			System.err.println("Exception: java.lang.NullPointerException");
		}
	}
	
	/**
	 * Removes an user, specified by his username, from set of registered users and deletes him from the database.
	 *
	 * @param username the username of the user to remove 
	 * @return true, if successful
	 * @throws InvalidUsernameException the invalid username exception, username doesn't exist in the set of registered usernames
	 */
	//Metodo per la rimozione di un utente esistente
	public boolean removeUtente(String username) throws InvalidUsernameException {
		try {
			//Verifica presenza utente
			if(!this.utenti.containsKey(username))
				throw new InvalidUsernameException("L'utente con username " + username + " non esiste!");
		
			//Ottenimento dell'oggetto utente per l'invocazione del metodo di rimozione di un utente
			//della classe GestoreDatiPersistenti
			Utente utente = this.utenti.get(username);
		
			//Eliminazione di eventuali posizioni associate all'utente (politica CASCADE)
			if(this.removePosizioni(username)) {
				//Eliminazione utente dal database
				if(!this.gestoreDB.removeUtente(utente)) {
					System.out.println("Errore rimozione utente dal database con username " + username + "!");
					return false;
				}
				else{
					//Eliminazione utente dalla lista degli utenti caricati in memoria
					this.utenti.remove(username);
					return true;
				}
			}else return false;
		} catch(NullPointerException e) {
			System.err.println("Exception: java.lang.NullPointerException");
			return false;
		}
	}
	
	/**
	 * Adds a user position to set of positions of its user and inserts it to the database.*
	 * 
	 * @param newPosizione the new position to add
	 * @throws InvalidUsernameException the invalid username exception, username doesn't exist
	 * @throws InvalidPositionException the invalid position exception, position already exists
	 */
	//Metodo per l'aggiunta di una posizione ad un utente esistente
	public void addPosizione(Posizione newPosizione) throws InvalidUsernameException, InvalidPositionException {
		try {
			//Verifica esistenza utente
			String username = newPosizione.getUtente().getUsername();
			if(!this.utenti.containsKey(username))
				throw new InvalidUsernameException("L'utente con username " + username + " non esiste!");
		
			//Verifica esistenza posizione
			if(this.posizioni.containsKey(newPosizione.getIdPosizione()))
				throw new InvalidPositionException("La posizione con id " + newPosizione.getIdPosizione().toString() + " esiste gia'!");
		
			//Aggiunta della posizione al database
			if(!this.gestoreDB.addPosizione(newPosizione)) {
				System.out.println("Errore aggiunta posizione" + newPosizione.getIdPosizione().toString() + " al database!");
			}
			else {
				//Aggiunta della posizione alla lista delle posizioni caricate in memoria
				this.posizioni.put(newPosizione.getIdPosizione(), newPosizione);
			
				//Aggiunta della posizione al set di posizione dell'utente
				this.utenti.get(username).addPosizione(newPosizione);
			}
		} catch(NullPointerException e) {
			System.err.println("Exception: java.lang.NullPointerException");
		}
	}
	
	/**
	 * Removes a user position, specified by its id, from set of positions of its user and deletes him from the database.
	 *
	 * @param idPosizione the id posizione
	 * @return true, if successful
	 * @throws InvalidPositionException the invalid position exception, position doesn't exist
	 */
	//Metodo per la rimozione di una posizione di un utente esistente
	public boolean removePosizione(IdPosizione idPosizione) throws InvalidPositionException {
		try {
			//Verifica presenza posizione
			if(!this.posizioni.containsKey(idPosizione))
				throw new InvalidPositionException("La posizione con id " + idPosizione.toString() + " non esiste!");
		
			//Ottenimento dell'oggetto posizione per l'invocazione del metodo di rimozione di una posizione
			// della classe GestoreDatiPersistenti
			Posizione posizione = this.posizioni.get(idPosizione);
		
			//Eliminazione posizione dal database
			if(!this.gestoreDB.removePosizione(posizione)) {
				System.out.println("Errore rimozione posizione dal database con id " + idPosizione + "!");
				return false;
			}
			else {
				//Rimozione posizione dalla lista delle posizioni caricate in memoria
				this.posizioni.remove(idPosizione);
			
				//Rimozione posizione dal set di posizioni dell'utente
				if(!this.utenti.get(posizione.getUtente().getUsername()).removePosizione(posizione))
					System.out.println("La posizione con id " + idPosizione + "non e' stata eliminata dall'oggetto utente!");
				return true;			
			}
		}catch(NullPointerException e) {
			System.err.println("Exception: java.lang.NullPointerException");
			return false;
		}
	}
	
	/**
	 * Removes all positions of an user specified by his username.
	 *
	 * @param username the username of the user to remove
	 * @return true, if successful
	 * @throws InvalidUsernameException the invalid username exception, username doesn't exist
	 */
	//Metodo per la rimozione di tutte le posizioni di un utente esistente
	public boolean removePosizioni(String username) throws InvalidUsernameException{
		try {
			//Verifica presenza utente
			if(!this.utenti.containsKey(username))
				throw new InvalidUsernameException("L'utente con username " + username + " non esiste!");
		
			//Eliminazione posizioni dell'utente dal database
			if(!this.gestoreDB.removePosizioniUtente(this.utenti.get(username))) {
				System.out.println("Errore rimozione posizioni dell'utente con username " + username);
				return false;
			}
			else {
				//Rimozione posizioni dalla lista delle posizioni caricate in memoria e dal set di posizioni
				//dell'utente
				//Creazione mappa temporanea per evitare la ConcurrentModificationException
				TreeMap<IdPosizione, Posizione> temp = new TreeMap<IdPosizione, Posizione>();
				temp.putAll(this.posizioni);
				for(IdPosizione idPosizione : temp.keySet()) {
					if((this.posizioni.get(idPosizione).getUtente().getUsername()).equals(username)) {
						//Rimozione posizione dalla lista delle posizioni caricate in memoria
						Posizione posizioneRemoved = this.posizioni.remove(idPosizione);
						this.utenti.get(username).removePosizione(posizioneRemoved);
					}
				}
				return true;
			}
		} catch(NullPointerException e) {
			System.err.println("Exception: java.lang.NullPointerException");
			return false;
		}
				
	}
	
	/**
	 * Gets all positions of an user specified by his username.
	 *
	 * @param username the username of the user
	 * @return posizioni all position of the user
	 * @throws InvalidUsernameException the invalid username exception, username doesn't exists
	 */
	//Metodo per l'ottenimento di tutte le posizioni di un determinato utente
	public Set<Posizione> getPosizioniByUtente(String username) throws InvalidUsernameException {
		try {
		//Si potrebbe anche scorrere la lista di posizioni!
		//Verifica presenza utente
		if(!this.utenti.containsKey(username))
			throw new InvalidUsernameException("L'utente con username " + username + " non esiste!");
		
		Utente utente = this.utenti.get(username);
		return utente.getPosizioni();
		} catch(NullPointerException e) {
			System.err.println("Exception: java.lang.NullPointerException");
			return new HashSet<Posizione>(0);
		}
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
	//Metodo per l'ottenenimento di tutte le posizioni di un utente che ricadono entro un intervallo di tempo
	public Set<Posizione> getPosizioniUtenteByData(String username, Timestamp from, Timestamp to) throws InvalidUsernameException, InvalidDateException {
		try {
			//verifico se esiste l'utente con l'username passato come argomento
			if(!this.utenti.containsKey(username))
				throw new InvalidUsernameException("L'utente con username " + username + " non esiste!");
		
			Set<Posizione> posizioniFiltrate = new HashSet<Posizione>(0);
			//entrambi i timestamp sono definiti quindi ritorna tutte le posizione tra le due date
			if(from!=null && to!=null){
				if(from.after(to))
					throw new InvalidDateException("La data iniziale '"+String.valueOf(from)+"' e' cronologicamente successiva alla data finale '"+String.valueOf(to)+"' !");
				for(Posizione idPos : this.utenti.get(username).getPosizioni()) {
					if(idPos.getIdPosizione().getTimestamp().after(from) && idPos.getIdPosizione().getTimestamp().before(to))
						posizioniFiltrate.add(idPos);
				}
			}
		
			//to e' null percio' ritorna le posizioni da una certa data
			else if(to==null && from!=null){
				for(Posizione idPos : this.utenti.get(username).getPosizioni()) {
					if(idPos.getIdPosizione().getTimestamp().after(from))
						posizioniFiltrate.add(idPos);
				}
			}

			//from e' null percio' ritorna le posizioni fino ad una certa data
			else if(from==null && to!=null){
				for(Posizione idPos : this.utenti.get(username).getPosizioni()) {
					if(idPos.getIdPosizione().getTimestamp().before(to))
						posizioniFiltrate.add(idPos);
				}
			}
			else{
				throw new InvalidDateException("Sia la data iniziale che quella finale sono 'null'!");
			}
		
			return posizioniFiltrate;
		} catch(NullPointerException e) {
			System.err.println("Exception: java.lang.NullPointerException");
			return new HashSet<Posizione>(0);
		}
	}
	
	/**
	 * Gets all registered users.
	 *
	 * @return utenti all registered users
	 */
	//Metodi getter
	public TreeMap<String, Utente> getUtenti(){
		return this.utenti;
	}
	
	/**
	 * Gets all registered positions.
	 *
	 * @return posizioni all registered positions
	 */
	public TreeMap<IdPosizione, Posizione> getPosizioni() {
		return this.posizioni;
	}
	
	/**
	 * Gets singleton istance of GestoreDatiPersistenti.
	 *
	 * @return gestoreDB singleton istance of GestoreDatiPersistenti
	 */
	public GestoreDatiPersistenti getGestoreDB() {
		return this.gestoreDB;
	}
	
	/** All registered users. */
	private TreeMap<String, Utente> utenti;
	
	/** All registered positions. */
	private TreeMap<IdPosizione, Posizione> posizioni;
	
	/** The singleton istance of GestoreDatiPersistenti. */
	private GestoreDatiPersistenti gestoreDB;
}
