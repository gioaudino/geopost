package com.gioaudino.geopost;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.gioaudino.geopost.Service.Values;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class StatusUpdateActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_update);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, Values.LOCATION_PERMISSION);
        else
            performRequest();

    }

    private void performRequest() {
        String status = ((TextView) this.findViewById(R.id.status_update)).getText().toString();
        if (status.length() <= 0) {
            Snackbar.make(this.findViewById(R.id.status_update_coordinator), "You cannot send an empty status update", Snackbar.LENGTH_LONG).show();
            return;
        }
        RequestQueue queue = Volley.newRequestQueue(this);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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

        } else {
            Snackbar.make(findViewById(R.id.status_update_coordinator), "You cannot update your status without granting permissions", Snackbar.LENGTH_SHORT).setAction("Give permission", view -> {
                if (ContextCompat.checkSelfPermission(StatusUpdateActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    ActivityCompat.requestPermissions(StatusUpdateActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, Values.LOCATION_PERMISSION);
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Values.LOCATION_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            performRequest();
        else Log.e("STATUS UPDATE", "OH COME ON");
    }
}