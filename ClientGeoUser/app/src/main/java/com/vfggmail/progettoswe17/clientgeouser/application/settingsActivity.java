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
                if(ip.getText().equals("") || port.getText().equals(""))
                    sn.make(v,"Inserisci i dati",Snackbar.LENGTH_SHORT);

                editor.putString("IP",ip.getText().toString());
                editor.putString("port",port.getText().toString());
                editor.commit();
                Intent myIntent=new Intent(settingsActivity.this,mainPage.class);
                startActivity(myIntent);
            }
        });

    }
}
