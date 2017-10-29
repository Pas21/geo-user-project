package server.backend;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

import commons.InvalidUsernameException;
import commons.User;
import server.web.frontend.UserRegistryWebApplication;
import commons.Posizione;

public class UserRegistry {
	
	public UserRegistry(){
		dict=new TreeMap<String,User>();
	}


	public int size(){
		return dict.size();
	}
	
	public void add(User u) throws InvalidUsernameException{
		if(dict.containsKey(u.getUsername()))
			throw new InvalidUsernameException("Duplicate username"+u.getUsername());
		dict.put(u.getUsername(), u);
	}
	
	public User get(String username) throws InvalidUsernameException{
		if(!dict.containsKey(username))
			throw new InvalidUsernameException("Invalid username "+username);
		return dict.get(username);
	}
	
	public void put(User u){
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
		dict.get(username).addPosition(pos);
		
	}
	
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
	
	
	public ArrayList<Posizione> getPositionarrayByTime(String username,String d1,String d2) throws InvalidUsernameException, ParseException{
		if(!dict.containsKey(username))
			throw new InvalidUsernameException("Invalid User: "+username);
		ArrayList<Posizione> posizioniMatch=new ArrayList<Posizione>();
		ArrayList<Posizione> posizioniUser=dict.get(username).getArray();
		Date date1,date2,dateUser;
		SimpleDateFormat format=new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");		//cambiare formato data in quello italiano "dd/MM/yyyy HH:mm:ss"
		date1=format.parse(d1);
		date2=format.parse(d2);
		System.out.println(date1.toString());
		for(Posizione p: posizioniUser){
			dateUser=format.parse(p.getTime().replaceAll("Time: ", ""));
			if(dateUser.after(date1) && dateUser.before(date2))
				posizioniMatch.add(p);
		}
		return posizioniMatch;
	}
	
	
	
	
	private TreeMap<String,User> dict;
}
