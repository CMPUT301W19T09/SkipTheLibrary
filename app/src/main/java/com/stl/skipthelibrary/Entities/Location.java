package com.stl.skipthelibrary.Entities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;

import java.util.Objects;

/**
 * This class describes a location
 */
public class Location {
    private double latitude;
    private double longitude;

    /**
     * The empty constructor
     */
    public Location() {
    }

    /**
     * The full constructor
     * @param latitude: the latitude of the location
     * @param longitude: the longitude of the location
     */
    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Get the location's latitude
     * @return the location's latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Set the location's latitude
     * @param latitude: the location's latitude
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Get the location's longitude
     * @return the location's longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Set the location's longitude
     * @param longitude: the location's longitude
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Determines if the location is identical to another object
     * @param o: An object to compare the location to
     * @return true if o is identical to the current location
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location)) return false;
        Location location = (Location) o;
        return Double.compare(location.getLatitude(), getLatitude()) == 0 &&
                Double.compare(location.getLongitude(), getLongitude()) == 0;
    }

    /**
     * Calculate and return the location's hashcode
     * @return the location's hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(getLatitude(), getLongitude());
    }

}
