package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hp on 11/5/2017.
 */
public class Bus implements ModelInterface, Comparable<Bus> {
    private String name, supervisorName, supervisorPhone;

    private double centerLatitude, centerLongitude, currentLatitude, currentLongitude;

    ArrayList<Station> stations = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSupervisorName() {
        return supervisorName;
    }

    public void setSupervisorName(String supervisorName) {
        this.supervisorName = supervisorName;
    }

    public String getSupervisorPhone() {
        return supervisorPhone;
    }

    public void setSupervisorPhone(String supervisorPhone) {
        this.supervisorPhone = supervisorPhone;
    }

    public double getCenterLatitude() {
        return centerLatitude;
    }

    public void setCenterLatitude(double centerLatitude) {
        this.centerLatitude = centerLatitude;
    }

    public double getCenterLongitude() {
        return centerLongitude;
    }

    public void setCenterLongitude(double centerLongitude) {
        this.centerLongitude = centerLongitude;
    }

    public double getCurrentLatitude() {
        return currentLatitude;
    }

    public void setCurrentLatitude(double currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public double getCurrentLongitude() {
        return currentLongitude;
    }

    public void setCurrentLongitude(double currentLongitude) {
        this.currentLongitude = currentLongitude;
    }

    public ArrayList<Station> getStations() {
        return stations;
    }

    public void setStations(ArrayList<Station> stations) {
        this.stations = stations;
    }

//    @Override
//    public int compareTo(Bus o) {
////        if (HelperMethods.calculateDistance(Constants.currentGpsTracker.getLongitude(),
////                Constants.currentGpsTracker.getLatitude(), Double.parseDouble(longitude), Double.parseDouble(latitude))
////                > HelperMethods.calculateDistance(Constants.currentGpsTracker.getLongitude(), Constants.currentGpsTracker.getLatitude(),
////                Double.parseDouble(o.longitude), Double.parseDouble(o.latitude))) {
//        if (HelperMethods.calculateDistance(Constants.currentGpsTracker.getLongitude(),
//                Constants.currentGpsTracker.getLatitude(), currentLongitude, currentLatitude)
//                > HelperMethods.calculateDistance(Constants.currentGpsTracker.getLongitude(), Constants.currentGpsTracker.getLatitude(),
//                o.currentLongitude, o.currentLatitude)) {
//            return 1;
//        }
//        return -1;
//    }

    @Override
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("name", name);
        result.put("supervisorName", supervisorName);
        result.put("supervisorPhone", supervisorPhone);
        result.put("centerLatitude", centerLatitude);
        result.put("centerLongitude", centerLongitude);
        result.put("currentLatitude", currentLatitude);
        result.put("currentLongitude", currentLongitude);
        result.put("stations", stations);

        return result;

    }


    @Override
    public int compareTo(Bus o) {

        return getName().compareToIgnoreCase(o.getName());
    }
}
