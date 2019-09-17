package com.aou.buswhere;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
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

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;

import model.AddInterface;
import model.Bus;
import model.GetDataInterface;
import model.Supervisor;
import util.HelperMethods;

public class EditSupervisorActivity extends AppCompatActivity implements AddInterface, GetDataInterface {

    EditText nameEditText, phoneEditText, emailEditText;
    Button saveBtn;
//    Spinner busesSpinner;
    ArrayList<String> busNamesList;
    ArrayAdapter<String> busesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_supervisor);

        nameEditText = findViewById(R.id.nameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        emailEditText = findViewById(R.id.emailEditText);

        saveBtn = findViewById(R.id.addSupervisorBtn);

//        busesSpinner = findViewById(R.id.busSpinner);

        nameEditText.setText(HelperMethods.currentSupervisor.getName());
        phoneEditText.setText(HelperMethods.currentSupervisor.getPhone());
        emailEditText.setText(HelperMethods.currentSupervisor.getEmail());

        emailEditText.setEnabled(false);

        HelperMethods.getData(EditSupervisorActivity.this, "Bus", "Please wait", "Loading");


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mandatoryFields
                if (checkIfAllMandatoryFieldsEntered()) {
                    saveSupervisor();

                }

            }
        });


    }

    private boolean checkIfAllMandatoryFieldsEntered() {
        // Reset errors.
//        nameEditText, emailEditText, phoneEditText, addressEditText
        nameEditText.setError(null);
        emailEditText.setError(null);
        phoneEditText.setError(null);

        // Store values at the time of the login attempt.
        String userName = nameEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String email = emailEditText.getText().toString();

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
        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is mandatory");
            focusView = emailEditText;
            cancel = true;
            focusView.requestFocus();
        }

        // Check for a valid email address.
        if (!email.contains("@")) {
            emailEditText.setError("Invalid email");
            focusView = emailEditText;
            cancel = true;
            focusView.requestFocus();
        }


        return !cancel;
    }


    public void saveSupervisor() {
        Supervisor currentSupervisor = HelperMethods.currentSupervisor;
        currentSupervisor.setName(nameEditText.getText().toString());
        currentSupervisor.setPhone(phoneEditText.getText().toString());
//        currentSupervisor.setBusName(busesSpinner.getSelectedItem().toString());

        currentSupervisor.setEmail(emailEditText.getText().toString());
        String generatedPassword = generatePassword(8);

        sendSMS(phoneEditText.getText().toString(), generatedPassword);
        currentSupervisor.setPassword(generatedPassword);

        HelperMethods.supervisorList.remove(HelperMethods.currentSupervisor);

        HelperMethods.supervisorList.add(currentSupervisor);

        HelperMethods.pushInFireBase("Supervisor", currentSupervisor, EditSupervisorActivity.this, "Please wait", "Loading...", currentSupervisor.getEmail().toString().replace(".", "").trim());

    }

    private void sendSMS(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }

    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final Random random = new SecureRandom();

    public static String generatePassword(int length) {
        StringBuilder returnValue = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            returnValue.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return new String(returnValue);
    }


    @Override
    public void updateUI(DatabaseError databaseError) {
        if (databaseError == null) {

            Toast.makeText(EditSupervisorActivity.this, "Added successfully", Toast.LENGTH_LONG).show();
            finish();
        } else {
            databaseError.toException().printStackTrace();
            Log.i("Details", "updateUI: " + databaseError.getDetails());
            Log.i("msg", "updateUI: " + databaseError.getMessage());
            Toast.makeText(EditSupervisorActivity.this, "error in saving", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void updateUI(DataSnapshot data) {
//        busNamesList = new ArrayList<>();
//        for (DataSnapshot currentChild : data.getChildren()) {
//            Bus currentBus = currentChild.getValue(Bus.class);
//            busNamesList.add(currentBus.getName());
//        }
//        busesAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, busNamesList);
//        busesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        busesSpinner.setAdapter(busesAdapter);

    }
}
