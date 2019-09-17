package model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yehia on 3/22/2017.
 */
public class Student implements ModelInterface, Comparable<Student> {
    private String email, password, busName, imgUrl, name, phone, address;

    private double pickupLatitude, pickupLongitude;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getPickupLatitude() {
        return pickupLatitude;
    }

    public void setPickupLatitude(double pickupLatitude) {
        this.pickupLatitude = pickupLatitude;
    }

    public double getPickupLongitude() {
        return pickupLongitude;
    }

    public void setPickupLongitude(double pickupLongitude) {
        this.pickupLongitude = pickupLongitude;
    }

    @Override
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("email", email);
        result.put("password", password);
        result.put("phone", phone);
        result.put("busName", busName);
        result.put("imgUrl", imgUrl);
        result.put("name", name);
        result.put("address", address);
        result.put("pickupLatitude", pickupLatitude);
        result.put("pickupLongitude", pickupLongitude);

        return result;
    }

    @Override
    public int compareTo(Student o) {

        return getName().compareToIgnoreCase(o.getName());
    }
}
