package com.gioaudino.geopost;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gioaudino.geopost.Model.MyPosition;
import com.gioaudino.geopost.Service.Helper;
import com.gioaudino.geopost.Service.LoginRequest;
import com.gioaudino.geopost.Service.MyLocationUpdater;
import com.google.android.gms.location.LocationServices;


public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.d("LOGIN", "KEY PRESSED: " + keyCode);
        if (keyCode == KeyEvent.KEYCODE_ENTER && ((EditText) this.findViewById(R.id.username)).getText().toString().length() > 0 &&
                ((EditText) this.findViewById(R.id.password)).getText().toString().length() > 0) {
            login(null);
            return true;
        } else {
            return super.onKeyUp(keyCode, event);
        }
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
                    new Thread(() -> MyLocationUpdater.getInstance().setup(this)).run();
                    MyPosition.getInstance().setPositionProvider(LocationServices.getFusedLocationProviderClient(this));
                    Intent intent = new Intent(this, ListSplashActivity.class);
                    startActivity(intent);
                },
                error -> {
                    spinner.setVisibility(View.INVISIBLE);
                    errorMsg.setVisibility(View.VISIBLE);
                    Log.d("LOGIN ACTIVITY", "REQUEST FAILED - " + error.getMessage());
                    if (error.networkResponse != null)
                        Log.d("LOGIN ACTIVITY", "REQUEST FAILED - " + error.networkResponse.statusCode + " " + error.networkResponse.data);
                }
        );
        queue.add(request);
    }

    public void autoLogin(View view) {
        int user = 0;
        switch (view.getId()) {
            case R.id.al_1:
                user = 1;
                break;
            case R.id.al_2:
                user = 2;
                break;
            case R.id.al_3:
                user = 3;
                break;
            case R.id.al_4:
                user = 4;
                break;
        }
        ((EditText) this.findViewById(R.id.username)).setText("886853_test" + user);
        ((EditText) this.findViewById(R.id.password)).setText("giorgio");
        this.login(view);
    }
}
