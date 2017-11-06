package server.backend;


import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.TreeMap;

import commons.InvalidUsernameException;
import commons.Utente;
import server.web.frontend.UserRegistryWebApplication;
import commons.Posizione;

public class UserRegistry {
	
	//Eventualmente dovrà essere eliminata la variabile dict poichè le informazioni dovrenno essere caricate attraverso hibernate
	
	public UserRegistry(){
		dict=new TreeMap<String,Utente>();
	}


	public int size(){
		return dict.size();
	}
	
	public void add(Utente u) throws InvalidUsernameException{
		if(dict.containsKey(u.getUsername()))
			throw new InvalidUsernameException("Duplicate username"+u.getUsername());
		dict.put(u.getUsername(), u);
	}
	
	public Utente get(String username) throws InvalidUsernameException{
		if(!dict.containsKey(username))
			throw new InvalidUsernameException("Invalid username "+username);
		return dict.get(username);
	}
	
	public void put(Utente u){
		dict.put(u.getUsername(), u);
	}
	
	public void remove(String username) throws InvalidUsernameException{
		if(!dict.containsKey(username))
			throw new InvalidUsernameException("Invalid Username");
		dict.remove(username);
	}
	
	public String[] getUsers(){
		return dict.keySet().toArray(new String[dict.keySet().size()]);
	}
	
	
	public boolean verifyUser(String username,String password) throws InvalidUsernameException{
		if(dict.containsKey(username))
			if(dict.get(username).getPassword().equals(password))
				return true;
			else 
				return false;
		else throw new InvalidUsernameException("Invalid Username");
	}
	
	public void logoutUser(String username){
		if(dict.containsKey(username))
			UserRegistryWebApplication.verifier.getLocalSecrets().remove(username);	//??
	}
	
	public void addUserPosition(String username,Posizione pos) throws InvalidUsernameException{
		if(!dict.containsKey(username))
			throw new InvalidUsernameException("Invalid Username");
		//In questo punto deve essere usato hibernate per inserire una posizione nel database
	}
	
/*	Classi non necessarie in quanto attraverso hibernate gli utenti  ele posizioni vengono prelevate dal database
	public void save(String fileOutName) throws IOException{
		FileOutputStream fileOut = new FileOutputStream(fileOutName);
	    ObjectOutputStream out = new ObjectOutputStream(fileOut);
	    out.writeObject(dict);
	    out.close();
	    fileOut.close();
	}
	
	public void load(String fileName) throws IOException, ClassNotFoundException{
	    FileInputStream fileIn = new FileInputStream(fileName);
	    ObjectInputStream in = new ObjectInputStream(fileIn);
	    dict = (TreeMap<String,User>) in.readObject();
	    in.close();
	    fileIn.close();
	}
*/
	
	public ArrayList<Posizione> getPositionarrayByTime(String username,Timestamp d1,Timestamp d2) throws InvalidUsernameException, ParseException{
		if(!dict.containsKey(username))
			throw new InvalidUsernameException("Invalid User: "+username);
		ArrayList<Posizione> posizioniMatch=new ArrayList<Posizione>();
		//In questo punto deve essere inserita la logica di selezione delle posizioni attraverso hibernate e query sul db
		
		return posizioniMatch;
	}
	
	
	
	
	private TreeMap<String,Utente> dict;
}
