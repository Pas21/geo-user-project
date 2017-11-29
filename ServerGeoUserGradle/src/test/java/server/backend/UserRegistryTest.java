package server.backend;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.TreeMap;

import commons.IdPosizione;
import commons.InvalidDateException;
import commons.InvalidEmailException;
import commons.InvalidPositionException;
import commons.InvalidUsernameException;
import commons.Posizione;
import commons.Utente;

public class UserRegistryTest {

	public static void main(String[] args) throws InvalidUsernameException, InvalidDateException {
		UserRegistry userreg = new UserRegistry();
		
		//Test ottenimento utenti e posizioni
		TreeMap<String, Utente> utenti = userreg.getUtenti();
		TreeMap<IdPosizione, Posizione> posizioni = userreg.getPosizioni();
		
		System.out.println("UTENTI CARICATI NEL DATABASE: ");
		for(String str : utenti.keySet()) {
			System.out.println(utenti.get(str).toString());
		}
		
		System.out.println("POSIZIONI CARICATE NEL DATABASE: ");
		for(IdPosizione id : posizioni.keySet()) {
			System.out.println(posizioni.get(id).toString());
		}
		
		//Il seguente test serve a verificare che a tutte le posizioni sia stato associato il rispettivo utente
		System.out.println("UTENTI CARICATI NEL DATABASE PER POSIZIONE:");
		for(IdPosizione id : posizioni.keySet()) {
			System.out.println("Utente: " + posizioni.get(id).getUtente().toString());
		}
		
		//Il seguente test serve a verificare che a ogni utente siano state associate le rispettive posizioni
		System.out.println("POSIZIONI CARICATE NEL DATABASE PER UTENTE: ");
		for(String str : utenti.keySet()) {
			System.out.println("Posizioni di " + str + ":");
			for(Posizione posizione : utenti.get(str).getPosizioni()) {
				System.out.println(posizione.toString());
			}
		}
		
		//Test aggiunta di un nuovo utente
		Utente newUtente = new Utente("pietro.user","pietro.pass","pietro.email","pietro","goglia");
		try {
			userreg.addUtente(newUtente);
			utenti = userreg.getUtenti();
			System.out.println("UTENTI CARICATI NEL DATABASE DOPO L'AGGIUNTA DI " + newUtente.getUsername() + ":");
			for(String str : utenti.keySet()) {
				System.out.println(utenti.get(str).toString());
			}
		} catch (InvalidUsernameException e) {
			System.err.println(e.getMessage());
		} catch (InvalidEmailException e) {
			System.err.println(e.getMessage());
		}
		
		//Test aggiunta di un nuovo utente
		Utente newUtente2 = new Utente("a.user","a.pass","pietro.email","a","a");
		try {
			System.out.println("AGGIUNTA DELL'UTENTE CON EMAIL ESISTENTE " + newUtente2.getEmail() + ":");
			userreg.addUtente(newUtente2);
			utenti = userreg.getUtenti();

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
			userreg.removeUtente(newUtente.getUsername());
			//userreg.removeUtente("antonio.user");
			utenti = userreg.getUtenti();
			System.out.println("UTENTI CARICATI NEL DATABASE DOPO LA RIMOZIONE DI " + newUtente.getUsername() + ":");
			for(String str : utenti.keySet()) {
				System.out.println(utenti.get(str).toString());
			}
		} catch (InvalidUsernameException e) {
			System.err.println(e.getMessage());
		}
		
		//Test aggiunta di una posizione ad un utente esistente
		Utente utente = userreg.getUtenti().get("antonio.user");
		IdPosizione idPosizione = new IdPosizione(new Timestamp(System.currentTimeMillis()/1000*1000),41.161718,14.811001);
		Posizione newPosizione = new Posizione(idPosizione, utente,20);
		try {
			userreg.addPosizione(newPosizione);
			utenti = userreg.getUtenti();
			posizioni = userreg.getPosizioni();
			System.out.println("POSIZIONI CARICATE NEL DATABASE DOPO L'AGGIUNTA DELLA POSIZIONE CON ID " + idPosizione.toString() + ":");
			for(IdPosizione id : posizioni.keySet()) {
				System.out.println(posizioni.get(id).toString());
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
			userreg.removePosizione(idPosizione);
			utenti = userreg.getUtenti();
			posizioni = userreg.getPosizioni();
			System.out.println("POSIZIONI CARICATE NEL DATABASE DOPO LA RIMOZIONE DELLA POSIZIONE CON ID " + idPosizione.toString() + ":");
			for(IdPosizione id : posizioni.keySet()) {
				System.out.println(posizioni.get(id).toString());
			}
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
		//NB: non cancellare anche se è commentato!
		/*try {
			userreg.removePosizioni("pietro.user");
			utenti = userreg.getUtenti();
			posizioni = userreg.getPosizioni();
			System.out.println("POSIZIONI CARICATE NEL DATABASE DOPO LA RIMOZIONE DELLE POSIZIONI DI pietro.user:");
			for(IdPosizione id : posizioni.keySet()) {
				System.out.println(posizioni.get(id).toString());
			}
			System.out.println("POSIZIONI CARICATE NEL DATABASE PER UTENTE DOPO LA RIMOZIONE DELLE POSIZIONI DI pietro.user:");
			for(String str : utenti.keySet()) {
				System.out.println("Posizioni di " + str + ":");
				for(Posizione posizione : utenti.get(str).getPosizioni()) {
					System.out.println(posizione.toString());
				}
			}
		} catch (InvalidUsernameException e) {
			System.err.println(e.getMessage());
		} */
		
		//Test ottenimento di tutte le posizioni di un utente
		try {
			for(Posizione posizione : userreg.getPosizioniByUtente("antonio.user"))
				System.out.println("Posizioni di antonio.user: " + posizione.toString());
		} catch (InvalidUsernameException e) {
			System.err.println(e.getMessage());
		}
		
		//Test ottenimento di tutte le posizioni di un utente entro un certo intervallo di tempo
		LocalDateTime localDateTime1 = LocalDateTime.of(2016, 02, 10, 00, 00, 00);
		LocalDateTime localDateTime2 = LocalDateTime.of(2017, 11, 10, 00, 00, 00);
		Set<Posizione> posizioniFiltrate = userreg.getPosizioniUtenteByData("antonio.user", Timestamp.valueOf(localDateTime1), Timestamp.valueOf(localDateTime2));
		System.out.println("POSIZIONI DI antonio.user DAL " + localDateTime1.toString() + " AL " + localDateTime2.toString() + ":");
		for(Posizione posizione : posizioniFiltrate) {
			System.out.println(posizione.toString());
		}
		
		//Test ottenimento di tutte le posizioni di un utente fino ad una certa data
		localDateTime2 = LocalDateTime.of(2017, 10, 21, 00, 00, 00);
		posizioniFiltrate = userreg.getPosizioniUtenteByData("antonio.user", null, Timestamp.valueOf(localDateTime2));
		System.out.println("POSIZIONI DI antonio.user FINO AL " + localDateTime2.toString() + ":");
		for(Posizione posizione : posizioniFiltrate) {
			System.out.println(posizione.toString());
		}
		
		//Test ottenimento di tutte le posizioni di un utente dopo una certa data
		localDateTime1 = LocalDateTime.of(2017, 10, 21, 00, 00, 00);
		posizioniFiltrate = userreg.getPosizioniUtenteByData("antonio.user", Timestamp.valueOf(localDateTime1), null);
		System.out.println("POSIZIONI DI antonio.user DAL " + localDateTime1.toString() + ":");
		for(Posizione posizione : posizioniFiltrate) {
			System.out.println(posizione.toString());
		}
		
		//Test ottenimento di tutte le posizioni con entrambe le date nulle
		localDateTime1 = LocalDateTime.of(2017, 10, 21, 00, 00, 00);
		System.out.println("INSERIMENTO DATA NULLE:");
		try{
			posizioniFiltrate = userreg.getPosizioniUtenteByData("antonio.user",null, null);
		}catch(InvalidDateException e){
			System.err.println(e.getMessage());
		}
		
		//Test ottenimento di tutte le posizioni con entrambe le date nulle
		localDateTime1 = LocalDateTime.of(2017, 10, 21, 00, 00, 00);
		System.out.println("INSERIMENTO UTENTE SBAGLIATO:");
		try{
			posizioniFiltrate = userreg.getPosizioniUtenteByData("dfshdgsdfg",null, null);
		}catch(InvalidUsernameException e){
			System.err.println(e.getMessage());
		}catch(InvalidDateException e){
			System.err.println(e.getMessage());
		}
	}

}
