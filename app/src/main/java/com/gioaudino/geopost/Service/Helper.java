package com.gioaudino.geopost.Service;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.gioaudino.geopost.Model.Friends;
import com.gioaudino.geopost.Model.MyPosition;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
        Friends.getInstance().clear();
        MyPosition.getInstance().clear();
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

    public static String round(float distance, int decimalPlace) {
        if (decimalPlace <= 0) return String.valueOf((int) distance);
        if (decimalPlace == 1) return round(distance);

        StringBuilder sb = new StringBuilder("#.");
        for (int i = 0; i < decimalPlace; i++) sb.append('#');

        return new DecimalFormat(sb.toString()).format(distance).replace(',', '.');
    }

    public static String formatDistance(float distance) {
        return distance > 1000 ? Helper.round(distance / 1000) + " km" : ((int) distance) + " m";
    }

    private static String round(float distance) {
        DecimalFormat df = new DecimalFormat("#.#");
        return df.format(distance).replace(',', '.');
    }

    public static String buildUrlToUpdateStatus(String url, String sessionId, String status, Location location) {
        return String.format(Locale.US, "%s?session_id=%s&message=%s&lat=%s&lon=%s", url, sessionId, status, String.valueOf(location.getLatitude()).replace(',', '.'), String.valueOf(location.getLongitude()).replace(',', '.'));
    }

    public static String getSessionId(Activity activity) {
        return activity.getSharedPreferences(Values.PREFERENCES_NAME, Context.MODE_PRIVATE).getString(Values.SESSION_ID, null);
    }
}
