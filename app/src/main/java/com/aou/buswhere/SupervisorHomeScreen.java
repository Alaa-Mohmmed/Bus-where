package com.aou.buswhere;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import adapter.CheckinCustomList;
import model.AddInterface;
import model.Bus;
import model.CheckIn;
import model.GetDataInterface;
import model.Station;
import model.Trip;
import services.LocationService;
import util.HelperMethods;

public class SupervisorHomeScreen extends AppCompatActivity implements GetDataInterface, AddInterface {


    CheckinCustomList checkinCustomList;
    ListView supervisorListView;
    String type = "";
    Intent serviceIntent;
    Button checkinbutton, checkoutbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        supervisorListView = findViewById(R.id.supervisorListView);
        HelperMethods.isStarted = true;
        serviceIntent = new Intent(SupervisorHomeScreen.this, LocationService.class);
        startService(serviceIntent);

        HelperMethods.getData(SupervisorHomeScreen.this, "Bus", HelperMethods.currentSupervisor.getBusName(), "Please wait", "Loading");


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.supervisor_home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.startTripMenu) {
            putToolTip("Trip type", "Please choose the trip type");

            return true;
        } else if (id == R.id.endTripMenu) {
            DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();

            HelperMethods.currentTrip.setEndTime(dateTimeFormat.format(date));

            ArrayList<CheckIn> checkIns = new ArrayList<>();


            for (int i = 0; i < HelperMethods.currentTrip.getCheckIn().size(); i++) {
                CheckIn currentCheckIn = HelperMethods.currentTrip.getCheckIn().get(i);
                if (currentCheckIn.getType().equalsIgnoreCase("Check In")) {
                    checkIns.add(currentCheckIn);
                }
            }


            updateCheckInList(checkIns);

            for (int i = 0; i < checkIns.size(); i++) {
                CheckIn currentCheckin = new CheckIn();
                currentCheckin.setStudentName(checkIns.get(i).getStudentName());
                currentCheckin.setTime(dateTimeFormat.format(date));
                currentCheckin.setType("CheckOut");
                HelperMethods.currentTrip.getCheckIn().add(currentCheckin);
            }


            HelperMethods.pushInFireBase("Trip", HelperMethods.checkInKey, HelperMethods.currentTrip, SupervisorHomeScreen.this, "loading", "plz wait");
            return true;
        } else if (id == R.id.stationsMenuBtn) {
            Intent i = new Intent(SupervisorHomeScreen.this, LocationStationsActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateCheckInList(ArrayList<CheckIn> checkIns) {
        for (int i = 0; i < HelperMethods.currentTrip.getCheckIn().size(); i++) {
            for (int j = 0; j < checkIns.size(); j++) {
                if (HelperMethods.currentTrip.getCheckIn().get(i).getStudentName().equalsIgnoreCase(checkIns.get(j).getStudentName())
                        && HelperMethods.currentTrip.getCheckIn().get(i).getType().equalsIgnoreCase("CheckOut")) {
                    checkIns.remove(checkIns.get(j));
                }
            }
        }
    }


    public void putToolTip(String title, String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(SupervisorHomeScreen.this);
        builder.setTitle(title);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage(msg);
        builder.setCancelable(true);
        builder.setPositiveButton("Incoming", new DialogInterface.OnClickListener() {


            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                type = "Incoming";

                DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                Date date = new Date();
                System.out.println(dateTimeFormat.format(date)); //2016/11/16 12:08:43
                System.out.println(dateFormat.format(date)); //2016/11/16

                HelperMethods.currentTrip = new Trip();
                HelperMethods.currentTrip.setBus(HelperMethods.currentBus.getName());
                HelperMethods.currentTrip.setDate(dateFormat.format(date));
                HelperMethods.currentTrip.setStartTime(dateTimeFormat.format(date));
                HelperMethods.currentTrip.setEndTime("");

                String key = HelperMethods.currentBus.getName() + ";" + dateTimeFormat.format(date) + "_Incoming";
                Log.i("key", "onClick: " + key);
                HelperMethods.checkInKey = key;
                HelperMethods.pushInFireBase("Trip", key, HelperMethods.currentTrip, SupervisorHomeScreen.this, "loading", "plz wait");

            }
        });

        builder.setNegativeButton("Outgoing", new DialogInterface.OnClickListener() {


            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                type = "Outgoing";

                DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                Date date = new Date();
                System.out.println(dateTimeFormat.format(date)); //2016/11/16 12:08:43
                System.out.println(dateFormat.format(date)); //2016/11/16

                HelperMethods.currentTrip = new Trip();
                HelperMethods.currentTrip.setBus(HelperMethods.currentBus.getName());
                HelperMethods.currentTrip.setDate(dateFormat.format(date));
                HelperMethods.currentTrip.setStartTime(dateTimeFormat.format(date));
                HelperMethods.currentTrip.setEndTime("");

                String key = HelperMethods.currentBus.getName() + ";" + dateTimeFormat.format(date) + "_Outcoming";
                HelperMethods.checkInKey = key;

                Log.i("idd", "onClick: " + key);
                HelperMethods.pushInFireBase("Trip", key, HelperMethods.currentTrip, SupervisorHomeScreen.this, "loading", "plz wait");


            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public void updateUI(DataSnapshot data) {


        try {
            HelperMethods.currentBus = data.getValue(Bus.class);
            checkinCustomList = new CheckinCustomList(this, HelperMethods.currentBus.getStations(), android.R.layout.simple_list_item_1);
            checkinCustomList.notifyDataSetChanged();
            supervisorListView.setAdapter(checkinCustomList);


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Wrong bus name", Toast.LENGTH_SHORT).show();
        }
    }

    public void checkIn(View v) {

        if (!HelperMethods.checkInKey.equalsIgnoreCase("") && HelperMethods.currentTrip.getEndTime().equalsIgnoreCase("")) {
            checkinbutton = (Button) v;
            Station currentStation = (Station) checkinbutton.getTag();

            CheckIn checkIn = new CheckIn();
            checkIn.setStudentName(currentStation.getStudentName());
            checkIn.setType("Check In");

            DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();

            checkIn.setTime(dateTimeFormat.format(date));

            HelperMethods.currentTrip.getCheckIn().add(checkIn);
            v.setClickable(false);
            checkinbutton.setBackgroundColor(0xFFC9650F);
            // checkoutbutton.setBackgroundColor(Color.TRANSPARENT);


            HelperMethods.pushInFireBase("Trip", HelperMethods.checkInKey, HelperMethods.currentTrip, SupervisorHomeScreen.this, "loading", "plz wait");
        } else {
            Toast.makeText(this, "Please start trip first", Toast.LENGTH_SHORT).show();
        }
    }

    public void checkOut(View v) {
        if (!HelperMethods.checkInKey.equalsIgnoreCase("") && HelperMethods.currentTrip.getEndTime().equalsIgnoreCase("")) {
            checkoutbutton = (Button) v;
            Station currentStation = (Station) checkoutbutton.getTag();

            CheckIn checkIn = new CheckIn();
            checkIn.setStudentName(currentStation.getStudentName());
            checkIn.setType("CheckOut");

            DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();

            checkIn.setTime(dateTimeFormat.format(date));
            HelperMethods.currentTrip.getCheckIn().add(checkIn);
            v.setClickable(false);
            checkoutbutton.setBackgroundColor(0xFFC9650F);

            HelperMethods.pushInFireBase("Trip", HelperMethods.checkInKey, HelperMethods.currentTrip, SupervisorHomeScreen.this, "loading", "plz wait");
        } else {
            Toast.makeText(this, "Please start trip first", Toast.LENGTH_SHORT).show();
        }

    }

    public void call(View v) {
        ImageButton button = (ImageButton) v;
        Station currentStation = (Station) button.getTag();

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + currentStation.getStudentPhone()));
        startActivity(callIntent);
    }

    @Override
    public void updateUI(DatabaseError databaseError) {
        if (databaseError == null) {
            Toast.makeText(SupervisorHomeScreen.this, "Saved successfully", Toast.LENGTH_SHORT).show();
        } else {
            Log.i("Error", "updateUI: " + databaseError.getMessage());
            Toast.makeText(SupervisorHomeScreen.this, "Error in saving", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        HelperMethods.isStarted = false;
        stopService(serviceIntent);
    }
}
