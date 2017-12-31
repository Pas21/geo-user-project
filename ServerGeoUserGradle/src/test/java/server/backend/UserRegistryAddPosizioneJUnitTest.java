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

public class UserRegistryAddPosizioneJUnitTest {
private UserRegistry userreg = new UserRegistry();
	
	@Before
	public void setUp() throws Exception {
		this.userreg.getGestoreDB().dropDatabase();
	}

	@After
	public void tearDown() throws Exception {
		this.userreg.getGestoreDB().dropDatabase();
	}
	
	//Il seguente caso di test fa riferimento all'aggiunta di una posizione null
	@Test
	public void testAddPosizione1() {
		try {
			this.userreg.addPosizione(null);
		} catch (InvalidUsernameException | InvalidPositionException e) {
			System.err.println(e.getMessage());
		} finally {
			assertTrue(this.userreg.getPosizioni().isEmpty());
		}
	}
	
	//Il seguente caso di test fa riferimento all'aggiunta di una posizione con utente null
	@Test
	public void testAddPosizione2() {
		Posizione pos = new Posizione();
		pos.setUtente(null);
		try {
			this.userreg.addPosizione(pos);
		} catch (InvalidUsernameException | InvalidPositionException e) {
			System.err.println(e.getMessage());
		} finally {
			assertTrue(this.userreg.getPosizioni().isEmpty());
		}
	}

	//Il seguente caso di test fa riferimento all'aggiunta di una posizione ad un utente con username null
	@Test
	public void testAddPosizione3() {
		Posizione pos = new Posizione();
		Utente utente = new Utente();
		utente.setUsername(null);
		pos.setUtente(utente);
		try {
			this.userreg.addPosizione(pos);
		} catch (InvalidUsernameException | InvalidPositionException e) {
			System.err.println(e.getMessage());
		} finally {
			assertTrue(this.userreg.getPosizioni().isEmpty());
		}		
	}	
	
	//Il seguente caso di test fa riferimento all'aggiunta di una posizione ad un utente non esistente
	@Test(expected=InvalidUsernameException.class)
	public void testAddPosizione4() throws InvalidUsernameException, InvalidPositionException {
		Posizione pos1 = new Posizione(new IdPosizione(Timestamp.valueOf(LocalDateTime.of(2017, 8, 27, 12, 0, 10)), Math.random(), Math.random()), new Utente("pietro.user","pietro.pass","pietro.email","pietro","goglia"), 20);
		this.userreg.addPosizione(pos1);
	}
	
	//Il seguente caso di test fa riferimento all'aggiunta di una posizione con id null ad un utente esistente
	@Test
	public void testAddPosizione5() {
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
		Posizione pos1 = new Posizione(null, utente, 20);
		try {
			this.userreg.addPosizione(pos1);
		} catch (InvalidUsernameException | InvalidPositionException e) {
			System.err.println(e.getMessage());
		} finally {
			assertTrue(this.userreg.getPosizioni().isEmpty());
		}	
	}	
	
	//Il seguente caso di test fa riferimento all'aggiunta di una posizione con id valido ad un utente esistente
	@Test
	public void testAddPosizione6() {
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
		Posizione pos1 = new Posizione(new IdPosizione(Timestamp.valueOf(LocalDateTime.of(2017, 8, 27, 12, 0, 10)), Math.random(), Math.random()), utente, 20);
		try {
			this.userreg.addPosizione(pos1);
		} catch (InvalidUsernameException | InvalidPositionException e) {
			System.err.println(e.getMessage());
		} finally {
			assertTrue(this.userreg.getPosizioni().containsValue(pos1));
		}			
	}
	
	//Il seguente caso di test fa riferimento all'aggiunta di una posizione con id non valido ad un utente esistente
	@Test(expected=InvalidPositionException.class)
	public void testAddPosizione7() throws InvalidUsernameException, InvalidPositionException {
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
		
		this.userreg.addPosizione(pos1);
	}
}
