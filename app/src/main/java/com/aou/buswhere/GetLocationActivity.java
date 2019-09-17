package com.aou.buswhere;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import tracking.AlertDialogManager;
import tracking.ConnectionDetector;
import tracking.GPSTracker;
import util.HelperMethods;

public class GetLocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location);
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


        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        HelperMethods.searchLocation = new LatLng(0, 0);
        // Check if Internet present
        boolean isInternetPresent = cd.isConnectingToInternet();
        if (!isInternetPresent) {
            // Internet Connection is not present

            AlertDialogManager alert = new AlertDialogManager();
            alert.showAlertDialog(GetLocationActivity.this,
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
                    13));

            mMap.addMarker(new MarkerOptions()
                    .title("Selected location")
                    .position(currentLocation)
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.mark_red)).draggable(true));
            mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {

                }

                @Override
                public void onMarkerDrag(Marker marker) {

                }

                @Override
                public void onMarkerDragEnd(Marker marker) {
//                    Toast.makeText(GetLocationActivity.this, "latitude " + marker.getPosition().latitude, Toast.LENGTH_SHORT).show();
//
//                    Toast.makeText(GetLocationActivity.this, "longitude " + marker.getPosition().longitude, Toast.LENGTH_SHORT).show();

                    alertDialog(marker);
                }
            });
        } else {
            Toast.makeText(GetLocationActivity.this, "Please enable GPS first and come back", Toast.LENGTH_SHORT).show();
        }
    }

    private void alertDialog(final Marker marker) {
        AlertDialog.Builder builder = new AlertDialog.Builder(GetLocationActivity.this);
        builder.setTitle("Do you want to save this location");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setCancelable(true);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {


            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();

                HelperMethods.searchLocation = marker.getPosition();
//                                HelperCLass.destinationLocation = new LatLng(marker.getPosition().longitude, marker.getPosition().latitude);
                finish();

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {


            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                HelperMethods.searchLocation = new LatLng(0, 0);

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
