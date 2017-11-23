package server.backend;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import commons.IdPosizione;
import commons.InvalidUsernameException;
import commons.Posizione;
import commons.Utente;
import server.backend.GestoreDatiPersistenti;


public class GestoreDatiPersistentiTest {
	public static void main(String[] args) throws InvalidUsernameException{
		Utente u=new Utente("pas","pas","pas@gmail.com","Pasquale","Forgione");
		IdPosizione idPos = new IdPosizione(new Timestamp(System.currentTimeMillis()/1000*1000), Math.random(),  Math.random());
		Posizione p=new Posizione(idPos,u, 20); //Questa posizione deve essere aggiunta al set di utente!
		GestoreDatiPersistenti g = GestoreDatiPersistenti.getInstance();
		
		//Test caricamento dati dal database
		g.getUtenti();
		g.getPosizioni();
		
		//Test aggiunta utente
		if(g.addUtente(u))
			System.out.println("Aggiunta utente OK");
		else
			System.out.println("ERRORE aggiunta utente");
		
		//Test aggiunta posizione
		if(g.addPosizione(p))
			System.out.println("Aggiunta posizione OK");
		else
			System.out.println("ERRORE aggiunta posizione");
		
		//Test rimozione posizione e utente
		if(g.removePosizione(p)) {
			System.out.println("Eliminazione posizione OK");
			if(g.removeUtente(u))
				System.out.println("Eliminazione utente OK");
			else
				System.out.println("ERRORE eliminazione utente");
		}
		else
			System.out.println("ERRORE eliminazione posizione");
		
		//Test rimozione di tutte le posizioni di un utente
		Utente antonio = g.getUtenti().get("antonio.user");
		LocalDateTime localDateTime = LocalDateTime.of(2017, 10, 20, 12, 0, 37);
		Timestamp timestamp = Timestamp.valueOf(localDateTime);
		IdPosizione idPosAntonio1 = new IdPosizione(timestamp, 41.130101,14.785006);
		localDateTime = LocalDateTime.of(2017, 11, 1, 15, 35, 37);
		timestamp = Timestamp.valueOf(localDateTime);
		IdPosizione idPosAntonio2 = new IdPosizione(timestamp, 41.132008, 14.778238);
		Posizione posAntonio1 = new Posizione(idPosAntonio1, antonio, 20);
		Posizione posAntonio2 = new Posizione(idPosAntonio2, antonio, 20);
		
		
		if(g.removePosizioniUtente(antonio))
			System.out.println("Eliminazione posizioni di Antonio ok!");
		else 
			System.out.println("Errore eliminazione posizioni di Antonio!");
		
		//Ripristino stato del database
		if(g.addPosizione(posAntonio1))
			System.out.println("Aggiunta posizione 1 ad Antonio ok!");
		else
			System.out.println("Errore aggiunta posizione 1 ad Antonio!");
		if(g.addPosizione(posAntonio2))
			System.out.println("Aggiunta posizione 2 ad Antonio ok!");
		else
			System.out.println("Errore aggiunta posizione 2 ad Antonio!");
	}
}
