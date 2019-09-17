package com.aou.buswhere;

import android.content.Intent;
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
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

import adapter.ViolationsCustomList;
import adapter.reportDetailsTripCustomList;
import model.GetDataInterface;
import model.Trip;
import model.Violation;
import util.HelperMethods;

public class ReportDetailsActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_details);

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
     * A placeholder fragment containing a simple view.
     */
    public static class TripsFragment extends Fragment implements GetDataInterface {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        public TripsFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static TripsFragment newInstance() {
            TripsFragment fragment = new TripsFragment();
            return fragment;
        }

        ListView reportDetailsListView;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_report_details, container, false);
            reportDetailsListView = rootView.findViewById(R.id.reportDetailsListView);

            HelperMethods.getDataFragment(TripsFragment.this, "Trip", "Please wait", "Loading");


            return rootView;
        }

        @Override
        public void updateUI(DataSnapshot data) {
            Log.i("Dataa", "updateUI: " + data.toString());

            ArrayList<Trip> checkInTrips = new ArrayList<>();


            for (DataSnapshot currentChild : data.getChildren()) {
                Trip currentTrip = currentChild.getValue(Trip.class);
                currentTrip.setKey(currentChild.getKey());
                StringTokenizer st = new StringTokenizer(currentTrip.getKey(), ";");
                String busName = st.nextToken();
                currentTrip.setBus(busName);
                StringTokenizer st2 = new StringTokenizer(st.nextToken(), "_");
                currentTrip.setStartTime(st2.nextToken());

                if (currentTrip.getBus().equalsIgnoreCase(HelperMethods.currentBus.getName())) {
                    currentTrip.setKey(currentChild.getKey());
                    checkInTrips.add(currentTrip);
                }

            }

            Collections.sort(checkInTrips);

            reportDetailsTripCustomList tripDetailsParentCustomList;
            tripDetailsParentCustomList = new reportDetailsTripCustomList(getContext(), checkInTrips, android.R.layout.simple_list_item_1);
            tripDetailsParentCustomList.notifyDataSetChanged();
            reportDetailsListView.setAdapter(tripDetailsParentCustomList);

        }

    }


    public static class ViolationFragment extends Fragment implements GetDataInterface {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        public ViolationFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static ViolationFragment newInstance() {
            ViolationFragment fragment = new ViolationFragment();
            return fragment;
        }

        ListView reportDetailsListView;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_report_details, container, false);
            reportDetailsListView = rootView.findViewById(R.id.reportDetailsListView);

            HelperMethods.getDataFragment(ViolationFragment.this, "Violations", "Please wait", "Loading");


            return rootView;
        }

        @Override
        public void updateUI(DataSnapshot data) {
            Log.i("Dataa", "updateUI: " + data.toString());

            ArrayList<Violation> violationArrayList = new ArrayList<>();


            for (DataSnapshot currentChild : data.getChildren()) {
                Violation currentViolation = currentChild.getValue(Violation.class);
                if (currentViolation.getBusName().equalsIgnoreCase(HelperMethods.currentBus.getName())) {
                    violationArrayList.add(currentViolation);
                }

            }

            Collections.sort(violationArrayList);

            ViolationsCustomList tripDetailsParentCustomList;
            tripDetailsParentCustomList = new ViolationsCustomList(getContext(), violationArrayList, android.R.layout.simple_list_item_1);
            tripDetailsParentCustomList.notifyDataSetChanged();
            reportDetailsListView.setAdapter(tripDetailsParentCustomList);

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
            // Return a TripsFragment (defined as a static inner class below).
            switch (position) {

                case 0:
                    return TripsFragment.newInstance();

                default:
                    return ViolationFragment.newInstance();


            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }
    }

    public void map(View v) {
        Violation currentViolation = (Violation) v.getTag();
        Intent i = new Intent(ReportDetailsActivity.this, ViolationLocation.class);
        HelperMethods.currentViolation = currentViolation;
        startActivity(i);
    }
}
