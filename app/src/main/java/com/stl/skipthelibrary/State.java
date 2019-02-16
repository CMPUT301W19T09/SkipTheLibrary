package com.stl.skipthelibrary;

/**
 * Created by Luke Slevinsky on 2019-02-15.
 */
public class State {
    private BookStatus bookStatus;
    private Location location;
    private HandoffState handoffState;

    public State() {
        this(BookStatus.AVAILABLE,null,null);
    }

    public State(BookStatus bookStatus, Location location, HandoffState handoffState) {
        this.bookStatus = bookStatus;
        this.location = location;
        this.handoffState = handoffState;
    }

    public BookStatus getBookStatus() {
        return bookStatus;
    }

    public void setBookStatus(BookStatus bookStatus) {
        this.bookStatus = bookStatus;
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
