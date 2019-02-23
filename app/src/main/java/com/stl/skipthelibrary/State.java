package com.stl.skipthelibrary;

import java.util.Objects;

/**
 * Created by Luke Slevinsky on 2019-02-15.
 */
public class State {
    private BookStatus bookStatus;
    private Location location;
    private HandoffState handoffState;

    // CONSTRUCTORS
    public State() {
        this(BookStatus.AVAILABLE,null,null);
    }

    public State(BookStatus bookStatus, Location location, HandoffState handoffState) {
        this.bookStatus = bookStatus;
        this.location = location;
        this.handoffState = handoffState;
    }

    // GETTERS AND SETTERS


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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof State)) return false;
        State state = (State) o;
        return getBookStatus() == state.getBookStatus() &&
                Objects.equals(getLocation(), state.getLocation()) &&
                getHandoffState() == state.getHandoffState();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBookStatus(), getLocation(), getHandoffState());
    }
    //////

}
