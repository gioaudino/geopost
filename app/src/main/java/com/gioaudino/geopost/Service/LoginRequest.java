package com.gioaudino.geopost.Service;

import android.app.Activity;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.gioaudino.geopost.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gioaudino on 07/12/17.
 * Package com.gioaudino.geopost.Service in project GeoPost
 */

public class LoginRequest extends StringRequest {
    private Map<String, String> params = new HashMap<>();

    public LoginRequest(Activity activity, String username, String password, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, activity.getResources().getString(R.string.login_POST), listener, errorListener);

        this.params.put("username", username);
        this.params.put("password", password);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return this.params;
    }
}
