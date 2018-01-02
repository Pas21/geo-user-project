package commons;

import java.io.Serializable;
import java.sql.Timestamp;

import com.google.gson.annotations.Expose;

/**
 * The Class IdPosizione represents the position id of the user position.
 */
public class IdPosizione implements Serializable,Comparable<Object>{
	
	/** The Constant serialVersionUID. */
	@Expose private static final long serialVersionUID = 7753776522879468559L;
	
	/** The latitude of the position. */
	@Expose private double latitudine;
	
	/** The longitude of the position. */
	@Expose private double longitudine;
	
	/** The timestamp of the position. */
	@Expose private Timestamp timestamp;
	
	/**
	 * Instantiates a position with a specified timestamp, latitudine, longitudine.
	 *
	 * @param timestamp the timestamp of the position
	 * @param latitudine the latitudine of the position
	 * @param longitudine the longitudine of the position
	 */
	public IdPosizione(Timestamp timestamp, double latitudine, double longitudine){
		this.timestamp=(Timestamp) timestamp.clone();
		this.latitudine=latitudine;
		this.longitudine=longitudine;
	}
	
	/**
	 * Instantiates a position id with empty fields.
	 */
	public IdPosizione(){}
	
	/**
	 * Gets the latitude of the position.
	 *
	 * @return latitudine the latitude of the position
	 */
	public double getLatitudine() {
		return latitudine;
	}

	/**
	 * Sets the value of the latitudine field to the specified double.
	 *
	 * @param latitudine the new latitude
	 */
	public void setLatitudine(double latitudine) {
		this.latitudine = latitudine;
	}

	/**
	 * Gets the longitude of the position.
	 *
	 * @return longitudine the longitude of the position
	 */
	public double getLongitudine() {
		return longitudine;
	}

	/**
	 * Sets the value of the longitudine field to the specified double.
	 *
	 * @param longitudine the new longitudine
	 */
	public void setLongitudine(double longitudine) {
		this.longitudine = longitudine;
	}

	/**
	 * Gets the timestamp of the position.
	 *
	 * @return timestamp the timestamp of the position
	 */
	public Timestamp getTimestamp() {
		return timestamp == null ? null : (Timestamp) timestamp.clone();
	}

	/**
	 * Sets the value of the timestamp field to the specified Timestamp.
	 *
	 * @param timestamp the new timestamp
	 */
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = (Timestamp) timestamp.clone();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "IdPosizione [latitudine=" + latitudine + ", longitudine=" + longitudine + ", timestamp=" + timestamp
				+ "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
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

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Object obj) {
		IdPosizione idPos = (IdPosizione) obj;
		return this.timestamp.compareTo(idPos.getTimestamp());
	}	
	
	
	

}

