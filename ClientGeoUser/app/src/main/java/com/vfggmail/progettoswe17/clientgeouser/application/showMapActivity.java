package com.vfggmail.progettoswe17.clientgeouser.application;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.model.CameraPosition;
import com.vfggmail.progettoswe17.clientgeouser.R;
import com.vfggmail.progettoswe17.clientgeouser.commons.Posizione;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.HashSet;

public class showMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private HashSet<Posizione> posizioni;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map);




        intent=getIntent();
        posizioni=(HashSet<Posizione>)intent.getSerializableExtra("array");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);


        LatLng place=null;

        // Add a marker and move the camera
        if(!posizioni.isEmpty()) {
            for (Posizione pos : posizioni) {
                place = new LatLng(pos.getIdPosizione().getLatitudine(),pos.getIdPosizione().getLongitudine());
                mMap.addMarker(new MarkerOptions().position(place).title(""));
            }

            CameraPosition cameraPosition = new CameraPosition.Builder().target(place).zoom(14.0f).build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
            mMap.moveCamera(cameraUpdate);

        }
    }
}
