package com.gioaudino.geopost.Model;

import android.location.Location;
import android.util.Log;

import com.gioaudino.geopost.Entity.User;
import com.gioaudino.geopost.Entity.UserFromServer;
import com.gioaudino.geopost.Service.Helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by gioaudino on 13/11/17.
 * Package com.gioaudino.geopost.Model in project GeoPost
 */

public class Friends {
    private static final Friends ourInstance = new Friends();

    private Map<String, User> friends = new HashMap<>();

    public static Friends getInstance() {
        return ourInstance;
    }

    private Friends() {
        friends = new TreeMap<>();
    }

    public List<User> getFriends() {
        return new ArrayList<>(friends.values());
    }

    public Map<String, User> mergeUsers(List<User> users) {
        if (this.friends.size() == 0) {
            this.friends = new HashMap<>();
        }
        for (User user : users)
            if (this.friends.containsKey(user.getUsername()))
                this.friends.replace(user.getUsername(), user);
            else
                this.friends.put(user.getUsername(), user);
        return friends;
    }

    public Map<String, User> mergeUsers(Followed followed) {
        if (this.friends.size() == 0) {
            this.friends = new HashMap<>();
        }
        for (UserFromServer user : followed.getFollowed()) {
            User u = user.toUser();
            if (this.friends.containsKey(user.getUsername()))
                this.friends.replace(user.getUsername(), u);
            else
                this.friends.put(user.getUsername(), u);
        }
        return friends;
    }

    public void updateDistances(Location location) {
        this.friends.forEach((key, value) -> {
            Log.d("DISTANCE UPDATE", value.getUsername() + " | " + Helper.formatDistance(value.getLocation().distanceTo(location)));
            value.setDistance(location);
        });
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        for (User friend : friends.values()) {
            sb.append(friend);
            sb.append('\n');
        }
        sb.append("\n\t}");
        return sb.toString();
    }

    public int size() {
        return this.friends.size();
    }
}
