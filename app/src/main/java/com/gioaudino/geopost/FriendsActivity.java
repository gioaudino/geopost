package com.gioaudino.geopost;

import android.content.Intent;
import android.view.View;

/**
 * Created by gioaudino on 12/12/17.
 * Package com.gioaudino.geopost in project GeoPost
 */

public abstract class FriendsActivity extends BaseActivity {

    @Override
    public void onBackPressed() {
    }

    public void addNewFriend(View view) {
        Intent intent = new Intent(this, AddFriendActivity.class);
        this.startActivity(intent);
    }

    public void goToProfile(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        this.startActivity(intent);
    }

    public void updateStatus(View view) {
        Intent intent = new Intent(this, StatusUpdateActivity.class);
        this.startActivity(intent);
    }
}
