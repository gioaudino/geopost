package com.gioaudino.geopost;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.gioaudino.geopost.Service.MyLocationUpdater;

/**
 * Created by gioaudino on 11/12/17.
 * Package com.gioaudino.geopost in project GeoPost
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();
//        Log.d("ACTIVITY" + this.getClass().getSimpleName(), "ON START");
//        if (!(this instanceof MainActivity))
//            MyLocationUpdater.getInstance().requestLocation(this);
    }

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
}
