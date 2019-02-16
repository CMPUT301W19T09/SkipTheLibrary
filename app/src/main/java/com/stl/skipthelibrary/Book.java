package com.stl.skipthelibrary;

import java.util.ArrayList;
import java.util.UUID;

public class Book {
    private BookDescription description;
    private UUID userID;
    private RequestHandler requests;
    private ArrayList<ViewableImage> images;

    public Book(BookDescription description, UUID userID, RequestHandler requests, ArrayList<ViewableImage> images) {
        this.description = description;
        this.userID = userID;
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


    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }
}
