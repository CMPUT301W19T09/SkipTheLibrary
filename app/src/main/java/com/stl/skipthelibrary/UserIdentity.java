package com.stl.skipthelibrary;

import java.util.ArrayList;

public class UserIdentity {
    private ArrayList<Book> bookList;
    private Rating rating;
    private UserMode userMode;

    public UserIdentity(UserMode userMode) {
        this.userMode = userMode;
        rating = new Rating();
        bookList = new ArrayList<Book>();
    }

    public UserIdentity(ArrayList<Book> bookList, Rating rating, UserMode userMode) {
        this.bookList = bookList;
        this.rating = rating;
        this.userMode = userMode;
    }

    public void setBookList(ArrayList<Book> bookList) {
        this.bookList = bookList;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public void setUserMode(UserMode userMode) {
        this.userMode = userMode;
    }

    public ArrayList<Book> getBookList() {
        return bookList;
    }

    public Rating getRating() {
        return rating;
    }

    public UserMode getUserMode() {
        return userMode;
    }

    public void addBook(Book book){
        bookList.add(book);
    }

    public boolean removeBook(Book book){
        return bookList.remove(book);
    }
}
