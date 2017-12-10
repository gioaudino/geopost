package com.gioaudino.geopost;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gioaudino.geopost.Model.MyPosition;
import com.gioaudino.geopost.Service.Helper;
import com.gioaudino.geopost.Service.LoginRequest;
import com.google.android.gms.location.LocationServices;


public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyPosition.getInstance().setPositionProvider(LocationServices.getFusedLocationProviderClient(this));
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION);
        }
        MyPosition.getInstance().getPositionProvider().getLastLocation().addOnSuccessListener(location -> MyPosition.getInstance().setLocation(location));
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
                    Intent intent = new Intent(this, FeedActivity.class);
                    startActivity(intent);
                },
                error -> {
                    spinner.setVisibility(View.INVISIBLE);
                    errorMsg.setVisibility(View.VISIBLE);
                    Log.d("LOGIN ACTIVITY", "REQUEST FAILED");
                }
        );
        queue.add(request);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("LOCATION PERMISSION", "GRANTED");
            } else {
                Log.d("LOCATION PERMISSION", "DENIED");
            }
        }
    }
}
