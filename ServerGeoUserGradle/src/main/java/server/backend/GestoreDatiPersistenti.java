package server.backend;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.TreeMap;

import com.google.gson.Gson;

import commons.IdPosizione;
import commons.Posizione;
import commons.Utente;

/**
 * The Class GestoreDatiPersistenti is a pure fabrication class that has the purpose of interfacing directly with the database through the MySQL driver.
 */
public class GestoreDatiPersistenti {
	
	/**
	 * Instantiates the singleton instance of GestoreDatiPersistenti.
	 */
	private GestoreDatiPersistenti() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println("Errore caricamento driver MySQL");
		}
		
		this.connection = null;
		
		Gson gson=new Gson();
		SettingsDB settingsDB=null;
		BufferedReader br = null;
		FileInputStream fis = null;
		InputStreamReader isr = null;
		try{
			fis = new FileInputStream("settingsdb.json");
			isr = new InputStreamReader(fis, Charset.defaultCharset());
			br = new BufferedReader(isr);
			settingsDB=gson.fromJson(br.readLine(), SettingsDB.class);
			System.err.println("Loading database settings from file");
			br.close();
			fis.close();
		}catch (IOException e){
			System.err.println("Database settings file not found, creation of the file with default values");
			BufferedWriter writer = null;
			try {
				writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("settingsdb.json"), Charset.defaultCharset()));
				writer.write("{userDB:'root',passwordDB:''}");
				settingsDB=gson.fromJson("{userDB:'root',passwordDB:''}", SettingsDB.class);
				writer.close();
			} catch (IOException e1) {
				System.err.println("Error in creation of the file, set the default values");
			}finally {
				if(writer!=null){
					try {
						writer.close();
					} catch (IOException e2) {
						e.printStackTrace();
					}
				}
			}
		}finally{
			if(br!=null){
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(isr!=null){
				try {
					isr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		try{
			url = "jdbc:mysql://localhost:3306/geouserdb?user="+settingsDB.userDB+"&password="+settingsDB.passwordDB;
			System.err.println("Credentials DB: " + url);
		}catch(NullPointerException e){
			url = "jdbc:mysql://localhost:3306/geouserdb?user=root&password=";
			System.err.println("Error in loading database credentials, set default url: " + url);
		}
		
		try {
			this.connection = DriverManager.getConnection(url);
		} catch (SQLException e) {
			System.err.println("Error in connecting to the database");
		}
	}
	
	/**
	 * Gets the singleton istance of GestoreDatiPersistenti.
	 *
	 * @return istance the singleton instance of GestoreDatiPersistenti
	 */
	public static synchronized GestoreDatiPersistenti getInstance() {
		if(instance == null) 
			instance = new GestoreDatiPersistenti();
		return instance;
	}
	
	/**
	 * Gets the connection to the database.
	 *
	 * @return connection the connection to the database
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * Gets all registered users on the database.
	 *
	 * @return utenti all registered users on the database
	 */
	//Caricamento utenti database
	public TreeMap<String, Utente> getUtenti(){
		TreeMap<String, Utente> utenti = new TreeMap<String, Utente>();
		Statement stm=null;
		ResultSet rs = null;
		Utente utente = null;
		System.out.println("Caricamento utenti...");
		try {
			stm=this.connection.createStatement();
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
		}finally {
			try {
				if(stm!=null)
					stm.close();
				if(rs!=null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Gets all registered positions on the database.
	 *
	 * @return posizioni all registered positions on the database
	 */
	//Caricamento posizioni database
	public TreeMap<IdPosizione, Posizione> getPosizioni() {
		TreeMap<IdPosizione, Posizione> posizioni = new TreeMap<IdPosizione, Posizione>();
		TreeMap<String, Utente> utenti;
		Statement stm=null;
		ResultSet rs = null;
		IdPosizione idPos = null;
		Posizione posizione = null;
		System.out.println("Caricamento posizioni...");
		
		try {
			stm=this.connection.createStatement();
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
		}finally {
			try {
				if(stm!=null)
					stm.close();
				if(rs!=null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * Inserts a new user to the database.
	 *
	 * @param utente the user to add
	 * @return true, if successful and false otherwise
	 */
	//Aggiunta utente al database
	public boolean addUtente(Utente utente) {
		System.out.println("Inserimento utente: "+utente.getUsername());
		PreparedStatement stm=null;
		try {
			//stm.executeUpdate("insert into utenti (username, password, email, nome, cognome) values('" + utente.getUsername() + "','" + utente.getPassword() + "','" + utente.getEmail() + "','" + utente.getNome() + "','" + utente.getCognome() + "')");

			stm = this.connection.prepareStatement("INSERT INTO utenti (username, password, email, nome, cognome) values (?,?,?,?,?)");
			stm.setString(1, utente.getUsername()); 	/* username */
			stm.setString(2, utente.getPassword()); 	/* password */
			stm.setString(3, utente.getEmail());  		/* email */
			stm.setString(4, utente.getNome());   		/* nome */
			stm.setString(5, utente.getCognome());    	/* cognome */
			stm.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.err.println("Errore inserimento utente: "+utente.getUsername());
			return false;
		}finally {
			try {
				if(stm!=null)
					stm.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Deletes user from database.
	 *
	 * @param utente the user to delete
	 * @return true, if successful and false otherwise
	 */
	//Rimozione utente dal database
	public boolean removeUtente(Utente utente) {
		System.out.println("Eliminazione utente: "+ utente.getUsername());
		PreparedStatement stm=null;
		try {
			stm = this.connection.prepareStatement("DELETE FROM utenti where username=?");
			stm.setString(1, utente.getUsername());
			stm.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.err.println("Errore eliminazione utente: "+ utente.getUsername());
			return false;
		}finally {
			try {
				if(stm!=null)
					stm.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
		
	/**
	 * Inserts a new position to the database.
	 *
	 * @param newPosition the new position to insert
	 * @return true, if successful and false otherwise
	 */
	//Aggiunta posizione al database
	public boolean addPosizione(Posizione newPosition) {
		System.out.println("Inserimento posizione: " + newPosition.toString());
		PreparedStatement stm=null;
		try {
			//stm.executeUpdate("insert into posizioni (utente, latitudine, longitudine, timestamp, accuratezza) values('" + pos.getUtente().getUsername() + "','" + pos.getIdPosizione().getLatitudine() + "','" + pos.getIdPosizione().getLongitudine() + "','" + pos.getIdPosizione().getTimestamp() + "','" + pos.getAccuratezza() + "')");

			stm = this.connection.prepareStatement("INSERT INTO posizioni (utente, latitudine, longitudine, timestamp, accuratezza) values(?,?,?,?,?)");
			stm.setString(1, newPosition.getUtente().getUsername()); 									/* utente */
			stm.setString(2, String.valueOf(newPosition.getIdPosizione().getLatitudine())); 			/* latitudine */
			stm.setString(3, String.valueOf(newPosition.getIdPosizione().getLongitudine()));  			/* longitudine */
			stm.setString(4, String.valueOf(newPosition.getIdPosizione().getTimestamp())); 				/* timestamp */
			stm.setString(5, String.valueOf(newPosition.getAccuratezza()));    							/* accuratezza */
			stm.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.err.println("Errore inserimento posizione: " + newPosition.toString());
			return false;
		}finally {
			try {
				if(stm!=null)
					stm.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Deletes an existent position from database.
	 *
	 * @param position the position to delete
	 * @return true, if successful and false otherwise
	 */
	//Rimozione posizione dal database
	public boolean removePosizione(Posizione position) {
		System.out.println("Eliminazione posizione: "+ position.getIdPosizione().toString());
		PreparedStatement stm=null;
		try {
			stm = this.connection.prepareStatement("DELETE FROM posizioni where utente=? AND timestamp=?");
			stm.setString(1, position.getUtente().getUsername());
			stm.setString(2, String.valueOf(position.getIdPosizione().getTimestamp()));
			stm.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.err.println("Errore eliminazione posizione: "+ position.getIdPosizione().toString());
			return false;
		}finally {
			try {
				if(stm!=null)
					stm.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}		
	
	/**
	 * Deletes all positions of an existent user from database.
	 *
	 * @param utente the user to whom to delete the positions 
	 * @return true, if successful and false otherwise
	 */
	//Rimozione tutte le posizioni dal database
	public boolean removePosizioniUtente(Utente utente) {
		System.out.println("Eliminazione posizioni dell'utente: "+ utente.getUsername());
		PreparedStatement stm=null;
		try {
			stm=this.connection.prepareStatement("delete from posizioni where utente=?");
			stm.setString(1, utente.getUsername());
			stm.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.err.println("Errore eliminazione posizioni dell'utente: "+ utente.getUsername());
			return false;
		}finally {
			try {
				if(stm!=null)
					stm.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}	
	
	/**
	 * Drop database.
	 *
	 * @return true, if successful and false otherwise
	 */
	//Svuotamento del database
	public boolean dropDatabase() {
		System.out.println("Svuotamento database... ");
		Statement stm=null;
		try {
			stm=this.connection.createStatement();
			stm.executeUpdate("delete from posizioni");
			stm.executeUpdate("delete from utenti");
			return true;
		} catch (SQLException e) {
			System.err.println("Errore svuotamento database");
			return false;
		}finally {
			try {
				if(stm!=null)
					stm.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}	

	/** The Constant url to connect to the database. */
	private String url;
	
	/** The reference to database connection. */
	private Connection connection;
	
	/** The singleton istance of GestoreDatiPersistenti. */
	private static GestoreDatiPersistenti instance;
	
	
	
	
	/**
	 * The Class Settings defines database credentials.
	 */
	private class SettingsDB{
		
		/** The username of the user of the database. */
		public String userDB;
	    
		/** The password of the user of the database. */
    	public String passwordDB;	    
	}
}
