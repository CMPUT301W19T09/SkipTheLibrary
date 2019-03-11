package com.stl.skipthelibrary.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.stl.skipthelibrary.Entities.Location;
import com.stl.skipthelibrary.R;
import com.stl.skipthelibrary.Singletons.CurrentLocation;

/**
 * This activity allows a user to set a location on a map. Adapted from
 * https://blog.mapbox.com/how-to-build-a-location-picker-for-your-app-8e61be7fc9cc
 */
public class MapBoxActivity extends AppCompatActivity {
    public static final int SET_LOCATION = 1;
    private MapView mapView;
    private Location location;
    private ImageView dropPinView;
    private MapboxMap mapboxMap;

    /**
     * Get the Mapbox instance
     * @param savedInstanceState: the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_map_box);

        getCurrentLocation(savedInstanceState);
    }

    /**
     * Get the user's current location
     * @param savedInstanceState
     */
    private void getCurrentLocation(final Bundle savedInstanceState) {
        location = CurrentLocation.getInstance().getLocation();
        afterLocationRecieved(savedInstanceState);
    }

    /**
     * After the location is recieved, open the map on the user's location
     * @param savedInstanceState: the saved instance state
     */
    private void afterLocationRecieved(Bundle savedInstanceState){
        mapView = findViewById(R.id.select_location_map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                MapBoxActivity.this.mapboxMap = mapboxMap;
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    /**
                     * Drop a pin on the user's current location
                     * @param style: the current style
                     */
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        dropPinView = new ImageView(MapBoxActivity.this);
                        dropPinView.setImageResource(R.drawable.red_pin);

                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
                        params.width = 120;
                        params.width = 120;
                        float density = getResources().getDisplayMetrics().density;
                        params.bottomMargin = (int) (12 * density);
                        dropPinView.setLayoutParams(params);
                        mapView.addView(dropPinView);

                    }
                });

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(location.getLatitude(), location.getLongitude()))
                        .zoom(15)
                        .tilt(20)
                        .build();
                mapboxMap.setCameraPosition(cameraPosition);
            }
        });

        findViewById(R.id.select_location_submit).setOnClickListener(new View.OnClickListener() {
            /**
             * When select location is selected, clean up the mapbox and return the result
             * @param view: the select location button
             */
            @Override
            public void onClick(View view) {
                mapView.onDestroy();
                location.setLatitude(mapboxMap.getCameraPosition().target.getLatitude());
                location.setLongitude(mapboxMap.getCameraPosition().target.getLongitude());
                Gson gson = new Gson();
                Intent intent=new Intent();
                intent.putExtra("Location", gson.toJson(location));
                intent.putExtra("username",getIntent().getExtras().getString("username"));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }


    /**
     * When finished, destroy the map view
     */
    @Override
        public void finish() {
        mapView.onDestroy();
        super.finish();
    }
}
