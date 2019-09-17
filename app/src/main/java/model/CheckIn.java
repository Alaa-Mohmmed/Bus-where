package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CheckIn implements ModelInterface, Comparable<CheckIn> {
    String studentName, time, type;

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
//        bus,date,endTime,startTime
        result.put("studentName", studentName);
        result.put("time", time);
        result.put("type", type);


        return result;
    }

    @Override
    public int compareTo(CheckIn o) {
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
