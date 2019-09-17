package com.aou.buswhere;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;

import model.Bus;
import model.GetDataInterface;
import util.HelperMethods;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GetDataInterface {

    private GoogleMap mMap;
    MarkerOptions currentMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near currentBusLocation, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        HelperMethods.getDataFragmentActivity(MapsActivity.this, "Bus", HelperMethods.currentStudent.getBusName(), "Please wait", "Loading");

        // Add a marker in currentBusLocation and move the camera
        LatLng currentBusLocation = new LatLng(HelperMethods.currentBus.getCurrentLatitude(), HelperMethods.currentBus.getCurrentLongitude());

        currentMarker = new MarkerOptions();
        currentMarker.position(currentBusLocation);
        currentMarker.title(HelperMethods.currentBus.getName());

        mMap.addMarker(currentMarker);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentBusLocation, 13));


    }


    public void updateUI(DataSnapshot data) {
        Log.i("Dataa", "updateUI: " + data.toString());

        mMap.clear();


        final Bus currentBus = data.getValue(Bus.class);
        HelperMethods.currentBus = currentBus;

        // Add a marker in currentBusLocation and move the camera
        LatLng currentBusLocation = new LatLng(HelperMethods.currentBus.getCurrentLatitude(), HelperMethods.currentBus.getCurrentLongitude());

        MarkerOptions currentMarker = new MarkerOptions();
        currentMarker.position(currentBusLocation);
        currentMarker.title(HelperMethods.currentBus.getName());

        mMap.addMarker(currentMarker);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentBusLocation, 13));
    }
}
