package com.gioaudino.geopost.Entity;

/**
 * Created by gioaudino on 07/12/17.
 * Package com.gioaudino.geopost.Entity in project GeoPost
 */

public class UserFromServer {
    private String username;
    private String msg;
    private double lat;
    private double lng;

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

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
