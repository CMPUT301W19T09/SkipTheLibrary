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
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void confirmBorrowed(){
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void returnBook(){
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void confirmReturned(){
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void addRequestor(User user){
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void acceptRequestor(User user){
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void denyRequestor(User user){
        throw new UnsupportedOperationException("Not implemented yet");
    }

}
