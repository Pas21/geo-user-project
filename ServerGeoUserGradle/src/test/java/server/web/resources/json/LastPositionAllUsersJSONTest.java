package server.web.resources.json;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.TreeMap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import commons.IdPosizione;
import commons.Posizione;
import commons.Utente;
import server.backend.GestoreDatiPersistenti;
import server.web.resources.json.LastPositionAllUsersJSON.MyTypeToken;

public class LastPositionAllUsersJSONTest {
	static GestoreDatiPersistenti g = GestoreDatiPersistenti.getInstance();

	Utente u1= new Utente("pas", "pas", "pas@gmail.com", "Pasquale", "Forgione");
	Utente u2= new Utente("lor", "lor", "lor@gmail.com", "Lorenzo", "Goglia");
	Utente u3= new Utente("ant", "ant", "ant@gmail.com", "Antonio", "Varone");

	Posizione pos1a = new Posizione(new IdPosizione(Timestamp.valueOf(LocalDateTime.of(2017, 8, 4, 12, 0, 10)), Math.random(), Math.random()), u1, 20);
	Posizione pos1b = new Posizione(new IdPosizione(Timestamp.valueOf(LocalDateTime.of(2017, 11, 25, 12, 0, 10)), Math.random(), Math.random()), u1, 20);		
	Posizione pos1c = new Posizione(new IdPosizione(Timestamp.valueOf(LocalDateTime.of(2017, 12, 22, 10, 0, 5)), Math.random(), Math.random()), u1, 20);		

	Posizione pos2a = new Posizione(new IdPosizione(Timestamp.valueOf(LocalDateTime.of(2017, 8, 29, 12, 0, 10)), Math.random(), Math.random()), u2, 20);
	Posizione pos2b = new Posizione(new IdPosizione(Timestamp.valueOf(LocalDateTime.of(2017, 8, 30, 12, 0, 10)), Math.random(), Math.random()), u2, 20);		
	Posizione pos2c = new Posizione(new IdPosizione(Timestamp.valueOf(LocalDateTime.of(2017, 8, 28, 12, 0, 10)), Math.random(), Math.random()), u2, 20);		

	Posizione pos3a = new Posizione(new IdPosizione(Timestamp.valueOf(LocalDateTime.of(2017, 8, 27, 12, 0, 20)), Math.random(), Math.random()), u3, 20);
	Posizione pos3b = new Posizione(new IdPosizione(Timestamp.valueOf(LocalDateTime.of(2017, 8, 27, 12, 0, 19)), Math.random(), Math.random()), u3, 20);		
	Posizione pos3c = new Posizione(new IdPosizione(Timestamp.valueOf(LocalDateTime.of(2017, 8, 27, 12, 0, 18)), Math.random(), Math.random()), u3, 20);		
	
	
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
		//Aggiunta utenti		
		assertTrue("L'utente "+u1.getUsername() +" non esiste e dovrebbe essere aggiunto al database!",g.addUtente(u1));
		assertTrue("L'utente "+u2.getUsername() +" non esiste e dovrebbe essere aggiunto al database!",g.addUtente(u2));
		assertTrue("L'utente "+u3.getUsername() +" non esiste e dovrebbe essere aggiunto al database!",g.addUtente(u3));

		//Aggiunta posizioni ad ogni utente u1 (last position is pos1c)
		assertTrue("La posizione1a pur non essendo presente nel database, non e' stata aggiunta ad un utente esistente!", g.addPosizione(pos1a) && g.getPosizioni().containsKey(pos1a.getIdPosizione()));	
		assertTrue("La posizione1b pur non essendo presente nel database, non e' stata aggiunta ad un utente esistente!", g.addPosizione(pos1b) && g.getPosizioni().containsKey(pos1b.getIdPosizione()));	
		assertTrue("La posizione1c pur non essendo presente nel database, non e' stata aggiunta ad un utente esistente!", g.addPosizione(pos1c) && g.getPosizioni().containsKey(pos1c.getIdPosizione()));	
		
		//Aggiunta posizioni ad ogni utente u2 (last position is pos2b)
		assertTrue("La posizione2a pur non essendo presente nel database, non e' stata aggiunta ad un utente esistente!", g.addPosizione(pos2a) && g.getPosizioni().containsKey(pos2a.getIdPosizione()));	
		assertTrue("La posizione2b pur non essendo presente nel database, non e' stata aggiunta ad un utente esistente!", g.addPosizione(pos2b) && g.getPosizioni().containsKey(pos2b.getIdPosizione()));	
		assertTrue("La posizione2c pur non essendo presente nel database, non e' stata aggiunta ad un utente esistente!", g.addPosizione(pos2c) && g.getPosizioni().containsKey(pos2c.getIdPosizione()));	

		//Aggiunta posizioni ad ogni utente u3 (last position is pos3a)
		assertTrue("La posizione3a pur non essendo presente nel database, non e' stata aggiunta ad un utente esistente!", g.addPosizione(pos3a) && g.getPosizioni().containsKey(pos3a.getIdPosizione()));	
		assertTrue("La posizione3b pur non essendo presente nel database, non e' stata aggiunta ad un utente esistente!", g.addPosizione(pos3b) && g.getPosizioni().containsKey(pos3b.getIdPosizione()));	
		assertTrue("La posizione3c pur non essendo presente nel database, non e' stata aggiunta ad un utente esistente!", g.addPosizione(pos3c) && g.getPosizioni().containsKey(pos3c.getIdPosizione()));	

		
	}

	@After
	public void tearDown() throws Exception {
		g.dropDatabase();
	}

	@Test
	public void test() {
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		LastPositionAllUsersJSON lastPositionAllUsersJSON = new LastPositionAllUsersJSON();
		String response = lastPositionAllUsersJSON.getUltimaPosizioneOgniUtente();
		System.out.println(response);
		TreeMap<String,Posizione> mapLastPositionAllUsers = gson.fromJson(response, MyTypeToken.listType().getType());
		System.out.println(mapLastPositionAllUsers.getClass());
		System.out.println(mapLastPositionAllUsers);
		
		assertTrue("La mappa dovrebbe contenere 3 elementi", mapLastPositionAllUsers.size()==3);
		assertTrue("L'ultima posizione dell'utente "+u1.getUsername()+" non e' quella prevista", mapLastPositionAllUsers.containsKey(u1.getUsername()) && mapLastPositionAllUsers.get(u1.getUsername()).equals(pos1c));
		assertTrue("L'ultima posizione dell'utente "+u2.getUsername()+" non e' quella prevista", mapLastPositionAllUsers.containsKey(u2.getUsername()) && mapLastPositionAllUsers.get(u2.getUsername()).equals(pos2b));
		assertTrue("L'ultima posizione dell'utente "+u3.getUsername()+" non e' quella prevista", mapLastPositionAllUsers.containsKey(u3.getUsername()) && mapLastPositionAllUsers.get(u3.getUsername()).equals(pos3a));	
	}
}
