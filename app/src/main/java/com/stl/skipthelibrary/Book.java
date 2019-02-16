package com.stl.skipthelibrary;

import java.util.ArrayList;

public class Book {
    private BookDescription description;
    private String ownerUserName;
    private RequestHandler requests;
    private ArrayList<ViewableImage> images;
    private Rating rating;


    public Book(BookDescription description, String ownerUserName, RequestHandler requests, ArrayList<ViewableImage> images, Rating rating) {
        this.description = description;
        this.ownerUserName = ownerUserName;
        this.requests = requests;
        this.images = images;
        this.rating = rating;
        pullRating();
    }


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







    private void pullRating(){

    }

    private User pullOwner(){
        return null;
    }

}
