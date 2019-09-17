package model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yehia on 3/22/2017.
 */
public class Supervisor implements ModelInterface, Comparable<Supervisor> {
    private String email, password, name, phone, busName;


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

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    @Override
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("email", email);
        result.put("password", password);
        result.put("phone", phone);
        result.put("name", name);
        result.put("busName", busName);

        return result;
    }


    @Override
    public int compareTo(Supervisor o) {

        return getName().compareToIgnoreCase(o.getName());
    }


}
