package com.vfggmail.progettoswe17.clientgeouser.application;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.vfggmail.progettoswe17.clientgeouser.R;
import com.vfggmail.progettoswe17.clientgeouser.commons.ErrorCodes;
import com.vfggmail.progettoswe17.clientgeouser.commons.InvalidEmailException;
import com.vfggmail.progettoswe17.clientgeouser.commons.InvalidUsernameException;
import com.vfggmail.progettoswe17.clientgeouser.commons.Utente;

import org.restlet.resource.ClientResource;

import java.io.IOException;


public class SignInActivity extends AppCompatActivity {


    private static EditText username;
    private static EditText password;
    private static EditText rePassword;
    private static EditText email;
    private static EditText name;
    private static EditText surname;
    private Button signIn;
    private SharedPreferences.Editor editor;
    public final static String prefName="Preference";
    private Gson gson = new Gson();




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        username = findViewById(R.id.signIn_username);
        password = findViewById(R.id.signIn_password);
        rePassword = findViewById(R.id.signIn_password2);
        email = findViewById(R.id.signIn_email);
        name = findViewById(R.id.signIn_Name);
        surname = findViewById(R.id.signIn_Surname);
        signIn = findViewById(R.id.signIn_signIn_button);


        editor = getSharedPreferences(prefName, MODE_PRIVATE).edit();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        editor.remove("username");
        editor.remove("password");
        //editor.putString("IP", "192.168.1.60");
        //editor.putString("port", "8182");
        editor.commit();


        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        rePassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar sn;
                if (username.getText().toString().equals("") || password.getText().toString().equals("") || rePassword.getText().toString().equals("") || email.getText().toString().equals("") || name.getText().toString().equals("") || surname.getText().toString().equals("")) {
                    View parent = (View) findViewById(R.id.activity_signin_page);
                    sn=Snackbar.make(parent, "Inserisci i dati", Snackbar.LENGTH_SHORT);
                    sn.show();
                } else
                    new RegRestTask().execute(String.valueOf(username.getText()), String.valueOf(password.getText()), String.valueOf(rePassword.getText()), String.valueOf(email.getText()), String.valueOf(name.getText()), String.valueOf(surname.getText()));
            }
        });
    }



    public RegRestTask createRegRestTask() {
        return new RegRestTask();
    }


    public class RegRestTask extends AsyncTask<String, Void, Integer> {


        protected Integer doInBackground(String... params) {


            if (params[1].equals(params[2])) {
                Utente utente = new Utente(params[0], params[1], params[3], params[4], params[5]);
                gson = new Gson();
                SharedPreferences editor=getSharedPreferences(prefName,MODE_PRIVATE);
                String URI = "http://"+editor.getString("IP","10.0.2.2")+":"+editor.getString("port","8182")+"/UserRegApplication/" + "users";
                ClientResource cr = new ClientResource(URI);
                String gsonResponse = null;
                try {
                    gsonResponse = cr.post(gson.toJson(utente, Utente.class)).getText();
                    if (cr.getStatus().getCode() == ErrorCodes.INVALID_USERNAME_CODE)
                        throw gson.fromJson(gsonResponse, InvalidUsernameException.class);
                    else if (cr.getStatus().getCode() == ErrorCodes.INVALID_EMAIL_CODE)
                        throw gson.fromJson(gsonResponse, InvalidEmailException.class);

                    return 0;
                } catch (IOException e) {
                    return 1;
                } catch (InvalidUsernameException e2) {
                    return 2;
                } catch(InvalidEmailException e3){
                    return 3;
                }
            } else {
                return 4;
            }
        }


        protected void onPostExecute(Integer c) {
            View parent = (View) findViewById(R.id.activity_signin_page);
            Snackbar sn;
            if (c == 0) {
                Intent myIntent = new Intent(SignInActivity.this, MainPage.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                editor.putString("username", username.getText().toString());
                editor.putString("password", password.getText().toString());
                editor.commit();
                startActivity(myIntent);
                overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
            } else if (c == 1) {
                sn=Snackbar.make(parent, "Errore", Snackbar.LENGTH_SHORT);
                sn.show();
            } else if (c == 2) {
                sn=Snackbar.make(parent, "Username utilizzato da un altro utente", Snackbar.LENGTH_SHORT);
                sn.show();
            } else if(c==3){
                sn=Snackbar.make(parent,"Email utilizzata da un altro utente",Snackbar.LENGTH_SHORT);
                sn.show();
            } else {
                sn=Snackbar.make(parent, "Password non coincidenti", Snackbar.LENGTH_SHORT);
                sn.show();
            }
        }
    }
}
