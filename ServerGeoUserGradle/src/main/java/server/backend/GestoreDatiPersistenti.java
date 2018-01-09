package server.backend;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import commons.IdPosizione;
import commons.Posizione;
import commons.Utente;

/**
 * The Class GestoreDatiPersistenti is a pure fabrication class that has the purpose of interfacing directly with the database with the hibernate framework.
 */
public class GestoreDatiPersistenti {
	
	/**
	 * Instantiates the singleton instance of GestoreDatiPersistenti.
	 */
	private GestoreDatiPersistenti(){
		try {
			this.factory = new Configuration().configure().buildSessionFactory();
		} catch (Throwable ex) { 
			System.err.println("Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex); 
		}
	}
	
	/**
	 * Gets the singleton instance of GestoreDatiPersistenti.
	 *
	 * @return instance the singleton instance of GestoreDatiPersistenti
	 */
	public static synchronized GestoreDatiPersistenti getInstance(){
		if(instance==null)
			instance=new GestoreDatiPersistenti();
		return instance;
	}
	
	/**
	 * Gets the connection to the database.
	 *
	 * @return connection the connection to the database
	 */
	public synchronized SessionFactory getFactory(){
		return factory;
	}
	/**
	 * Gets all registered users on the database.
	 *
	 * @return utenti all registered users on the database
	 */
	//Caricamento utenti database
	public TreeMap<String,Utente> getUtenti(){
		Session session = this.factory.openSession();
		Transaction tx = null;
		TreeMap<String,Utente> utenti = new TreeMap<String,Utente>();

		try{
			tx = session.beginTransaction(); 
			// Query su classe java e non su tabella
			List<?> listaUtenti = session.createQuery("FROM Utente").list();
			for (Iterator<?> iterator = listaUtenti.iterator(); iterator.hasNext();){
				Utente utente = (Utente) iterator.next(); 
				utenti.put(utente.getUsername(), utente);
				System.out.println(utente.toString());
			}
			session.flush();
			tx.commit();
		}catch (Exception e) {
			if (tx!=null) tx.rollback();
			utenti = null;
			e.printStackTrace();
		}finally{
			session.close();
		}
		return utenti;
	}
	
	/**
	 * Gets all registered positions on the database.
	 *
	 * @return posizioni all registered positions on the database
	 */
	//Caricamento posizioni database
	public TreeMap<IdPosizione,Posizione> getPosizioni(){
		Session session = this.factory.openSession();
		Transaction tx = null;
		TreeMap<IdPosizione,Posizione> posizioni = new TreeMap<IdPosizione,Posizione>();

		try{
			tx = session.beginTransaction();
			List<?> listaPosizioni = session.createQuery("FROM Posizione").list();  // Query su classe java e non su tabella
			for (Iterator<?> iterator = listaPosizioni.iterator(); iterator.hasNext();){
				Posizione posizione = (Posizione) iterator.next(); 
				IdPosizione idPosizione = new IdPosizione(posizione.getIdPosizione().getTimestamp(), posizione.getIdPosizione().getLatitudine(), posizione.getIdPosizione().getLongitudine());
				posizioni.put(idPosizione,posizione);
				//System.out.println(posizione.toString());
			}
			session.flush();
			tx.commit();
		}catch (Exception e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
		}finally{
			session.close();
		}
		return posizioni;
	}
	
	
	/**
	 * Inserts a new user to the database.
	 *
	 * @param utente the user to add
	 * @return true, if successful and false otherwise
	 */
	//Aggiunta utente al database
	public boolean addUtente(Utente utente){
		Session session = this.factory.openSession();
		Transaction tx = null;
		boolean addok = true;
		try{
			tx = session.beginTransaction();
			session.save(utente); 
			session.flush();
			tx.commit();
		}catch (Exception e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
			addok=false;
		}finally{
			session.close(); 
		}
		return addok;
	}
	
	/**
	 * Deletes user from database.
	 *
	 * @param utente the user to delete
	 * @return true, if successful and false otherwise
	 */
	//Rimozione utente dal database
	public boolean removeUtente(Utente utente){
		Session session = this.factory.openSession();
		Transaction tx = null;
		boolean removeok=true;

		try{
			tx = session.beginTransaction();
			session.delete(utente); 
			session.flush();
			tx.commit();
		}catch (Exception e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
			removeok=false;
		}finally{
			session.close();	       
		}
		return removeok;
	}
		
	/**
	 * Inserts a new position to the database.
	 *
	 * @param newPosition the new position to insert
	 * @return true, if successful and false otherwise
	 */
	//Aggiunta posizione al database
	public boolean addPosizione(Posizione newPosition){
		Session session = this.factory.openSession();
		Transaction tx = null;
		boolean addok = true;

		try{
			tx = session.beginTransaction();
			session.save(newPosition); 
			session.flush();
			tx.commit();
		}catch (Exception e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
			addok=false;
		}finally{
			session.close(); 
		}
		return addok;
	}
	
	/**
	 * Deletes an existent position from database.
	 *
	 * @param position the position to delete
	 * @return true, if successful and false otherwise
	 */
	//Rimozione posizione dal database
	public boolean removePosizione(Posizione posizione){
		Session session = this.factory.openSession();
		Transaction tx = null;
		boolean removeok=true;

		try{
			tx = session.beginTransaction();
			session.delete(posizione); 
			session.flush();
			tx.commit();
		}catch (Exception e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
			removeok=false;
		}finally{
			session.close(); 
		}
		return removeok;
	}
	
	/**
	 * Deletes all positions of an existent user from database.
	 *
	 * @param utente the user to whom to delete the positions 
	 * @return true, if successful and false otherwise
	 */
	//Rimozione tutte le posizioni dal database
	public boolean removePosizioniUtente(Utente utente) {
		TreeMap<IdPosizione, Posizione> posizioni = this.getPosizioni();		
		Session session = this.factory.openSession();
		Transaction tx = null;
		boolean removeok =  true;
		Posizione posizione = null;

		try{
			//Si puo' fare anche con createNativeQuery!
			for(Entry<IdPosizione, Posizione> id : posizioni.entrySet()) {
				posizione = id.getValue();
				//Nell'if si deve usare il metodo equals della classe utente
				if(posizione.getUtente().getUsername().equals(utente.getUsername())) {
					tx = session.beginTransaction();
					session.delete(posizione); 
					session.flush();
					tx.commit();
				}
			}
		}catch (Exception e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
			removeok=false;
		}finally{
			session.close(); 
		}
		return removeok;
	}

	/**
	 * Drop database.
	 *
	 * @return true, if successful and false otherwise
	 */
	//Svuotamento del database
	public boolean dropDatabase() {
		System.out.println("Svuotamento database... ");
		Session session = this.factory.openSession();
		Transaction tx = null;
		boolean dropok =  true;
		try{
		tx = session.beginTransaction();
		session.createNativeQuery("delete from posizioni").executeUpdate();
		session.createNativeQuery("delete from utenti").executeUpdate();
		tx.commit();
		}catch (Exception e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
			dropok=false;
		}finally{
			session.close(); 
		}
		return dropok;
	}


	/** The instance of Session Factory to connect to database with hibernate. */
	public SessionFactory factory; 	
	
	/** The singleton instance of GestoreDatiPersistenti. */
	private static GestoreDatiPersistenti instance;
}
