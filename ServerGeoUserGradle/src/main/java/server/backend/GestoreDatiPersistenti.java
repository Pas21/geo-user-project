package server.backend;

import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import commons.IdPosizione;
import commons.Posizione;
import commons.Utente;

public class GestoreDatiPersistenti {
	//Pattern Singleton
	protected GestoreDatiPersistenti(){
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
	
	
	//Metodo per ottenere dal database tutti gli utenti
	public TreeMap<String,Utente> getUtenti(){
	      Session session = this.factory.openSession();
	      Transaction tx = null;
	      TreeMap<String,Utente> utenti = new TreeMap<String,Utente>();
	      
	      try {
	         tx = session.beginTransaction();
	         List<?> listaUtenti = session.createQuery("FROM Utente").list(); 		// Query su classe java e non su tabella
	         for (Iterator<?> iterator = listaUtenti.iterator(); iterator.hasNext();){
	            Utente utente = (Utente) iterator.next(); 
	            utenti.put(utente.getUsername(), utente);
	            //System.out.println(utente.toString());
	         }
	         tx.commit();
	      } catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      } 
	      catch (Exception e) {
		     e.printStackTrace(); 
		  }finally {
	         session.close();
	      }
	      return utenti;
	   }

	//Metodo per ottenere dal database tutte le posizioni degli utenti
	public TreeMap<IdPosizione,Posizione> getPosizioni(){
		Session session = this.factory.openSession();
		Transaction tx = null;
		TreeMap<IdPosizione,Posizione> posizioni = new TreeMap<IdPosizione,Posizione>();

		try {
			tx = session.beginTransaction();
			List<?> listaPosizioni = session.createQuery("FROM Posizione").list();  // Query su classe java e non su tabella
			for (Iterator<?> iterator = listaPosizioni.iterator(); iterator.hasNext();){
				Posizione posizione = (Posizione) iterator.next(); 
				IdPosizione idPosizione = new IdPosizione(posizione.getIdPosizione().getTimestamp(), posizione.getIdPosizione().getLatitudine(), posizione.getIdPosizione().getLongitudine());
				posizioni.put(idPosizione,posizione);
	            //System.out.println(posizione.toString());
			}
			tx.commit();
		} catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
		} 
		catch (Exception e) {
			e.printStackTrace(); 
		}finally {
			session.close();
		}
		return posizioni;
	}
	
	
	// Metodo per aggiungere un utente al database
	public boolean addUtente(Utente utente){	//Fare controllo in backend se l'utente esiste già non deve invocare questo metodo(vale lo stesso per tutti i successivi metodi)
		Session session = this.factory.openSession();
	    Transaction tx = null;
	    boolean addok = true;
	    try {
	       tx = session.beginTransaction();
	       session.save(utente); 
	       tx.commit();
	    } catch (HibernateException e) {
	       if (tx!=null) tx.rollback();
	       e.printStackTrace(); 
	       addok=false;
	    } finally {
	       session.close(); 
	    }
	    return addok;
	}
	
	/* Metodo per cancellare un utente dal database */
	public boolean removeUtente(Utente utente){
		Session session = this.factory.openSession();
	    Transaction tx = null;
	    boolean removeok=true;
	    
	    try {
	       tx = session.beginTransaction();
	       session.delete(utente); 
	       tx.commit();
	    } catch (HibernateException e) {
	       if (tx!=null) tx.rollback();
	       e.printStackTrace(); 
	       removeok=false;
	    } finally {
	       session.close();	       
	    }
	    return removeok;
	}
	
	// Metodo per aggiungere una posizione al database 
	public boolean addPosizione(Posizione posizione){
		Session session = this.factory.openSession();
	    Transaction tx = null;
	    boolean addok = true;
	    
	    try {
	       tx = session.beginTransaction();
	       session.save(posizione); 
	       tx.commit();
	    } catch (HibernateException e) {
	       if (tx!=null) tx.rollback();
	       e.printStackTrace(); 
	       addok=false;
	    } finally {
	       session.close(); 
	    }
	    return addok;
	}
	
	// Metodo per cancellare una posizione dal database 
	public boolean removePosizione(Posizione posizione){
		Session session = this.factory.openSession();
	    Transaction tx = null;
	    boolean removeok=true;
	    
	    try {
	       tx = session.beginTransaction();
	       session.delete(posizione); 
	       tx.commit();
	    } catch (HibernateException e) {
	       if (tx!=null) tx.rollback();
	       e.printStackTrace(); 
	       removeok=false;
	    } finally {
	       session.close(); 
	    }
	    return removeok;
	}
	
	//Metodo per rimuovere tutte le posizioni di un utente
	public boolean removePosizioniUtente(Utente utente) {
		TreeMap<IdPosizione, Posizione> posizioni = this.getPosizioni();
		if(posizioni == null) return false;
		
		Session session = this.factory.openSession();
		Transaction tx = null;
		boolean removeok =  true;
		Posizione posizione = null;
		
		try {
			//Si può fare anche con createNativeQuery!
			for(IdPosizione id : posizioni.keySet()) {
				posizione = posizioni.get(id);
				//Nell'if si deve usare il metodo equals della classe utente
				if(posizione.getUtente().getUsername().equals(utente.getUsername())) {
					tx = session.beginTransaction();
					session.delete(posizione); 
				    tx.commit();			
				}
			}
			
		} catch (HibernateException e) {
			if (tx!=null) tx.rollback();
		    e.printStackTrace(); 
		    removeok=false;
		} finally {
			session.close(); 
		}
		return removeok;
	}

	
	 
	private SessionFactory factory; 	
	private static GestoreDatiPersistenti instance;
}
