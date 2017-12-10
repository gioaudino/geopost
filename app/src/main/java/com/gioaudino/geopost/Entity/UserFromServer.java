package com.gioaudino.geopost.Entity;

import android.location.Location;

import com.gioaudino.geopost.Model.MyPosition;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by gioaudino on 07/12/17.
 * Package com.gioaudino.geopost.Entity in project GeoPost
 */

public class UserFromServer {
    private String username;
    private String msg;
    private Double lat;
    private Double lng;

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

    public User toUser() {
        User u = new User();
        u.setUsername(this.username);
        if (null != this.msg && this.msg.length() > 0)
            u.setMsg(this.msg);
        if (null != this.lat && null != this.lng) {
            u.setLastPosition(new LatLng(this.lat, this.lng));
            Location loc = new Location("");
            loc.setLatitude(this.lat);
            loc.setLongitude(this.lng);
            u.setDistance(MyPosition.getInstance().getLocation().distanceTo(loc));
        }
        return u;
    }
}
