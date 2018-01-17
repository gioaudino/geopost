package com.gioaudino.geopost;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.gioaudino.geopost.Service.Values;

public class NoLocationActivity extends BaseActivity {

    private String parentActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_location);

        this.parentActivity = this.getIntent().getStringExtra(Values.CALLING_ACTIVITY);
        Log.d("NoLocationActivity", "Parent Activity: " + this.parentActivity);
    }

    public void checkPermission(View view) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, Values.LOCATION_PERMISSION);
        } else goBack();
    }

    private void goBack() {
        Class activity;
        try {
            activity = Class.forName(this.parentActivity);
        } catch (ClassNotFoundException e) {
            activity = MainActivity.class;
            Log.e("NoLocationActivity", "ClassNotFoundException: " + e.getMessage());
        }
        Log.d("NoLocationActivity", "Will go back to " + activity.getSimpleName());
        Intent intent = new Intent(this, activity);
        this.startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == Values.LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                goBack();
            }
        }
    }
}
