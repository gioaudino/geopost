package com.gioaudino.geopost;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.gioaudino.geopost.Service.MyLocationUpdater;
import com.gioaudino.geopost.Service.Values;

/**
 * Created by gioaudino on 11/12/17.
 * Package com.gioaudino.geopost in project GeoPost
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("ACTIVITY" + this.getClass().getSimpleName(), "ON PAUSE");
        if (!(this instanceof MainActivity))
            MyLocationUpdater.getInstance().pauseLocationRequest();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("ACTIVITY" + this.getClass().getSimpleName(), "ON RESUME");
        if (!(this instanceof MainActivity))
            MyLocationUpdater.getInstance().requestLocation(this);
    }
    
    protected void checkLocationPermission() {
        Log.d("BaseActivity", "CHECKING PERMISSION");
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, Values.LOCATION_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        Log.d("BASE ACTIVITY", "PERMISSION RESULT");
        Log.d("BASE ACTIVITY", "Would go back to " + this.getClass().getCanonicalName());
        if (requestCode == Values.LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(this, NoLocationActivity.class);
                String callingActivity = this.getClass().getCanonicalName();
                intent.putExtra(Values.CALLING_ACTIVITY, callingActivity);
                Log.d("PERMISSION DENIED", "Would go back to " + callingActivity);
                this.startActivity(intent);
            }
        }
    }
}
