package model;

import java.util.HashMap;
import java.util.Map;

public class Driver implements ModelInterface, Comparable<Driver> {
    String phone, name, busName;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    @Override
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("phone", phone);
        result.put("name", name);
        result.put("busName", busName);

        return result;
    }

    @Override
    public int compareTo(Driver o) {

        return getName().compareToIgnoreCase(o.getName());
    }
}
