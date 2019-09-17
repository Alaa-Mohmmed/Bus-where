package com.aou.buswhere;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;

import model.AddInterface;
import model.Bus;
import model.GetDataInterface;
import model.Station;
import model.Student;
import util.HelperMethods;

public class AddStudentActivity extends AppCompatActivity implements AddInterface, GetDataInterface {

    EditText nameEditText, emailEditText, phoneEditText, addressEditText;
    Button pickupLocationBtn, pickupImgBtn, saveBtn;
    Spinner busesSpinner;
    ArrayList<String> busNamesList;
    ArrayList<Bus> busList;
    ArrayAdapter<String> busesAdapter;

    int count = 0;

    // add image

    int PICK_IMAGE_REQUEST = 111;
    Uri filePath;
    ProgressDialog pd;
    //creating reference to firebase storage
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://buswhere-40b83.appspot.com");    //change the url according to your firebase app

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.parentEmailEditText);
        phoneEditText = findViewById(R.id.parentPhoneEditText);
        addressEditText = findViewById(R.id.addressEditText);
        pickupImgBtn = findViewById(R.id.pickUpImgBtn);
        pickupLocationBtn = findViewById(R.id.pickUpLocationBtn);
        saveBtn = findViewById(R.id.addStudentBtn);

        busesSpinner = findViewById(R.id.busSpinner);
        pd = new ProgressDialog(this);
        pd.setMessage("Uploading....");


        HelperMethods.getData(AddStudentActivity.this, "Bus", "Please wait", "Loading");


        pickupLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddStudentActivity.this, GetLocationActivity.class);
                startActivity(i);
            }
        });

        pickupImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mandatoryFields
                if (checkIfAllMandatoryFieldsEntered()) {
                    Long tsLong = System.currentTimeMillis() / 1000;
                    String ts = tsLong.toString();
                    String imgName = "image" + ts + ".jpg";
                    uploadImg(imgName);

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
        addressEditText.setError(null);

        // Store values at the time of the login attempt.
        String userName = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String address = addressEditText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(userName)) {
            nameEditText.setError("name is mandatory");
            focusView = nameEditText;
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


        if (TextUtils.isEmpty(address)) {
            addressEditText.setError("Address is mandatory");
            focusView = addressEditText;
            cancel = true;
            focusView.requestFocus();
        }

        if (TextUtils.isEmpty(phone)) {
            phoneEditText.setError("Phone is mandatory");
            focusView = phoneEditText;
            cancel = true;
            focusView.requestFocus();
        }

        /*if (HelperMethods.searchLocation.latitude == 0) {
            Toast.makeText(AddStudentActivity.this, "Location is mandatory", Toast.LENGTH_SHORT).show();
            cancel = true;
        }*/
        if (filePath == null) {
            Toast.makeText(AddStudentActivity.this, "Image is mandatory", Toast.LENGTH_SHORT).show();
            cancel = true;
        }

        return !cancel;
    }


    private void uploadImg(String imgName) {
        pd.show();


        final StorageReference childRef = storageRef.child(imgName);

        //uploading the image
        UploadTask uploadTask = childRef.putFile(filePath);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.i("url", "onComplete4 : " + uri.toString());
                        pd.dismiss();

                        saveStudent(uri.toString());
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddStudentActivity.this, "Error in upload", Toast.LENGTH_SHORT).show();
                pd.dismiss();

            }
        });


    }

    public void saveStudent(String filePath) {
        Student currentStudent = new Student();
        currentStudent.setName(nameEditText.getText().toString());
        currentStudent.setAddress(addressEditText.getText().toString());
        currentStudent.setImgUrl(filePath);
        currentStudent.setEmail(emailEditText.getText().toString().trim());
        currentStudent.setPassword("");
        currentStudent.setPhone(phoneEditText.getText().toString());
        currentStudent.setBusName(busesSpinner.getSelectedItem().toString());
        currentStudent.setPickupLatitude(HelperMethods.searchLocation.latitude);
        currentStudent.setPickupLongitude(HelperMethods.searchLocation.longitude);

        String generatedPassword = generatePassword(8);

        sendSMS(phoneEditText.getText().toString(), generatedPassword);
        currentStudent.setPassword(generatedPassword);

        HelperMethods.studentsList.add(currentStudent);

        HelperMethods.pushInFireBase("Student", currentStudent, AddStudentActivity.this, "Please wait", "Loading...", currentStudent.getEmail().replace(".", "").trim());

        int index = 0;
        Log.i("length", "index: " + busList.size());
        for (int i = 0; i < busList.size(); i++) {
            if (currentStudent.getBusName().equalsIgnoreCase(busList.get(i).getName())) {
//                for (int j = 0; j < HelperMethods.busesList.get(i).getStations().size(); j++) {

                Station s = new Station();
                s.setImgUrl(currentStudent.getImgUrl());
                s.setLatitude(currentStudent.getPickupLatitude());
                s.setLongitude(currentStudent.getPickupLongitude());
                s.setStudentPhone(currentStudent.getPhone());
                s.setStudentName(currentStudent.getName());
                busList.get(i).getStations().add(s);
                Log.e("index", "index: " + index);
                index = i;
                break;
//                }
            }
        }

        Log.e("Add student", "saveStudent: " + busList.get(index).getName());


        HelperMethods.pushInFireBase("Bus", currentStudent.getBusName(), busList.get(index), AddStudentActivity.this, "Please wait", "Loading...");


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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            Log.i("Dataaa", "onActivityResult: " + getRealPathFromURI(filePath));

        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};

        //This method was deprecated in API level 11
        //Cursor cursor = managedQuery(contentUri, proj, null, null, null);

        CursorLoader cursorLoader = new CursorLoader(
                this,
                contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        int column_index =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    @Override
    public void updateUI(DatabaseError databaseError) {

        if (count > 0) {

            if (databaseError == null) {

                Toast.makeText(AddStudentActivity.this, "Added successfully", Toast.LENGTH_LONG).show();
                finish();
            } else {
                databaseError.toException().printStackTrace();
                Log.i("Details", "updateUI: " + databaseError.getDetails());
                Log.i("msg", "updateUI: " + databaseError.getMessage());
                Toast.makeText(AddStudentActivity.this, "error in saving", Toast.LENGTH_SHORT).show();
            }
        }
        count++;
    }

    @Override
    public void updateUI(DataSnapshot data) {
        busNamesList = new ArrayList<>();
        busList = new ArrayList<>();
        for (DataSnapshot currentChild : data.getChildren()) {
            Bus currentBus = currentChild.getValue(Bus.class);
            busNamesList.add(currentBus.getName());
            busList.add(currentBus);
        }
        busesAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, busNamesList);
        busesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        busesSpinner.setAdapter(busesAdapter);

    }
}
