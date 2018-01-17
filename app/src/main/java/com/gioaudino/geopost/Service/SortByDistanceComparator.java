package com.gioaudino.geopost.Service;

import com.gioaudino.geopost.Entity.User;

import java.util.Comparator;

/**
 * Created by gioaudino on 07/12/17.
 * Package com.gioaudino.geopost.Service in project GeoPost
 */

public class SortByDistanceComparator implements Comparator<User> {

    @Override
    public int compare(User u0, User u1) {
        if (u0.getLocation() == u1.getLocation())
            return 0;
        if (null != u0.getLocation() && null != u1.getLocation())
            return (int) (u0.getDistance() - u1.getDistance());
        if (u0.getLocation() != null)
            return -1;
        return 1;
//        return (int) (u0.getDistance() - u1.getDistance());

    }
}
