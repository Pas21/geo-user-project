package commons;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

/**
 * The Class Posizione represents the abstraction of an user position.
 */
public class Posizione implements Serializable{

	/** The Constant serialVersionUID. */
	@Expose private static final long serialVersionUID = 7753776522879468559L;
	
	/** The position id. */
	@Expose private IdPosizione idPosizione;
	
	/** The position accuracy. */
	@Expose private float accuratezza;
	
	/** The user to whom the position belongs. */
	private Utente utente;

	
	
	/**
	 * Instantiates a position with empty fields.
	 */
	public Posizione(){}
	
	/**
	 * Instantiates a position with a specified position id, user, accuracy.
	 *
	 * @param idPosizione the position id
	 * @param utente the user to whom the position belongs.
	 * @param accuratezza position accuracy
	 */
	public Posizione(IdPosizione idPosizione, Utente utente, float accuratezza){
		this.idPosizione=idPosizione;
		this.accuratezza=accuratezza;
		this.utente=utente;
	}
	
	/**
	 * Gets the position id.
	 *
	 * @return idPosizione the position id
	 */
	public IdPosizione getIdPosizione() {
		return idPosizione;
	}

	/**
	 * Sets the value of the idPosizione field to the specified IdPosizione.
	 * 
	 * @param idPosizione the new position id
	 */
	public void setIdPosizione(IdPosizione idPosizione) {
		this.idPosizione = idPosizione;
	}

	/**
	 * Gets the position accuracy.
	 *
	 * @return accuratezza the position accuracy
	 */
	public float getAccuratezza() {
		return accuratezza;
	}

	/**
	 * Sets the value of the accuratezza field to the specified float.
	 *
	 * @param accuratezza the new accuracy
	 */
	public void setAccuratezza(float accuratezza) {
		this.accuratezza = accuratezza;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(accuratezza);
		result = prime * result + ((idPosizione == null) ? 0 : idPosizione.hashCode());
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
		Posizione other = (Posizione) obj;
		if (Float.floatToIntBits(accuratezza) != Float.floatToIntBits(other.accuratezza))
			return false;
		if (idPosizione == null) {
			if (other.idPosizione != null)
				return false;
		} else if (!idPosizione.equals(other.idPosizione))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Posizione ["+this.idPosizione.toString()+", accuratezza=" + accuratezza + "]";
	}

	/**
	 * Gets the user to whom the position belongs.
	 *
	 * @return utente the user to whom the position belongs.
	 */
	public Utente getUtente() {
		return utente;
	}

	/**
	 * Sets the value of the utente field to the specified Utente.
	 *
	 * @param utente the new user owner
	 */
	public void setUtente(Utente utente) {
		this.utente = utente;
	}
	
	
}
