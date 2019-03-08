package com.stl.skipthelibrary.Entities;

import com.stl.skipthelibrary.Enums.BookStatus;
import com.stl.skipthelibrary.Enums.HandoffState;

import java.util.Objects;

/**
 * This class fully describes the state of a book
 */
public class State {
    private BookStatus bookStatus;
    private Location location;
    private HandoffState handoffState;

    /**
     * The empty constructor
     */
    public State() {
        this(BookStatus.AVAILABLE,null,HandoffState.NULL_STATE);
    }

    /**
     * The full constructor
     * @param bookStatus: the book's status
     * @param location: the handoff location
     * @param handoffState: the book's handoff state
     */
    public State(BookStatus bookStatus, Location location, HandoffState handoffState) {
        this.bookStatus = bookStatus;
        this.location = location;
        this.handoffState = handoffState;
    }


    /**
     * Get the book's book status
     * @return the book's book status
     */
    public BookStatus getBookStatus() {
        return bookStatus;
    }

    /**
     * Set the book's book status
     * @param bookStatus: the book's book status
     */
    public void setBookStatus(BookStatus bookStatus) {
        this.bookStatus = bookStatus;
    }

    /**
     * Get the handoff location
     * @return the handoff location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Set the handoff location
     * @param location: the handoff location
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Get the book's handoff state
     * @return the book's handoff state
     */
    public HandoffState getHandoffState() {
        return handoffState;
    }

    /**
     * Set the book's handoff state
     * @param handoffState: the book's handoff state
     */
    public void setHandoffState(HandoffState handoffState) {
        this.handoffState = handoffState;
    }

    /**
     * Determines if the state is identical to another object
     * @param o: An object to compare the state to
     * @return true if o is identical to the current state
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof State)) return false;
        State state = (State) o;
        return getBookStatus() == state.getBookStatus() &&
                Objects.equals(getLocation(), state.getLocation()) &&
                getHandoffState() == state.getHandoffState();
    }

    /**
     * Calculate and return the state's hashcode
     * @return the state's hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(getBookStatus(), getLocation(), getHandoffState());
    }

}
