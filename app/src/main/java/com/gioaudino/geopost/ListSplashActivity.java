package com.gioaudino.geopost;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gioaudino.geopost.Model.Followed;
import com.gioaudino.geopost.Model.Friends;
import com.gioaudino.geopost.Model.MyPosition;
import com.gioaudino.geopost.Service.Helper;
import com.gioaudino.geopost.Service.Values;
import com.google.gson.Gson;

public class ListSplashActivity extends BaseActivity {

    private boolean locationAvailable = false;
    private boolean permissionGranted = false;
    private boolean followedAvailable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("SPLASH ACTIVITY", "OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Log.d("SPLASH ACTIVITY", "GETTING PERMISSION");
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, Values.LOCATION_PERMISSION);
        } else {
            this.permissionGranted = true;
            updateLocation();
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest followedRequest = new StringRequest(
                Request.Method.GET,
                Helper.buildSimpleUrl(this.getResources().getString(R.string.followed_GET),
                        Helper.getSessionId(this)),
                response -> {
                    Followed f = new Gson().fromJson(response, Followed.class);
                    Friends.getInstance().mergeUsers(f);
                    this.followedAvailable = true;
                    Log.d("FOLLOWED REQUEST", "REQUEST SUCCESSFUL");
                    Log.d("FOLLOWED", Friends.getInstance().toString());
                    this.startNewActivity();
                },
                error -> {
                    Log.e("FOLLOWED REQUEST", "REQUEST FAILED");
                }
        );
        queue.add(followedRequest);
    }

    private void startNewActivity() {
        Log.d("SPLASH ACTIVITY", "GOING - location: " + locationAvailable + ", permission: " + permissionGranted + ", followed: " + followedAvailable);
        if (this.locationAvailable && this.permissionGranted && this.followedAvailable) {
            Intent intent = new Intent(this, ListActivity.class);
            this.startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Values.LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                this.permissionGranted = true;
                updateLocation();
                Log.d("LOCATION PERMISSION", "GRANTED");

            } else {
                Log.d("LOCATION PERMISSION", "DENIED");
            }
        }
    }

    private void updateLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            MyPosition.getInstance().getPositionProvider().getLastLocation().addOnSuccessListener(
                    location -> {
                        MyPosition.getInstance().setLocation(location);
                        this.locationAvailable = true;
                        this.startNewActivity();
                    });
    }
}
