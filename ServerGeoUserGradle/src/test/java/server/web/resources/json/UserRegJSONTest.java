package server.web.resources.json;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;

import commons.Utente;
import server.backend.GestoreDatiPersistenti;
import server.web.frontend.UserRegistryWebApplication;

public class UserRegJSONTest {
	static GestoreDatiPersistenti g = GestoreDatiPersistenti.getInstance();
	static UserRegJSON userRegJson= new UserRegJSON();
	static Gson gson=new Gson();
	
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
		Utente utente1= new Utente("pas", "pas", "pas@gmail.com", "Pasquale", "Forgione");
		Utente utente2= new Utente("lor", "lor", "lor@gmail.com", "Lorenzo", "Goglia");
		Utente utente3= new Utente("ant", "ant", "ant@gmail.com", "Antonio", "Varone");
		
		userRegJson.addUser(gson.toJson(utente1,Utente.class));
		UserRegistryWebApplication.verifier.getLocalSecrets().put(utente1.getUsername(), utente1.getPassword().toCharArray());
		userRegJson.addUser(gson.toJson(utente2,Utente.class));
		UserRegistryWebApplication.verifier.getLocalSecrets().put(utente2.getUsername(), utente2.getPassword().toCharArray());
		userRegJson.addUser(gson.toJson(utente3,Utente.class));
		UserRegistryWebApplication.verifier.getLocalSecrets().put(utente3.getUsername(), utente3.getPassword().toCharArray());
	}

	@After
	public void tearDown() throws Exception {
		g.dropDatabase();
	}

	@Test
	public void test() {		
		Utente u1= new Utente("pas", "pas", "pas@gmail.com", "Pasquale", "Forgione");
		Utente u2= new Utente("gio", "gio", "gio@gmail.com", "Giovanni", "Pirozzi");
		Utente u3= new Utente("a", "a", "a@gmail.com", "A", "A");
	

		//aggiunta utente gia' esistente
		String u1S=gson.toJson(u1,Utente.class);
		try{
			gson.fromJson(userRegJson.addUser(u1S),String.class);
			assertTrue("L'utente "+u1.getUsername() +" gia' esiste e non dovrebbe essere aggiunto al database!", false);
		}catch (Exception e) {
			assertTrue("L'utente "+u1.getUsername() +" gia' esiste e non dovrebbe essere aggiunto al database!", true);
		}
		
		//aggiunta utente con email esistente
		Utente u1e=new Utente("pasquale", "pasquale", "pas@gmail.com", "Pasquale", "Forgione");
		String u1eS=gson.toJson(u1e,Utente.class);
		try{
			gson.fromJson(userRegJson.addUser(u1eS),String.class);
			assertTrue("L'utente "+u1e.getUsername() +" ha un email gia' esistente e non dovrebbe essere aggiunto al database!", false);
		}catch (Exception e) {
			assertTrue("L'utente "+u1e.getUsername() +" ha un email gia' esistente e non dovrebbe essere aggiunto al database!", true);
		}
		

		//aggiunta utente non esistente
		String u2S=gson.toJson(u2,Utente.class);
		try{
			gson.fromJson(userRegJson.addUser(u2S),String.class);
			assertTrue("L'utente "+u2.getUsername() +" non esiste e dovrebbe essere aggiunto al database!", true);
		}catch (Exception e) {
			assertTrue("L'utente "+u2.getUsername() +" non esiste e dovrebbe essere aggiunto al database!", false);
		}
		UserRegistryWebApplication.verifier.getLocalSecrets().put(u2.getUsername(), u2.getPassword().toCharArray());


		System.out.println(UserRegistryWebApplication.verifier.getLocalSecrets().toString());
		//check utente esistente password corretta
		assertTrue("L'utente "+u1.getUsername() +" esiste, le credenziali sono corrette e dovrebbe autenticarsi!", gson.fromJson(userRegJson.checkUser(gson.toJson(u1.getUsername()+";"+u1.getPassword(),String.class)), Boolean.class));

		//check utente esistente password sbagliata
		assertTrue("L'utente "+u1.getUsername() +" esiste, ma le credenziali sono sbagliate e non potrebbe autenticarsi!", !gson.fromJson(userRegJson.checkUser(gson.toJson(u1.getUsername()+";ahbfakshvbadfk",String.class)), Boolean.class));
		
		//check utente non esistente
		assertTrue("L'utente "+u3.getUsername() +" non esiste e non potrebbe autenticarsi!", !gson.fromJson(userRegJson.checkUser(gson.toJson(u3.getUsername()+";"+u3.getPassword(),String.class)), Boolean.class));

	}

}
