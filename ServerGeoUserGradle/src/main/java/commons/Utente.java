package commons;
import java.io.Serializable;

/**
 * Created by Antonio on 15/02/2017.
 */

public class Utente implements Serializable{
    
	private static final long serialVersionUID = 1L;
	private String username;
    private String password;
    private String email;
    private String name;
    private String surname;
    

    public Utente(String username,String password,String email,String name,String surname){
        this.username=username;
        this.password=password;
        this.email=email;
        this.name=name;
        this.surname=surname;
    }

    public Utente(){}
    
    
    
    
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
