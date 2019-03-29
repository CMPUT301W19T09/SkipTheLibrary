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
import com.stl.skipthelibrary.Entities.Location;
import com.stl.skipthelibrary.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
/** This fragment displays a specified location (latitude, longitude) in
 * a Mapbox element
 */
public class MapboxFragment extends Fragment {
    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    private MapView mapView;
    private ImageView dropPinView;
    private MapboxMap mapboxMap;
    private Location bookPickupLocation;


    /**
     * Creates a new instance of MapboxFragment with specified location
     * @param latitude
     * @param longitude
     * @return MapboxFragment
     */
    public static MapboxFragment newInstance(double latitude, double longitude) {
        MapboxFragment fragment = new MapboxFragment();
        Bundle args = new Bundle();
        args.putDouble("latitude", latitude);
        args.putDouble("longitude", longitude);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Defines and inflates the fragment view with the specified parameters inside parent
     * @param inflater
     * @param parent
     * @param savedInstanceState
     * @return View
     */
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

    /**
     * Styles map and sets proper location once received, disables panning and zooming
     * so the user can't move the specified pin location
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
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
                mapboxMap.getUiSettings().setAllGesturesEnabled(false);
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
