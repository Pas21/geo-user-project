package server.backend;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import commons.InvalidEmailException;
import commons.InvalidUsernameException;
import commons.Utente;

public class UserRegistryAddUtenteJUnitTest {
private UserRegistry userreg = new UserRegistry();
	
	@Before
	public void setUp() throws Exception {
		this.userreg.getGestoreDB().dropDatabase();
	}

	@After
	public void tearDown() throws Exception {
		this.userreg.getGestoreDB().dropDatabase();
	}
	
	//Il seguente caso di test fa riferimento all'aggiunta di un utente null.
	@Test
	public void testAddUtente1() {
		Utente utente = null;
		try {
			this.userreg.addUtente(utente);
		} catch (InvalidUsernameException | InvalidEmailException e) {
			System.err.println(e.getMessage());
		} finally {
			assertTrue(this.userreg.getUtenti().isEmpty());
		}
		
	}
	
	//Il seguente caso di test fa riferimento all'aggiunta di un utente con username null
	@Test
	public void testAddUtente2() {
		Utente utente = new Utente();
		utente.setUsername(null);
		try {
			this.userreg.addUtente(utente);
		} catch (InvalidUsernameException | InvalidEmailException e) {
			System.err.println(e.getMessage());
		} finally {
			assertTrue(this.userreg.getUtenti().isEmpty());
		}
		
	}
	
	//Il seguente caso di test fa riferimento all'aggiunta di un nuovo utente con email null
	@Test
	public void testAddUtente3() {
		Utente utente = new Utente("pietro.user","pietro.pass",null,"pietro","goglia");
		try {
			this.userreg.addUtente(utente);
		} catch (InvalidUsernameException | InvalidEmailException e) {
			System.err.println(e.getMessage());
		} finally {
			assertTrue(!this.userreg.getUtenti().containsKey(utente.getUsername()));
			assertTrue(this.userreg.getUtenti().isEmpty());
		}		
	}
	
	// Il seguente caso di test fa riferimento all'aggiunta di un nuovo utente.
	@Test
	public void testAddUtente4() {
		Utente utente = new Utente("pietro.user","pietro.pass","pietro.email","pietro","goglia");
		try {
			this.userreg.addUtente(utente);
		} catch (InvalidUsernameException | InvalidEmailException e) {
			System.err.println(e.getMessage());
		} finally {
			assertTrue(this.userreg.getUtenti().containsValue(utente));
		}
		
	}
	
	// Il seguente caso di test fa riferimento all'aggiunta di un utente esistente.
	@Test(expected=InvalidUsernameException.class)
	public void testAddUtente5() throws InvalidUsernameException, InvalidEmailException {
		//Aggiunta nuovo utente
		Utente utente = new Utente("pietro.user","pietro.pass","ptr.email","pietro","goglia");
		try {
			this.userreg.addUtente(utente);
		} catch (InvalidUsernameException | InvalidEmailException e) {
			System.err.println(e.getMessage());
		} finally {
			assertTrue(this.userreg.getUtenti().containsValue(utente));
		}
		
		//Aggiunta utente precedente		
		this.userreg.addUtente(utente);
	}
	
	//Il seguente caso di test fa riferimento all'aggiunta di un nuovo utente con email esistente
	@Test(expected=InvalidEmailException.class)
	public void testAddUtente6() throws InvalidUsernameException, InvalidEmailException {
		//Aggiunta nuovo utente
		Utente utente = new Utente("pietro.user","pietro.pass","pietro.email","pietro","goglia");
		try {
			this.userreg.addUtente(utente);
		} catch (InvalidUsernameException | InvalidEmailException e) {
			System.err.println(e.getMessage());
		} finally {
			assertTrue(this.userreg.getUtenti().containsValue(utente));
		}
		
		//Aggiunta utente precedente
		Utente utente2 = new Utente("antonio.user","antonio.pass","pietro.email","antonio","varone");
		this.userreg.addUtente(utente2);
	}
	
}
