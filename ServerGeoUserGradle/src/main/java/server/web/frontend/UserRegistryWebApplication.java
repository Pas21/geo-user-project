package server.web.frontend;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.util.Map;
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
import com.google.gson.JsonSyntaxException;

import commons.Utente;
import server.backend.wrapper.UserRegistryAPI;
import server.web.resources.json.LastPositionAllUsersJSON;
import server.web.resources.json.PositionsByUserAndDateJSON;
import server.web.resources.json.PositionsByUserJSON;
import server.web.resources.json.UserAuthJSON;
import server.web.resources.json.UserRegJSON;

/**
 * The Class UserRegistryWebApplication defines the front-end class for setting server configuration and its running.
 */
public class UserRegistryWebApplication extends Application{
	
	/** The Constant verifier that contains all credentials of registered users for authentication. */
	//Creo il Map Verifier (mappa degli utenti registrati)
	public final static MapVerifier verifier = new MapVerifier(); 		
	
	/**
	 * The Class Settings defines server port and static content web directory.
	 */
	private class Settings{
		
		/** The port on which the server is listening. */
		public int port;
	    
    	/** The web base directory */
    	public String web_base_dir;
	    
	}
	
	/** The root dir for web static files. */
	private static String rootDirForWebStaticFiles;

	/* (non-Javadoc)
	 * @see org.restlet.Application#createInboundRoot()
	 */
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
		
		ChallengeAuthenticator guardStaticLastPositionUsers=new ChallengeAuthenticator(getContext(),ChallengeScheme.HTTP_BASIC,"guardStaticLastPositionUsers");
		guardStaticLastPositionUsers.setVerifier(verifier);
		guardStaticLastPositionUsers.setNext(LastPositionAllUsersJSON.class);
		
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
		router.attach("/UserRegApplication/auth/positions",guardStaticLastPositionUsers);
		router.attach("/UserRegApplication/auth/positions/",guardStaticLastPositionUsers);

		
		return router;
		
	}
	
	/**
	 * The main method loads server settings from file, loads credentials of registered users on MapVerifier and runs server.
	 * If the settings file does not exist, it is created by setting the default values (port=8182, web_base_dir:"web")
	 *
	 * @param args the arguments of the main method
	 */
	public static void main(String[] args){
		Gson gson=new Gson();
		Settings settings=null;
		UserRegistryAPI urapi=UserRegistryAPI.instance();

		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("settings.json"), Charset.defaultCharset()));
			settings=gson.fromJson(br.readLine(), Settings.class);
			br.close();
			System.err.println("Loading settings from file");
		}catch (IOException e1){
			System.err.println("Settings file not found, creation of the file with default values");
			try {
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("settings.json"), Charset.defaultCharset()));
				writer.write("{port:8182,web_base_dir:web}");
				writer.close();
			} catch (IOException e) {
				System.err.println("Error in creation of the file, set the default values");
			}
		}catch(JsonSyntaxException e){
			System.err.println("Wrong settings file, set the default values");
		}finally{
			if(settings==null){
				settings=gson.fromJson("{port:8182,web_base_dir:web}", Settings.class);
			}				
		}

		
		try{
			rootDirForWebStaticFiles="file:\\\\"+System.getProperty("user.dir")+"\\"+settings.web_base_dir;
			System.err.println("Web Directory: " + rootDirForWebStaticFiles);
		}catch(NullPointerException e ){
			System.err.println("EXCEPTION: Web Directory: " + rootDirForWebStaticFiles + "not found");
		}
		
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
	        System.err.println("Port Server:"+settings.port);
	        
			//Collego l'applicazione UserRegistryApplication
			component.getDefaultHost().attach(new UserRegistryWebApplication());
			//Avvio il component
			
			component.start();
			
		}catch(Exception e){	//Qualcosa non va
			e.printStackTrace();
			
		}
	}
	
}
