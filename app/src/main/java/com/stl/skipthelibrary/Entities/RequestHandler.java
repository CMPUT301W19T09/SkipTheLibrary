package com.stl.skipthelibrary.Entities;

import com.stl.skipthelibrary.DatabaseAndAPI.DatabaseHelper;
import com.stl.skipthelibrary.Enums.BookStatus;
import com.stl.skipthelibrary.Enums.HandoffState;
import com.stl.skipthelibrary.Enums.NotificationType;
import com.stl.skipthelibrary.Exceptions.RequestorsUnavailableException;

import java.util.ArrayList;
import java.util.Objects;

/**
 * This class handles all requests for a book
 */
public class RequestHandler {
    private State state;
    private ArrayList<String> requestors; // User name
    private String acceptedRequestor; // User name

    /**
     * The empty constructor
     */
    public RequestHandler() {
        this(new State(),new ArrayList<String>(), "");

    }

    /**
     * The constructor that only takes in a state
     * @param state: the state of the book
     */
    public RequestHandler(State state) {
        this(state,new ArrayList<String>(), "");

    }

    /**
     * The full constructor
     * @param state: the state of the book
     * @param pendingRequestors: an arraylist of pending requesters usernames
     * @param acceptedRequestor: the accepted requester's username
     */
    public RequestHandler(State state, ArrayList<String> pendingRequestors, String acceptedRequestor) {
        this.state = state;
        this.requestors = pendingRequestors;
        this.acceptedRequestor = acceptedRequestor;
    }


    /**
     * Get the state of the book
     * @return the state of the book
     */
    public State getState() {
        return state;
    }

    /**
     * Set the state of the book
     * @param state: the state of the book
     */
    public void setState(State state) {
        this.state = state;
    }

    /**
     * Get the current requestors
     * @return the requestors usernames
     */
    public ArrayList<String> getRequestors() {
        return requestors;
    }

    /**
     * Set the current requestors usernames
     * @param requestors: the requestors usernames
     */
    public void setRequestors(ArrayList<String> requestors) {
        this.requestors = requestors;
    }

    /**
     * Get the accepted requestor's username
     * @return the accepted requestor's username
     */
    public String getAcceptedRequestor() {
        return acceptedRequestor;
    }

    /**
     * Set the accepted requestor's username
     * @param acceptedRequestor: the accepted requestor's username
     */
    public void setAcceptedRequestor(String acceptedRequestor) {
        this.acceptedRequestor = acceptedRequestor;
    }


    /**
     * Change a book's handoff state to owner lent
     */
    public void lendBook(){
        getState().setHandoffState(HandoffState.OWNER_LENT);
    }

    /**
     * Change a book's handoff state to borrower recieved
     */
    public void confirmBorrowed(){
        getState().setBookStatus(BookStatus.BORROWED);
        getState().setHandoffState(HandoffState.BORROWER_RECEIVED);
    }

    /**
     * Change the book's status to borrower returned
     */
    public void returnBook(){
        getState().setBookStatus(BookStatus.BORROWED);
        getState().setHandoffState(HandoffState.BORROWER_RETURNED);
    }

    /**
     * Change the state to confirm requested
     */
    public void confirmReturned(){
       getState().setBookStatus(BookStatus.AVAILABLE);
       getState().setHandoffState(HandoffState.NULL_STATE);
       setAcceptedRequestor("");
    }

    /**
     * Add a requestor
     * @param user: the user's username
     */
    public void addRequestor(String user){
        getRequestors().add(user);
    }

    /**
     * Accept a request from a user
     * @param user: the user's username to accept the request from
     */
    public void acceptRequestor(String user, String bookID, String bookName) throws RequestorsUnavailableException {
        if (!getRequestors().contains(user)) { throw new RequestorsUnavailableException(); }
        setAcceptedRequestor(user);
        requestors.remove(user);
        denyAllOtherRequestors(bookID, bookName);
        getState().setBookStatus(BookStatus.ACCEPTED);
        getState().setHandoffState(HandoffState.READY_FOR_PICKUP);

        DatabaseHelper databaseHelper = new DatabaseHelper(null);
        databaseHelper.sendNotification(NotificationType.REQUEST_ACCEPTED, user, bookID, bookName);
    }

    private void denyAllOtherRequestors(String bookID, String bookName){
        ArrayList<String> copyRequestors = new ArrayList<>(requestors);
        for (String requestor: copyRequestors) {
            denyRequestor(requestor, bookID, bookName);
        }
        getRequestors().clear();
    }

    /**
     * Deny a request from a user
     * @param user: the user's username to deny the request from
     */
    public void denyRequestor(String user, String bookID, String bookName) throws  RequestorsUnavailableException{
        if (!getRequestors().contains(user)) { throw new RequestorsUnavailableException(); }
        getRequestors().remove(user);
        if (getRequestors().size() == 0 && (acceptedRequestor.equals(""))){
            getState().setBookStatus(BookStatus.AVAILABLE);
        }

        DatabaseHelper databaseHelper = new DatabaseHelper(null);
        databaseHelper.sendNotification(NotificationType.REQUEST_DENIED, user, bookID, bookName);
    }

    /**
     * Determines if the requesthandler is identical to another object
     * @param o: An object to compare the requesthandler to
     * @return true if o is identical to the current requesthandler
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RequestHandler)) return false;
        RequestHandler that = (RequestHandler) o;
        return Objects.equals(getState(), that.getState()) &&
                Objects.equals(getRequestors(), that.getRequestors()) &&
                Objects.equals(getAcceptedRequestor(), that.getAcceptedRequestor());
    }

    /**
     * Calculate and return the requesthandler's hashcode
     * @return the requesthandler's hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(getState(), getRequestors(), getAcceptedRequestor());
    }
}
