package com.gioaudino.geopost;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gioaudino.geopost.Model.Followed;
import com.gioaudino.geopost.Model.Friends;
import com.gioaudino.geopost.Service.Helper;
import com.google.gson.Gson;

public class FeedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest followedRequest = new StringRequest(
                Request.Method.GET,
                Helper.buildUrl(this.getResources().getString(R.string.followed_GET),
                        this.getSharedPreferences(Helper.PREFERENCES_NAME, MODE_PRIVATE).getString("session_id", null)),
                response -> {
                    Followed f = new Gson().fromJson(response, Followed.class);
                    Friends.getInstance().mergeUsers(f);
                    Log.d("FOLLOWED REQUEST", "REQUEST SUCCESSFUL");
                    Log.d("FOLLOWED", Friends.getInstance().toString());
                },
                error -> {
                    Log.e("FOLLOWED REQUEST", "REQUEST FAILED");
                }
        );
        queue.add(followedRequest);

        setContentView(R.layout.activity_feed);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    public void goToMap(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public void addNewFriend(View view) {
        Intent intent = new Intent(this, AddFriendActivity.class);
        this.startActivity(intent);
    }
}
