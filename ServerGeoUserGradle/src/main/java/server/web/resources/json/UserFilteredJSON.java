package server.web.resources.json;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;

import commons.ErrorCodes;
import commons.InvalidDataException;
import commons.InvalidUsernameException;
import commons.Posizione;
import server.backend.wrapper.UserRegistryAPI;

public class UserFilteredJSON extends ServerResource{
	@Get
	public String getFilteredPosition() {
		String data=getAttribute("data");
		data=data.replaceAll("%69", "/");
		data=data.replaceAll("%20", " ");
		Gson gson=new Gson();
		UserRegistryAPI urapi=UserRegistryAPI.instance();
		StringTokenizer st=new StringTokenizer(data,"&");
		String data1S=st.nextToken();
		String data2S=st.nextToken();
		try{
			SimpleDateFormat format=new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			Date d1,d2;
			d1=format.parse(data1S);
			d2=format.parse(data2S);
			if(d2.before(d1) || (d2.equals(d1)))
				throw new InvalidDataException("errore");
			ArrayList<Posizione> posizioni=urapi.getPositionUser(getAttribute("username"), data1S,data2S);
			return gson.toJson(posizioni, ArrayList.class);
		}catch(InvalidUsernameException e){
			Status s=new Status(ErrorCodes.INVALID_USERNAME_CODE);
			setStatus(s);
			return gson.toJson(e, InvalidUsernameException.class);
		} catch (ParseException e) {
			Status s=new Status(ErrorCodes.INVALID_DATA_CODE);
			setStatus(s);
			return gson.toJson(e, InvalidDataException.class);
		}catch (InvalidDataException e1) {
			Status s=new Status(ErrorCodes.INVALID_DATA_CODE);
			setStatus(s);
			return gson.toJson(e1, InvalidDataException.class);
		}
			
	}
	
	@Post
	public String addUserPosition(){
		Gson gson=new Gson();
		UserRegistryAPI urapi=UserRegistryAPI.instance();
		StringTokenizer st=new StringTokenizer(getAttribute("data")," & ");
		String accuracy=st.nextToken();
		String time=st.nextToken();
		String latitude=st.nextToken();
		String longitude=st.nextToken();
		try{
			Posizione pos=new Posizione(accuracy,time,latitude,longitude);
			urapi.addUserPosition(getAttribute("username"), pos);
			return gson.toJson("Position added to "+getAttribute("username"),String.class);
		}catch(InvalidUsernameException e){
			Status s=new Status(ErrorCodes.INVALID_USERNAME_CODE);
			setStatus(s);
			return gson.toJson(e, InvalidUsernameException.class);
		}
	}
	
	

}
