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
//        this.locationCallback = new LocationCallback() {
//            @Override
//            public void onLocationResult(LocationResult locationResult) {
//                super.onLocationResult(locationResult);
//                ProfileActivity.this.lastLocation = locationResult.getLastLocation();
//                new Thread(() -> Friends.getInstance().updateDistance(locationResult.getLastLocation())).run();
//            }
//        };

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(
                Request.Method.GET,
                Helper.buildSimpleUrl(
                        this.getResources().getString(R.string.profile_GET),
                        Helper.getSessionId(this)),
                response -> {
                    this.me = new Gson().fromJson(response, UserFromServer.class).toUser();
                    this.profileAvailable = true;
                    Log.d("PROFILE", "HTTP response: " + response);
                    publishProfile();
                }, error -> {
        });
        queue.add(request);
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.profile_map);
        mapFragment.getMapAsync(this);

    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (this.googleApiClient != null) {
//            LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(this.locationCallback);
//        }
//    }

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
                this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
            }
        }

    }

    public void goToMap(View view) {
        Snackbar.make(findViewById(R.id.profile_coordinator), "Something happening here", Snackbar.LENGTH_SHORT).show();
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

//    protected synchronized void buildGoogleApiClient() {
//        this.googleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();
//        this.googleApiClient.connect();
//    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        this.mapAvailable = true;
        this.publishProfile();
    }

//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//        this.locationRequest = new LocationRequest();
//        this.locationRequest.setInterval(1000);
//        this.locationRequest.setFastestInterval(1000);
//        this.locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//            LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(this.locationRequest, this.locationCallback, null);
//        }
//    }

//    @Override
//    public void onConnectionSuspended(int i) {
//        Log.e("PROFILE ACTIVITY", "Connection suspended (?)");
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//        this.lastLocation = location;
//
//        new Thread(() -> Friends.getInstance().updateDistance(location)).run();
//
//        if (myPos != null) myPos.remove();
//        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(latLng);
//        markerOptions.title("Current Position");
//        this.myPos = this.map.addMarker(markerOptions);
//
//        this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//        Log.e("PROFILE ACTIVITY", "Connection failed (?)");
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case Values.LOCATION_PERMISSION: {
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    if (ContextCompat.checkSelfPermission(this,
//                            Manifest.permission.ACCESS_FINE_LOCATION)
//                            == PackageManager.PERMISSION_GRANTED) {
////
////                        if (this.googleApiClient == null) {
////                            buildGoogleApiClient();
////                        }
////                        this.map.setMyLocationEnabled(true);
//                    }
//
//                } else {
//                    Log.d("PROFILE ACTIVITY", "PERMISSION DENIED");
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//
//                }
//            }
//        }
//    }
}
