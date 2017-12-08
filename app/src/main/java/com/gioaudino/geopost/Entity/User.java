package com.gioaudino.geopost.Entity;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by gioaudino on 13/11/17.
 */

public class User {

    private String username;

    private String msg;

    private LatLng lastPosition;

    public User(String username, String msg, LatLng lastPosition) {
        this.username = username;
        this.msg = msg;
        this.lastPosition = lastPosition;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public LatLng getLastPosition() {
        return lastPosition;
    }

    public void setLastPosition(LatLng lastPosition) {
        this.lastPosition = lastPosition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", msg='" + msg + '\'' +
                ", lastPosition=" + lastPosition +
                '}';
    }
}
