package com.gioaudino.geopost.Entity;

import android.location.Location;

/**
 * Created by gioaudino on 13/11/17.
 */

public class User {

    private String username;

    private String msg;

    private Location location;

    private float distance;

    public User() {
    }

    public User(String username, String msg, Location location) {
        this.username = username;
        this.msg = msg;
        this.location = location;
    }

    public User(String username, String msg, double lat, double lng) {
        this.username = username;
        this.msg = msg;
        Location loc = new Location("");
        loc.setLatitude(lat);
        loc.setLongitude(lng);
        this.location = loc;
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(double lat, double lng) {
        Location loc = new Location("");
        loc.setLatitude(lat);
        loc.setLongitude(lng);
        this.location = loc;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public void setDistance(Location location) {
        if(null != location && null != this.location)
            this.distance = location.distanceTo(this.location);
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
                ", location=" + location +
                ", distance=" + distance +
                '}';
    }
}
