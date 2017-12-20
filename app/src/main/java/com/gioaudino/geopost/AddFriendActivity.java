package com.gioaudino.geopost;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gioaudino.geopost.Model.Usernames;
import com.gioaudino.geopost.Service.Helper;
import com.gioaudino.geopost.Service.Values;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class AddFriendActivity extends BaseActivity {
    private final static int MIN_LENGTH = 3;
    private static final int MAX_FETCHING = 5;
    private Activity activity;
    private String searchString;
    private RequestQueue queue;
    private List<String> values = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = this;
        this.queue = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_add_friend);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView listView = findViewById(R.id.list_search_users);
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, this.values));
        listView.setOnItemClickListener((adapterView, view, position, l) -> {
            Helper.closeKeyboard(activity);
            String username = adapterView.getItemAtPosition(position).toString();
            StringRequest request =
                    new StringRequest(
                            Request.Method.GET,
                            Helper.buildUrlToFollowFriend(
                                    getResources().getString(R.string.follow_GET),
                                    getSharedPreferences(Values.PREFERENCES_NAME, MODE_PRIVATE).getString("session_id", null),
                                    username),
                            response -> {
                                Snackbar.make(findViewById(R.id.layout_add_friend), "You're now following " + username, Snackbar.LENGTH_LONG).show();
                            },
                            error -> {
                                Log.e("FOLLOW USER", "400: " + new String(error.networkResponse.data));
                                Snackbar err = Snackbar.make(findViewById(R.id.layout_add_friend), Html.fromHtml(new String(error.networkResponse.data)), Snackbar.LENGTH_LONG);
                                err.getView().setBackgroundColor(Color.parseColor("#E54A1B"));
                                err.show();
                            });
            queue.add(request);
        });

        EditText tView = findViewById(R.id.username_field);
        tView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchString = charSequence.toString();
                Log.d("TEXT CHANGED", charSequence.toString());
                values.clear();
                BaseAdapter adapter = ((BaseAdapter) ((ListView) activity.findViewById(R.id.list_search_users)).getAdapter());
                if (null != adapter)
                    adapter.notifyDataSetChanged();
                Log.d("ON TEXT CHANGED", "ADAPTER RESET");

                if (charSequence.length() >= MIN_LENGTH) {
                    Log.d("ON TEXT CHANGED", "CREATING TASK");

                    StringRequest request =
                            new StringRequest(
                                    Request.Method.GET,
                                    Helper.buildUrlToFetchUsernames(
                                            activity.getResources().getString(R.string.users_GET),
                                            activity.getSharedPreferences(Values.PREFERENCES_NAME, Context.MODE_PRIVATE).getString("session_id", null),
                                            searchString,
                                            MAX_FETCHING),
                                    response -> {
                                        activity.findViewById(R.id.error_msg).setVisibility(View.INVISIBLE);
                                        Helper.replaceValues(values, new Gson().fromJson(response, Usernames.class).getUsernames());
                                        if (values.size() == 0)
                                            activity.findViewById(R.id.error_msg).setVisibility(View.VISIBLE);
                                        ((BaseAdapter) ((ListView) activity.findViewById(R.id.list_search_users)).getAdapter()).notifyDataSetChanged();
                                    },
                                    error -> {
                                        values.clear();
                                        ((BaseAdapter) ((ListView) activity.findViewById(R.id.list_search_users)).getAdapter()).notifyDataSetChanged();
                                    }
                            );
                    queue.add(request);

                    Log.d("ON TEXT CHANGED", "TASK EXECUTED");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    public void goToList(View view) {
        Intent intent = new Intent(this, ListSplashActivity.class);
        this.startActivity(intent);
    }

    public void goToMap(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        this.startActivity(intent);
    }
}


