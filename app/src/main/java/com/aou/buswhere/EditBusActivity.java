package com.aou.buswhere;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

import model.AddInterface;
import model.Bus;
import model.GetDataInterface;
import model.Supervisor;
import util.HelperMethods;

public class EditBusActivity extends AppCompatActivity implements AddInterface, GetDataInterface {

    EditText nameEditText;
    Button saveBtn, centerPointBtn;

    Spinner supervisorSpinner;
    ArrayList<String> supervisorNamesList;
    ArrayList<Supervisor> supervisorList;
    ArrayAdapter<String> supervisorsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bus);

        nameEditText = findViewById(R.id.nameEditText);
        saveBtn = findViewById(R.id.addStudentBtn);
        centerPointBtn = findViewById(R.id.busCenterPointBtn);

        supervisorSpinner = findViewById(R.id.supervisorSpinner);
        HelperMethods.getData(EditBusActivity.this, "Supervisor", "Please wait", "Loading");


        nameEditText.setText(HelperMethods.currentBus.getName());
        nameEditText.setEnabled(false);
        HelperMethods.searchLocation = new LatLng(HelperMethods.currentBus.getCenterLatitude(), HelperMethods.currentBus.getCenterLongitude());

        centerPointBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditBusActivity.this, GetLocationActivity.class);
                startActivity(i);
            }
        });


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mandatoryFields
                if (HelperMethods.searchLocation.latitude == 0) {
                    Toast.makeText(EditBusActivity.this, "Please select location first", Toast.LENGTH_SHORT).show();
                } else {
                    saveBus();
                }


            }
        });


    }


    public void saveBus() {

        Bus currentBus = HelperMethods.currentBus;


        currentBus.setCenterLatitude(HelperMethods.searchLocation.latitude);
        currentBus.setCenterLongitude(HelperMethods.searchLocation.longitude);
        currentBus.setSupervisorName(supervisorSpinner.getSelectedItem().toString());
        currentBus.setSupervisorPhone(supervisorList.get(supervisorSpinner.getSelectedItemPosition()).getPhone());


        HelperMethods.busesList.remove(HelperMethods.currentBus);

        HelperMethods.busesList.add(currentBus);

        supervisorList.get(supervisorSpinner.getSelectedItemPosition()).setBusName(currentBus.getName());


        HelperMethods.pushInFireBase("Bus", currentBus, EditBusActivity.this, "Please wait", "Loading...", currentBus.getName().trim());
        HelperMethods.pushInFireBase("Supervisor", supervisorList.get(supervisorSpinner.getSelectedItemPosition()), EditBusActivity.this, supervisorList.get(supervisorSpinner.getSelectedItemPosition()).getEmail().replace(".", "").trim());

    }


    @Override
    public void updateUI(DatabaseError databaseError) {
        HelperMethods.searchLocation = new LatLng(0, 0);

        if (databaseError == null) {

            Toast.makeText(EditBusActivity.this, "Added successfully", Toast.LENGTH_LONG).show();
            finish();
        } else {
            databaseError.toException().printStackTrace();
            Log.i("Details", "updateUI: " + databaseError.getDetails());
            Log.i("msg", "updateUI: " + databaseError.getMessage());
            Toast.makeText(EditBusActivity.this, "error in saving", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void updateUI(DataSnapshot data) {
        supervisorNamesList = new ArrayList<>();
        supervisorList = new ArrayList<>();
        for (DataSnapshot currentChild : data.getChildren()) {
            Supervisor currentSupervisor = currentChild.getValue(Supervisor.class);
            supervisorNamesList.add(currentSupervisor.getName());
            supervisorList.add(currentSupervisor);
        }


        supervisorsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, supervisorNamesList);
        supervisorsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        supervisorSpinner.setAdapter(supervisorsAdapter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        HelperMethods.searchLocation = new LatLng(0, 0);
    }
}
