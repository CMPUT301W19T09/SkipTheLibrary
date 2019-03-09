package com.stl.skipthelibrary.Activities;

import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.stl.skipthelibrary.Helpers.NavigationHandler;
import com.stl.skipthelibrary.R;
import androidx.appcompat.app.AppCompatActivity;

/**
 * The notification activity. On this screen all notifications will be displayed
 */
public class NotificationActivity extends AppCompatActivity {

    /**
     * Bind UI elements
     * @param savedInstanceState: the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(new NavigationHandler(this));
        navigation.setSelectedItemId(R.id.home);
    }

    /**
     * Disable back presses on screen with the navigation bar
     */
    @Override
    public void onBackPressed() {
    }

}
