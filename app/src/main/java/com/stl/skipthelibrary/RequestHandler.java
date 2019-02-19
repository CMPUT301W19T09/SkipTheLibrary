package com.stl.skipthelibrary;

import java.util.ArrayList;

/**
 * Created by Luke Slevinsky on 2019-02-15.
 */
public class RequestHandler {
    private State state;
    private ArrayList<String> requestors; // User name
    private String acceptedRequestor; // User name

    public RequestHandler(State state) {
        this(state,new ArrayList<String>(), null);

    }

    public RequestHandler(State state, ArrayList<String> pendingRequestors, String acceptedRequestor) {
        this.state = state;
        this.requestors = pendingRequestors;
        this.acceptedRequestor = acceptedRequestor;
    }

    // GETTERS AND SETTERS
    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public ArrayList<String> getRequestors() {
        return requestors;
    }

    public void setRequestors(ArrayList<String> requestors) {
        this.requestors = requestors;
    }

    public String getAcceptedRequestor() {
        return acceptedRequestor;
    }

    public void setAcceptedRequestor(String acceptedRequestor) {
        this.acceptedRequestor = acceptedRequestor;
    }
    ////////



    public void lendBook(){
        getState().setHandoffState(HandoffState.OWNER_LENT);
    }

    public void confirmBorrowed(){
        getState().setBookStatus(BookStatus.BORROWED);
        getState().setHandoffState(HandoffState.BORROWER_RECEIVED);
    }

    public void returnBook(){
        getState().setBookStatus(BookStatus.BORROWED);
        getState().setHandoffState(HandoffState.BORROWER_RETURNED);
    }

    public void confirmReturned(){
       getState().setBookStatus(BookStatus.AVAILABLE);
       getState().setHandoffState(HandoffState.OWNER_RECEIVED);
    }

    public void addRequestor(String user){
        getRequestors().add(user);
    }

    public void acceptRequestor(String user){
        if (!getRequestors().contains(user)) { throw new RequestorsUnavailableException(); }
        setAcceptedRequestor(user);
//        for (String s: getRequestors()) {
//            sendNotificaition()
//        }
        getRequestors().clear();
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void denyRequestor(String user){
        if (!getRequestors().contains(user)) { throw new RequestorsUnavailableException(); }
        setAcceptedRequestor(user);
//        sendNotificaition();
        getRequestors().remove(user);
        throw new UnsupportedOperationException("Not implemented yet");
    }

}
