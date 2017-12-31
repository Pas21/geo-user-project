package server.backend;

import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import commons.IdPosizione;
import commons.InvalidEmailException;
import commons.InvalidPositionException;
import commons.InvalidUsernameException;
import commons.Posizione;
import commons.Utente;

public class UserRegistryGetPosizioniByUtenteJUnitTest {
private UserRegistry userreg = new UserRegistry();
	
	@Before
	public void setUp() throws Exception {
		this.userreg.getGestoreDB().dropDatabase();
	}

	@After
	public void tearDown() throws Exception {
		this.userreg.getGestoreDB().dropDatabase();
	}
	
	//Il seguente caso di test fa riferimento all'ottenimento delle posizioni di un utente con username null
	@Test
	public void testGetPosizioniByUtente1() {
		Set<Posizione> posizioni = null;
		try {
			posizioni = this.userreg.getPosizioniByUtente(null);
		} catch (InvalidUsernameException e) {
			System.err.println(e.getMessage());
		} finally {
			assertTrue(posizioni.isEmpty());
		}
	}
	
	//Il seguente caso di test fa riferimento all'ottenimento delle posizioni di un utente esistente
	@Test
	public void testGetPosizioniByUtente2() {
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
		Posizione pos2 = new Posizione(new IdPosizione(Timestamp.valueOf(LocalDateTime.of(2017, 10, 2, 10, 23, 0)), Math.random(), Math.random()), utente, 20);		
		try {
			this.userreg.addPosizione(pos1);
			this.userreg.addPosizione(pos2);
		} catch (InvalidUsernameException | InvalidPositionException e) {
			System.err.println(e.getMessage());
		} finally {
			assertTrue(this.userreg.getPosizioni().containsValue(pos1) && this.userreg.getPosizioni().containsValue(pos2));
		}
		
		Set<Posizione> posizioni = null;
		try {
			posizioni = this.userreg.getPosizioniByUtente(utente.getUsername());
		} catch (InvalidUsernameException e) {
			System.err.println(e.getMessage());
		} finally {
			assertTrue(posizioni.contains(pos1) && posizioni.contains(pos2));
		}
	}
	
	//Il seguente caso di test fa riferimento all'ottenimento delle posizioni di un utente non esistente
	@Test(expected=InvalidUsernameException.class)
	public void testGetPosizioniByUtente3() throws InvalidUsernameException {
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
		Posizione pos2 = new Posizione(new IdPosizione(Timestamp.valueOf(LocalDateTime.of(2017, 10, 2, 10, 23, 0)), Math.random(), Math.random()), utente, 20);		
		try {
			this.userreg.addPosizione(pos1);
			this.userreg.addPosizione(pos2);
		} catch (InvalidUsernameException | InvalidPositionException e) {
			System.err.println(e.getMessage());
		} finally {
			assertTrue(this.userreg.getPosizioni().containsValue(pos1) && this.userreg.getPosizioni().containsValue(pos2));
		}
				
		this.userreg.getPosizioniByUtente("antonio.user");
	}

}
