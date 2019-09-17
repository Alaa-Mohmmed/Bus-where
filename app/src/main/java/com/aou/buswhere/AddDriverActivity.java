package com.aou.buswhere;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

import model.AddInterface;
import model.Bus;
import model.Driver;
import model.GetDataInterface;
import util.HelperMethods;

public class AddDriverActivity extends AppCompatActivity implements AddInterface, GetDataInterface {

    EditText nameEditText, phoneEditText;
    Button saveBtn;
    Spinner busesSpinner;
    ArrayList<String> busNamesList;
    ArrayAdapter<String> busesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_driver);

        nameEditText = findViewById(R.id.nameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        saveBtn = findViewById(R.id.addStudentBtn);

        busesSpinner = findViewById(R.id.busSpinner);


        HelperMethods.getData(AddDriverActivity.this, "Bus", "Please wait", "Loading");


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mandatoryFields
                if (checkIfAllMandatoryFieldsEntered()) {
                    saveDriver();

                }

            }
        });


    }

    private boolean checkIfAllMandatoryFieldsEntered() {
        // Reset errors.
//        nameEditText, emailEditText, phoneEditText, addressEditText
        nameEditText.setError(null);
        phoneEditText.setError(null);

        // Store values at the time of the login attempt.
        String userName = nameEditText.getText().toString();
        String phone = phoneEditText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(userName)) {
            nameEditText.setError("name is mandatory");
            focusView = nameEditText;
            cancel = true;
            focusView.requestFocus();
        }


        if (TextUtils.isEmpty(phone)) {
            phoneEditText.setError("Phone is mandatory");
            focusView = phoneEditText;
            cancel = true;
            focusView.requestFocus();
        }


        return !cancel;
    }


    public void saveDriver() {
        Driver currentDriver = new Driver();
        currentDriver.setName(nameEditText.getText().toString());
        currentDriver.setPhone(phoneEditText.getText().toString());
        currentDriver.setBusName(busesSpinner.getSelectedItem().toString());


        HelperMethods.driversList.add(currentDriver);

        HelperMethods.pushInFireBase("Driver", currentDriver, AddDriverActivity.this, "Please wait", "Loading...", currentDriver.getPhone().trim());

    }


    @Override
    public void updateUI(DatabaseError databaseError) {
        if (databaseError == null) {

            Toast.makeText(AddDriverActivity.this, "Added successfully", Toast.LENGTH_LONG).show();
            finish();
        } else {
            databaseError.toException().printStackTrace();
            Log.i("Details", "updateUI: " + databaseError.getDetails());
            Log.i("msg", "updateUI: " + databaseError.getMessage());
            Toast.makeText(AddDriverActivity.this, "error in saving", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void updateUI(DataSnapshot data) {
        busNamesList = new ArrayList<>();
        for (DataSnapshot currentChild : data.getChildren()) {
            Bus currentBus = currentChild.getValue(Bus.class);
            busNamesList.add(currentBus.getName());
        }
        busesAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, busNamesList);
        busesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        busesSpinner.setAdapter(busesAdapter);

    }
}
