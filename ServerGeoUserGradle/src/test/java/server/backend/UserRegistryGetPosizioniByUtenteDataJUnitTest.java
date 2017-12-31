package server.backend;

import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import commons.IdPosizione;
import commons.InvalidDateException;
import commons.InvalidEmailException;
import commons.InvalidPositionException;
import commons.InvalidUsernameException;
import commons.Posizione;
import commons.Utente;

public class UserRegistryGetPosizioniByUtenteDataJUnitTest {
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
	public void testGetPosizioniByUtenteData1() {
		Set<Posizione> posizioni = null;
		try {
			posizioni = this.userreg.getPosizioniUtenteByData(null, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
		} catch (InvalidUsernameException | InvalidDateException e) {
			System.err.println(e.getMessage());
		} finally {
			assertTrue(posizioni.isEmpty());
		}
	}
	
	//Il seguente caso di test fa riferimento all'ottenimento delle posizioni di un utente non esistente
	@Test(expected=InvalidUsernameException.class)
	public void testGetPosizioniByUtenteData2() throws InvalidUsernameException, InvalidDateException {
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
				
		this.userreg.getPosizioniUtenteByData("antonio.user", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));	
	}
	
	//Il seguente caso di test fa riferimento all'ottenimento delle posizioni di un utente esistente con date (from e to) null
	@Test(expected=InvalidDateException.class)
	public void testGetPosizioniByUtenteData3() throws InvalidUsernameException, InvalidDateException {
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
				
		this.userreg.getPosizioniUtenteByData(utente.getUsername(), null, null);	
		
	}
	
	//Il seguente caso di test fa riferimento all'ottenimento delle posizioni di un utente con una data null (from) e una non valida (to)
	@Test
	public void testGetPosizioniByUtenteData4() {
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
			posizioni = this.userreg.getPosizioniUtenteByData(utente.getUsername(), null, Timestamp.valueOf(LocalDateTime.of(2017, 8, 15, 12, 0, 10)));
		} catch (InvalidUsernameException | InvalidDateException e) {
			System.err.println(e.getMessage());
		} finally {
			assertTrue(!(posizioni.contains(pos1) && posizioni.contains(pos2)));
		}
	}
	
	//Il seguente caso di test fa riferimento all'ottenimento delle posizioni di un utente con una data null (from) e una valida (to)
	@Test
	public void testGetPosizioniByUtenteData5() {
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
			posizioni = this.userreg.getPosizioniUtenteByData(utente.getUsername(), null, Timestamp.valueOf(LocalDateTime.of(2017, 9, 10, 12, 0, 10)));
		} catch (InvalidUsernameException | InvalidDateException e) {
			System.err.println(e.getMessage());
		} finally {
			assertTrue(posizioni.contains(pos1) && !posizioni.contains(pos2));
		}		
	}

	//Il seguente caso di test fa riferimento all'ottenimento delle posizioni di un utente con una data  valida (from) e una null (to) 
	@Test
	public void testGetPosizioniByUtenteData6() {
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
			posizioni = this.userreg.getPosizioniUtenteByData(utente.getUsername(), Timestamp.valueOf(LocalDateTime.of(2017, 9, 10, 12, 0, 10)), null);
		} catch (InvalidUsernameException | InvalidDateException e) {
			System.err.println(e.getMessage());
		} finally {
			assertTrue(!posizioni.contains(pos1) && posizioni.contains(pos2));
		}		
	}

	//Il seguente caso di test fa riferimento all'ottenimento delle posizioni di un utente con una data non valida (from) e una null (to) 
	@Test
	public void testGetPosizioniByUtenteData7() {
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
			posizioni = this.userreg.getPosizioniUtenteByData(utente.getUsername(), Timestamp.valueOf(LocalDateTime.of(2017, 11, 5, 12, 0, 10)), null);
		} catch (InvalidUsernameException | InvalidDateException e) {
			System.err.println(e.getMessage());
		} finally {
			assertTrue(!(posizioni.contains(pos1) && posizioni.contains(pos2)));
		}				
	}
	
	//Il seguente caso di test fa riferimento all'ottenimento delle posizioni di un utente con date valide (from e to)
	@Test
	public void testGetPosizioniByUtenteData8() {
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
			posizioni = this.userreg.getPosizioniUtenteByData(utente.getUsername(), Timestamp.valueOf(LocalDateTime.of(2017, 9, 17, 12, 0, 10)), Timestamp.valueOf(LocalDateTime.of(2017, 12, 8, 12, 0, 10)));
		} catch (InvalidUsernameException | InvalidDateException e) {
			System.err.println(e.getMessage());
		} finally {
			assertTrue(!posizioni.contains(pos1) && posizioni.contains(pos2));
		}				
	}

	//Il seguente caso di test fa riferimento all'ottenimento delle posizioni di un utente con from successiva a to
	@Test(expected=InvalidDateException.class)
	public void testGetPosizioniByUtenteData9() throws InvalidUsernameException, InvalidDateException {
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
		
		this.userreg.getPosizioniUtenteByData(utente.getUsername(), Timestamp.valueOf(LocalDateTime.of(2017, 12, 8, 12, 0, 10)), Timestamp.valueOf(LocalDateTime.of(2017, 9, 4, 12, 0, 10)));				
	}

}
