package server.backend;

import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import commons.IdPosizione;
import commons.InvalidEmailException;
import commons.InvalidPositionException;
import commons.InvalidUsernameException;
import commons.Posizione;
import commons.Utente;

public class UserRegistryRemovePosizioneJUnitTest {
private UserRegistry userreg = new UserRegistry();
	
	@Before
	public void setUp() throws Exception {
		this.userreg.getGestoreDB().dropDatabase();
	}

	@After
	public void tearDown() throws Exception {
		this.userreg.getGestoreDB().dropDatabase();
	}
	
	//Il seguente caso di test fa riferimento alla rimozione di una posizione con id null
	@Test
	public void testRemovePosizione1() {
		boolean result = false;
		try {
			result = this.userreg.removePosizione(null);
		} catch (InvalidPositionException e) {
			System.err.println(e.getMessage());
		} finally {
			assertTrue(!result);
		}
	}

	//Il seguente caso di test fa riferimento alla rimozione di una posizione esistente
	@Test
	public void testRemovePosizione2() {
		//Aggiunta nuovo utente
		Utente utente = new Utente("pietro.user","pietro.pass","ptr.email","pietro","goglia");
		try {
			this.userreg.addUtente(utente);
		} catch (InvalidUsernameException | InvalidEmailException e) {
			System.err.println(e.getMessage());
		} finally {
			assertTrue(this.userreg.getUtenti().containsValue(utente));
		}		
		
		//Aggiunta posizioni utente
		IdPosizione idpos = new IdPosizione(Timestamp.valueOf(LocalDateTime.of(2017, 8, 27, 12, 0, 10)), Math.random(), Math.random());
		Posizione pos1 = new Posizione(idpos, utente, 20);
		try {
			this.userreg.addPosizione(pos1);
		} catch (InvalidUsernameException | InvalidPositionException e) {
			System.err.println(e.getMessage());
		} finally {
			assertTrue(this.userreg.getPosizioni().containsValue(pos1));
		}
		
		boolean result = false;
		try {
			result = this.userreg.removePosizione(idpos);
		} catch (InvalidPositionException e) {
			System.err.println(e.getMessage());
		} finally {
			assertTrue(!this.userreg.getPosizioni().containsKey(idpos));
			assertTrue(!this.userreg.getUtenti().get(utente.getUsername()).getPosizioni().contains(pos1));
			assertTrue(result);
		}
	}
	
	//Il seguente caso di test fa riferimento alla rimozione di una posizione non esistente
	@Test(expected=InvalidPositionException.class)
	public void testRemovePosizione3() throws InvalidPositionException {
		//Aggiunta nuovo utente
		Utente utente = new Utente("pietro.user","pietro.pass","ptr.email","pietro","goglia");
		try {
			this.userreg.addUtente(utente);
		} catch (InvalidUsernameException | InvalidEmailException e) {
			System.err.println(e.getMessage());
		} finally {
			assertTrue(this.userreg.getUtenti().containsValue(utente));
		}		
		
		//Aggiunta posizioni utente
		IdPosizione idpos = new IdPosizione(Timestamp.valueOf(LocalDateTime.of(2017, 8, 27, 12, 0, 10)), Math.random(), Math.random());
		Posizione pos1 = new Posizione(idpos, utente, 20);
		try {
			this.userreg.addPosizione(pos1);
		} catch (InvalidUsernameException | InvalidPositionException e) {
			System.err.println(e.getMessage());
		} finally {
			assertTrue(this.userreg.getPosizioni().containsValue(pos1));
		}
		
		this.userreg.removePosizione(new IdPosizione(Timestamp.valueOf(LocalDateTime.of(2016, 8, 27, 12, 0, 10)), Math.random(), Math.random()));
	}
}
