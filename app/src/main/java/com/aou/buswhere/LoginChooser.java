package com.aou.buswhere;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import util.HelperMethods;

public class LoginChooser extends AppCompatActivity {

    ImageButton parentBtn, supervisorBtn, adminBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_chooser);

        parentBtn = findViewById(R.id.parentBtn);
        supervisorBtn = findViewById(R.id.supervisorBtn);
        adminBtn = findViewById(R.id.adminBtn);

        parentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelperMethods.loginType = "Student";
                generateBtn();

            }
        });

        supervisorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelperMethods.loginType = "Supervisor";
                generateBtn();


            }
        });

        adminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelperMethods.loginType = "Admin";
                generateBtn();
            }
        });

    }

    public void generateBtn() {
        Intent i = new Intent(LoginChooser.this, LoginActivity.class);
        startActivity(i);
    }
}
