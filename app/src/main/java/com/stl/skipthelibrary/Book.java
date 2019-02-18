package com.stl.skipthelibrary;

import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

public class Book implements Scannable {
    public String TAG = getClass().getSimpleName();

    private UUID uuid;
    private BookDescription description;
    private String ownerUserName;
    private RequestHandler requests;
    private ArrayList<ViewableImage> images;
    private Rating rating;


    public Book(BookDescription description, String ownerUserName, RequestHandler requests, ArrayList<ViewableImage> images, Rating rating) {
        this.uuid = UUID.randomUUID();
        this.description = description;
        this.ownerUserName = ownerUserName;
        this.requests = requests;
        this.images = images;
        this.rating = rating;
    }


    // Getters and Setters
    public RequestHandler getRequests() {
        return requests;
    }

    public void setRequests(RequestHandler requests) {
        this.requests = requests;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public ArrayList<ViewableImage> getImages() {
        return images;
    }

    public void setImages(ArrayList<ViewableImage> images) {
        this.images = images;
    }

    public BookDescription getDescription() {
        return description;
    }

    public void setDescription(BookDescription description) {
        this.description = description;
    }


    public String getOwnerUserName() {
        return ownerUserName;
    }

    public void setOwnerUserName(String ownerUserName) {
        this.ownerUserName = ownerUserName;
    }

    public UUID getUuid() {
        return uuid;
    }
    /**************************************************/

    public Boolean scan() {
        Log.d(TAG, "scan: OPEN UP THE SCANNER");
        return false;
    }
}
