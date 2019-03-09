package com.stl.skipthelibrary.Singletons;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.stl.skipthelibrary.Entities.Location;

/**
 * This class is a singleton and contains the current location, if the user's location changes
 * this class will automatically update
 */
public class CurrentLocation {
    private static final double DEFAULT_LATITUDE = 53.527322;
    private static final double DEFAULT_LONGITUDE = -113.529477;
    private static final CurrentLocation ourInstance = new CurrentLocation();
    private Location location;
    private LocationListener locationListener;

    /**
     * The empty constructor, creates a new location with the default location
     */
    private CurrentLocation(){
        location = new Location(DEFAULT_LATITUDE, DEFAULT_LONGITUDE);
    }

    /**
     * Automatically update this class's location if the user's location changes by implementing
     * a onLocationChanged listener
     * @param context
     */
    @SuppressLint("MissingPermission")
    public void updateLocation(Context context){
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        android.location.Location androidLocation
                = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (androidLocation != null){
            location.setLatitude(androidLocation.getLatitude());
            location.setLongitude(androidLocation.getLongitude());
        }

        locationListener = new LocationListener() {
            /**
             * On location changed update the location
             * @param receivedLocation: the recieved location
             */
            @Override
            public void onLocationChanged(android.location.Location receivedLocation) {
                location.setLatitude(receivedLocation.getLatitude());
                location.setLongitude(receivedLocation.getLongitude());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }


    /**
     * Get the current instance
     * @return the current instance
     */
    public static CurrentLocation getInstance() {
        return ourInstance;
    }

    /**
     * Get the current location
     * @return the current location
     */
    public Location getLocation(){
        return location;
    }
}
