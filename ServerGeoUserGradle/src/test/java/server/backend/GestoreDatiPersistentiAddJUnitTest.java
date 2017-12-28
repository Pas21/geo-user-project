package server.backend;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.TreeMap;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import commons.IdPosizione;
import commons.Posizione;
import commons.Utente;

public class GestoreDatiPersistentiAddJUnitTest {
	static GestoreDatiPersistenti g = GestoreDatiPersistenti.getInstance();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		SessionFactory sessionFactory = g.getFactory();
		//Svuotamento delle tabelle del DB
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
		//Svuotamento delle tabelle del DB
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.createNativeQuery("delete from posizioni").executeUpdate();
		session.createNativeQuery("delete from utenti").executeUpdate();
		tx.commit();
		session.close();
	}

	@Before
	public void setUp() throws Exception {
		SessionFactory sessionFactory = g.getFactory();
		//svuoto le tabelle del DB
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.createNativeQuery("delete from posizioni").executeUpdate();
		session.createNativeQuery("delete from utenti").executeUpdate();
		tx.commit();
		session.close();
	}

	@After
	public void tearDown() throws Exception {
		SessionFactory sessionFactory = g.getFactory();
		//Svuotamento delle tabelle del DB
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.createNativeQuery("delete from posizioni").executeUpdate();
		session.createNativeQuery("delete from utenti").executeUpdate();
		tx.commit();
		session.close();
	}

	@Test
	public void test() {
		//DB senza Utenti
		TreeMap<String, Utente> utenti = g.getUtenti();
		assertTrue("La lista di utenti ottenuta deve essere vuota!", utenti.isEmpty());	
		
		//DB aggiunta utente non esistente
		Utente u=new Utente("pas","pas","pas@gmail.com","Pasquale","Forgione");
		assertTrue("La lista di utenti deve contenere solo l'utente aggiunto!", g.addUtente(u) && g.getUtenti().containsKey(u.getUsername()));	

		//DB aggiunta utente esistente
		assertTrue("L'utente gia' esistente e' stato aggiunto erroneamente!",	!g.addUtente(u));
	
		

		
		
		
		//DB senza Posizioni
		TreeMap<IdPosizione, Posizione> posizioni = g.getPosizioni();
		assertTrue("La lista delle posizioni ottenuta deve essere vuota!", posizioni.isEmpty());	


		//DB aggiunta posizione non esistente a utente non esistente
		LocalDateTime localDateTime = LocalDateTime.of(2016, 02, 10, 00, 00, 00);
		IdPosizione idPos = new IdPosizione(Timestamp.valueOf(localDateTime), 14.4111111,  14.4111111);
		Utente u1=new Utente("lor","lor","lor@gmail.com","Lorenzo","Goglia");
		Posizione p=new Posizione(idPos,u1, 20);
		assertTrue("La posizione e' stata aggiunta ad un utente non esistente!", !g.addPosizione(p));	
		
		posizioni=g.getPosizioni();
		assertTrue("La lista delle posizioni ottenuta deve essere vuota!", posizioni.isEmpty());	


		//DB aggiunta posizione non esistente a utente esistente
		p=new Posizione(idPos,u, 20);
		assertTrue("La posizione pur non essendo presente nel database, non e' stata aggiunta ad un utente esistente!", g.addPosizione(p) && g.getPosizioni().containsKey(idPos));	
		
		//DB con Posizioni
		posizioni=g.getPosizioni();
		assertTrue("La lista di posizioni deve contenere solo la posizione aggiunta!", posizioni.containsKey(idPos));
		
		//DB aggiunta posizione gia' esistente
		assertTrue("La posizione gia' esistente e' stata aggiunta di nuovo!", !g.addPosizione(p));
		
		//DB aggiunta posizione gia' esistente ad un utente
		assertTrue("Aggiunta consentita di una posizione gia' esistente ad un utente!", !g.addPosizione(p) && g.getPosizioni().containsKey(idPos));

	}

}
