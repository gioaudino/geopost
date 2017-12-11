package com.gioaudino.geopost;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gioaudino.geopost.Service.Helper;
import com.gioaudino.geopost.Service.LoginRequest;


public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void login(View view) {
        Log.d("LOGIN ACTIVITY", "   LOGIN REQUEST");

        Helper.closeKeyboard(this);
        View spinner = findViewById(R.id.progbar);
        View errorMsg = findViewById(R.id.error_msg);
        errorMsg.setVisibility(View.INVISIBLE);
        spinner.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(this);
        String username = ((EditText) this.findViewById(R.id.username)).getText().toString();
        String password = ((EditText) this.findViewById(R.id.password)).getText().toString();

        StringRequest request = new LoginRequest(this,
                username,
                password,
                response -> {
                    Log.d("LOGIN ACTIVITY", "REQUEST SUCCESSFUL - Token: " + response);
                    Helper.saveData(this, username, password, response);
                    spinner.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(this, SplashActivity.class);
                    startActivity(intent);
                },
                error -> {
                    spinner.setVisibility(View.INVISIBLE);
                    errorMsg.setVisibility(View.VISIBLE);
                    Log.d("LOGIN ACTIVITY", "REQUEST FAILED - " + error.getMessage());
                    Log.d("LOGIN ACTIVITY", "REQUEST FAILED - " + error.networkResponse.statusCode + " " + error.networkResponse.data);
                }
        );
        queue.add(request);
    }

}
