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

import java.util.ArrayList;
import java.util.Collections;

import adapter.StudentsCustomList;
import adapter.TripDetailsParentCustomList;
import model.CheckIn;
import model.GetDataInterface;
import model.Student;
import model.Trip;
import util.HelperMethods;

public class ManageStudentsActivity extends AppCompatActivity implements GetDataInterface {
    ListView studentsListView;

    StudentsCustomList studentsCustomList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_students);
        studentsListView = findViewById(R.id.studentsListView);

        HelperMethods.getData(ManageStudentsActivity.this, "Student", "Please wait", "Loading");


    }

    @Override
    public void onResume() {
        super.onResume();
        // put your code here...
        Collections.sort(HelperMethods.studentsList);

        studentsCustomList = new StudentsCustomList(ManageStudentsActivity.this, HelperMethods.studentsList, android.R.layout.simple_list_item_1);
        studentsListView.setAdapter(studentsCustomList);
    }


    @Override
    public void updateUI(DataSnapshot data) {
        Log.i("Dataa", "updateUI: " + data.toString());

        HelperMethods.studentsList.clear();

        for (DataSnapshot currentChild : data.getChildren()) {
            Student currentStudent = currentChild.getValue(Student.class);
            HelperMethods.studentsList.add(currentStudent);

        }

        Collections.sort(HelperMethods.studentsList);

        studentsCustomList = new StudentsCustomList(ManageStudentsActivity.this, HelperMethods.studentsList, android.R.layout.simple_list_item_1);
        studentsListView.setAdapter(studentsCustomList);

    }

    public void call(View v) {
        Student currentStudent = (Student) v.getTag();
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + currentStudent.getPhone()));
        startActivity(callIntent);
    }

    public void delete(View v) {
        Student currentStudent = (Student) v.getTag();
        HelperMethods.deleteFromFirebase("Student", currentStudent.getEmail().replace(".", ""), ManageStudentsActivity.this, "Please wait", "Loading...");
        HelperMethods.studentsList.remove(currentStudent);
        studentsCustomList.notifyDataSetChanged();

    }

    public void details(View v) {
        Student currentStudent = (Student) v.getTag();
        Intent i = new Intent(ManageStudentsActivity.this, EditStudentActivity.class);
        HelperMethods.currentStudent = currentStudent;
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
                Intent i = new Intent(ManageStudentsActivity.this, AddStudentActivity.class);
                startActivity(i);

        }
        return true;
    }

}
