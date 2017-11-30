package com.vfggmail.progettoswe17.clientgeouser.commons;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.sql.Timestamp;

//Classe che racchiude le informazioni relative ad una posizione rilevata

public class IdPosizione implements Serializable,Comparable<Object>{

	@Expose private static final long serialVersionUID = 7753776522879468559L;
	@Expose private double latitudine,longitudine;
	@Expose private Timestamp timestamp;

	public IdPosizione(Timestamp timestamp, double latitudine, double longitudine){
		this.timestamp=(Timestamp) timestamp.clone();
		this.latitudine=latitudine;
		this.longitudine=longitudine;
	}

	public IdPosizione(){}

	public double getLatitudine() {
		return latitudine;
	}

	public void setLatitudine(double latitudine) {
		this.latitudine = latitudine;
	}

	public double getLongitudine() {
		return longitudine;
	}

	public void setLongitudine(double longitudine) {
		this.longitudine = longitudine;
	}

	public Timestamp getTimestamp() {
		return timestamp == null ? null : (Timestamp) timestamp.clone();
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = (Timestamp) timestamp.clone();
	}

	@Override
	public String toString() {
		return "IdPosizione [latitudine=" + latitudine + ", longitudine=" + longitudine + ", timestamp=" + timestamp
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(latitudine);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(longitudine);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
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
		IdPosizione other = (IdPosizione) obj;
		if (Double.doubleToLongBits(latitudine) != Double.doubleToLongBits(other.latitudine))
			return false;
		if (Double.doubleToLongBits(longitudine) != Double.doubleToLongBits(other.longitudine))
			return false;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		return true;
	}

	@Override
	public int compareTo(Object obj) {
		IdPosizione idPos = (IdPosizione) obj;
		return this.timestamp.compareTo(idPos.getTimestamp());
	}




}

