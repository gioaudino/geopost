package com.gioaudino.geopost;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.gioaudino.geopost.Model.Followed;
import com.gioaudino.geopost.Model.Friends;
import com.gioaudino.geopost.Model.MyPosition;
import com.gioaudino.geopost.Service.Helper;
import com.gioaudino.geopost.Service.Values;
import com.google.gson.Gson;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class FeedActivity extends AppCompatActivity {

    private boolean positionOk = false;
    private boolean followedOk = false;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.sharedPreferences = this.getSharedPreferences(Values.PREFERENCES_NAME, MODE_PRIVATE);
        Log.d("FEED ACTIVITY", "OnStart");
        long now = System.currentTimeMillis() / 1000;
        boolean followed = now - sharedPreferences.getLong(Values.FOLLOWED, 0) > Values.FETCHING_INTERVAL;
        boolean position = now - sharedPreferences.getLong(Values.POSITION, 0) > Values.FETCHING_INTERVAL;
        if (followed || position) {
            Log.d("FEED ACTIVITY", "SOMETHING HAS TO BE UPDATED");
            setContentView(R.layout.activity_splash);
            if (followed) {
                new RefreshFollowed().execute(this);
            } else
                this.followedOk = true;
            if (position) {
                this.refreshPosition();
            } else
                this.positionOk = true;
        } else go(true);
    }

    private void refreshPosition() {
        Log.d("FEED ACTIVITY", "REFRESHING POSITION");
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, Values.LOCATION_PERMISSION);
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        MyPosition.getInstance().getPositionProvider().getLastLocation().addOnSuccessListener(
                location -> {
                    MyPosition.getInstance().setLocation(location);
                    editor.putLong(Values.POSITION, System.currentTimeMillis() / 1000);
                    this.positionOk = true;
                    editor.apply();
                    if (followedOk)
                        go(false);
                });
    }

    private void go(boolean forced) {
        Log.d("FEED ACTIVITY", "Everything is set - " + forced);
        if (forced || followedOk && positionOk) {
            setContentView(R.layout.activity_feed);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
        }
    }


    public void goToMap(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public void addNewFriend(View view) {
        Intent intent = new Intent(this, AddFriendActivity.class);
        this.startActivity(intent);
    }

    private class RefreshFollowed extends AsyncTask<Activity, Void, String> {
        @Override
        protected String doInBackground(Activity... act) {
            Log.d("FEED ACTIVITY", "REFRESHING FOLLOWED");
            try {
                return Unirest.get(Helper.buildUrl(act[0].getResources().getString(R.string.followed_GET),
                        sharedPreferences.getString("session_id", null))).asString().getBody();
            } catch (UnirestException e) {
                Log.e("REFRESHING FOLLOWED", "EXCEPTION: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            Followed f = new Gson().fromJson(response, Followed.class);
            Friends.getInstance().mergeUsers(f);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong(Values.FOLLOWED, System.currentTimeMillis() / 1000);
            editor.apply();
            followedOk = true;
            if (positionOk) {
                go(false);
            }
        }
    }

}
