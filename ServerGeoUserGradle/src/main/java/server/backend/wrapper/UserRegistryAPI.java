package server.backend.wrapper;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import commons.InvalidUsernameException;
import commons.Posizione;
import commons.User;
import server.backend.UserRegistry;

public class UserRegistryAPI {
	
	protected UserRegistryAPI(){
		ur=new UserRegistry();
	}
	
	public static synchronized UserRegistryAPI instance(){
		if(instance==null)
			instance=new UserRegistryAPI();
		return instance;
	}
	
	public synchronized int size(){
		return ur.size();
	}
	
	public synchronized User get(String username) throws InvalidUsernameException{
		return ur.get(username);
	}
	
	public synchronized void add(User u) throws InvalidUsernameException{
		ur.add(u);
		commit();
	}
	
	public synchronized void update(User u){
		ur.put(u);
		commit();
	}
	
	public synchronized void remove(String username) throws InvalidUsernameException{
		ur.remove(username);
		commit();
	}
	
	public synchronized boolean verifyUser(String username,String password) throws InvalidUsernameException{
		return ur.verifyUser(username, password);
	}
	
	public synchronized void removeUser(String username){
		ur.logoutUser(username);
	}
	
	public synchronized void addUserPosition(String username,Posizione pos) throws InvalidUsernameException{
		ur.addUserPosition(username, pos);
		commit();
	}
	
	public synchronized ArrayList<Posizione> getPositionUser(String username,String d1,String d2) throws InvalidUsernameException, ParseException{
		return ur.getPositionarrayByTime(username, d1, d2);
	}
	
	public synchronized String[] getAllUsers(){
		return ur.getUsers();
	}
	
	public void setStorageFiles(String rootDirForStorageFile, String baseStorageFile){
		this.rootDirForStorageFile=rootDirForStorageFile;
		this.baseStorageFile=baseStorageFile;
		System.err.println("Storage Directory: " + this.rootDirForStorageFile);
		System.err.println("Storage Base File: " + this.baseStorageFile);	
	}
	
	protected int buildStorageFileExtension() throws NullPointerException {
		System.err.println(this.rootDirForStorageFile);
		final File folder=new File(this.rootDirForStorageFile);
		int c;
		int max=-1;
		
		File[] listFilesStorage = folder.listFiles();
		if(listFilesStorage==null)
			return -1;
		
		for (File fileEntry : listFilesStorage) {
			if (fileEntry.getName().substring(0, baseStorageFile.length()).equalsIgnoreCase(baseStorageFile)){
	            try {
	                c = Integer.parseInt(fileEntry.getName().substring(baseStorageFile.length()+1));
	            } catch (NumberFormatException | StringIndexOutOfBoundsException e){
	            	c=-1;
	            }
	            if (c>max) max=c;
	        }	            	
	    }
		return max;
	}
	
	public void commit(){
		int extension=buildStorageFileExtension();
		String fileName = rootDirForStorageFile+baseStorageFile+"."+(extension+1);
		System.err.println("Commit storage to: "+fileName);
		try {
			ur.save(fileName);
		} catch (IOException e) {
			System.err.println("Commit failed");
		}		
	}
	
	
	public void restore(){
		int extension=buildStorageFileExtension();
		if (extension==-1){
			System.err.println("No data to load - starting a new registry");
		} else {
			String fileName = rootDirForStorageFile+baseStorageFile+"."+extension;
			System.err.println("Restore storage from: " + fileName);			
			try {
				ur.load(fileName);
			} catch (ClassNotFoundException | IOException e) {
				System.err.println("Restore failed - starting a new registry");
				ur=new UserRegistry();
			}
		}
	}
	
	
	private static UserRegistryAPI instance;
	private UserRegistry ur;
	private String rootDirForStorageFile;
	private String baseStorageFile;

}
