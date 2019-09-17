package com.aou.buswhere;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;

import model.Bus;
import model.GetDataInterface;
import model.Station;
import util.HelperMethods;

public class LocationStationsActivity extends FragmentActivity implements OnMapReadyCallback, GetDataInterface {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_stations);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        HelperMethods.getDataFragmentActivity(LocationStationsActivity.this, "Bus", HelperMethods.currentSupervisor.getBusName(), "Please wait", "Loading");

        // Add a marker in currentBusLocation and move the camera

        for (int i = 0; i < HelperMethods.currentBus.getStations().size(); i++) {
            Station currentStation = HelperMethods.currentBus.getStations().get(i);
            LatLng currentBusLocation = new LatLng(currentStation.getLatitude(), currentStation.getLongitude());

            MarkerOptions currentMarker;

            currentMarker = new MarkerOptions();
            currentMarker.position(currentBusLocation);
            currentMarker.title(HelperMethods.currentBus.getName());

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentBusLocation, 13));

        }


    }

    public void updateUI(DataSnapshot data) {
        Log.i("Dataa", "updateUI: " + data.toString());

        mMap.clear();


        final Bus currentBus = data.getValue(Bus.class);
        HelperMethods.currentBus = currentBus;

        for (int i = 0; i < HelperMethods.currentBus.getStations().size(); i++) {
            Station currentStation = HelperMethods.currentBus.getStations().get(i);
            LatLng currentBusLocation = new LatLng(currentStation.getLatitude(), currentStation.getLongitude());

            MarkerOptions currentMarker;

            currentMarker = new MarkerOptions();
            currentMarker.position(currentBusLocation);
            currentMarker.title(currentStation.getStudentName());

            mMap.addMarker(currentMarker);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentBusLocation, 10));

        }

    }
}
