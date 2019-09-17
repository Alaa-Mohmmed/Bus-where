package com.aou.buswhere;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

import model.Bus;
import model.GetDataInterface;
import util.HelperMethods;

public class ReportsHomeScreen extends AppCompatActivity implements GetDataInterface {

    EditText searchEditText;
    ArrayList<String> busNamesList;
    ArrayList<Bus> busList;


    ArrayList<String> busNamesListFiltered;
    ArrayList<Bus> busListFiltered;


    ListView resultListView;
    ArrayAdapter<String> busesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_home_screen);
        searchEditText = findViewById(R.id.searchEditText);
        resultListView = findViewById(R.id.searchResultListView);
        busNamesList = new ArrayList<>();
        busList = new ArrayList<>();
        busNamesListFiltered = new ArrayList<>();
        busListFiltered = new ArrayList<>();

        HelperMethods.getData(ReportsHomeScreen.this, "Bus", "Please wait", "Loading");


        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                busListFiltered.clear();
                busNamesListFiltered.clear();


                for (int i = 0; i < busNamesList.size(); i++) {

                    if (busNamesList.get(i).toUpperCase().contains(searchEditText.getText().toString().toUpperCase())) {
                        busNamesListFiltered.add(busNamesList.get(i));
                        busListFiltered.add(busList.get(i));
                    }
                }

//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ReportsHomeScreen.this,
//                        android.R.layout.simple_list_item_1, android.R.id.text1, busNamesListFiltered);
                busesAdapter.notifyDataSetChanged();

                // Assign adapter to ListView
//                resultListView.setAdapter(adapter);


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void updateUI(DataSnapshot data) {
        busListFiltered.clear();
        busNamesListFiltered.clear();
        busList.clear();
        busNamesList.clear();
        for (DataSnapshot currentChild : data.getChildren()) {
            Bus currentBus = currentChild.getValue(Bus.class);
            busNamesList.add(currentBus.getName());
            busList.add(currentBus);
            busNamesListFiltered.add(currentBus.getName());
            busListFiltered.add(currentBus);
            Log.i("data", "updateUI: " + currentBus.getName());
        }
        busesAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, busNamesListFiltered);

        resultListView.setAdapter(busesAdapter);

        resultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(ReportsHomeScreen.this, ReportDetailsActivity.class);
                HelperMethods.currentBus = busListFiltered.get(position);
                startActivity(i);
            }
        });

    }
}
