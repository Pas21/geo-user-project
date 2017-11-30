package com.vfggmail.progettoswe17.clientgeouser.commons;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.annotations.Expose;

public class Utente implements Serializable,Comparable<Object>{
    
	@Expose private static final long serialVersionUID = 1L;
	@Expose private String username;
	@Expose private String password;
	@Expose private String email;
	@Expose private String nome;
	@Expose private String cognome;
    private Set<Posizione> posizioni = new HashSet<Posizione>(0);
    

	public Utente(){}

    public Utente(String username,String password,String email,String nome,String cognome){
        this.username=username;
        this.password=password;
        this.email=email;
        this.nome=nome;
        this.cognome=cognome;
    }

    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cognome == null) ? 0 : cognome.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Utente other = (Utente) obj;
		if (cognome == null) {
			if (other.cognome != null)
				return false;
		} else if (!cognome.equals(other.cognome))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Utente [username=" + username + ", password=" + password + ", email=" + email + ", nome=" + nome
				+ ", cognome=" + cognome + "]";
	}

	@Override
	public int compareTo(Object o) {
		Utente u = (Utente) o;
		return username.compareTo(u.getUsername()); 
	}

	public Set<Posizione> getPosizioni() {
		return posizioni;
	}

	public void setPosizioni(Set<Posizione> posizioni) {
		this.posizioni = posizioni;
	}
	
	public void addPosizione(Posizione newPosizione) {
		this.posizioni.add(newPosizione);
	}
	
	public boolean removePosizione(Posizione posizione) {
		if(!this.posizioni.contains(posizione)) return false;
		else {
			this.posizioni.remove(posizione);
			return true;
		}
	}
	
	

}
