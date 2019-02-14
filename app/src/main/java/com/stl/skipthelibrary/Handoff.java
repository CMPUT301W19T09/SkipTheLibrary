package com.stl.skipthelibrary;

public class Handoff {
    private Book book;
    private Location location;
    private HandoffState handoffState;

    public Handoff(Book book, Location location, HandoffState handoffState) {
        this.book = book;
        this.location = location;
        this.handoffState = handoffState;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public HandoffState getHandoffState() {
        return handoffState;
    }

    public void setHandoffState(HandoffState handoffState) {
        this.handoffState = handoffState;
    }
}
