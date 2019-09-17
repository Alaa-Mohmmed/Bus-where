package com.aou.buswhere;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

import adapter.TripDetailsParentCustomList;
import model.Bus;
import model.CheckIn;
import model.GetDataInterface;
import model.Trip;
import services.NotificationService;
import tracking.GPSTracker;
import util.HelperMethods;

public class ParentHomeScreen extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    String mail = HelperMethods.currentStudent.getEmail().replace(".", "");
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_home_screen);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase = mDatabase.child("Student").child(mail).child("pickupLatitude");
        i = new Intent(ParentHomeScreen.this, NotificationService.class);
        startService(i);

        if (HelperMethods.currentStudent.getPickupLatitude() == 0) {

            AlertDialog.Builder builder = new AlertDialog.Builder(ParentHomeScreen.this);
            builder.setTitle("Do you want to save your location as pickup point");
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setCancelable(true);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {


                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    GPSTracker gpsTracker = new GPSTracker(getApplicationContext());
                    if (gpsTracker.canGetLocation() && gpsTracker.getLongitude() != 0) {
                        HelperMethods.pushInFireBaseService("Student", mail, "pickupLatitude", gpsTracker.getLatitude());
                        HelperMethods.pushInFireBaseService("Student", mail, "pickupLongitude", gpsTracker.getLongitude());


                    } else {
                        Toast.makeText(getApplicationContext(), "Please enable GPS", Toast.LENGTH_SHORT).show();
                    }


                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {


                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();


                }
            });
            AlertDialog alert = builder.create();
            alert.show();

        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


    }


    /**
     * A TripFragment fragment containing a simple view.
     */
    public static class TripFragment extends Fragment implements GetDataInterface {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        ListView tripDetailsListView;

        public TripFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static TripFragment newInstance() {
            TripFragment fragment = new TripFragment();
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_parent_trip_info, container, false);
            tripDetailsListView = (ListView) rootView.findViewById(R.id.tripDetailsListView);
            HelperMethods.getDataFragment(TripFragment.this, "Trip", "Please wait", "Loading");


            return rootView;
        }

        @Override
        public void updateUI(DataSnapshot data) {
            Log.i("Dataa", "updateUI: " + data.toString());

            ArrayList<CheckIn> checkInTrips = new ArrayList<>();


            for (DataSnapshot currentChild : data.getChildren()) {
                Trip currentTrip = currentChild.getValue(Trip.class);
                currentTrip.setKey(currentChild.getKey());
                StringTokenizer st = new StringTokenizer(currentTrip.getKey(), ";");
                String busName = st.nextToken();
                currentTrip.setBus(busName);
                StringTokenizer st2 = new StringTokenizer(st.nextToken(), "_");
                currentTrip.setStartTime(st2.nextToken());

                for (int i = 0; i < currentTrip.getCheckIn().size(); i++) {

                    if (currentTrip.getCheckIn().get(i).getStudentName().equalsIgnoreCase(HelperMethods.currentStudent.getName())) {
                        checkInTrips.add(currentTrip.getCheckIn().get(i));
                    }
                }
            }

            Collections.sort(checkInTrips);

            TripDetailsParentCustomList tripDetailsParentCustomList;
            tripDetailsParentCustomList = new TripDetailsParentCustomList(getContext(), checkInTrips, android.R.layout.simple_list_item_1);
            tripDetailsParentCustomList.notifyDataSetChanged();
            tripDetailsListView.setAdapter(tripDetailsParentCustomList);

        }

    }


    /**
     * A TripFragment fragment containing a simple view.
     */
    public static class BusFragment extends Fragment implements GetDataInterface {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        TextView busName, driverName;
        ImageButton mapsImgBtn;
        ImageButton callBusBtn, callSchoolBtn;


        public BusFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static BusFragment newInstance() {
            BusFragment fragment = new BusFragment();
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_parent_bus_info, container, false);
            busName = rootView.findViewById(R.id.busNameTextView);
            driverName = rootView.findViewById(R.id.driverNameTextView);

            mapsImgBtn = rootView.findViewById(R.id.mapsImgBtn);

            callBusBtn = rootView.findViewById(R.id.callBusBtn);
            callSchoolBtn = rootView.findViewById(R.id.callSchoolBtn);

            HelperMethods.getDataFragment(BusFragment.this, "Bus", HelperMethods.currentStudent.getBusName(), "Please wait", "Loading");


            return rootView;
        }

        @Override
        public void updateUI(DataSnapshot data) {
            Log.i("Dataa", "updateUI: " + data.toString());


            final Bus currentBus = data.getValue(Bus.class);
            HelperMethods.currentBus = currentBus;

            busName.setText("Bus name: " + currentBus.getName());
            driverName.setText("Supervisor name: " + currentBus.getSupervisorName());

            callBusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + currentBus.getSupervisorPhone()));
                    startActivity(callIntent);
                }
            });

            callSchoolBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:01155951324"));
                    startActivity(callIntent);
                }
            });

            mapsImgBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), MapsActivity.class);
                    startActivity(i);
                }
            });

        }

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a TripFragment (defined as a static inner class below).
            if (position == 0) {
                return TripFragment.newInstance();
            } else {
                return BusFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
    }


}
