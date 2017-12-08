package com.gioaudino.geopost.Service;

import android.app.Activity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by gioaudino on 07/12/17.
 * Package com.gioaudino.geopost.Service in project GeoPost
 */

public class FollowedRequest extends StringRequest {

    public FollowedRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }

}
