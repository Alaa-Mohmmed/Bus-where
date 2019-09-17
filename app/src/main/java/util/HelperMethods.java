package util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.aou.buswhere.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.AddInterface;
import model.Admin;
import model.Bus;
import model.Driver;
import model.GetDataInterface;
import model.ModelInterface;
import model.Student;
import model.Supervisor;
import model.Trip;
import model.Violation;


/**
 * Created by Yehia on 15/01/2016.
 */
public class HelperMethods {


    public static Student currentStudent = new Student();
    public static Admin currentAdmin = new Admin();
    public static Supervisor currentSupervisor = new Supervisor();
    public static Trip currentTrip = new Trip();
    public static Violation currentViolation = new Violation();


    public static String checkInKey = "";

    public static LatLng searchLocation = new LatLng(0, 0);


    public static Bus currentBus = new Bus();
    public static Driver currentDriver = new Driver();


    public static String loginType = "";
    public static boolean isStarted = false;
//    public static boolean notificationBoolean = false;


    // For admin
    public static ArrayList<Student> studentsList = new ArrayList<>();
    public static ArrayList<Driver> driversList = new ArrayList<>();
    public static ArrayList<Bus> busesList = new ArrayList<>();
    public static ArrayList<Supervisor> supervisorList = new ArrayList<>();


    private static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private static ProgressDialog blg;

    public static void showDialog(Activity currentActivity, String title, String msg) {
        blg = new ProgressDialog(currentActivity);
        blg.setTitle(title);
        blg.setMessage(msg);
        blg.show();

    }

    public static void hideDialog(Activity currentActivity) {
        blg.cancel();
    }

    public static void putToolTip(final Activity activity, String seatNumber) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Seat Number");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Seat Number : " + seatNumber);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {


            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void saveInFireBase(Object o, String childName1, String childName2) {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase = mDatabase.child(childName1).child(childName2);
        mDatabase.setValue(o);
    }

//    public static void pushInFireBase(Object o, String childName, final Activity ac, String title, String msg, final boolean showToast) {
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//        mDatabase = mDatabase.child(childName);
//        HelperMethods.showDialog(ac, title, msg);
//        mDatabase.push().setValue(o, new DatabaseReference.CompletionListener() {
//            @Override
//            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//                HelperMethods.hideDialog(ac);
//
//                if (databaseError == null) {
//                    if (showToast) {
//                        Toast.makeText(ac, "Added successfully", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(ac, "Saving error", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });
//    }


    public static void pushInFireBase(String childName, String childName1, String childName2, ModelInterface currentObject, final AddInterface ac, String msg, String title) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String key = mDatabase.child(childName).push().getKey();
        Map<String, Object> postValues = currentObject.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + childName + "/" + childName1 + "/" + childName2, postValues);
        //mDatabase.updateChildren(childUpdates);
        HelperMethods.showDialog((Activity) ac, title, msg);

        mDatabase.updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                HelperMethods.hideDialog((Activity) ac);
                ac.updateUI(databaseError);

            }
        });
    }


    public static void pushInFireBase(String childName, ModelInterface currentObject, final AddInterface ac, String id) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String key = mDatabase.child(childName).push().getKey();
        Map<String, Object> postValues = currentObject.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + childName + "/" + id, postValues);
        //mDatabase.updateChildren(childUpdates);

        mDatabase.updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//                ac.updateUI(databaseError);

            }
        });
    }

    public static void pushInFireBase(String childName, ModelInterface currentObject, final AddInterface ac, String msg, String title, String id) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String key = mDatabase.child(childName).push().getKey();
        Map<String, Object> postValues = currentObject.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + childName + "/" + id, postValues);
        //mDatabase.updateChildren(childUpdates);
        HelperMethods.showDialog((Activity) ac, title, msg);

        mDatabase.updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                HelperMethods.hideDialog((Activity) ac);
                ac.updateUI(databaseError);

            }
        });
    }


    public static void pushInFireBase(String childName1, String childName2, ModelInterface currentObject, final AddInterface ac, String msg, String title) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> postValues = currentObject.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + childName1 + "/" + childName2 + "/", postValues);
        //mDatabase.updateChildren(childUpdates);
        HelperMethods.showDialog((Activity) ac, title, msg);

        mDatabase.updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                HelperMethods.hideDialog((Activity) ac);
                ac.updateUI(databaseError);

            }
        });
    }

    public static void getData(final GetDataInterface currentActivity, String childName, String childName1, String title, String msg) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        HelperMethods.showDialog((Activity) currentActivity, title, msg);
        mDatabase.child(childName).child(childName1).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        Student user = dataSnapshot.getValue(Student.class);
                        HelperMethods.hideDialog((Activity) currentActivity);
                        currentActivity.updateUI(dataSnapshot);

                        // ...
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("error", "getUser:onCancelled", databaseError.toException());
                        // ...
                    }
                });
    }

    public static void getDataFragmentActivity(final GetDataInterface currentActivity, String childName, String childName1, String title, String msg) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        HelperMethods.showDialog((FragmentActivity) currentActivity, title, msg);
        mDatabase.child(childName).child(childName1).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        Student user = dataSnapshot.getValue(Student.class);
                        HelperMethods.hideDialog((FragmentActivity) currentActivity);
                        currentActivity.updateUI(dataSnapshot);

                        // ...
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("error", "getUser:onCancelled", databaseError.toException());
                        // ...
                    }
                });
    }

    public static void getDataFragmentActivity(final GetDataInterface currentActivity, String childName, String title, String msg) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        HelperMethods.showDialog((FragmentActivity) currentActivity, title, msg);
        mDatabase.child(childName).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        Student user = dataSnapshot.getValue(Student.class);
                        HelperMethods.hideDialog((FragmentActivity) currentActivity);
                        currentActivity.updateUI(dataSnapshot);

                        // ...
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("error", "getUser:onCancelled", databaseError.toException());
                        // ...
                    }
                });
    }

    public static void getData(final GetDataInterface currentActivity, String childName, String title, String msg) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        HelperMethods.showDialog((Activity) currentActivity, title, msg);
        mDatabase.child(childName).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        Student user = dataSnapshot.getValue(Student.class);
                        HelperMethods.hideDialog((Activity) currentActivity);
                        currentActivity.updateUI(dataSnapshot);

                        // ...
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("error", "getUser:onCancelled", databaseError.toException());
                        // ...
                    }
                });
    }


    public static void getDataFragment(final GetDataInterface currentActivity, String childName, String childName1, String title, String msg) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        HelperMethods.showDialog(((Fragment) currentActivity).getActivity(), title, msg);
        mDatabase.child(childName).child(childName1).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        HelperMethods.hideDialog(((Fragment) currentActivity).getActivity());
                        currentActivity.updateUI(dataSnapshot);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("error", "getUser:onCancelled", databaseError.toException());
                    }
                });
    }

    public static void getDataFragment(final GetDataInterface currentActivity, String childName, String title, String msg) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
