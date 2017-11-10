package com.vfggmail.progettoswe17.clientgeouser.application;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vfggmail.progettoswe17.clientgeouser.R;
import com.vfggmail.progettoswe17.clientgeouser.commons.ErrorCodes;
import com.vfggmail.progettoswe17.clientgeouser.commons.InvalidDataException;
import com.vfggmail.progettoswe17.clientgeouser.commons.InvalidUsernameException;
import com.vfggmail.progettoswe17.clientgeouser.commons.Posizione;

import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import android.support.design.widget.Snackbar;

public class findUserActivity extends AppCompatActivity {

    private EditText userC;
    private EditText data1;
    private EditText data2;
    private Button cerca;
    private Intent intent;
    private String username,password;
    private Gson gson;
    private Snackbar sn;
    private ArrayList<Posizione> posizioni;
    SharedPreferences editor;
    public final static String prefName="Preference";




    @Override
    protected void onCreate(Bundle savedInstanceState) {




        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);



        userC=(EditText) findViewById(R.id.utenteC);
        data1=(EditText) findViewById(R.id.data1);
        data2=(EditText) findViewById(R.id.data2);
        cerca=(Button) findViewById(R.id.cerca);

        editor=getSharedPreferences(prefName,MODE_PRIVATE);

        username=editor.getString("username","");
        password=editor.getString("password","");


        userC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userC.setText("");
            }
        });

        data1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data1.setText("");
            }
        });

        data2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data2.setText("");
            }
        });

        cerca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date d1=null, d2=null;
                if(userC.getText().equals("") || data1.getText().equals("") || data2.getText().equals(""))
                    sn.make(v,"Inserisci i dati",Snackbar.LENGTH_SHORT).show();

                else
                    new findRestTask().execute(String.valueOf(userC.getText()), String.valueOf(data1.getText()), String.valueOf(data2.getText()));

            }

        });


    }

    public findRestTask createFindRestTask() {return new findRestTask();}


    public class findRestTask extends AsyncTask<String, Void, Integer> {


        protected Integer doInBackground(String... params) {

            String data1S=params[1],data2S=params[2];
            data1S=data1S.replaceAll("/", "%69");
            data2S=data2S.replaceAll("/", "%69");
            data1S=data1S.replaceAll(" ", "%20");
            data2S=data2S.replaceAll(" ", "%20");
            System.out.println(data1S+" "+data2S);
            String URI=mainPage.baseURI+"auth/users/"+params[0]+"/"+data1S+"&"+data2S;
            ClientResource cr=new ClientResource(URI);
            String gsonResponse=null;
            posizioni=new ArrayList<Posizione>();
            gson=new Gson();

            ChallengeScheme scheme = ChallengeScheme.HTTP_BASIC;
            ChallengeResponse authentication = new ChallengeResponse(scheme,username,password);

            cr.setChallengeResponse(authentication);




            try{
                gsonResponse=cr.get().getText();
                if(cr.getStatus().getCode()== ErrorCodes.INVALID_USERNAME_CODE)
                    throw gson.fromJson(gsonResponse, InvalidUsernameException.class);
                else if(cr.getStatus().getCode()==ErrorCodes.INVALID_DATA_CODE)
                    throw gson.fromJson(gsonResponse, InvalidDataException.class);
                posizioni = gson.fromJson(gsonResponse, new TypeToken<ArrayList<Posizione>>() {}.getType());

                return 0;

            }catch(InvalidUsernameException e2){
                return 1;
            } catch (ResourceException e1) {
                return 2;
            } catch (IOException e1) {
                return 2;
            } catch (InvalidDataException e) {
                return 3;
            }


        }


        protected void onPostExecute(Integer c) {
            View parent = (View) findViewById(R.id.activity_find_user);
            if (c == 0) {
                Intent myIntent = new Intent(findUserActivity.this, showMapActivity.class);
                myIntent.putExtra("array",posizioni);
                startActivity(myIntent);
            } else if (c == 1) {
                sn.make(parent, "Utente non registrato", Snackbar.LENGTH_SHORT).show();
            } else if (c == 2) {
                sn.make(parent, "Errore", Snackbar.LENGTH_SHORT).show();
            } else if(c == 3){
                sn.make(parent, "Date in formato errato", Snackbar.LENGTH_SHORT).show();

            }

        }

    }

}
