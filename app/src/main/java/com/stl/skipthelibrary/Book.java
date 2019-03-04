package com.stl.skipthelibrary;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Book{
    final public static String TAG = "Book";

    private String uuid;
    private String ISBN;
    private BookDescription description;
    private String ownerUserName;
    private RequestHandler requests;
    private ArrayList<ViewableImage> images;
    private Rating rating;

    public Book() {
        this.uuid = UUID.randomUUID().toString();
    }

    public Book(BookDescription description, String ISBN, String ownerUserName, ArrayList<ViewableImage> images) {
        this(ISBN, description, ownerUserName,new RequestHandler(), images, new Rating());
    }

    public Book(String ISBN, BookDescription description, String ownerUserName, RequestHandler requests, ArrayList<ViewableImage> images, Rating rating) {
        this.uuid = UUID.randomUUID().toString();
        this.ISBN = ISBN;
        this.description = description;
        this.ownerUserName = ownerUserName;
        this.requests = requests;
        this.images = images;
        this.rating = rating;
    }

    public boolean userIsInterested(String userName){
        if (requests == null){
            return false;
        }

        if (requests.getRequestors() != null && requests.getRequestors().contains(userName)){
            return true;
        }

        if (requests.getAcceptedRequestor() != null && requests.getAcceptedRequestor().equals(userName)){
            return true;
        }

        return false;
    }

    // Getters and Setters

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public String getUuid() {
        return uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return Objects.equals(getUuid(), book.getUuid()) &&
                Objects.equals(getISBN(), book.getISBN()) &&
                Objects.equals(getDescription(), book.getDescription()) &&
                Objects.equals(getOwnerUserName(), book.getOwnerUserName()) &&
                Objects.equals(getRequests(), book.getRequests()) &&
                Objects.equals(getImages(), book.getImages()) &&
                Objects.equals(getRating(), book.getRating());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUuid(), getISBN(), getDescription(), getOwnerUserName(), getRequests(), getImages(), getRating());
    }
}
