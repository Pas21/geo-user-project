package com.vfggmail.progettoswe17.clientgeouser.application;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
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
import java.util.List;

public class MainPage extends AppCompatActivity implements OnMapReadyCallback{

    private static final long POLLING_FREQ = 1000 * 10;
    private static final float MIN_DISTANCE = 10.0f;
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 8000;
    static String baseURI;
    private String username;
    private String password;
    private static String TAG = "ANTONIO_APPLICATION";
    private Boolean exit = false;


    private GoogleMap mMap;

    private Button cerca;
    private Button update;
    private Gson gson;
    private final static int MY_PERMISSIONS_REQUEST=123;


    // Miglior posizione corrente stimata
    private Location mLastReading;

    // Reference to the LocationManager and LocationListener
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    public final static String prefName="Preference";


    private SharedPreferences editor;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        //setSupportActionBar(toolbar);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_COARSE_LOCATION)) {
                showMessageOKCancel("You need to allow access to position",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MainPage.this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST);
                            }
                        });
                return;
            }

            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST);
            return;
        }else {

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            editor = getSharedPreferences(prefName, MODE_PRIVATE);

            baseURI = "http://" + editor.getString("IP", "10.0.2.2") + ":" + editor.getString("port", "8182") + "/UserRegApplication/";

            username = editor.getString("username", "");
            password = editor.getString("password", "");



            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            final Drawable d = getDrawable(R.drawable.position);
            fab.setImageDrawable(d);


            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar sn;
                    if (mLastReading == null) {
                        sn = Snackbar.make(view, "Impossibile aggiungere posizione inesistente", Snackbar.LENGTH_SHORT);
                        sn.show();
                    }

                    else
                        new AddPositionRestTask().execute(String.valueOf(mLastReading.getAccuracy()), String.valueOf(mLastReading.getLatitude()), String.valueOf(mLastReading.getLongitude()));


                }
            });


            cerca = (Button) findViewById(R.id.Posizione);
            update = (Button) findViewById(R.id.update);


            //ottengo una referenza al LocationManager

            if (null == (mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE)))
                finish();


            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (null == (mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE)))
                            finish();

                        mLastReading = lastKnownLocation();
                        updateDisplay(mLastReading);
                    } catch (Exception e) {
                        Snackbar sn=Snackbar.make(v, "Non disponibile al momento", Snackbar.LENGTH_SHORT);
                        sn.show();
                    }
                }
            });


            cerca.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(MainPage.this, FindUserActivity.class);

                    startActivity(myIntent);
                    overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);

                }
            });

            // Get best last location measurement
            mLastReading = lastKnownLocation();

            //Visualizzo le ultime informazioni lette

            if (mLastReading != null) {
                updateDisplay(mLastReading);
            } else
                Toast.makeText(this, "Nessuna locazione iniziale disponibile", Toast.LENGTH_SHORT).show();
            mLocationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    mLastReading = location;

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
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(place).title(""));
            CameraPosition cameraPosition = new CameraPosition.Builder().target(place).zoom(14.0f).build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
            mMap.moveCamera(cameraUpdate);
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
            SharedPreferences.Editor editor=getSharedPreferences(prefName,MODE_PRIVATE).edit();
            editor.remove("username");
            editor.remove("password");
            editor.commit();
            Intent myIntent=new Intent(MainPage.this,LoginActivity.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(myIntent);
            overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);


        LatLng place=null;

        // Add a marker and move the camera

        if(mLastReading!=null) {
            place = new LatLng(mLastReading.getLatitude(), mLastReading.getLongitude());
            mMap.addMarker(new MarkerOptions().position(place).title(""));
            CameraPosition cameraPosition = new CameraPosition.Builder().target(place).zoom(14.0f).build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
            mMap.moveCamera(cameraUpdate);
        }
    }
    public AddPositionRestTask createAddRestTask(){
        return new AddPositionRestTask();
    }




    public class AddPositionRestTask extends AsyncTask<String, Void, Integer> {

        private IdPosizione idpos;
        private Posizione pos;


        protected Integer doInBackground(String... params) {

            String URI=baseURI+"auth/positions/"+username;

            gson=new Gson();
            ClientResource cr=new ClientResource(URI);

            ChallengeScheme scheme = ChallengeScheme.HTTP_BASIC;
            ChallengeResponse authentication = new ChallengeResponse(scheme,username,password);
            cr.setChallengeResponse(authentication);


            String jsonResponse = null;


            try {
                idpos=new IdPosizione(new Timestamp((System.currentTimeMillis()/1000)*1000),Double.parseDouble(params[1]),Double.parseDouble(params[2]));
                Utente utente=new Utente(username,password,null,null,null);
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
            
            if (c==1) {
                Toast.makeText(MainPage.this, "Inserimento avvenuto con successo", Toast.LENGTH_SHORT).show();

            }

            else
                Toast.makeText(MainPage.this, "inserimento fallito", Toast.LENGTH_SHORT).show();

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




    //Gestico la risposta dell'utente alla richiesta dei permessi
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                    editor = getSharedPreferences(prefName, MODE_PRIVATE);

                    baseURI = "http://" + editor.getString("IP", "10.0.2.2") + ":" + editor.getString("port", "8182") + "/UserRegApplication/";

                    username = editor.getString("username", "");
                    password = editor.getString("password", "");

                    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                    final Drawable d = getDrawable(R.drawable.position);
                    fab.setImageDrawable(d);


                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Snackbar sn;
                            if (mLastReading == null) {
                                sn = Snackbar.make(view, "Impossibile aggiungere posizione inesistente", Snackbar.LENGTH_SHORT);
                                sn.show();
                            }
                            else
                                new AddPositionRestTask().execute(String.valueOf(mLastReading.getAccuracy()), String.valueOf(mLastReading.getLatitude()), String.valueOf(mLastReading.getLongitude()));


                        }
                    });


                    cerca = (Button) findViewById(R.id.Posizione);
                    update = (Button) findViewById(R.id.update);


                    //ottengo una referenza al LocationManager

                    if (null == (mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE)))
                        finish();


                    update.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                if (null == (mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE)))
                                    finish();

                                mLastReading = lastKnownLocation();
                                updateDisplay(mLastReading);
                            } catch (Exception e) {
                                Snackbar sn=Snackbar.make(v, "Non disponibile al momento", Snackbar.LENGTH_SHORT);
                                sn.show();
                            }
                        }
                    });


                    cerca.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent myIntent = new Intent(MainPage.this, FindUserActivity.class);

                            startActivity(myIntent);
                            overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);

                        }
                    });

                    // Get best last location measurement
                    mLastReading = lastKnownLocation();

                    //Visualizzo le ultime informazioni lette

                    if (mLastReading != null) {
                        updateDisplay(mLastReading);
                    } else
                        Toast.makeText(this, "Nessuna locazione iniziale disponibile", Toast.LENGTH_SHORT).show();
                    mLocationListener = new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            mLastReading = location;

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


                } else {
                    Toast.makeText(getApplicationContext(), "Check the permission", Toast.LENGTH_SHORT).show();
                    finish();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }




    //Visualizzo un messaggio di conferma
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainPage.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }




}
