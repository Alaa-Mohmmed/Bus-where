package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Violation implements ModelInterface, Comparable<Violation> {

    private String busName, time;
    private double latitude, longitude, speed;

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    @Override
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();


        result.put("busName", busName);
        result.put("time", time);
        result.put("latitude", latitude);
        result.put("longitude", longitude);
        result.put("speed", speed);

        return result;
    }

    @Override
    public int compareTo(Violation o) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        try {
            Date currentTime = formatter.parse(time);
            Date otherTime = formatter.parse(o.time);

            if (currentTime.before(otherTime)) {
                return 1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;

    }
}