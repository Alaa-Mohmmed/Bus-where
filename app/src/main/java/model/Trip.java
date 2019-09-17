package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Trip implements ModelInterface, Comparable<Trip> {

    private String bus, date, endTime, startTime, key;

    ArrayList<CheckIn> checkIn = new ArrayList<>();

    public Trip() {
    }

    public String getBus() {
        return bus;
    }

    public void setBus(String bus) {
        this.bus = bus;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public ArrayList<CheckIn> getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(ArrayList<CheckIn> checkIn) {
        this.checkIn = checkIn;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
//        bus,date,endTime,startTime
//        result.put("bus", bus);
        result.put("date", date);
        result.put("endTime", endTime);
//        result.put("startTime", startTime);
        result.put("checkIn", checkIn);


        return result;
    }

    @Override
    public int compareTo(Trip o) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        try {
            Date currentTime = formatter.parse(startTime);
            Date otherTime = formatter.parse(o.startTime);

            if (currentTime.before(otherTime)) {
                return 1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;

    }
}
