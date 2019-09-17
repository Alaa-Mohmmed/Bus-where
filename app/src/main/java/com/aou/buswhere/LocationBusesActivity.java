package com.aou.buswhere;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;

import model.Bus;
import model.GetDataInterface;
import tracking.AlertDialogManager;
import tracking.ConnectionDetector;
import tracking.GPSTracker;
import util.HelperMethods;

public class LocationBusesActivity extends FragmentActivity implements OnMapReadyCallback, GetDataInterface {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_buses);
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
        HelperMethods.getDataFragmentActivity(LocationBusesActivity.this, "Bus", "Please wait", "Loading");

        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        HelperMethods.searchLocation = new LatLng(0, 0);
        // Check if Internet present
        boolean isInternetPresent = cd.isConnectingToInternet();
        if (!isInternetPresent) {
            // Internet Connection is not present

            AlertDialogManager alert = new AlertDialogManager();
            alert.showAlertDialog(LocationBusesActivity.this,
                    "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // stop executing code by return
            return;
        }

        // creating GPS Class object
        GPSTracker gps = new GPSTracker(this);

        // check if GPS location can get
        if (gps.canGetLocation() && gps.getLatitude() != 0) {
            Log.d("Your Location", "latitude:" + gps.getLatitude()
                    + ", longitude: " + gps.getLongitude());
            LatLng currentLocation = new LatLng(gps.getLatitude(),
                    gps.getLongitude());


            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,
                    10));


        } else {
            Toast.makeText(LocationBusesActivity.this, "Please enable GPS first and come back", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateUI(DataSnapshot data) {
        Log.i("Dataa", "updateUI: " + data.toString());

        mMap.clear();
        for (DataSnapshot currentChild : data.getChildren()) {
            Bus currentBus = currentChild.getValue(Bus.class);
            LatLng currentBusLocation = new LatLng(currentBus.getCurrentLatitude(), currentBus.getCurrentLongitude());

            MarkerOptions currentMarker;

            currentMarker = new MarkerOptions();
            currentMarker.position(currentBusLocation);
            currentMarker.title(currentBus.getName());

            mMap.addMarker(currentMarker);
        }


    }
}
