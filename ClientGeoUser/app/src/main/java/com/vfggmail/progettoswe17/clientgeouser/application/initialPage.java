package com.vfggmail.progettoswe17.clientgeouser.application;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.vfggmail.progettoswe17.clientgeouser.R;
import com.vfggmail.progettoswe17.clientgeouser.commons.ErrorCodes;
import com.vfggmail.progettoswe17.clientgeouser.commons.InvalidUsernameException;
import com.vfggmail.progettoswe17.clientgeouser.commons.User;

import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import java.io.IOException;

public class initialPage extends AppCompatActivity {

    private ToggleButton toggle;
    private Button send;
    private static EditText username;
    private static EditText password;
    private static EditText rePassword;
    private static EditText email;
    private static EditText name;
    private static EditText surname;
    private Snackbar sn;
    private Gson gson = new Gson();
    private String TAG = "SWENG_APPLICATION";
    public final static String prefName="Preference";


    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_page);
        editor=getSharedPreferences(prefName,MODE_PRIVATE).edit();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        editor.remove("username");
        editor.remove("password");
        editor.putString("IP","192.168.43.67");
        editor.putString("port","8182");
        editor.commit();

        toggle = (ToggleButton) findViewById(R.id.toggleButton);
        send = (Button) findViewById(R.id.button);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password1);
        rePassword = (EditText) findViewById(R.id.password2);
        name = (EditText) findViewById(R.id.name);
        surname = (EditText) findViewById(R.id.surname);
        email = (EditText) findViewById(R.id.email);


        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        rePassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.setText("");
            }
        });

        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password.setText("");
                password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });

        rePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rePassword.setText("");
                rePassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email.setText("");
            }
        });

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setText("");
            }
        });

        surname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                surname.setText("");
            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().equals("") || password.getText().toString().equals("") || rePassword.getText().toString().equals("") || email.getText().toString().equals("") || name.getText().toString().equals("") || surname.getText().toString().equals("")){
                    View parent=(View) findViewById(R.id.activity_initial_page);
                    sn.make(parent, "Inserisci i dati",Snackbar.LENGTH_SHORT).show();
                } else
                    new regRestTask().execute(String.valueOf(username.getText()), String.valueOf(password.getText()), String.valueOf(rePassword.getText()), String.valueOf(email.getText()), String.valueOf(name.getText()), String.valueOf(surname.getText()));
            }
        });

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rePassword.setVisibility(View.INVISIBLE);
                    email.setVisibility(View.INVISIBLE);
                    name.setVisibility(View.INVISIBLE);
                    surname.setVisibility(View.INVISIBLE);
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    rePassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    send.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            if(username.getText().toString().equals("") || password.getText().toString().equals("") ){
                                View parent=(View) findViewById(R.id.activity_initial_page);
                                sn.make(parent, "Inserisci i dati",Snackbar.LENGTH_SHORT).show();
                            } else
                                new loginRestTask().execute(String.valueOf(username.getText()), String.valueOf(password.getText()));

                        }
                    });
                } else {
                    rePassword.setVisibility(View.VISIBLE);
                    email.setVisibility(View.VISIBLE);
                    name.setVisibility(View.VISIBLE);
                    surname.setVisibility(View.VISIBLE);
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    rePassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    send.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(username.getText().toString().equals("") || password.getText().toString().equals("") || rePassword.getText().toString().equals("") || email.getText().toString().equals("") || name.getText().toString().equals("") || surname.getText().toString().equals("")){
                                View parent=(View) findViewById(R.id.activity_initial_page);
                                sn.make(parent, "Inserisci i dati",Snackbar.LENGTH_SHORT).show();
                            } else
                                new regRestTask().execute(String.valueOf(username.getText()), String.valueOf(password.getText()), String.valueOf(rePassword.getText()), String.valueOf(email.getText()), String.valueOf(name.getText()), String.valueOf(surname.getText()));
                        }
                    });


                }
            }
        });


    }

    public loginRestTask createLoginRestTask() {
        return new loginRestTask();
    }


    public class loginRestTask extends AsyncTask<String, Void, Integer> {


        protected Integer doInBackground(String... params) {

            SharedPreferences editor=getSharedPreferences(prefName,MODE_PRIVATE);
            String URI = "http://"+editor.getString("IP","10.0.2.2")+":"+editor.getString("port","8182")+"/UserRegApplication/" + "users";
            gson = new Gson();
            ClientResource cr = new ClientResource(URI);
            String gsonResponse = null;


            try {
                gsonResponse = cr.post(gson.toJson(params[0] + "&" + params[1], String.class)).getText();
                if (!gsonResponse.contains("not") && cr.getStatus().getCode() == 200) {
                    return 0;

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
            View parent = (View) findViewById(R.id.activity_initial_page);
            if (c == 0) {
                Intent myIntent = new Intent(initialPage.this, mainPage.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                editor.putString("username", username.getText().toString());
                editor.putString("password", password.getText().toString());
                editor.commit();
                startActivity(myIntent);
            } else if (c == 1) {
                sn.make(parent, "Utente non registrato", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } else if (c == 2) {
                sn.make(parent, "Password non corretta", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

        }
    }


    public regRestTask createRegRestTask() {
        return new regRestTask();
    }


    public class regRestTask extends AsyncTask<String, Void, Integer> {


        protected Integer doInBackground(String... params) {


            if (params[1].equals(params[2])) {
                User u = new User(params[0], params[1], params[3], params[4], params[5]);
                gson = new Gson();
                SharedPreferences editor=getSharedPreferences(prefName,MODE_PRIVATE);
                String URI = "http://"+editor.getString("IP","10.0.2.2")+":"+editor.getString("port","8182")+"/UserRegApplication/" + "users";
                ClientResource cr = new ClientResource(URI);
                String gsonResponse = null;
                try {
                    gsonResponse = cr.put(gson.toJson(u, User.class)).getText();
                    if (cr.getStatus().getCode() == ErrorCodes.INVALID_USERNAME_CODE)
                        throw gson.fromJson(gsonResponse, InvalidUsernameException.class);
                    return 0;
                } catch (IOException e) {
                    return 1;
                } catch (InvalidUsernameException e2) {
                    return 2;
                }
            } else {
                return 3;
            }
        }


        protected void onPostExecute(Integer c) {
            View parent = (View) findViewById(R.id.activity_initial_page);
            if (c == 0) {
                Intent myIntent = new Intent(initialPage.this, mainPage.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                editor.putString("username", username.getText().toString());
                editor.putString("password", password.getText().toString());
                editor.commit();
                startActivity(myIntent);
            } else if (c == 1) {
                sn.make(parent, "Errore", Snackbar.LENGTH_SHORT).show();
            } else if (c == 2) {
                sn.make(parent, "Username utilizzato da un altro utente", Snackbar.LENGTH_SHORT).show();
            } else {
                sn.make(parent, "Password non coincidenti", Snackbar.LENGTH_SHORT).show();
            }

        }
    }
}

