package com.gioaudino.geopost;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gioaudino.geopost.Entity.User;
import com.gioaudino.geopost.Model.Followed;
import com.gioaudino.geopost.Model.Friends;
import com.gioaudino.geopost.Model.MyPosition;
import com.gioaudino.geopost.Service.Helper;
import com.gioaudino.geopost.Service.SortByDistanceComparator;
import com.gioaudino.geopost.Service.UserAdapter;
import com.gioaudino.geopost.Service.Values;
import com.google.gson.Gson;

import java.util.List;

public class ListActivity extends FriendsActivity {

    private boolean positionOk = false;
    private boolean followedOk = false;
    private SharedPreferences sharedPreferences;
    private List<User> values;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_splash);
        this.sharedPreferences = this.getSharedPreferences(Values.PREFERENCES_NAME, MODE_PRIVATE);

        Log.d("LIST ACTIVITY", "OnStart");

        this.refreshFollowed();
        this.refreshPosition();
    }

    private void refreshFollowed() {
        Log.d("LIST ACTIVITY", "REFRESHING FOLLOWED");
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(
                Request.Method.GET,
                Helper.buildSimpleUrl(
                        this.getResources().getString(R.string.followed_GET),
                        sharedPreferences.getString("session_id", null)),
                response -> {
                    Followed f = new Gson().fromJson(response, Followed.class);
                    Friends.getInstance().mergeUsers(f);
                    this.setData();
                    followedOk = true;
                    Log.d("LIST ACTIVITY", "REFRESHING COMPLETED");
                    if (positionOk) {
                        go();
                    }
                },
                error -> {
                    Log.e("REFRESHING FOLLOWED", "EXCEPTION: " + error.getMessage());
                });
        queue.add(request);
    }

    private void setData() {
        this.values = Friends.getInstance().getFriends();
        this.values.sort(new SortByDistanceComparator());
    }

    private void refreshPosition() {
        Log.d("LIST ACTIVITY", "REFRESHING POSITION");
        this.checkLocationPermission();
        MyPosition.getInstance().getPositionProvider().getLastLocation().addOnSuccessListener(
                location -> {
                    MyPosition.getInstance().setLocation(location);
                    this.positionOk = true;
                    if (followedOk)
                        go();
                });

    }

    private void go() {
        Log.d("LIST ACTIVITY", "Let's go");
        if (followedOk && positionOk) {
            setContentView(R.layout.activity_feed);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            publishList(false);
        }
    }

    private void publishList(boolean shouldShowSnackBar) {
        this.setData();
        ListView listView = this.findViewById(R.id.friends_list);
        BaseAdapter adapter = (BaseAdapter) listView.getAdapter();
        if (null == adapter)
            listView.setAdapter(new UserAdapter(this, R.layout.list_element, this.values));
        else
            adapter.notifyDataSetChanged();

        if (shouldShowSnackBar)
            Snackbar.make(this.findViewById(R.id.friends_list_coordinator), "Friend list, their position and your position have been updated", Snackbar.LENGTH_LONG).show();


        if (Friends.getInstance().size() == 0) {
            listView.setVisibility(View.GONE);
            this.snackbar = Snackbar.make(this.findViewById(R.id.friends_list_coordinator), R.string.empty_list, Snackbar.LENGTH_INDEFINITE);
            this.snackbar.setAction("Dismiss", view -> ListActivity.this.snackbar.dismiss());
            this.snackbar.show();
        } else {
            listView.setVisibility(View.VISIBLE);
            if (this.snackbar != null)
                this.snackbar.dismiss();
        }

        followedOk = false;
        positionOk = false;
    }

    public void goToMap(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        this.startActivity(intent);
    }

    public void refresh(View view) {
        Log.d("LIST ACTIVITY", "REFRESH ACTION");
        Log.d("LIST ACTIVITY", "REFRESHING POSITION");
        this.checkLocationPermission();

        RequestQueue queue = Volley.newRequestQueue(this);

        MyPosition.getInstance().getPositionProvider().getLastLocation().addOnSuccessListener(
                location -> {
                    MyPosition.getInstance().setLocation(location);
                    this.positionOk = true;
                    if (followedOk)
                        publishList(true);
                });
        Log.d("LIST ACTIVITY", "REFRESHING FOLLOWED");

        StringRequest request = new StringRequest(
                Request.Method.GET,
                Helper.buildSimpleUrl(
                        this.getResources().getString(R.string.followed_GET),
                        sharedPreferences.getString("session_id", null)),
                response -> {
                    Followed f = new Gson().fromJson(response, Followed.class);
                    Friends.getInstance().mergeUsers(f);
                    this.setData();
                    followedOk = true;
                    Log.d("LIST ACTIVITY", "FOLLOWED REFRESHING COMPLETED");
                    if (positionOk) {
                        publishList(true);
                    }
                },
                error -> {
                    Log.e("REFRESHING FOLLOWED", "EXCEPTION: " + error.getMessage());
                });
        queue.add(request);
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }

    @Override
    public void addNewFriend(View view) {
        super.addNewFriend(view);
    }

    @Override
    public void goToProfile(View view) {
        super.goToProfile(view);
    }

    @Override
    public void updateStatus(View view) {
        super.updateStatus(view);
    }
}
