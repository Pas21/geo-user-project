package commons;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.annotations.Expose;

/**
 * The Class Utente represents the abstraction of a user.
 *
 */
public class Utente implements Serializable,Comparable<Object>{
    
	/** The Constant serialVersionUID. */
	@Expose private static final long serialVersionUID = 1L;
	
	/** The username of the user. */
	@Expose private String username;
	
	/** The password of the user. */
	@Expose private String password;
	
	/** The email of the user. */
	@Expose private String email;
	
	/** The name of the user. */
	@Expose private String nome;
	
	/** The surname of the user. */
	@Expose private String cognome;
    
    /** Set of positions of the user. */
    private Set<Posizione> posizioni = new HashSet<Posizione>(0);
    
    /**
     * Instantiates a user with empty fields.
     */
	public Utente(){}

	
	/**
	 * Instantiates a user with a specified username, password, email, nome, cognome.
	 *
	 * @param username username of the user
	 * @param password password of the user
	 * @param email email of the user
	 * @param nome name of the user
	 * @param cognome surname of the user
	 */
    public Utente(String username,String password,String email,String nome,String cognome){
        this.username=username;
        this.password=password;
        this.email=email;
        this.nome=nome;
        this.cognome=cognome;
    }

    
    /**
     * Gets the username of the user.
     *
     * @return username of the user
     */
    public String getUsername() {
		return username;
	}

	/**
	 * Sets the value of the username field to the specified String.
	 * 
	 * @param username username of the user
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Gets the password of the user.
	 *
	 * @return password password of the user
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the value of the password field to the specified String.
	 * 
	 * @param password password of the user
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * Gets the email of the user.
	 *
	 * @return email email of the user
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the value of the email field to the specified String.
	 * 
	 * @param email email of the user
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets the name of the user.
	 *
	 * @return nome name of the user
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * Sets the value of the nome field to the specified String.
	 * 
	 * @param nome name of the user
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * Gets the surname of the user.
	 *
	 * @return cognome surname of the user
	 */
	public String getCognome() {
		return cognome;
	}

	/**
	 * Sets the value of the cognome field to the specified String.
	 * 
	 * @param cognome surname of the user
	 */
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
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

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Utente [username=" + username + ", password=" + password + ", email=" + email + ", nome=" + nome
				+ ", cognome=" + cognome + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Object o) {
		Utente u = (Utente) o;
		return username.compareTo(u.getUsername()); 
	}

	
	/**
	 * Gets set of positions of the user.
	 *
	 * @return posizioni Set of positions of the user
	 */
	public Set<Posizione> getPosizioni() {
		return posizioni;
	}

	/**
	 * Sets the value of the posizioni field to the specified Set of Posizione objects.
	 * 
	 * @param posizioni Set of positions of the user
	 */
	public void setPosizioni(Set<Posizione> posizioni) {
		this.posizioni = posizioni;
	}
	
	/**
	 * This adds a position to the set of positions of the user.
	 *
	 * @param newPosizione a new position of the user
	 */
	public void addPosizione(Posizione newPosizione) {
		this.posizioni.add(newPosizione);
	}
	
	/**
	 * This removes the specified user position from the set of positions of the user.
	 *
	 * @param posizione a user position
	 * @return boolean return true if this adds the specified position and return false if the specified position already exists
	 */
	public boolean removePosizione(Posizione posizione) {
		if(!this.posizioni.contains(posizione)) return false;
		else {
			this.posizioni.remove(posizione);
			return true;
		}
	}
	
	

}
