package com.vfggmail.progettoswe17.clientgeouser.application;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.vfggmail.progettoswe17.clientgeouser.R;
import com.vfggmail.progettoswe17.clientgeouser.commons.ErrorCodes;
import com.vfggmail.progettoswe17.clientgeouser.commons.IdPosizione;
import com.vfggmail.progettoswe17.clientgeouser.commons.InvalidUsernameException;
import com.vfggmail.progettoswe17.clientgeouser.commons.Posizione;
import com.vfggmail.progettoswe17.clientgeouser.commons.Utente;

import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class mainPage extends AppCompatActivity implements OnMapReadyCallback{

    private static final long POLLING_FREQ = 1000 * 10;
    private static final float MIN_DISTANCE = 10.0f;
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 8000;
    static String baseURI;
    private String username;
    private String password;
    private final String TAG = "ANTONIO_APPLICATION";
    private Boolean exit = false;


    private Utente utente;
    private GoogleMap mMap;

    private Button cerca;
    private Button update;
    private Snackbar sn;
    private Gson gson;

    // Miglior posizione corrente stimata
    private Location mLastReading;

    // Reference to the LocationManager and LocationListener
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    public final static String prefName="Preference";


    SharedPreferences editor;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        //setSupportActionBar(toolbar);


        utente=(Utente) getIntent().getSerializableExtra("utente");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        editor=getSharedPreferences(prefName,MODE_PRIVATE);

        baseURI= "http://"+editor.getString("IP","10.0.2.2")+":"+editor.getString("port","8182")+"/UserRegApplication/";

        username=editor.getString("username","");
        password=editor.getString("password","");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final Drawable d = getDrawable(R.drawable.position);
        fab.setImageDrawable(d);




        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mLastReading==null)
                    sn.make(view,"Impossibile aggiungere posizione inesistente",Snackbar.LENGTH_SHORT).show();
                else
                    new AddPositionRestTask().execute(String.valueOf(mLastReading.getAccuracy()), String.valueOf(mLastReading.getLatitude()), String.valueOf(mLastReading.getLongitude()));


            }
        });




        cerca=(Button) findViewById(R.id.Posizione);
        update=(Button) findViewById(R.id.update);


        //ottengo una referenza al LocationManager

        if(null==(mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE)))
            finish();


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (null == (mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE)))
                        finish();

                    mLastReading = lastKnownLocation();
                    updateDisplay(mLastReading);
                }catch(Exception e){
                    sn.make(v,"Non disponibile al momento",Snackbar.LENGTH_SHORT).show();
                }
            }
        });


        cerca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(mainPage.this, findUserActivity.class);

                startActivity(myIntent);
            }
        });

        // Get best last location measurement
        mLastReading = lastKnownLocation();

        //Visualizzo le ultime informazioni lette

        if(mLastReading!=null) {
            updateDisplay(mLastReading);
        }
        else
            Toast.makeText(this, "Nessuna locazione iniziale disponibile", Toast.LENGTH_SHORT).show();
        mLocationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mLastReading=location;

                updateDisplay(location);
            }



            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };




        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.main_map);
        mapFragment.getMapAsync(this);


    }




    protected void onResume(){
        super.onResume();
        Log.i("Entered onresume()","onResume()");


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {


            Toast toast = Toast.makeText(getApplicationContext(), "L'applicazione è stata arrestata poichè sono stati negati i permessi", Toast.LENGTH_SHORT);
            toast.show();
            finish();
        }
        else {
            //Si registra per update dal network
            if (null != mLocationManager.getProvider(LocationManager.NETWORK_PROVIDER)) {
                mLocationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, POLLING_FREQ, MIN_DISTANCE, mLocationListener);
            }

            //Si registra per update da GPS
            if (null != mLocationManager.getProvider(LocationManager.GPS_PROVIDER)) {
                mLocationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, POLLING_FREQ, MIN_DISTANCE, mLocationListener);
            }


        }
    }

    //Listener posizione non registrati
    protected void onPause(){
        super.onPause();
        Log.i("Entered onPause()","onPause()");

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            Toast toast = Toast.makeText(getApplicationContext(), "L'applicazione è stata arrestata poichè sono stati negati i permessi", Toast.LENGTH_SHORT);
            toast.show();
            finish();
        }
        else
            mLocationManager.removeUpdates(mLocationListener);
    }


    //Il prossimo metodo legge la posizione da tutti i providers e restituisce la migliore
    private Location lastKnownLocation() {
        Location bestResult = null;
        float bestAccuracy = Float.MAX_VALUE;


        try {
            List<String> matchingProviders = mLocationManager.getAllProviders();
            for (String provider : matchingProviders) {
                Location location = mLocationManager.getLastKnownLocation(provider);
                if (location != null) {
                    float accuracy = location.getAccuracy();
                    if (accuracy < bestAccuracy) {
                        bestResult = location;
                        bestAccuracy = accuracy;
                    }
                }
            }
        }catch(SecurityException e){
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {


                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION);

            }
        }

        return bestResult;
    }


    public void updateDisplay(Location location){
        /*
        mAccuracyView.setText("Accuracy: "+location.getAccuracy());
        mTimeView.setText("Time: "+new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault()).format(new Date(location.getTime())));
        mLatView.setText("Latitude: "+location.getLatitude());
        mLngView.setText("Longitude: "+location.getLongitude());
        */
        if(mMap!=null) {
            LatLng place = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions().position(place).title(""));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(place));
        }
    }





    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.i("Entered onPermission()","onPermissionResult()");

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {



                } else {

                    Toast toast = Toast.makeText(getApplicationContext(), "L'applicazione è stata arrestata poichè sono stati negati i permessi", Toast.LENGTH_SHORT);
                    toast.show();
                    finish();

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id ==R.id.action_logout){
            new logoutRestTask().execute();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        LatLng place=null;

        // Add a marker and move the camera

        if(mLastReading!=null) {
            place = new LatLng(mLastReading.getLatitude(), mLastReading.getLongitude());
            mMap.addMarker(new MarkerOptions().position(place).title(""));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(place));
        }
    }
    public AddPositionRestTask createAddRestTask(){
        return new AddPositionRestTask();
    }




    public class AddPositionRestTask extends AsyncTask<String, Void, Integer> {

        private IdPosizione idpos;
        private Posizione pos;


        protected Integer doInBackground(String... params) {

            String URI=baseURI+"auth/users/"+username;

            gson=new Gson();
            ClientResource cr=new ClientResource(URI);

            ChallengeScheme scheme = ChallengeScheme.HTTP_BASIC;
            ChallengeResponse authentication = new ChallengeResponse(scheme,username,password);
            cr.setChallengeResponse(authentication);


            String jsonResponse = null;


            try {
                idpos=new IdPosizione(new Timestamp(new Date().getTime()),Double.parseDouble(params[1]),Double.parseDouble(params[2]));
                pos=new Posizione(idpos,utente,Float.parseFloat(params[0]));
                jsonResponse = cr.post(gson.toJson(pos,Posizione.class)).getText();
                if (cr.getStatus().getCode()== ErrorCodes.INVALID_USERNAME_CODE)
                    throw gson.fromJson(jsonResponse, InvalidUsernameException.class);

            } catch (ResourceException | IOException e1) {
                String text = "Error: " + cr.getStatus().getCode() + " - " + cr.getStatus().getDescription()+ " - " + cr.getStatus().getReasonPhrase();
                Log.e(TAG,text);
                return 0;
            } catch (InvalidUsernameException  e2) {
                String text = "Error: " + cr.getStatus().getCode() + " - " + e2.getMessage();
                Log.e(TAG,text);
                return 0;
            }

            return 1;
        }


        protected void onPostExecute(Integer c) {
            View parent=(View) findViewById(R.id.content_main);
            if (c==1){
                sn.make(parent, "Inserimento avvenuto con successo", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

            else {
                sn.make(parent, "Inserimento fallito", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

        }
    }


    public logoutRestTask createlogoutRestTask(){
        return new logoutRestTask();
    }


    public class logoutRestTask extends AsyncTask<String, Void, Integer> {


        protected Integer doInBackground(String... params) {

            String URI=baseURI+"users/remove/"+username;
            Gson gson=new Gson();
            ClientResource cr=new ClientResource(URI);
            String gsonResponse=null;

            ChallengeScheme scheme = ChallengeScheme.HTTP_BASIC;
            ChallengeResponse authentication = new ChallengeResponse(scheme,username,password);
            cr.setChallengeResponse(authentication);

            try{
                gsonResponse=cr.delete().getText();
                if(cr.getStatus().getCode()==ErrorCodes.INVALID_USERNAME_CODE)
                    throw gson.fromJson(gsonResponse, InvalidUsernameException.class);
                return 0;
            }catch(IOException e){
                return 2;
            }catch(InvalidUsernameException e1){
                return 1;
            }

        }


        protected void onPostExecute(Integer c) {

            View parent=(View) findViewById(R.id.content_main);
            if (c==0){
                sn.make(parent, "Eliminato", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent myIntent=new Intent(mainPage.this,initialPage.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(myIntent);
            }

            else if(c==1) {
                sn.make(parent, "Credenziali non esistenti", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }else{
                sn.make(parent, "Errore Interno", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }


        }
    }

    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }

}
