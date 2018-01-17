package com.gioaudino.geopost.Entity;

import com.gioaudino.geopost.Model.MyPosition;

/**
 * Created by gioaudino on 07/12/17.
 * Package com.gioaudino.geopost.Entity in project GeoPost
 */

public class UserFromServer {
    private String username;
    private String msg;
    private Double lat;
    private Double lon;

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

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public User toUser() {
        User u = new User();
        u.setUsername(this.username);
        if (null != this.msg && this.msg.length() > 0)
            u.setMsg(this.msg);
        if (null != this.lat && null != this.lon) {
            u.setLocation(this.lat, this.lon);
            u.setDistance(MyPosition.getInstance().getLocation());
        }
        return u;
    }

    @Override
    public String toString() {
        return "UserFromServer{" +
                "username='" + username + '\'' +
                ", msg='" + msg + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}
