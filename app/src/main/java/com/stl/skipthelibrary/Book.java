package com.stl.skipthelibrary;

import java.util.ArrayList;

public class Book {
    private BookDescription description;
    private BookStatus status;
    private User owner;
    private ArrayList<Request> pendingRequests;
    private Request acceptedRequest;
    private ArrayList<ViewableImage> images;

    public Book(BookDescription description, BookStatus status, User owner, ArrayList<Request> pendingRequests, Request acceptedRequest, ArrayList<ViewableImage> images) {
        this.description = description;
        this.status = status;
        this.owner = owner;
        this.pendingRequests = pendingRequests;
        this.acceptedRequest = acceptedRequest;
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

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public ArrayList<Request> getPendingRequests() {
        return pendingRequests;
    }

    public void setPendingRequests(ArrayList<Request> pendingRequests) {
        this.pendingRequests = pendingRequests;
    }

    public Request getAcceptedRequest() {
        return acceptedRequest;
    }

    public void setAcceptedRequest(Request acceptedRequest) {
        this.acceptedRequest = acceptedRequest;
    }
}
