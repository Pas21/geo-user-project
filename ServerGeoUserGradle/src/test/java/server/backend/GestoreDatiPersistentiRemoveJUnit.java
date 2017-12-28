package server.backend;

import static org.junit.Assert.*;

import java.sql.Timestamp;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import commons.IdPosizione;
import commons.Posizione;
import commons.Utente;

public class GestoreDatiPersistentiRemoveJUnit {
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
		SessionFactory sessionFactory = g.getFactory();
		//svuoto le tabelle del DB
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.createNativeQuery("delete from posizioni").executeUpdate();
		session.createNativeQuery("delete from utenti").executeUpdate();
		tx.commit();
		session.close();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		SessionFactory sessionFactory = g.getFactory();
		//svuoto le tabelle del DB
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.createNativeQuery("delete from posizioni").executeUpdate();
		session.createNativeQuery("delete from utenti").executeUpdate();
		tx.commit();
		session.close();
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
	}

	@Test
	public void test() {
		
		//Eliminazione utente avente posizioni
		assertTrue("Eliminazione utente avente posizioni memorizzate!", !g.removeUtente(u1));
		
		//Eliminazione utente senza posizioni
		assertTrue("Eliminazione non consentita di un utente senza posizioni memorizzate!", g.removeUtente(u3));
		
		//Eliminazione una posizione di un utente
		assertTrue("Eliminazione non consentita di una posizione di un utente!", g.removePosizione(p1) && !g.getPosizioni().containsValue(p1));
		
		//Aggiungo di nuovo la posizione
		assertTrue("Aggiunta non consentita di una nuova posizione ad un utente!", g.addPosizione(p1) && g.getPosizioni().containsKey(idPos1));

		
		//Eliminazione posizioni utente con una posizione
		assertTrue("Eliminazione non consentita di pi� posizioni dell'utente 'lor' avente una posizione!", g.removePosizioniUtente(u2));
		//vedo se le posizioni dell'utente sono assenti nel DB
		SessionFactory sessionFactory = g.getFactory();
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		NativeQuery<?> results=session.createNativeQuery("select * from posizioni where utente='lor'");
		assertTrue("La lista delle posizioni dell'utente 'lor' deve essere vuota!", results.getFirstResult()==0);
		tx.commit();
		session.close();

		//Eliminazione posizioni utente con pi� di una posizione
		sessionFactory = g.getFactory();
		session = sessionFactory.openSession();
		tx = session.beginTransaction();
		results=session.createNativeQuery("select * from posizioni where utente='pas'");
		assertTrue("La lista delle posizioni dell'utente 'pas' deve avere pi� di una posizione!", results.getResultList().size()>1);
		tx.commit();
		session.close();
		
		assertTrue("Eliminazione non consentita di piu' posizioni dell'utente 'pas' avente piu' posizioni!", g.removePosizioniUtente(u1));
		//vedo se le posizioni dell'utente sono assenti nel DB
		sessionFactory = g.getFactory();
		session = sessionFactory.openSession();
		tx = session.beginTransaction();
		results=session.createNativeQuery("select * from posizioni where utente='pas'");
		assertTrue("La lista delle posizioni dell'utente 'lor' deve essere vuota!", results.getFirstResult()==0);
		tx.commit();
		session.close();


	}

}
