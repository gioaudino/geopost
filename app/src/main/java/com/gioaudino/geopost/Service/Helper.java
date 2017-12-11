package com.gioaudino.geopost.Service;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gioaudino.geopost.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gioaudino on 17/11/17.
 * Package com.gioaudino.geopost.Service in project GeoPost
 */

public class Helper {

    public static String buildSimpleUrl(String url, String sessionId) {
        return url + "?session_id=" + sessionId;
    }

    public static String buildUrlToFetchUsernames(String url, String sessionId, CharSequence usernameStart, int limit) {
        return url + "?session_id=" + sessionId + "&usernamestart=" + usernameStart + "&limit=" + limit;
    }

    public static String buildUrlToFollowFriend(String url, String sessionId, String username) {
        return url + "?session_id=" + sessionId + "&username=" + username;
    }

    public static void saveData(Activity activity, String username, String password, String sessionID) {
        SharedPreferences preferences = activity.getSharedPreferences(Values.PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor
                .putString("username", username)
                .putString("password", password)
                .putString("session_id", sessionID)
                .apply();
    }

    public static void deleteData(Activity activity) {
        SharedPreferences preferences = activity.getSharedPreferences(Values.PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor
                .remove("username")
                .remove("password")
                .remove("session_id")
                .apply();
    }

    public static void closeKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public static void replaceValues(List<String> old, List<String> values) {
        List<String> tbr = new ArrayList<>();
        for (String val : old)
            if (values.contains(val)) values.remove(val);
            else tbr.add(val);

        old.removeAll(tbr);
        old.addAll(values);
    }

    public static void destroy(Activity activity) {

    }

}
