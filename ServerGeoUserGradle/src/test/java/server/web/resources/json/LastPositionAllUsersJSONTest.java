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
import com.google.gson.reflect.TypeToken;

import commons.IdPosizione;
import commons.Posizione;
import commons.Utente;
import server.backend.GestoreDatiPersistenti;
import server.backend.wrapper.UserRegistryAPI;

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
		g.dropDatabase();
		UserRegistryAPI ur = UserRegistryAPI.instance();
		TreeMap<String, Utente> temp = new TreeMap<String, Utente>();
		temp.putAll(ur.getUtenti());
		for(String username : temp.keySet()) {
			ur.removeUtente(username);
		}

		//Aggiunta utenti		
		try{
			ur.addUtente(u1);
		}catch(Exception e){
			assertTrue("L'utente "+u1.getUsername() +" non esiste e dovrebbe essere aggiunto al database!", false);			
		}
		try{
			ur.addUtente(u2);
		}catch(Exception e){
			assertTrue("L'utente "+u2.getUsername() +" non esiste e dovrebbe essere aggiunto al database!", false);			
		}
		try{
			ur.addUtente(u3);
		}catch(Exception e){
			assertTrue("L'utente "+u3.getUsername() +" non esiste e dovrebbe essere aggiunto al database!", false);			
		}

		//Aggiunta posizioni ad ogni utente u1 (last position is pos1c)
		try{
			ur.addPosizione(pos1a);
			assertTrue("La posizione1a pur non essendo presente nel database, non e' stata aggiunta ad un utente esistente!", ur.getPosizioni().containsKey(pos1a.getIdPosizione()));	
		}catch(Exception e){
			assertTrue("La posizione1a pur non essendo presente nel database, non e' stata aggiunta ad un utente esistente!", false);	
		}
		try{
			ur.addPosizione(pos1b);
			assertTrue("La posizione1b pur non essendo presente nel database, non e' stata aggiunta ad un utente esistente!", ur.getPosizioni().containsKey(pos1b.getIdPosizione()));	
		}catch(Exception e){
			assertTrue("La posizione1b pur non essendo presente nel database, non e' stata aggiunta ad un utente esistente!", false);	
		}
		try{
			ur.addPosizione(pos1c);
			assertTrue("La posizione1c pur non essendo presente nel database, non e' stata aggiunta ad un utente esistente!", ur.getPosizioni().containsKey(pos1c.getIdPosizione()));	
		}catch(Exception e){
			assertTrue("La posizione1c pur non essendo presente nel database, non e' stata aggiunta ad un utente esistente!", false);	
		}
		
		
		//Aggiunta posizioni ad ogni utente u2 (last position is pos2b)
		try{
			ur.addPosizione(pos2a);
			assertTrue("La posizione2a pur non essendo presente nel database, non e' stata aggiunta ad un utente esistente!", ur.getPosizioni().containsKey(pos2a.getIdPosizione()));	
		}catch(Exception e){
			assertTrue("La posizione2a pur non essendo presente nel database, non e' stata aggiunta ad un utente esistente!", false);	
		}
		try{
			ur.addPosizione(pos2b);
			assertTrue("La posizione2b pur non essendo presente nel database, non e' stata aggiunta ad un utente esistente!", ur.getPosizioni().containsKey(pos2b.getIdPosizione()));	
		}catch(Exception e){
			assertTrue("La posizione2b pur non essendo presente nel database, non e' stata aggiunta ad un utente esistente!", false);	
		}
		try{
			ur.addPosizione(pos2c);
			assertTrue("La posizione2c pur non essendo presente nel database, non e' stata aggiunta ad un utente esistente!", ur.getPosizioni().containsKey(pos2c.getIdPosizione()));	
		}catch(Exception e){
			assertTrue("La posizione2c pur non essendo presente nel database, non e' stata aggiunta ad un utente esistente!", false);	
		}

		//Aggiunta posizioni ad ogni utente u3 (last position is pos3a)
		try{
			ur.addPosizione(pos3a);
			assertTrue("La posizione3a pur non essendo presente nel database, non e' stata aggiunta ad un utente esistente!", ur.getPosizioni().containsKey(pos3a.getIdPosizione()));	
		}catch(Exception e){
			assertTrue("La posizione3a pur non essendo presente nel database, non e' stata aggiunta ad un utente esistente!", false);	
		}
		try{
			ur.addPosizione(pos3b);
			assertTrue("La posizione3b pur non essendo presente nel database, non e' stata aggiunta ad un utente esistente!", ur.getPosizioni().containsKey(pos3b.getIdPosizione()));	
		}catch(Exception e){
			assertTrue("La posizione3b pur non essendo presente nel database, non e' stata aggiunta ad un utente esistente!", false);	
		}
		try{
			ur.addPosizione(pos3c);
			assertTrue("La posizione3c pur non essendo presente nel database, non e' stata aggiunta ad un utente esistente!", ur.getPosizioni().containsKey(pos3c.getIdPosizione()));	
		}catch(Exception e){
			assertTrue("La posizione3c pur non essendo presente nel database, non e' stata aggiunta ad un utente esistente!", false);	
		}
		
		
		assertTrue("Il database dovrebbe avere 3 utenti, size="+g.getUtenti().size(), g.getUtenti().size()==3);	
		assertTrue("La mappa dovrebbe contenere 9 posizioni, size="+g.getPosizioni().size(), g.getPosizioni().size()==9);		
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
		TreeMap<String,Posizione> mapLastPositionAllUsers = gson.fromJson(response, new TypeToken<TreeMap<String,Posizione>>() {}.getType());
		System.out.println(mapLastPositionAllUsers.getClass());
		System.out.println(mapLastPositionAllUsers);
		
		assertTrue("La mappa dovrebbe contenere 3 elementi, size="+mapLastPositionAllUsers.size(), mapLastPositionAllUsers.size()==3);
		assertTrue("L'ultima posizione dell'utente "+u1.getUsername()+" non e' quella prevista", mapLastPositionAllUsers.containsKey(u1.getUsername()) && mapLastPositionAllUsers.get(u1.getUsername()).equals(pos1c));
		assertTrue("L'ultima posizione dell'utente "+u2.getUsername()+" non e' quella prevista", mapLastPositionAllUsers.containsKey(u2.getUsername()) && mapLastPositionAllUsers.get(u2.getUsername()).equals(pos2b));
		assertTrue("L'ultima posizione dell'utente "+u3.getUsername()+" non e' quella prevista", mapLastPositionAllUsers.containsKey(u3.getUsername()) && mapLastPositionAllUsers.get(u3.getUsername()).equals(pos3a));	
	}
}