//        HelperMethods.showDialog(((Fragment) currentActivity).getActivity(), title, msg);
        mDatabase.child(childName).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        HelperMethods.hideDialog(((Fragment) currentActivity).getActivity());
                        currentActivity.updateUI(dataSnapshot);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("error", "getUser:onCancelled", databaseError.toException());
                    }
                });
    }


    public static void getData(final GetDataInterface currentActivity, String childName, String childName1, String childName2, String title, String msg) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        HelperMethods.showDialog((Activity) currentActivity, title, msg);
        mDatabase.child(childName).child(childName1).child(childName2).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        Student user = dataSnapshot.getValue(Student.class);
                        HelperMethods.hideDialog((Activity) currentActivity);
                        currentActivity.updateUI(dataSnapshot);

                        // ...
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("error", "getUser:onCancelled", databaseError.toException());
                        // ...
                    }
                });
    }

    public static void getDataService(final GetDataInterface currentActivity, String childName, String childName1) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(childName).child(childName1).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        currentActivity.updateUI(dataSnapshot);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("error", "getUser:onCancelled", databaseError.toException());
                    }
                });
    }

    public static void getDataServiceBusLocation(final GetDataInterface currentActivity, String childName, String childName1) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(childName).child(childName1).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        currentActivity.updateUI(dataSnapshot);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("error", "getUser:onCancelled", databaseError.toException());
                    }
                });
    }

    public static double calculateDistance(double srcLong, double srcLat, double distLong, double distLat) {
        double x1 = Math.toRadians(srcLong);
        double y1 = Math.toRadians(srcLat);
        System.out.print("Enter latitude and longitude for the second location: ");
        double x2 = Math.toRadians(distLong);
        double y2 = Math.toRadians(distLat);

        double sec1 = Math.sin(x1) * Math.sin(x2);
        double dl = Math.abs(y1 - y2);
        double sec2 = Math.cos(x1) * Math.cos(x2);
        //sec1,sec2,dl are in degree, need to convert to radians
        double centralAngle = Math.acos(sec1 + sec2 * Math.cos(dl));
        //Radius of Earth: 6378.1 kilometers
        double distance = centralAngle * 6378.1;
        System.out.println("The distance is " + distance + " kilometers.");
        return distance;
    }

    public static void pushInFireBaseService(String childName1, String childName2, String childName3, double value) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase = mDatabase.child(childName1).child(childName2).child(childName3);
        mDatabase.setValue(value);
    }

    public static void pushInFireBaseService(String childName1, ModelInterface value) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase = mDatabase.child(childName1);
        mDatabase.push().setValue(value);
    }

    public static void deleteFromFirebase(String child1, String child2, final Activity ac, String title, String msg) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        HelperMethods.showDialog(ac, title, msg);

        mDatabase.child(child1).child(child2).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                HelperMethods.hideDialog((Activity) ac);
                Toast.makeText(ac, "Deleted successfully", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                HelperMethods.hideDialog((Activity) ac);

                Toast.makeText((Activity) ac, "Error in saving", Toast.LENGTH_SHORT).show();
            }
        });


    }


}
