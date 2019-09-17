package com.aou.buswhere;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;

import java.util.Collections;

import adapter.BusesCustomList;
import model.Bus;
import model.GetDataInterface;
import util.HelperMethods;

public class ManageBusesActivity extends AppCompatActivity implements GetDataInterface {
    ListView busesListView;

    BusesCustomList busesCustomList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_buses);
        busesListView = findViewById(R.id.busesListView);

        HelperMethods.getData(ManageBusesActivity.this, "Bus", "Please wait", "Loading");


    }

    @Override
    public void onResume() {
        super.onResume();
        // put your code here...
        Collections.sort(HelperMethods.busesList);

        busesCustomList = new BusesCustomList(ManageBusesActivity.this, HelperMethods.busesList, android.R.layout.simple_list_item_1);
        busesListView.setAdapter(busesCustomList);
    }


    @Override
    public void updateUI(DataSnapshot data) {
        Log.i("Dataa", "updateUI: " + data.toString());

        HelperMethods.busesList.clear();

        for (DataSnapshot currentChild : data.getChildren()) {
            Bus currentBus = currentChild.getValue(Bus.class);
            HelperMethods.busesList.add(currentBus);

        }

        Collections.sort(HelperMethods.busesList);

        busesCustomList = new BusesCustomList(ManageBusesActivity.this, HelperMethods.busesList, android.R.layout.simple_list_item_1);
        busesListView.setAdapter(busesCustomList);

    }


    public void delete(View v) {
        Bus currentBus = (Bus) v.getTag();
        HelperMethods.deleteFromFirebase("Bus", currentBus.getName(), ManageBusesActivity.this, "Please wait", "Loading...");
        HelperMethods.busesList.remove(currentBus);
        busesCustomList.notifyDataSetChanged();

    }

    public void details(View v) {
        Bus currentBus = (Bus) v.getTag();
        Intent i = new Intent(ManageBusesActivity.this, EditBusActivity.class);
        HelperMethods.currentBus = currentBus;
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
                Intent i = new Intent(ManageBusesActivity.this, AddBusActivity.class);
                startActivity(i);

        }
        return true;
    }


}