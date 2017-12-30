package server.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
		
	//Aggiunta posizione al database
	public boolean addPosizione(Posizione pos) {
		System.out.println("Inserimento posizione: " + pos.toString());
		PreparedStatement stm=null;
		try {
			//stm.executeUpdate("insert into posizioni (utente, latitudine, longitudine, timestamp, accuratezza) values('" + pos.getUtente().getUsername() + "','" + pos.getIdPosizione().getLatitudine() + "','" + pos.getIdPosizione().getLongitudine() + "','" + pos.getIdPosizione().getTimestamp() + "','" + pos.getAccuratezza() + "')");

			stm = this.connection.prepareStatement("INSERT INTO posizioni (utente, latitudine, longitudine, timestamp, accuratezza) values(?,?,?,?,?)");
			stm.setString(1, pos.getUtente().getUsername()); 									/* utente */
			stm.setString(2, String.valueOf(pos.getIdPosizione().getLatitudine())); 			/* latitudine */
			stm.setString(3, String.valueOf(pos.getIdPosizione().getLongitudine()));  			/* longitudine */
			stm.setString(4, String.valueOf(pos.getIdPosizione().getTimestamp())); 				/* timestamp */
			stm.setString(5, String.valueOf(pos.getAccuratezza()));    							/* accuratezza */
			stm.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.err.println("Errore inserimento posizione: " + pos.toString());
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
	
	//Rimozione posizione dal database
	public boolean removePosizione(Posizione pos) {
		System.out.println("Eliminazione posizione: "+ pos.getIdPosizione().toString());
		PreparedStatement stm=null;
		try {
			stm = this.connection.prepareStatement("DELETE FROM posizioni where utente=? AND timestamp=?");
			stm.setString(1, pos.getUtente().getUsername());
			stm.setString(2, String.valueOf(pos.getIdPosizione().getTimestamp()));
			stm.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.err.println("Errore eliminazione posizione: "+ pos.getIdPosizione().toString());
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

	private final static String url = "jdbc:mysql://localhost:3306/geouserdb?user=root&password=";
	private Connection connection;
	private static GestoreDatiPersistenti istanza;
}
