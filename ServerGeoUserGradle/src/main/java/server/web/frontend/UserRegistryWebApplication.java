package server.web.frontend;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Protocol;
import org.restlet.resource.Directory;
import org.restlet.routing.Router;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.MapVerifier;

import com.google.gson.Gson;

import server.backend.wrapper.UserRegistryAPI;
import server.web.resources.json.UserAuthJSON;
import server.web.resources.json.UserFilteredJSON;
import server.web.resources.json.UserJSON;
import server.web.resources.json.UserLogOutJSON;
import server.web.resources.json.UserRegJSON;
import server.web.resources.json.UserRegSizeJSON;

public class UserRegistryWebApplication extends Application{
	
	//Creo il Map Verifier
	public final static MapVerifier verifier=new MapVerifier();
	
	private class Settings{
		public int port;
	    public String web_base_dir;
	    public String storage_base_dir;
	    public String storage_base_file;
	}
	
	private static String rootDirForWebStaticFiles;

	public Restlet createInboundRoot(){
		//creiamo un router restlet che ha la funzioni di rispondere ad ogni chiamata con l'appropriata serverResource
		Router router=new Router(getContext());

		//Dopo aver creato il Map Verifier, lo associo a tutte le guardie 
		ChallengeAuthenticator guardiaSize=new ChallengeAuthenticator(getContext(),ChallengeScheme.HTTP_BASIC,"guardiaSize");
		guardiaSize.setVerifier(verifier);
		guardiaSize.setNext(UserRegSizeJSON.class);
		
		ChallengeAuthenticator guardiaFilter=new ChallengeAuthenticator(getContext(), ChallengeScheme.HTTP_BASIC,"guardiaFilter");
		guardiaFilter.setVerifier(verifier);
		guardiaFilter.setNext(UserFilteredJSON.class);
		
		ChallengeAuthenticator guardiaUser=new ChallengeAuthenticator(getContext(),ChallengeScheme.HTTP_BASIC,"guardiaUser");
		guardiaUser.setVerifier(verifier);
		guardiaUser.setNext(UserJSON.class);
		
		ChallengeAuthenticator guardiaUserReg=new ChallengeAuthenticator(getContext(),ChallengeScheme.HTTP_BASIC,"guardiaUserReg");
		guardiaUserReg.setVerifier(verifier);
		guardiaUserReg.setNext(UserRegJSON.class);
		
		ChallengeAuthenticator guardiaRemove=new ChallengeAuthenticator(getContext(),ChallengeScheme.HTTP_BASIC,"guardiaRemove");
		guardiaRemove.setVerifier(verifier);
		guardiaRemove.setNext(UserLogOutJSON.class);
		
		
		
		Directory directory= new Directory(getContext(),rootDirForWebStaticFiles);
		directory.setListingAllowed(true);
		directory.setDeeplyAccessible(true);
		router.attach("/UserRegApplication/web/",directory);
		router.attach("/UserRegApplication/web",directory);
		
		router.attach("/UserRegApplication/auth/size",guardiaSize);
		router.attach("/UserRegApplication/auth/users",guardiaUserReg);
		router.attach("/UserRegApplication/auth/users/{username}",guardiaUser);
		router.attach("/UserRegApplication/auth/users/{username}/{data}",guardiaFilter);
		router.attach("/UserRegApplication/users",UserAuthJSON.class);
		router.attach("/UserRegApplication/users/remove/{username}",guardiaRemove);
		
		return router;
		
	}
	
	public static void main(String[] args){
		Gson gson=new Gson();
		Settings settings=null;
		try{
			Scanner scanner=new Scanner(new File("Settings.json"));
			settings=gson.fromJson(scanner.nextLine(), Settings.class);
			scanner.close();
			System.err.println("Loading settings from file");
		} catch (FileNotFoundException e1) {
		System.err.println("Settings file not found");
		System.exit(-1);
	}
		
		rootDirForWebStaticFiles="file:\\\\"+System.getProperty("user.dir")+"\\"+settings.web_base_dir;
		System.err.println("Web Directory: " + rootDirForWebStaticFiles);
		
		UserRegistryAPI urapi=UserRegistryAPI.instance();
		urapi.setStorageFiles(System.getProperty("user.dir")+File.separator+settings.storage_base_dir+File.separator, settings.storage_base_file);
		urapi.restore();
			
		
		try{
			//Provo a creare un nuovo component
			Component component=new Component();
			//Aggiungo un server http in ascolto sulla porta indicata dal file settings.json
			component.getServers().add(Protocol.HTTP, settings.port);
			//Aggiungo un handler per i file statici
			component.getClients().add(Protocol.FILE);
			//Collego l'applicazione UserRegistryApplication
			component.getDefaultHost().attach(new UserRegistryWebApplication());
			//Avvio il component
			
			component.start();
			
		}catch(Exception e){	//Qualcosa non va
			e.printStackTrace();
			
		}
	}
	
}
