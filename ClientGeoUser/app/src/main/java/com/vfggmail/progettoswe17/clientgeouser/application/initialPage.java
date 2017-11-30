package com.vfggmail.progettoswe17.clientgeouser.application;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.vfggmail.progettoswe17.clientgeouser.R;

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

