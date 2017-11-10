package com.vfggmail.progettoswe17.clientgeouser.application;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.vfggmail.progettoswe17.clientgeouser.R;
import com.vfggmail.progettoswe17.clientgeouser.commons.Posizione;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import java.util.ArrayList;

public class showMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<Posizione> posizioni;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map);




        intent=getIntent();
        posizioni=(ArrayList<Posizione>)intent.getSerializableExtra("array");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng place=null;

        // Add a marker and move the camera
        if(!posizioni.isEmpty()) {
            for (Posizione pos : posizioni) {
                place = new LatLng(Double.parseDouble(pos.getLatitude().replaceAll("Latitude: ", "")), Double.parseDouble(pos.getLongitude().replaceAll("Longitude: ", "")));
                mMap.addMarker(new MarkerOptions().position(place).title(""));
            }

            mMap.moveCamera(CameraUpdateFactory.newLatLng(place));
        }
    }
}
