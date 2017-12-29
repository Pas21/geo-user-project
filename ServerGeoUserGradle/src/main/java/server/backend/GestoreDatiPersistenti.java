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

public class GestoreDatiPersistenti {
	//Pattern Singleton
	private GestoreDatiPersistenti(){
		try {
			this.factory = new Configuration().configure().buildSessionFactory();
		} catch (Throwable ex) { 
			System.err.println("Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex); 
		}
	}

	//Costruttore pubblico
	public static synchronized GestoreDatiPersistenti getInstance(){
		if(instance==null)
			instance=new GestoreDatiPersistenti();
		return instance;
	}

	//Per JUnit
	public synchronized SessionFactory getFactory(){
		return factory;
	}


	//Metodo per ottenere dal database tutti gli utenti
	public TreeMap<String,Utente> getUtenti(){
		Session session = this.factory.openSession();
		Transaction tx = null;
		TreeMap<String,Utente> utenti = new TreeMap<String,Utente>();

		try{
			tx = session.beginTransaction(); 
			// Query su classe java e non su tabella
			//List<?> listaUtenti = session.createQuery("FROM Utente").list();
			//for (Iterator<?> iterator = listaUtenti.iterator(); iterator.hasNext();){
			//	Utente utente = (Utente) iterator.next(); 
			//	utenti.put(utente.getUsername(), utente);
			//	System.out.println(utente.toString());
			//}
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

	//Metodo per ottenere dal database tutte le posizioni degli utenti
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


	// Metodo per aggiungere un utente al database
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

	// Metodo per cancellare un utente dal database 
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

	// Metodo per aggiungere una posizione al database 
	public boolean addPosizione(Posizione posizione){
		Session session = this.factory.openSession();
		Transaction tx = null;
		boolean addok = true;

		try{
			tx = session.beginTransaction();
			session.save(posizione); 
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

	// Metodo per cancellare una posizione dal database 
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

	//Metodo per rimuovere tutte le posizioni di un utente
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



	public SessionFactory factory; 	
	private static GestoreDatiPersistenti instance;
}
