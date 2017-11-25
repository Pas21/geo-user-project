package server.web.frontend;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

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

import commons.Utente;
import server.backend.wrapper.UserRegistryAPI;
import server.web.resources.json.PositionsByUserAndDateJSON;
import server.web.resources.json.PositionsByUserJSON;
import server.web.resources.json.UserAuthJSON;
import server.web.resources.json.UserRegJSON;

public class UserRegistryWebApplication extends Application{
	
	//Creo il Map Verifier (mappa degli utenti registrati)
	public static MapVerifier verifier = new MapVerifier(); 		
	
	private class Settings{
		public int port;
	    public String web_base_dir;
	    
	}
	
	private static String rootDirForWebStaticFiles;

	public Restlet createInboundRoot(){
		//creiamo un router restlet che ha la funzioni di rispondere ad ogni chiamata con l'appropriata serverResource
		Router router=new Router(getContext());

		//Dopo aver creato il Map Verifier, lo associo a tutte le guardie 		
		ChallengeAuthenticator guardiaUserAuth=new ChallengeAuthenticator(getContext(),ChallengeScheme.HTTP_BASIC,"guardStaticUserAuth");
		guardiaUserAuth.setVerifier(verifier);
		guardiaUserAuth.setNext(UserAuthJSON.class);
		
		ChallengeAuthenticator guardStaticPositionsByUserAndDate=new ChallengeAuthenticator(getContext(),ChallengeScheme.HTTP_BASIC,"guardStaticPositionsByDate");
		guardStaticPositionsByUserAndDate.setVerifier(verifier);
		guardStaticPositionsByUserAndDate.setNext(PositionsByUserAndDateJSON.class);
		
		ChallengeAuthenticator guardStaticPositionsByUser=new ChallengeAuthenticator(getContext(),ChallengeScheme.HTTP_BASIC,"guardStaticPositionsByUser");
		guardStaticPositionsByUser.setVerifier(verifier);
		guardStaticPositionsByUser.setNext(PositionsByUserJSON.class);
		
		Directory directory= new Directory(getContext(),rootDirForWebStaticFiles);
		directory.setListingAllowed(true);
		directory.setDeeplyAccessible(true);
		router.attach("/UserRegApplication/web/",directory);
		router.attach("/UserRegApplication/web",directory);
		
		router.attach("/UserRegApplication/users",UserRegJSON.class);
		router.attach("/UserRegApplication/users/",UserRegJSON.class);
		router.attach("/UserRegApplication/auth/users",guardiaUserAuth);
		router.attach("/UserRegApplication/auth/users/",guardiaUserAuth);
		router.attach("/UserRegApplication/auth/positions/{username}",guardStaticPositionsByUser);
		router.attach("/UserRegApplication/auth/positions/{username}/",guardStaticPositionsByUser);
		router.attach("/UserRegApplication/auth/positions/{username}/{fromdata}/{todata}",guardStaticPositionsByUserAndDate);
		router.attach("/UserRegApplication/auth/positions/{username}/{fromdata}/{todata}/",guardStaticPositionsByUserAndDate);
		
		return router;
		
	}
	
	public static void main(String[] args){
		Gson gson=new Gson();
		Settings settings=null;
		UserRegistryAPI urapi=UserRegistryAPI.instance();

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
		
		//Caricamento utenti nel MapVerifier per il login
		TreeMap<String,Utente> utenti = urapi.getUtenti();
		System.err.println("Loading users in MapVerifier");
		
		for(Map.Entry<String, Utente> entry : utenti.entrySet()) {
		    //String username = entry.getKey();
		    //Utente utente = entry.getValue();
			
			String username = entry.getKey();
		    String password= entry.getValue().getPassword();
			verifier.getLocalSecrets().put(username, password.toCharArray());
			System.err.println("username:"+entry.getKey()+" password:"+ entry.getValue().getPassword());
		}
			
		
		try{
			//Provo a creare un nuovo component
			Component component=new Component();
			//Aggiungo un server http in ascolto sulla porta indicata dal file settings.json
			component.getServers().add(Protocol.HTTP, settings.port);
			//Aggiungo un handler per i file statici
			component.getClients().add(Protocol.FILE);

	        //IP
	        System.err.println("IP Server:"+InetAddress.getLocalHost().getHostAddress());
	        
			//Collego l'applicazione UserRegistryApplication
			component.getDefaultHost().attach(new UserRegistryWebApplication());
			//Avvio il component
			
			component.start();
			
		}catch(Exception e){	//Qualcosa non va
			e.printStackTrace();
			
		}
	}
	
}
