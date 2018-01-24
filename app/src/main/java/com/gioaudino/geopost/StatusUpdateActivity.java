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
import com.gioaudino.geopost.Model.MyPosition;
import com.gioaudino.geopost.Service.Helper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class StatusUpdateActivity extends BaseActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_update);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.checkLocationPermission();
        getMap();

    }

    public void goToMap(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        this.startActivity(intent);
    }

    public void goToList(View view) {
        Intent intent = new Intent(this, ListSplashActivity.class);
        this.startActivity(intent);
    }

    public void sendUpdate(View view) {
        Helper.closeKeyboard(this);
        performRequest();
    }

    private void performRequest() {
        String status = ((TextView) this.findViewById(R.id.status_update)).getText().toString();
        if (status.length() <= 0) {
            Snackbar.make(this.findViewById(R.id.status_update_coordinator), "You cannot send an empty status update", Snackbar.LENGTH_LONG).show();
            return;
        }
        RequestQueue queue = Volley.newRequestQueue(this);

        this.checkLocationPermission();
        Location location = MyPosition.getInstance().getLocation();
        Log.d("STATUS UPDATE", "POSITION RETRIEVED - " + (location == null ? "null" : location.getLatitude() + " | " + location.getLongitude()));
        MyPosition.getInstance().setLocation(location);
        String url = "BAU";
        try {
            url = Helper.buildUrlToUpdateStatus(
                    getResources().getString(R.string.status_update_GET),
                    Helper.getSessionId(StatusUpdateActivity.this),
                    URLEncoder.encode(status, "UTF-8"),
                    location);
        } catch (UnsupportedEncodingException ignored) {

        }
        Log.d("STATUS UPDATE", "URL: " + url);
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                response -> Snackbar.make(this.findViewById(R.id.status_update_coordinator), "Status updated!", Snackbar.LENGTH_LONG).show(),
                error -> {
                    try {
                        Log.e("STATUS UPDATE", "Something went wrong - " + error.getMessage() + " [" + error.networkResponse.statusCode + "] " + new String(error.networkResponse.data));
                    } catch (NullPointerException e) {
                        Log.e("STATUS UPDATE", "Something went wrong - " + error.getMessage());
                    }
                }
        );
        queue.add(request);


    }

    private void getMap() {
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.update_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.checkLocationPermission();
        Location myPos = MyPosition.getInstance().getLocation();
        LatLng latLng = new LatLng(myPos.getLatitude(), myPos.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(latLng).title("You are here"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        this.findViewById(R.id.button_update).setEnabled(true);
        this.findViewById(R.id.update_progress).setVisibility(View.INVISIBLE);
    }

}