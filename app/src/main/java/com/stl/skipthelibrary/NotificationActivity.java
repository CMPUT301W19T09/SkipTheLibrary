package com.stl.skipthelibrary;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(new NavigationHandler(this));
        navigation.setSelectedItemId(R.id.home);

        findViewById(R.id.NotificationTitle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotificationActivity.this, MapBoxActivity.class);
                startActivityForResult(intent, MapBoxActivity.SET_LOCATION);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MapBoxActivity.SET_LOCATION) {
            String locationString = data.getStringExtra("Location");
            Gson gson = new Gson();
            Location location = gson.fromJson(locationString, Location.class);
            Toast.makeText(this, location.getLatitude() + " \n " + location.getLongitude(), Toast.LENGTH_SHORT).show();
        }
    }
}