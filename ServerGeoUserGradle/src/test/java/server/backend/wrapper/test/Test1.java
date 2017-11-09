package server.backend.wrapper.test;

import java.sql.Timestamp;

import commons.IdPosizione;
import commons.InvalidUsernameException;
import commons.Posizione;
import commons.Utente;
import server.backend.GestoreDatiPersistenti;


public class Test1 {
	public static void main(String[] args) throws InvalidUsernameException{
		Utente u=new Utente("pas","pas","pas@gmail.com","Pasquale","Forgione");
		IdPosizione idPos = new IdPosizione(new Timestamp(System.currentTimeMillis()/1000*1000), Math.random(),  Math.random());
		Posizione p=new Posizione(idPos,u, 20); 
		GestoreDatiPersistenti g = GestoreDatiPersistenti.getInstance();
		g.listaUtenti();
		g.listaPosizioni();
		
		
		if(g.aggiuntaUtente(u))
			System.out.println("Aggiunta utente OK");
		else
			System.out.println("ERRORE aggiunta utente");
		if(g.aggiuntaPosizione(p))
			System.out.println("Aggiunta posizione OK");
		else
			System.out.println("ERRORE aggiunta posizione");
		
		if(g.cancellaPosizione(p))
			System.out.println("Eliminazione posizione OK");
		else
			System.out.println("ERRORE eliminazione posizione");
		
		if(g.cancellaUtente(u))
			System.out.println("Eliminazione utente OK");
		else
			System.out.println("ERRORE eliminazione utente");
		
	}
}
