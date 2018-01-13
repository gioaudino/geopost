package com.gioaudino.geopost;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gioaudino.geopost.Entity.User;
import com.gioaudino.geopost.Entity.UserFromServer;
import com.gioaudino.geopost.Service.Helper;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

public class ProfileActivity extends BaseActivity implements OnMapReadyCallback {

    private User me;
    private GoogleMap map;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Location lastLocation;
    private Marker myPos;

    private boolean profileAvailable = false;
    private boolean mapAvailable = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_profile);

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(
                Request.Method.GET,
                Helper.buildSimpleUrl(
                        this.getResources().getString(R.string.profile_GET),
                        Helper.getSessionId(this)),
                response -> {
                    UserFromServer usf = new Gson().fromJson(response, UserFromServer.class);
                    Log.d("PROFILE", "HTTP response: " + response);
                    Log.d("PROFILE", "USF: " + usf);
                    this.me = usf.toUser();
                    this.profileAvailable = true;
                    publishProfile();
                }, error -> {
        });
        queue.add(request);
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.profile_map);
        mapFragment.getMapAsync(this);

    }

    private void publishProfile() {
        Log.d("PROFILE ACTIVITY", "PUBLISH PROFILE");
        if (this.mapAvailable && this.profileAvailable) {
            Log.d("PROFILE ACTIVITY", "PUBLISHING PROFILE");
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            Log.d("PROFILE ACTIVITY", me == null ? "null" : this.me.getUsername());
            Log.d("PROFILE ACTIVITY", me.getLocation() != null ? (me.getLocation().getLatitude() + " | " + me.getLocation().getLongitude()) : "WAIT... WHAT??");


            TextView username = this.findViewById(R.id.profile_username);
            TextView message = this.findViewById(R.id.profile_message);

            username.setText(this.me.getUsername());
            message.setText(this.me.getMsg() == null || this.me.getMsg().length() == 0 ? " - " : this.me.getMsg());

            if (this.me.getLocation() != null) {
                LatLng latLng = new LatLng(me.getLocation().getLatitude(), me.getLocation().getLongitude());
                this.map.addMarker(new MarkerOptions().position(latLng).title("Last known position"));
                this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
            }
        }

    }

    public void goToMap(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        this.startActivity(intent);
    }

    public void goToList(View view) {
        Intent intent = new Intent(this, ListSplashActivity.class);
        this.startActivity(intent);
    }

    public void logout(View view) {
        Helper.deleteData(this);
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        this.mapAvailable = true;
        this.publishProfile();
    }

}
