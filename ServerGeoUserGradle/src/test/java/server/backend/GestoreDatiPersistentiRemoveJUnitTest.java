package server.backend;

import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import commons.IdPosizione;
import commons.Posizione;
import commons.Utente;

public class GestoreDatiPersistentiRemoveJUnitTest {
	static GestoreDatiPersistenti g = GestoreDatiPersistenti.getInstance();
	Utente u1=new Utente("pas","pas","pas@gmail.com","Pasquale","Forgione");//utente con due posizioni
	Utente u2=new Utente("lor","lor","lor@gmail.com","Lorenzo","Goglia");//utente con una posizione
	Utente u3=new Utente("ant","ant","ant@gmail.com","Antonio","Varone");//utente senza posizioni

	IdPosizione idPos1 = new IdPosizione(new Timestamp((System.currentTimeMillis()+11000)/1000*1000), Math.random(),  Math.random());
	Posizione p1=new Posizione(idPos1,u1, 20); 

	IdPosizione idPos2 = new IdPosizione(new Timestamp((System.currentTimeMillis()+21002)/1000*1000), Math.random(),  Math.random());
	Posizione p2=new Posizione(idPos2,u2, 20); 

	IdPosizione idPos3 = new IdPosizione(new Timestamp((System.currentTimeMillis()+31003)/1000*1000), Math.random(),  Math.random());
	Posizione p3=new Posizione(idPos3,u1, 20);


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		g.dropDatabase();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		g.dropDatabase();
	}

	@Before
	public void setUp() throws Exception {
		//Aggiunta Utenti
		assertTrue("Errore nell'aggiunta utente1!", g.addUtente(u1) && g.getUtenti().containsKey(u1.getUsername()));	
		assertTrue("Errore nell'aggiunta utente2!", g.addUtente(u2) && g.getUtenti().containsKey(u2.getUsername()));	
		assertTrue("Errore nell'aggiunta utente2!", g.addUtente(u3) && g.getUtenti().containsKey(u3.getUsername()));	

		assertTrue("La posizione1 pur non essendo presente nel database, non e' stata aggiunta ad un utente esistente!", g.addPosizione(p1) && g.getPosizioni().containsKey(idPos1));	

		assertTrue("La posizione2 pur non essendo presente nel database, non e' stata aggiunta ad un utente esistente!", g.addPosizione(p2) && g.getPosizioni().containsKey(idPos2));	

		assertTrue("La posizione3 pur non essendo presente nel database, non e' stata aggiunta ad un utente esistente!", g.addPosizione(p3) && g.getPosizioni().containsKey(idPos3));	
	}

	@After
	public void tearDown() throws Exception {
		//Svuotamento delle tabelle del DB
		g.dropDatabase();
	}

	@Test
	public void test() {

		//Eliminazione utente avente posizioni
		assertTrue("Eliminazione utente avente posizioni memorizzate!", !g.removeUtente(u1));

		//Eliminazione utente senza posizioni
		assertTrue("Eliminazione non consentita di un utente senza posizioni memorizzate!", g.removeUtente(u3));

		//Eliminazione una posizione di un utente
		assertTrue("Eliminazione non consentita di una posizione di un utente!", g.removePosizione(p1) && !g.getPosizioni().containsValue(p1));

		//Aggiunta della posizione eliminata allo stesso utente
		assertTrue("Aggiunta non consentita di una nuova posizione ad un utente!", g.addPosizione(p1) && g.getPosizioni().containsKey(idPos1));

		//Eliminazione posizioni utente con una posizione
		assertTrue("Eliminazione non consentita di piu' posizioni dell'utente 'lor' avente una posizione!", g.removePosizioniUtente(u2));
		//verificare che le posizioni dell'utente sono assenti nel DB
		Statement stm=null;
		ResultSet results=null;
		try {
			stm = g.getConnection().createStatement();
			results=stm.executeQuery("select * from posizioni where utente='lor'");
			results.last();
			assertTrue("La lista delle posizioni dell'utente 'lor' deve essere vuota!", results.getRow()==0);
		} catch (SQLException e) {
			assertTrue("Eccezione sulla query!", false);
		}finally {
			try {
				if(stm!=null)
					stm.close();
				if(results!=null)
					results.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		//Verificare che l'utente u1 abbia piu' di una posizione memorizzata nel DB
		try{
			stm = g.getConnection().createStatement();
			results=stm.executeQuery("select * from posizioni where utente='pas'");
			results.last();
			System.out.println(results.getRow());
			assertTrue("La lista delle posizioni dell'utente 'pas' deve avere piu' di una posizione!", results.getRow()>1);
		} catch (SQLException e) {
			assertTrue("Eccezione sulla query!", false);
		}finally {
			try {
				if(stm!=null)
					stm.close();
				if(results!=null)
					results.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		//Eliminazione posizioni utente con piu' di una posizione
		assertTrue("Eliminazione non consentita di piu' posizioni dell'utente 'pas' avente piu' posizioni!", g.removePosizioniUtente(u1));

		//Verificare che le posizioni dell'utente sono assenti nel DB
		try{
			stm = g.getConnection().createStatement();
			results=stm.executeQuery("select * from posizioni where utente='pas'");
			results.last();
			assertTrue("La lista delle posizioni dell'utente 'pas' deve essere vuota!", results.getRow()==0);
		} catch (SQLException e) {
			assertTrue("Eccezione sulla query!", false);
		}finally {
			try {
				if(stm!=null)
					stm.close();
				if(results!=null)
					results.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}