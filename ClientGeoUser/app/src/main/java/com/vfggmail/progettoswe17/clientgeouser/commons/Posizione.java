package com.vfggmail.progettoswe17.clientgeouser.commons;

import java.io.Serializable;

public class Posizione implements Serializable{

	private static final long serialVersionUID = 7753776522879468559L;
	private IdPosizione idPosizione;
	private float accuratezza;
	private Utente utente;


	public Posizione(IdPosizione idPosizione, Utente utente, float accuratezza){
		this.idPosizione=idPosizione;
		this.accuratezza=accuratezza;
		this.utente=utente;
	}

	public Posizione(){}


	public IdPosizione getIdPosizione() {
		return idPosizione;
	}

	public void setIdPosizione(IdPosizione idPosizione) {
		this.idPosizione = idPosizione;
	}

	public float getAccuratezza() {
		return accuratezza;
	}

	public void setAccuratezza(float accuratezza) {
		this.accuratezza = accuratezza;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(accuratezza);
		result = prime * result + ((idPosizione == null) ? 0 : idPosizione.hashCode());
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

	@Override
	public String toString() {
		return "Posizione ["+this.idPosizione.toString()+", accuratezza=" + accuratezza + "]";
	}

	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}


}
