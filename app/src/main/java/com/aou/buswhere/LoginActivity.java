package com.aou.buswhere;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;

import model.Admin;
import model.GetDataInterface;
import model.Student;
import model.Supervisor;
import services.LocationService;
import util.HelperMethods;

public class LoginActivity extends AppCompatActivity implements GetDataInterface {
    private static final String TAG = "LoginActivity";
    EditText userNameEditText, passwordEditText;
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userNameEditText = (EditText) findViewById(R.id.userNameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        loginBtn = (Button) findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkIfAllMandatoryFieldsEntered()) {

                    HelperMethods.getData(LoginActivity.this, HelperMethods.loginType, "Please wait", "Loading");
                }
            }
        });


    }

    private boolean checkIfAllMandatoryFieldsEntered() {
        userNameEditText.setError(null);
        passwordEditText.setError(null);

        String userName = userNameEditText.getText().toString();
        String pass = passwordEditText.getText().toString();
        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(userName)) {
            userNameEditText.setError("Email is mandatory");
            focusView = userNameEditText;
            cancel = true;

        }


        if (TextUtils.isEmpty(pass)) {
            passwordEditText.setError("password is mandatory");
            focusView = passwordEditText;
            cancel = true;

        }
        if (cancel) {
            focusView.requestFocus();
        }

        return !cancel;


    }


    @Override
    public void updateUI(DataSnapshot data) {
        Log.i(TAG, "updateUI: " + data.toString());
        boolean isExist = false;
        for (DataSnapshot currentChild : data.getChildren()) {


            if (HelperMethods.loginType.equalsIgnoreCase("Admin")) {
                Admin currentUser = currentChild.getValue(Admin.class);

                if (currentUser.getEmail().trim().equalsIgnoreCase(userNameEditText.getText().toString().trim())
                        && currentUser.getPassword().equalsIgnoreCase(passwordEditText.getText().toString())) {
                    isExist = true;

                    HelperMethods.currentAdmin = currentUser;
                    break;
                }
            }

            if (HelperMethods.loginType.equalsIgnoreCase("Supervisor")) {
                Supervisor currentUser = currentChild.getValue(Supervisor.class);

                if (currentUser.getEmail().trim().equalsIgnoreCase(userNameEditText.getText().toString().trim())
                        && currentUser.getPassword().equalsIgnoreCase(passwordEditText.getText().toString())) {
                    isExist = true;

                    HelperMethods.currentSupervisor = currentUser;
                    break;
                }

            }

            if (HelperMethods.loginType.equalsIgnoreCase("Student")) {
                Student currentUser = currentChild.getValue(Student.class);

                if (currentUser.getEmail().trim().equalsIgnoreCase(userNameEditText.getText().toString().trim())
                        && currentUser.getPassword().equalsIgnoreCase(passwordEditText.getText().toString())) {
                    isExist = true;

                    HelperMethods.currentStudent = currentUser;
                    break;
                }

            }
        }

        if (isExist) {
            Intent i = null;
            if (HelperMethods.loginType.equalsIgnoreCase("Admin")) {
                i = new Intent(LoginActivity.this, AdminHomeScreen.class);
            }
            if (HelperMethods.loginType.equalsIgnoreCase("Supervisor")) {
                i = new Intent(LoginActivity.this, SupervisorHomeScreen.class);
            }
            if (HelperMethods.loginType.equalsIgnoreCase("Student")) {
                i = new Intent(LoginActivity.this, ParentHomeScreen.class);
            }

            startActivity(i);
        } else {
            Toast.makeText(LoginActivity.this, "Wrong Credentials", Toast.LENGTH_SHORT).show();
        }
    }
}
