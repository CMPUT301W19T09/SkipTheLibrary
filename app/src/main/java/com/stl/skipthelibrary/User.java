package com.stl.skipthelibrary;

import java.util.ArrayList;

public class User {
    private String name;
    private ViewableImage image;
    private ContactInfo contactInfo;
    private Credentials credentials;
    private Rating ownerRating;
    private Rating borrowerRating;
    private ArrayList<Book> booksIOwn;
    private ArrayList<Book> booksIAmInterestedIn;

    public User(String name, ViewableImage image, ContactInfo contactInfo, Credentials credentials, Rating ownerRating, Rating borrowerRating, ArrayList<Book> booksIOwn, ArrayList<Book> booksIAmInterestedIn) {
        this.name = name;
        this.image = image;
        this.contactInfo = contactInfo;
        this.credentials = credentials;
        this.ownerRating = ownerRating;
        this.borrowerRating = borrowerRating;
        this.booksIOwn = booksIOwn;
        this.booksIAmInterestedIn = booksIAmInterestedIn;
    }

    public Rating getOwnerRating() {
        return ownerRating;
    }

    public void setOwnerRating(Rating ownerRating) {
        this.ownerRating = ownerRating;
    }

    public Rating getBorrowerRating() {
        return borrowerRating;
    }

    public void setBorrowerRating(Rating borrowerRating) {
        this.borrowerRating = borrowerRating;
    }

    public ArrayList<Book> getBooksIOwn() {
        return booksIOwn;
    }

    public void setBooksIOwn(ArrayList<Book> booksIOwn) {
        this.booksIOwn = booksIOwn;
    }

    public ArrayList<Book> getBooksIAmInterestedIn() {
        return booksIAmInterestedIn;
    }

    public void setBooksIAmInterestedIn(ArrayList<Book> booksIAmInterestedIn) {
        this.booksIAmInterestedIn = booksIAmInterestedIn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ViewableImage getImage() {
        return image;
    }

    public void setImage(ViewableImage image) {
        this.image = image;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }
}
