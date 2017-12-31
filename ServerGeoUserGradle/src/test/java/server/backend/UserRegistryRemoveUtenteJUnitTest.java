package server.backend;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import commons.InvalidEmailException;
import commons.InvalidUsernameException;
import commons.Utente;

public class UserRegistryRemoveUtenteJUnitTest {
private UserRegistry userreg = new UserRegistry();
	
	@Before
	public void setUp() throws Exception {
		this.userreg.getGestoreDB().dropDatabase();
	}

	@After
	public void tearDown() throws Exception {
		this.userreg.getGestoreDB().dropDatabase();
	}
	
	//Il seguente caso di test fa riferimento alla rimozione di un utente null
	@Test
	public void testRemoveUtente1() {
		boolean result = false;
		try {
			result = this.userreg.removeUtente(null);
		} catch (InvalidUsernameException e) {
			System.err.println(e.getMessage());
		} finally {
			assertTrue(!result);
		}
	}
	
	//Il seguente caso di test fa riferimento alla rimozione di un utente esistente
	@Test
	public void testRemoveUtente2() {
		//Aggiunta nuovo utente
		Utente utente = new Utente("pietro.user","pietro.pass","ptr.email","pietro","goglia");
		try {
			this.userreg.addUtente(utente);
		} catch (InvalidUsernameException | InvalidEmailException e) {
			System.err.println(e.getMessage());
		} finally {
			assertTrue(this.userreg.getUtenti().containsValue(utente));
		}
		
		boolean result = false;
		try {
			result = this.userreg.removeUtente(utente.getUsername());
		} catch(InvalidUsernameException e) {
			System.err.println(e.getMessage());
		} finally {
			assertTrue(!this.userreg.getUtenti().containsValue(utente));
			assertTrue(this.userreg.getUtenti().isEmpty());
			assertTrue(result);
		}
	}
	
	//Il seguente caso di test fa riferimento alla rimozione di un utente non esistente
	@Test(expected=InvalidUsernameException.class)
	public void testRemoveUtente3() throws InvalidUsernameException {
		//Aggiunta nuovo utente
		Utente utente = new Utente("pietro.user","pietro.pass","ptr.email","pietro","goglia");
		try {
			this.userreg.addUtente(utente);
		} catch (InvalidUsernameException | InvalidEmailException e) {
			System.err.println(e.getMessage());
		} finally {
			assertTrue(this.userreg.getUtenti().containsValue(utente));
		}
		
		this.userreg.removeUtente("antonio.user");
		assertTrue(this.userreg.getUtenti().containsValue(utente));
	}	
}
