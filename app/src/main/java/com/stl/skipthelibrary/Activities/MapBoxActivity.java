package com.stl.skipthelibrary.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

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

public class MapBoxActivity extends AppCompatActivity {
    public static final int SET_LOCATION = 1;
    private MapView mapView;
    private Location location;
    private ImageView dropPinView;
    private MapboxMap mapboxMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_map_box);

        getCurrentLocation(savedInstanceState);
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation(final Bundle savedInstanceState) {
        location = CurrentLocation.getInstance().getLocation();
        afterLocationRecieved(savedInstanceState);
    }

    private void afterLocationRecieved(Bundle savedInstanceState){
        mapView = findViewById(R.id.select_location_map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                MapBoxActivity.this.mapboxMap = mapboxMap;
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {

                        // Map is set up and the style has loaded. Now you can add data or make other map adjustments
                        // Create drop pin using custom image
                        dropPinView = new ImageView(MapBoxActivity.this);
                        dropPinView.setImageResource(R.drawable.red_pin);

                        // Statically Set drop pin in center of screen
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
            @Override
            public void onClick(View view) {
                mapView.onDestroy();
                location.setLatitude(mapboxMap.getCameraPosition().target.getLatitude());
                location.setLongitude(mapboxMap.getCameraPosition().target.getLongitude());
                Gson gson = new Gson();
                Intent intent=new Intent();
                intent.putExtra("Location", gson.toJson(location));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void finish() {
        mapView.onDestroy();
        super.finish();
    }
}
