package com.vfggmail.progettoswe17.clientgeouser.application;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.vfggmail.progettoswe17.clientgeouser.R;

public class settingsActivity extends AppCompatActivity {


    private EditText ip;
    private EditText port;
    private Button change;
    public final static String prefName="Preference";
    SharedPreferences.Editor editor;
    Snackbar sn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);



        editor=getSharedPreferences(prefName,MODE_PRIVATE).edit();



        ip=(EditText) findViewById(R.id.settings_IP);
        port=(EditText) findViewById(R.id.settings_port);
        change=(Button)findViewById(R.id.settings_change);


        ip.setText(getSharedPreferences(prefName,MODE_PRIVATE).getString("IP","10.0.2.2"));
        port.setText(getSharedPreferences(prefName,MODE_PRIVATE).getString("port","8182"));


        ip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ip.setText("");
            }
        });

        port.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                port.setText("");
            }
        });


        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(String.valueOf(ip.getText()).equals("") || String.valueOf(port.getText()).equals(""))
                    sn.make(v,"Inserisci i dati",Snackbar.LENGTH_SHORT);
                else {
                    editor.putString("IP", String.valueOf(ip.getText()));
                    editor.putString("port", String.valueOf(port.getText()));
                    editor.commit();
                    Intent myIntent = new Intent(settingsActivity.this, loginActivity.class);
                    startActivity(myIntent);
                }
            }
        });

    }
}
