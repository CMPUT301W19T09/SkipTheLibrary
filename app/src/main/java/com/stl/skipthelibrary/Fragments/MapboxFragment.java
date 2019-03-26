package com.stl.skipthelibrary.Fragments;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.stl.skipthelibrary.Activities.MapBoxActivity;
import com.stl.skipthelibrary.Entities.Location;
import com.stl.skipthelibrary.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class MapboxFragment extends Fragment {
    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    private MapView mapView;
    private ImageView dropPinView;
    private MapboxMap mapboxMap;
    private Location bookPickupLocation;

    public static MapboxFragment newInstance(Location location) {
        MapboxFragment fragment = new MapboxFragment();
        Bundle args = new Bundle();
        args.putDouble("latitude", location.getLatitude());
        args.putDouble("longitude", location.getLongitude());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(getActivity(), getString(R.string.mapbox_access_token));
        double latitude = getArguments().getDouble("latitude", 0);
        double longitude = getArguments().getDouble("longitude", 0);
        this.bookPickupLocation = new Location(latitude, longitude);
        return inflater.inflate(R.layout.fragment_mapbox, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        mapView = view.findViewById(R.id.pickupLocationMapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mbm) {
                MapboxFragment.this.mapboxMap = mbm;
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    /**
                     * Drop a pin on the user's current location
                     * @param style: the current style
                     */
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        dropPinView = new ImageView(getActivity());
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
                        .target(new LatLng(bookPickupLocation.getLatitude(), bookPickupLocation.getLongitude()))
                        .zoom(15)
                        .tilt(20)
                        .build();
                mapboxMap.setCameraPosition(cameraPosition);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mapView.onDestroy();

    }
}
