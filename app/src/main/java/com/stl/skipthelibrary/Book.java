package com.stl.skipthelibrary;

import java.util.ArrayList;

public class Book {
    private BookDescription description;
    private String ownerUserName;
    private RequestHandler requests;
    private ArrayList<ViewableImage> images;

    public Book(BookDescription description, String ownerUserName, RequestHandler requests, ArrayList<ViewableImage> images) {
        this.description = description;
        this.ownerUserName = ownerUserName;
        this.requests = requests;
        this.images = images;
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
}
