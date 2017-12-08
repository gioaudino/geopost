package com.gioaudino.geopost.Service;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gioaudino.geopost.Entity.User;
import com.gioaudino.geopost.R;

import java.util.List;

/**
 * Created by gioaudino on 08/12/17.
 * Package com.gioaudino.geopost.Service in project GeoPost
 */

public class UserAdapter extends ArrayAdapter<User> {
    public UserAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public UserAdapter(@NonNull Context context, int resource, @NonNull List<User> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.list_element, null);
        }
        User user = getItem(position);
        if (user != null) {
            TextView tt1 = v.findViewById(R.id.line1);
            TextView tt2 = v.findViewById(R.id.line2);
            tt1.setText(user.getUsername());
            tt2.setText(user.getMsg());
        }
        return v;
    }
}
