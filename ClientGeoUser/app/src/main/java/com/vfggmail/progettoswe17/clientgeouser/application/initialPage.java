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

    private Button login;
    private Button signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_page);


        login=(Button)findViewById(R.id.initial_page_login);
        signin=(Button) findViewById(R.id.initial_page_signin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent=new Intent(initialPage.this,loginActivity.class);
                startActivity(myIntent);
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent=new Intent(initialPage.this,signInActivity.class);
                startActivity(myIntent);
            }
        });


    }





}

