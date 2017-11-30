package com.vfggmail.progettoswe17.clientgeouser.commons;
import java.io.Serializable;
import java.util.ArrayList;

//Classe che identifica un utente dell'applicazione

public class Utente implements Serializable {
    
	private static final long serialVersionUID = 1L;
	private String username;
    private String password;
    private String email;
    private String nome;
    private String cognome;
    private ArrayList<Posizione> posizioni;

    public Utente(String username, String password, String email, String name, String surname){
        this.username=username;
        this.password=password;
        this.email=email;
        this.nome=name;
        this.cognome=surname;
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
        this.nome=name;
    }

    public void setSurname(String surname){
        this.cognome=surname;
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
        return this.nome;
    }

    public String getSurname(){
        return this.cognome;
    }




}
