package com.aou.buswhere;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AdminHomeScreen extends AppCompatActivity {

    ListView adminMainScreenListView;
    private String[] values = new String[]{"Manage Students", "Manage Driver", "Manage Buses",
            "Manage Supervisors", "View Reports", "View all buses"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_screen);
        adminMainScreenListView = findViewById(R.id.adminMainScreenListView);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, values);
        adminMainScreenListView.setAdapter(adapter);

        adminMainScreenListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i;

                switch (position) {
                    case 0:
                        i = new Intent(AdminHomeScreen.this, ManageStudentsActivity.class);
                        break;
                    case 1:
                        i = new Intent(AdminHomeScreen.this, ManageDriversActivity.class);
                        break;

                    case 2:
                        i = new Intent(AdminHomeScreen.this, ManageBusesActivity.class);
                        break;

                    case 3:
                        i = new Intent(AdminHomeScreen.this, ManageSupervisorActivity.class);
                        break;

                    case 4:
                        i = new Intent(AdminHomeScreen.this, ReportsHomeScreen.class);
//                        i = new Intent();
                        break;

                    case 5:
                        i = new Intent(AdminHomeScreen.this, LocationBusesActivity.class);
                        break;
                    default:
                        i = new Intent(AdminHomeScreen.this, LocationBusesActivity.class);

                }
                startActivity(i);
            }
        });

    }
}
