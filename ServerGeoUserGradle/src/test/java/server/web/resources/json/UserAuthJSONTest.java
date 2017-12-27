package server.web.resources.json;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;

import commons.Utente;
import server.backend.GestoreDatiPersistenti;

public class UserAuthJSONTest {
	static GestoreDatiPersistenti g = GestoreDatiPersistenti.getInstance();

	
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
		Utente u1= new Utente("pas", "pas", "pas@gmail.com", "Pasquale", "Forgione");
		Utente u2= new Utente("lor", "lor", "lor@gmail.com", "Lorenzo", "Goglia");
		Utente u3= new Utente("ant", "ant", "ant@gmail.com", "Antonio", "Varone");
		
		assertTrue("L'utente "+u1.getUsername() +" non esiste e dovrebbe essere aggiunto al database!",g.addUtente(u1));
		assertTrue("L'utente "+u2.getUsername() +" non esiste e dovrebbe essere aggiunto al database!",g.addUtente(u2));
		assertTrue("L'utente "+u3.getUsername() +" non esiste e dovrebbe essere aggiunto al database!",g.addUtente(u3));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		Gson gson=new Gson();
		UserAuthJSON userAuthJson= new UserAuthJSON();
		String usernamesGSON = userAuthJson.getUsers();
		String[] usernamesArray = gson.fromJson(usernamesGSON,String[].class);
		ArrayList<String> usernames = new ArrayList<String>((Arrays.asList(usernamesArray)));
		for(String username : usernames){
			assertTrue("L'username "+username+" non è presente nella lista di username di tutti gli utenti registati!", usernames.contains(username));
		}
	}

}
