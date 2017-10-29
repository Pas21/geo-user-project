package commons;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Antonio on 15/02/2017.
 */

public class User implements Serializable{
    
	private static final long serialVersionUID = 1L;
	private String username;
    private String password;
    private String email;
    private String name;
    private String surname;
    private ArrayList<Posizione> posizioni;

    public User(String username,String password,String email,String name,String surname){
        this.username=username;
        this.password=password;
        this.email=email;
        this.name=name;
        this.surname=surname;
        this.posizioni=new ArrayList<Posizione>();
    }

    
    public void addPosition(Posizione p){
    	posizioni.add(p);
    }
    
    public ArrayList<Posizione> getArray(){
    	return posizioni;
    }
    
    public int getPositionNumber(){
    	return posizioni.size();
    }
    
    public void setUsername(String username){
        this.username=username;
    }

    public void setPassword(String password){
        this.password=password;
    }

    public void setEmail(String email){
        this.email=email;
    }

    public void setName(String name){
        this.name=name;
    }

    public void setSurname(String surname){
        this.surname=surname;
    }

    public String getUsername(){
        return this.username;
    }

    public String getPassword(){
        return this.password;
    }

    public String getEmail(){
        return this.email;
    }

    public String getName(){
        return this.name;
    }

    public String getSurname(){
        return this.surname;
    }




}
