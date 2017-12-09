package com.gioaudino.geopost;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
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
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class AddFriendActivity extends AppCompatActivity {
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

        ((ListView) findViewById(R.id.list_search_users)).setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, this.values));

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
                                    Helper.buildUrl(
                                            activity.getResources().getString(R.string.users_GET),
                                            activity.getSharedPreferences(Helper.PREFERENCES_NAME, Context.MODE_PRIVATE).getString("session_id", null),
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

        FloatingActionButton fab = findViewById(R.id.fab_map);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
    }


}


