package server.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.TreeMap;

import commons.IdPosizione;
import commons.Posizione;
import commons.Utente;

public class GestoreDatiPersistenti {
	private GestoreDatiPersistenti() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println("Errore caricamento driver MySQL");
		}
		
		this.connection = null;
		
		try {
			this.connection = DriverManager.getConnection(url);
			this.stm = this.connection.createStatement();
		} catch (SQLException e) {
			System.err.println("Errore connessione database");
		}
	}
	
	public static synchronized GestoreDatiPersistenti getInstance() {
		if(istanza == null) 
			istanza = new GestoreDatiPersistenti();
		return istanza;
	}
	
	//Caricamento utenti database
	public TreeMap<String, Utente> getUtenti(){
		TreeMap<String, Utente> utenti = new TreeMap<String, Utente>();
		ResultSet rs = null;
		Utente utente = null;
		System.out.println("Caricamento utenti:");
		try {
			rs = stm.executeQuery("select * from utenti");
			while (rs.next()) {
				utente = new Utente(rs.getString("username"), rs.getString("password"), rs.getString("email"), rs.getString("nome"), rs.getString("cognome"));
				utenti.put(utente.getUsername(), utente);
				System.out.println(utente.toString());
			}
			return utenti;
		} catch (SQLException e) {
			System.err.println("Errore caricamento utenti");
			return null;
		}
	}
	
	//Caricamento posizioni database
	public TreeMap<IdPosizione, Posizione> getPosizioni() {
		TreeMap<IdPosizione, Posizione> posizioni = new TreeMap<IdPosizione, Posizione>();
		TreeMap<String, Utente> utenti;
		ResultSet rs = null;
		IdPosizione idPos = null;
		Posizione posizione = null;
		System.out.println("Caricamento posizioni:");
		
		try {
			utenti=getUtenti();
			rs = stm.executeQuery("select * from posizioni");
			while (rs.next()) {
				idPos = new IdPosizione(Timestamp.valueOf(rs.getString("timestamp")), Double.parseDouble(rs.getString("latitudine")), Double.parseDouble(rs.getString("longitudine")));
				posizione = new Posizione(idPos, utenti.get(rs.getString("utente")), Float.parseFloat(rs.getString("accuratezza")));
				posizioni.put(idPos, posizione);
				System.out.println(posizione.toString());
			}
			return posizioni;
		} catch (SQLException e) {
			System.err.println("Errore caricamento posizioni");
			return null;
		}
	}
	
	
	
	//Aggiunta utente al database
	public boolean addUtente(Utente utente) {
		System.out.println("Inserimento utente: "+utente.getUsername());
		try {
			//System.out.println("insert into utenti (username, password, email, nome, cognome) values('" + utente.getUsername() + "','" + utente.getPassword() + "','" + utente.getEmail() + "','" + utente.getNome() + "','" + utente.getCognome() + "')");
			stm.executeUpdate("insert into utenti (username, password, email, nome, cognome) values('" + utente.getUsername() + "','" + utente.getPassword() + "','" + utente.getEmail() + "','" + utente.getNome() + "','" + utente.getCognome() + "')");
			return true;
		} catch (SQLException e) {
			System.err.println("Errore inserimento utente: "+utente.getUsername());
			return false;
		}
	}
	
	//Rimozione utente dal database
	public boolean removeUtente(Utente utente) {
		System.out.println("Eliminazione utente: "+ utente.getUsername());
		try {
			stm.executeUpdate("delete from utenti where username='"+utente.getUsername()+"'");
			return true;
		} catch (SQLException e) {
			System.err.println("Errore eliminazione utente: "+ utente.getUsername());
			return false;
		}
	}
	
	
	//Aggiunta posizione al database
	public boolean addPosizione(Posizione pos) {
		System.out.println("Inserimento posizione: " + pos.toString());
		try {
			stm.executeUpdate("insert into posizioni (utente, latitudine, longitudine, timestamp, accuratezza) values('" + pos.getUtente().getUsername() + "','" + pos.getIdPosizione().getLatitudine() + "','" + pos.getIdPosizione().getLongitudine() + "','" + pos.getIdPosizione().getTimestamp() + "','" + pos.getAccuratezza() + "')");
			return true;
		} catch (SQLException e) {
			System.err.println("Errore inserimento posizione: " + pos.toString());
			return false;
		}
	}
	
	//Rimozione posizione dal database
	public boolean removePosizione(Posizione pos) {
		System.out.println("Eliminazione posizione: "+ pos.getIdPosizione().toString());
		try {
			stm.executeUpdate("delete from posizioni where utente='"+pos.getUtente().getUsername()+"' AND timestamp='"+pos.getIdPosizione().getTimestamp()+"'");
			return true;
		} catch (SQLException e) {
			System.err.println("Errore eliminazione posizione: "+ pos.getIdPosizione().toString());
			return false;
		}
	}		
	
	//Rimozione tutte le posizioni dal database
	public boolean removePosizioniUtente(Utente utente) {
		System.out.println("Eliminazione posizioni dell'utente: "+ utente.getUsername());
		try {
			stm.executeUpdate("delete from posizioni where utente='"+utente.getUsername()+"'");
			return true;
		} catch (SQLException e) {
			System.err.println("Errore eliminazione posizioni dell'utente: "+ utente.getUsername());
			return false;
		}
	}	
	
	//Svuotamento del database
	public boolean dropDatabase() {
		System.out.println("Svuotamento database... ");
		try {
			stm.executeUpdate("delete from posizioni");
			stm.executeUpdate("delete from utenti");
			return true;
		} catch (SQLException e) {
			System.err.println("Errore svuotamento database");
			return false;
		}
	}	

	private final static String url = "jdbc:mysql://localhost:3306/geouserdb?user=root&password=";
	private Connection connection;
	private Statement stm;
	private static GestoreDatiPersistenti istanza;
}
