package com.vfggmail.progettoswe17.clientgeouser.application;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import com.google.gson.Gson;
import com.vfggmail.progettoswe17.clientgeouser.R;
import com.vfggmail.progettoswe17.clientgeouser.commons.ErrorCodes;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;



public class loginActivity extends AppCompatActivity  {


    private EditText mUsernameView;
    private EditText mPasswordView;
    public final static String prefName="Preference";
    private String TAG = "SWENG_APPLICATION";
    private SharedPreferences.Editor editor;
    private Snackbar sn;
    private Button mLoginInButton;
    private Button mSignInButton;
    private final int MY_PERMISSIONS_REQUEST=123;
    private SharedPreferences preferences;
    private AsyncTask logintask;
    private static int MAX_TARDINESS=5000;
    private static Activity activity;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=this;
        preferences=getSharedPreferences(prefName,MODE_PRIVATE);
        editor = getSharedPreferences(prefName, MODE_PRIVATE).edit();

        if(preferences!=null && preferences.contains("username") && preferences.contains("password")){
            setContentView(R.layout.load_layout);

            new loginRestTask().execute(preferences.getString("username",null),preferences.getString("password",null));

        } else {
            setContentView(R.layout.activity_login);


            //Verifico i permessi e nel caso li richiedo
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    showMessageOKCancel("You need to allow access to position",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(loginActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                                            MY_PERMISSIONS_REQUEST);
                                }
                            });
                    return;
                }

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST);
                return;
            } else {
                statusCheck();
                mUsernameView = (EditText) findViewById(R.id.username);


                mPasswordView = (EditText) findViewById(R.id.password);
                mPasswordView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                        if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {

                            return true;
                        }
                        return false;
                    }
                });

                mLoginInButton = (Button) findViewById(R.id.login_button);

                preferences = getSharedPreferences(prefName, MODE_PRIVATE);
                if (preferences != null && preferences.contains("username") && preferences.contains("password")) {
                    mPasswordView.setText(preferences.getString("password", null));
                    mUsernameView.setText(preferences.getString("username", null));
                    new loginRestTask().execute(preferences.getString("username", null), preferences.getString("password", null));

                }


                mLoginInButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mUsernameView.getText().toString().equals("") || mPasswordView.getText().toString().equals("")) {
                            View parent = (View) findViewById(R.id.activity_login_page);
                            sn.make(parent, "Inserisci i dati", Snackbar.LENGTH_SHORT).show();
                        } else
                            new loginRestTask().execute(String.valueOf(mUsernameView.getText()), String.valueOf(mPasswordView.getText()));
                    }
                });


                mSignInButton = (Button) findViewById(R.id.login_signin);
                mSignInButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent myIntent = new Intent(loginActivity.this, signInActivity.class);
                        startActivity(myIntent);
                        overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);

                    }
                });

            }


        }
    }




    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Set your gps on")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }







    public loginRestTask createLoginRestTask() {
        return new loginRestTask();
    }


    public class loginRestTask extends AsyncTask<String, Void, Integer> {

        private Gson gson = new Gson();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            logintask=this;

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    logintask.cancel(true);
                    Log.i("TASK","Cancellato");
                    if (!(logintask.getStatus()== Status.FINISHED)) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(loginActivity.this, "Autenticazione con il server fallita", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                }
            }, MAX_TARDINESS);
        }

        protected Integer doInBackground(String... params) {



            SharedPreferences editor=getSharedPreferences(prefName,MODE_PRIVATE);
            String URI = "http://"+editor.getString("IP","10.0.2.2")+":"+editor.getString("port","8182")+"/UserRegApplication/" + "users/";
            gson = new Gson();
            ClientResource cr = new ClientResource(URI);
            String gsonResponse = null;
            Boolean response;


            try {
                gsonResponse = cr.put(gson.toJson(params[0] + ";" + params[1], String.class)).getText();
                if (cr.getStatus().getCode() == 200) {
                    response=gson.fromJson(gsonResponse,Boolean.class);
                    if(response)
                        return 0;
                    return 2;

                } else if (cr.getStatus().getCode() == ErrorCodes.INVALID_USERNAME_CODE) {
                    return 1;


                } else {
                    return 2;

                }


            } catch (ResourceException | IOException e1) {
                String text = "Error: " + cr.getStatus().getCode() + " - " + cr.getStatus().getDescription() + " - " + cr.getStatus().getReasonPhrase();
                Log.e(TAG, text);
                return 3;
            }
        }


        protected void onPostExecute(Integer c) {
            View parent = (View) findViewById(R.id.activity_login_page);
            if (c == 0) {
                Intent myIntent = new Intent(loginActivity.this, mainPage.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                if(!preferences.contains("username")) {
                    editor.putString("username", String.valueOf(mUsernameView.getText()));
                    editor.putString("password", String.valueOf(mPasswordView.getText()));
                }
                editor.commit();

                startActivity(myIntent);
                overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);

            } else if (c == 1) {
                sn.make(parent, "Utente non registrato", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } else if (c == 2) {
                sn.make(parent, "Credenziali non corrette", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }


        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id ==R.id.action_settings){
            Intent myIntent=new Intent(loginActivity.this,settingsActivity.class);
            startActivity(myIntent);
            overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);

        }

        return super.onOptionsItemSelected(item);
    }



    //Gestico la risposta dell'utente alla richiesta dei permessi
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    mUsernameView = (EditText) findViewById(R.id.username);
                    editor=getSharedPreferences(prefName,MODE_PRIVATE).edit();

                    mPasswordView = (EditText) findViewById(R.id.password);
                    mPasswordView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                    mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {

                                return true;
                            }
                            return false;
                        }
                    });

                    mLoginInButton = (Button) findViewById(R.id.login_button);









                    mLoginInButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(mUsernameView.getText().toString().equals("") || mPasswordView.getText().toString().equals("") ){
                                View parent=(View) findViewById(R.id.activity_login_page);
                                sn.make(parent, "Inserisci i dati",Snackbar.LENGTH_SHORT).show();
                            } else
                                new loginRestTask().execute(String.valueOf(mUsernameView.getText()), String.valueOf(mPasswordView.getText()));
                        }
                    });


                    mSignInButton=(Button) findViewById(R.id.login_signin);
                    mSignInButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent myIntent=new Intent(loginActivity.this, signInActivity.class);
                            startActivity(myIntent);
                            overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);

                        }
                    });

                    statusCheck();
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
        new AlertDialog.Builder(loginActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }




}

