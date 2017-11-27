package server.backend.wrapper;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import commons.IdPosizione;
import commons.InvalidEmailException;
import commons.InvalidPositionException;
import commons.InvalidUsernameException;
import commons.Posizione;
import commons.Utente;

public class UserRegistryAPITest {

	public static void main(String[] args) {
		UserRegistryAPI userregAPI = UserRegistryAPI.instance();
		
		//Test ottenimento di tutti gli utenti
		TreeMap<String, Utente> utenti = userregAPI.getUtenti();
		
		System.out.println("UTENTI CARICATI NEL DATABASE: ");
		for(Entry<String, Utente> str : utenti.entrySet()) {
			System.out.println(str.getValue().toString());
		}
		
		//Test ottenimento di tutte le posizioni
		Set<Posizione> posizioni = new HashSet<Posizione>(0);
		for(String str : utenti.keySet()) {
			posizioni.addAll(utenti.get(str).getPosizioni());
		}
		
		System.out.println("POSIZIONI CARICATE NEL DATABASE: ");
		for(Posizione pos : posizioni) {
			System.out.println(pos.toString());
		}
		
		//Test aggiunta di un nuovo utente
		Utente newUtente = new Utente("pietro.user","pietro.pass","pietro.email","pietro","goglia");
		try {
			userregAPI.addUtente(newUtente);
			utenti = userregAPI.getUtenti();
			
			System.out.println("UTENTI CARICATI NEL DATABASE DOPO L'AGGIUNTA DELL'UTENTE CON USERNAME " + newUtente.getUsername() + ":");
			for(String str : utenti.keySet()) {
				System.out.println(utenti.get(str).toString());
			}
		} catch (InvalidUsernameException e) {
			System.err.println(e.getMessage());
		}catch (InvalidEmailException e) {
			System.err.println(e.getMessage());
		}
		
		//Test aggiunta di un nuovo utente
		Utente newUtente2 = new Utente("a.user","a.pass","pietro.email","a","a");
		try {
			System.out.println("AGGIUNTA DELL'UTENTE CON EMAIL ESISTENTE " + newUtente2.getEmail() + ":");
			userregAPI.addUtente(newUtente2);
			utenti = userregAPI.getUtenti();

			System.out.println("UTENTI CARICATI NEL DATABASE DOPO L'AGGIUNTA DELL'UTENTE CON EMAIL ESISTENTE " + newUtente2.getEmail() + ":");
			for(String str : utenti.keySet()) {
				System.out.println(utenti.get(str).toString());
			}
		} catch (InvalidUsernameException e) {
			System.err.println(e.getMessage());
		}catch (InvalidEmailException e) {
			System.err.println(e.getMessage());
		}

		//Test rimozione di un utente esistente
		try {
			userregAPI.removeUtente(newUtente.getUsername());
			utenti = userregAPI.getUtenti();
			
			System.out.println("UTENTI CARICATI NEL DATABASE DOPO LA RIMOZIONE DELL'UTENTE CON USERNAME " + newUtente.getUsername() + ":");
			for(String str : utenti.keySet()) {
				System.out.println(utenti.get(str).toString());
			}
		} catch (InvalidUsernameException e) {
			System.err.println(e.getMessage());
		}
		
		//Test aggiunta di una posizione ad un utente esistente
		Utente utente = userregAPI.getUtenti().get("antonio.user");
		IdPosizione idPosizione = new IdPosizione(new Timestamp(System.currentTimeMillis()/1000*1000),41.161718,14.811001);
		Posizione newPosizione = new Posizione(idPosizione, utente,20);
		try {
			userregAPI.addPosizione(newPosizione);
			utenti = userregAPI.getUtenti();
			posizioni = userregAPI.getPosizioniByUtente(utente.getUsername());
			System.out.println("POSIZIONI DI " + utente.getUsername() + " CARICATE NEL DATABASE DOPO L'AGGIUNTA DELLA POSIZIONE CON ID " + idPosizione.toString() + ":");
			for(Posizione posizione : posizioni) {
				System.out.println(posizione.toString());
			}
			System.out.println("POSIZIONI CARICATE NEL DATABASE PER UTENTE DOPO L'AGGIUNTA DELLA POSIZIONE CON ID " + idPosizione.toString() + ":");
			for(String str : utenti.keySet()) {
				System.out.println("Posizioni di " + str + ":");
				for(Posizione posizione : utenti.get(str).getPosizioni()) {
					System.out.println(posizione.toString());
				}
			}
		} catch (InvalidUsernameException | InvalidPositionException e) {
			System.err.println(e.getMessage());
		}
		
		//Test rimozione posizione di un utente esistente
		try {
			userregAPI.removePosizione(idPosizione);
			System.out.println("POSIZIONI CARICATE NEL DATABASE PER UTENTE DOPO LA RIMOZIONE DELLA POSIZIONE CON ID " + idPosizione.toString() + ":");
			for(String str : utenti.keySet()) {
				System.out.println("Posizioni di " + str + ":");
				for(Posizione posizione : utenti.get(str).getPosizioni()) {
					System.out.println(posizione.toString());
				}
			}
		} catch (InvalidPositionException e) {
			System.err.println(e.getMessage());
		}
		
		//Test rimozione di tutte le posizioni di un utente
		/*try {
			userregAPI.removePosizioni(newUtente.getUsername());
			posizioni = userregAPI.getPosizioniByUtente(newUtente.getUsername());
			System.out.println("POSIZIONI DI " + newUtente.getUsername() + " CARICATE NEL DATABASE DOPO LA RIMOZIONE DI TUTTE LE SUE POSIZIONI:");
			for(Posizione posizione : posizioni) {
				System.out.println(posizione.toString());
			}
			System.out.println("POSIZIONI CARICATE NEL DATABASE PER UTENTE DOPO LA RIMOZIONE DELLE POSIZIONI DI " + newUtente.getUsername() + ":");
			for(String str : utenti.keySet()) {
				System.out.println("Posizioni di " + str + ":");
				for(Posizione posizione : utenti.get(str).getPosizioni()) {
					System.out.println(posizione.toString());
				}
			}
		} catch (InvalidUsernameException e) {
			System.err.println(e.getMessage());
		}*/
		
		//Test ottenimento di tutte le posizioni di un utente entro un certo intervallo di tempo
		LocalDateTime localDateTime1 = LocalDateTime.of(2016, 02, 10, 00, 00, 00);
		LocalDateTime localDateTime2 = LocalDateTime.of(2017, 11, 12, 00, 00, 00);
		Set<Posizione> posizioniFiltrate = userregAPI.getPosizioniUtenteByData("antonio.user", Timestamp.valueOf(localDateTime1), Timestamp.valueOf(localDateTime2));
		System.out.println("POSIZIONI DI antonio.user DAL " + localDateTime1.toString() + " AL " + localDateTime2.toString() + ":");
		for(Posizione posizione : posizioniFiltrate) {
			System.out.println(posizione.toString());
		}
		
		//Test ottenimento di tutte le posizioni di un utente
		try {
			for(Posizione posizione : userregAPI.getPosizioniByUtente("antonio.user")) {
				System.out.println("Posizioni di antonio.user: " + posizione.toString());
			}
		} catch (InvalidUsernameException e) {
			System.err.println(e.getMessage());
		}
	}

}
