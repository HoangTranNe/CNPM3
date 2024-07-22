package com.example.dating_app.UserObject;

import java.io.Serializable;

public class UserObject implements  Serializable{
    private String uid, name, phone, noficationKey;
    private Boolean selected  = false;

    public UserObject(String uid) {
        this.uid = uid;
    }

    public UserObject(String name, String phone, String noficationKey) {
        this.name = name;
        this.phone = phone;
        this.noficationKey = noficationKey;
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

    public String getNoficationKey() {
        return noficationKey;
    }

    public void setNoficationKey(String noficationKey) {
        this.noficationKey = noficationKey;
    }
}
