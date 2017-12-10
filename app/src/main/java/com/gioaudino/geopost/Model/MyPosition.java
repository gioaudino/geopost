package com.gioaudino.geopost.Model;

import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;

/**
 * Created by gioaudino on 10/12/17.
 * Package com.gioaudino.geopost.Model in project GeoPost
 */

public class MyPosition {

    private static MyPosition instance = new MyPosition();

    public static MyPosition getInstance() {
        return instance;
    }

    private MyPosition() {
    }

    private Location location;
    private FusedLocationProviderClient positionProvider;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public FusedLocationProviderClient getPositionProvider() {
        return positionProvider;
    }

    public void setPositionProvider(FusedLocationProviderClient positionProvider) {
        this.positionProvider = positionProvider;
    }

}
