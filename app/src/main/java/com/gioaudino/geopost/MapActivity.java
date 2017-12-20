package com.gioaudino.geopost;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gioaudino.geopost.Entity.User;
import com.gioaudino.geopost.Model.Followed;
import com.gioaudino.geopost.Model.Friends;
import com.gioaudino.geopost.Model.MyPosition;
import com.gioaudino.geopost.Service.Helper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.List;

public class MapActivity extends FriendsActivity implements OnMapReadyCallback {

    private boolean mapReady = false;
    private boolean followedUpdated = false;
    private GoogleMap map;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.updateFollowed();
        this.getMap();

    }

    @Override
    protected void onResume() {
        super.onResume();
        this.updateFollowed();
    }

    private void getMap() {
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map_map);
        mapFragment.getMapAsync(this);
    }

    public void goToList(View view) {
        Intent intent = new Intent(this, ListSplashActivity.class);
        this.startActivity(intent);
    }

    private void updateFollowed() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest followedRequest = new StringRequest(
                Request.Method.GET,
                Helper.buildSimpleUrl(this.getResources().getString(R.string.followed_GET),
                        Helper.getSessionId(this)),
                response -> {
                    Followed f = new Gson().fromJson(response, Followed.class);
                    Friends.getInstance().mergeUsers(f);
                    this.followedUpdated = true;
                    Log.d("FOLLOWED REQUEST", "REQUEST SUCCESSFUL");
                    Log.d("FOLLOWED", Friends.getInstance().toString());
                    this.go();
                },
                error -> Log.e("FOLLOWED REQUEST", "REQUEST FAILED")
        );
        queue.add(followedRequest);
    }

    private void go() {
        Log.d("MAP ACTIVITY", "GOING - Map: " + this.mapReady + " - Followed: " + this.followedUpdated);

        this.snackbar = Snackbar.make(this.findViewById(R.id.map_coordinator), R.string.empty_list, Snackbar.LENGTH_INDEFINITE);
        this.snackbar.setAction("Dismiss", view -> MapActivity.this.snackbar.dismiss());

        if (this.mapReady && this.followedUpdated) {
            this.map.clear();
            List<User> friends = Friends.getInstance().getFriends();
            boolean didSomething = false;
            double minLat = Integer.MAX_VALUE, minLon = Integer.MAX_VALUE, maxLat = Integer.MIN_VALUE, maxLon = Integer.MIN_VALUE;
            if (friends.size() > 0) {
                if (this.snackbar != null)
                    this.snackbar.dismiss();
                for (User friend : friends) {
                    if (null != friend.getLocation()) {
                        didSomething = true;
                        LatLng pos = new LatLng(friend.getLocation().getLatitude(), friend.getLocation().getLongitude());
                        this.map.addMarker(new MarkerOptions().position(pos).title(friend.getUsername() + " | " + friend.getMsg()));
                        minLat = Math.min(friend.getLocation().getLatitude(), minLat);
                        minLon = Math.min(friend.getLocation().getLongitude(), minLon);
                        maxLat = Math.max(friend.getLocation().getLatitude(), maxLat);
                        maxLon = Math.max(friend.getLocation().getLongitude(), maxLon);
                    }
                }
            } else {
                this.snackbar.show();
            }
            if (didSomething) {
                LatLngBounds bounds = new LatLngBounds(new LatLng(minLat, minLon), new LatLng(maxLat, maxLon));
                this.map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0));
            } else
                this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                        MyPosition.getInstance().getLocation().getLatitude(),
                        MyPosition.getInstance().getLocation().getLongitude()), 1));
            followedUpdated = false;
        }
        if (!mapReady)
            this.getMap();

    }

    public void refresh(View view) {
        this.updateFollowed();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mapReady = true;
        this.map = googleMap;
        this.go();
    }

    @Override
    public void addNewFriend(View view) {
        super.addNewFriend(view);
    }

    @Override
    public void goToProfile(View view) {
        super.goToProfile(view);
    }

    @Override
    public void updateStatus(View view) {
        super.updateStatus(view);
    }


}
