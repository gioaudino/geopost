package com.gioaudino.geopost;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gioaudino.geopost.Service.Helper;
import com.gioaudino.geopost.Service.Values;

/**
 * Created by gioaudino on 11/12/17.
 * Package com.gioaudino.geopost in project GeoPost
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("DESTRUCTION - " + this.getClass().getCanonicalName(), "IN PROGRESS");
        SharedPreferences preferences = this.getSharedPreferences(Values.PREFERENCES_NAME, Context.MODE_PRIVATE);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(
                Request.Method.GET,
                Helper.buildSimpleUrl(
                        this.getResources().getString(R.string.logout_GET),
                        preferences.getString(Values.SESSION_ID, null)),
                response -> {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.apply();
                    Log.d("DESTRUCTION", "COMPLETED");
                },
                error -> {
                    Log.e("DESTRUCTION", "Something went wrong with the HTTP logout call");
                }
        );
        queue.add(request);
    }
}
