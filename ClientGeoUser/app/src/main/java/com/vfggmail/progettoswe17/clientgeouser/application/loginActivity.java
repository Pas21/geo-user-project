package com.vfggmail.progettoswe17.clientgeouser.application;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
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

    }







    public loginRestTask createLoginRestTask() {
        return new loginRestTask();
    }


    public class loginRestTask extends AsyncTask<String, Void, Integer> {

        private Gson gson = new Gson();
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
            View parent = (View) findViewById(R.id.activity_login_page);
            if (c == 0) {
                Intent myIntent = new Intent(loginActivity.this, mainPage.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                editor.putString("username", mUsernameView.getText().toString());
                editor.putString("password", mPasswordView.getText().toString());
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

}

