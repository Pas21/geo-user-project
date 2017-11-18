package server.backend;


import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

import commons.IdPosizione;
import commons.InvalidPositionException;
import commons.InvalidUsernameException;
import commons.Utente;
import commons.Posizione;

public class UserRegistry {
	
	//Costruttore
	public UserRegistry(){
		//Ottenimento di un'istanza del gestore del database
		this.gestoreDB = GestoreDatiPersistenti.getInstance();
		
		//Caricamento degli utenti e delle posizioni dal database
		this.utenti = this.gestoreDB.getUtenti();
		this.posizioni = this.gestoreDB.getPosizioni();
		
		//Aggiornamento set di posizioni degli utenti
		Set<Posizione> posizioniUtente = new HashSet<Posizione>(0);
		for(String username : this.utenti.keySet()) {
			for(IdPosizione idPosizione : this.posizioni.keySet()) {
				Posizione posizione = this.posizioni.get(idPosizione);
				if(posizione.getUtente().getUsername().equals(username)) {
					posizioniUtente.add(posizione);
				}
			}
			this.utenti.get(username).setPosizioni(posizioniUtente);
			posizioniUtente = new HashSet<Posizione>(0);
		}
		
		//Aggiornamento utente delle posizioni
		for(String username : this.utenti.keySet()) {
			for(IdPosizione idPosizione : this.posizioni.keySet()) {
				Posizione posizione = this.posizioni.get(idPosizione);
				if(posizione.getUtente().getUsername().equals(username)) {
					posizione.setUtente(this.utenti.get(username));
					this.posizioni.put(idPosizione, posizione);
				}
			}
		}
	}

	//Metodo per l'aggiunta di un nuovo utente
	public void addUtente(Utente newUtente) throws InvalidUsernameException{
		//Verifica presenza utente
		if(this.utenti.containsKey(newUtente.getUsername()))
			throw new InvalidUsernameException("L'utente con username " + newUtente.getUsername() + " già esiste!");
		
		//Aggiunta utente al database
		if(!this.gestoreDB.addUtente(newUtente)) {
			System.out.println("Errore aggiunta utente al database con username " + newUtente.getUsername() + "!");
		}
		else {
			//Aggiunta utente alla lista degli utenti caricata in memoria
			this.utenti.put(newUtente.getUsername(), newUtente);	
		}
	}
	
	//Metodo per la rimozione di un utente esistente
	public void removeUtente(String username) throws InvalidUsernameException {
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
			}
			else {
				//Eliminazione utente dalla lista degli utenti caricati in memoria
				this.utenti.remove(username);
			}	
		}
	}
	
	//Metodo per l'aggiunta di una posizione ad un utente esistente
	public void addPosizione(Posizione newPosizione) throws InvalidUsernameException, InvalidPositionException {
		//Verifica esistenza utente
		String username = newPosizione.getUtente().getUsername();
		if(!this.utenti.containsKey(username))
			throw new InvalidUsernameException("L'utente con username " + username + " non esiste!");
		
		//Verifica esistenza posizione
		if(this.posizioni.containsKey(newPosizione.getIdPosizione()))
			throw new InvalidPositionException("La posizione con id " + newPosizione.getIdPosizione().toString() + " esiste gia!");
		
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
	}
	
	//Metodo per la rimozione di una posizione di un utente esistente
	public boolean removePosizione(IdPosizione idPosizione) throws InvalidPositionException {
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
				System.out.println("La posizione con id " + idPosizione + "non è stata eliminata dall'oggetto utente!");
			return true;
		}
	}
	
	//Metodo per la rimozione di tutte le posizioni di un utente esistente
	public boolean removePosizioni(String username) throws InvalidUsernameException{
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
				
	}
	
	//Metodo per l'ottenimento di tutte le posizioni di un determinato utente
	public Set<Posizione> getPosizioniByUtente(String username) throws InvalidUsernameException {
		//Si potrebbe anche scorrere la lista di posizioni!
		//Verifica presenza utente
		if(!this.utenti.containsKey(username))
			throw new InvalidUsernameException("L'utente con username " + username + " non esiste!");
		
		Utente utente = this.utenti.get(username);
		return utente.getPosizioni();
	}
	
	//Metodo per l'ottenenimento di tutte le posizioni di un utente che ricadono entro un intervallo di tempo
	public Set<Posizione> getPosizioniUtenteByData(String username, Timestamp from, Timestamp to) {
		Set<Posizione> posizioniFiltrate = new HashSet<Posizione>(0);
		for(IdPosizione idPosizione : this.posizioni.keySet()) {
			if(this.posizioni.get(idPosizione).getUtente().getUsername().equals(username) && idPosizione.getTimestamp().after(from) && idPosizione.getTimestamp().before(to))
				posizioniFiltrate.add(this.posizioni.get(idPosizione));
		}
		return posizioniFiltrate;
	}
	
	//Metodi getter
	public TreeMap<String, Utente> getUtenti(){
		return this.utenti;
	}
	
	public TreeMap<IdPosizione, Posizione> getPosizioni() {
		return this.posizioni;
	}
	
	private TreeMap<String, Utente> utenti;
	private TreeMap<IdPosizione, Posizione> posizioni;
	private GestoreDatiPersistenti gestoreDB;
}
