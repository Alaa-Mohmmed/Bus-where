package com.aou.buswhere;

import android.content.Intent;
import android.net.Uri;
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

import adapter.SupervisorsCustomList;
import model.GetDataInterface;
import model.Supervisor;
import util.HelperMethods;

public class ManageSupervisorActivity extends AppCompatActivity implements GetDataInterface {
    ListView driversListView;

    SupervisorsCustomList supervisorsCustomList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_supervisor);
        driversListView = findViewById(R.id.supervisorListView);

        HelperMethods.getData(ManageSupervisorActivity.this, "Supervisor", "Please wait", "Loading");


    }

    @Override
    public void onResume() {
        super.onResume();
        // put your code here...
        Collections.sort(HelperMethods.supervisorList);

        supervisorsCustomList = new SupervisorsCustomList(ManageSupervisorActivity.this, HelperMethods.supervisorList, android.R.layout.simple_list_item_1);
        driversListView.setAdapter(supervisorsCustomList);
    }


    @Override
    public void updateUI(DataSnapshot data) {
        Log.i("Dataa", "updateUI: " + data.toString());

        HelperMethods.supervisorList.clear();

        for (DataSnapshot currentChild : data.getChildren()) {
            Supervisor currentSupervisor = currentChild.getValue(Supervisor.class);
            HelperMethods.supervisorList.add(currentSupervisor);

        }

        Collections.sort(HelperMethods.driversList);

        supervisorsCustomList = new SupervisorsCustomList(ManageSupervisorActivity.this, HelperMethods.supervisorList, android.R.layout.simple_list_item_1);
        driversListView.setAdapter(supervisorsCustomList);

    }

    public void call(View v) {
        Supervisor currentSupervisor = (Supervisor) v.getTag();
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + currentSupervisor.getPhone()));
        startActivity(callIntent);
    }

    public void delete(View v) {
        Supervisor currentSupervisor = (Supervisor) v.getTag();
        HelperMethods.deleteFromFirebase("Supervisor", currentSupervisor.getEmail().replace(".", "").trim(), ManageSupervisorActivity.this, "Please wait", "Loading...");
        HelperMethods.supervisorList.remove(currentSupervisor);
        supervisorsCustomList.notifyDataSetChanged();

    }

    public void details(View v) {
        Supervisor currentSupervisor = (Supervisor) v.getTag();
        Intent i = new Intent(ManageSupervisorActivity.this, EditSupervisorActivity.class);
        HelperMethods.currentSupervisor = currentSupervisor;
        startActivity(i);
    }

//    public void stations(View v) {
//        Supervisor currentSupervisor = (Supervisor) v.getTag();
//        Intent i = new Intent(ManageSupervisorActivity.this, LocationStationsActivity.class);
//        HelperMethods.currentSupervisor = currentSupervisor;
//        startActivity(i);
//    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_admin_student, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addStudentMenu:
                Intent i = new Intent(ManageSupervisorActivity.this, AddSupervisorActivity.class);
                startActivity(i);

        }
        return true;
    }

}