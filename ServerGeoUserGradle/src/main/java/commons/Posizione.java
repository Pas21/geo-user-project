package commons;


import java.io.Serializable;
import java.sql.Timestamp;

public class Posizione implements Serializable{
	
	private static final long serialVersionUID = 7753776522879468559L;
	private float accuracy;
	private double latitude,longitude;
	private Utente utente;
	private Timestamp time;
	
	public Posizione(float accuracy, Timestamp time,double latitude,double longitude){
		this.accuracy=accuracy;
		this.time=time;
		this.latitude=latitude;
		this.longitude=longitude;
		this.utente=null;
	}
	
	public Posizione(){}
	
	
	
	
	public float getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(float accuracy) {
		this.accuracy = accuracy;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

}
