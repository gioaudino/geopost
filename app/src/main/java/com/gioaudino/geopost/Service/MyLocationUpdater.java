package com.gioaudino.geopost.Service;

import android.app.Activity;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.gioaudino.geopost.Model.MyPosition;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;

/**
 * Created by gioaudino on 16/12/17.
 * Package com.gioaudino.geopost.Service in project GeoPost
 */

public class MyLocationUpdater {
    private static MyLocationUpdater instance = new MyLocationUpdater();

    public static MyLocationUpdater getInstance() {
        return instance;
    }

    private MyLocationUpdater() {
    }

    private static final int INTERVAL = 10000;
    private static final int FASTEST_INTERVAL = 5000;
    private static final int PRIORITY = LocationRequest.PRIORITY_HIGH_ACCURACY;

    private LocationRequest locationRequest;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;

    public void setup(Activity activity) {
        Log.d("LOCATION UPDATER", "SET UP");
        this.fusedLocationClient = new FusedLocationProviderClient(activity);
        this.locationRequest = new LocationRequest();
        this.locationRequest.setInterval(INTERVAL).setFastestInterval(FASTEST_INTERVAL).setPriority(PRIORITY);
        this.locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                MyPosition.getInstance().setLocation(locationResult.getLastLocation());
                Log.d("LOCATION UPDATER", "LOCATION UPDATED - " + locationResult.getLastLocation().getLatitude() + " | " + locationResult.getLastLocation().getLongitude());
            }
        };

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(this.locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(activity);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(activity, locationSettingsResponse -> this.requestLocation(activity));
        task.addOnFailureListener(activity, e -> {
            Log.e("LOCATION UPDATER ERROR", e.getClass().getSimpleName() + " | " + e.getMessage());
            if (e instanceof ResolvableApiException) {
                Log.e("LOCATION UPDATER STARTUP", "CANNOT ACCESS LOCATION - TURN IT ON");
                try {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(activity,
                            7);
                } catch (IntentSender.SendIntentException ignored) {

                }
            }
        });
    }

    public void requestLocation(Activity activity) {
        Log.d("LOCATION UPDATER", "REQUESTING LOCATION");
        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            this.fusedLocationClient.requestLocationUpdates(this.locationRequest, this.locationCallback, null);
        }
//        else ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, Values.LOCATION_PERMISSION);
    }

    public void pauseLocationRequest() {
        Log.d("LOCATION UPDATER", "LOCATION REQUEST PAUSED");
        this.fusedLocationClient.removeLocationUpdates(this.locationCallback);
    }
}
