package com.gioaudino.geopost;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gioaudino.geopost.Model.Followed;
import com.gioaudino.geopost.Model.Friends;
import com.gioaudino.geopost.Service.Helper;
import com.google.gson.Gson;

public class MapSplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_splash);


    }

    private void startNewActivity() {
        Log.d("SPLASH ACTIVITY", "GOING");
        Intent intent = new Intent(this, MapActivity.class);
        this.startActivity(intent);

    }
}
