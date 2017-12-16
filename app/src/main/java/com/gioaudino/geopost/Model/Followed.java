package com.gioaudino.geopost.Model;

import com.gioaudino.geopost.Entity.User;
import com.gioaudino.geopost.Entity.UserFromServer;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gioaudino on 07/12/17.
 * Package com.gioaudino.geopost.Model in project GeoPost
 */

public class Followed {
    private List<UserFromServer> followed = new ArrayList<>();

    public List<UserFromServer> getFollowed() {
        return followed;
    }

    public List<User> toUsers() {
        return toUsers(new ArrayList<>());
    }

    public List<User> toUsers(List<User> result) {
        for (UserFromServer user : followed) {
            User u = new User(user.getUsername(), user.getMsg(), user.getLat(), user.getLng());
            result.add(u);
        }
        return result;
    }
    
    
}
