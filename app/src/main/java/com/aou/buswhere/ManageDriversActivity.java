package com.aou.buswhere;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;

import java.util.Collections;

import adapter.DriversCustomList;
import model.Driver;
import model.GetDataInterface;
import model.Student;
import util.HelperMethods;

public class ManageDriversActivity extends AppCompatActivity implements GetDataInterface {
    ListView driversListView;

    DriversCustomList driversCustomList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_drivers);
        driversListView = findViewById(R.id.driversListView);

        HelperMethods.getData(ManageDriversActivity.this, "Driver", "Please wait", "Loading");


    }

    @Override
    public void onResume() {
        super.onResume();
        // put your code here...
        Collections.sort(HelperMethods.driversList);

        driversCustomList = new DriversCustomList(ManageDriversActivity.this, HelperMethods.driversList, android.R.layout.simple_list_item_1);
        driversListView.setAdapter(driversCustomList);
    }


    @Override
    public void updateUI(DataSnapshot data) {
        Log.i("Dataa", "updateUI: " + data.toString());

        HelperMethods.driversList.clear();

        for (DataSnapshot currentChild : data.getChildren()) {
            Driver currentDriver = currentChild.getValue(Driver.class);
            HelperMethods.driversList.add(currentDriver);

        }

        Collections.sort(HelperMethods.driversList);

        driversCustomList = new DriversCustomList(ManageDriversActivity.this, HelperMethods.driversList, android.R.layout.simple_list_item_1);
        driversListView.setAdapter(driversCustomList);

    }

    public void call(View v) {
        Driver currentDriver = (Driver) v.getTag();
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + currentDriver.getPhone()));
        startActivity(callIntent);
    }

    public void delete(View v) {
        Driver currentDriver = (Driver) v.getTag();
        HelperMethods.deleteFromFirebase("Driver", currentDriver.getPhone(), ManageDriversActivity.this, "Please wait", "Loading...");
        HelperMethods.driversList.remove(currentDriver);
        driversCustomList.notifyDataSetChanged();

    }

    public void details(View v) {
        Driver currentDriver = (Driver) v.getTag();
        Intent i = new Intent(ManageDriversActivity.this, EditDriverActivity.class);
        HelperMethods.currentDriver = currentDriver;
        startActivity(i);
    }

    public void stations(View v) {
        Driver currentDriver = (Driver) v.getTag();
        Intent i = new Intent(ManageDriversActivity.this, LocationStationsActivity.class);
        HelperMethods.currentDriver = currentDriver;
        startActivity(i);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_admin_student, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addStudentMenu:
                Intent i = new Intent(ManageDriversActivity.this, AddDriverActivity.class);
                startActivity(i);

        }
        return true;
    }


}
