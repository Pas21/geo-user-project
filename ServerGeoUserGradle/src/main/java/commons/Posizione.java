package commons;


import java.io.Serializable;

public class Posizione implements Serializable{
	
	private static final long serialVersionUID = 7753776522879468559L;
	private String accuracy,latitude,longitude,time;
	
	public Posizione(String accuracy, String time,String latitude,String longitude){
		this.accuracy=accuracy;
		this.time=time;
		this.latitude=latitude;
		this.longitude=longitude;
	}
	
	
	public String getAccuracy(){
		return accuracy;
	}
	
	public String getTime(){
		return time;
	}
	
	
	public String getLatitude(){
		return latitude;
	}
	
	public String getLongitude(){
		return longitude;
	}
	
	public void setAccuracy(String newAccuracy){
		this.accuracy=newAccuracy;
	}
	
	public void setTime(String newTime){
		time=newTime;
	}
	
	
	public void setLatitude(String newLatitude){
		latitude=newLatitude;
	}
	
	public void setLongitude(String newLongitude){
		longitude=newLongitude;
	}
	
	public String toString(){
		return "Accuracy: "+this.accuracy+" Time: "+this.time+" Latitude: "+this.latitude+" Longitude: "+this.longitude;
	}
}
