package com.stl.skipthelibrary;

import java.util.ArrayList;

public class UserIdentity {
    private ArrayList<Book> booksList;
    private Rating rating;


    public UserIdentity(ArrayList<Book> booksList, Rating rating) {
        this.booksList = booksList;
        this.rating = rating;
    }

    public ArrayList<Book> getBooksList() {
        return booksList;
    }

    public Rating getRating() {
        return rating;
    }

    public void setBooksList(ArrayList<Book> booksList) {
        this.booksList = booksList;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }
}