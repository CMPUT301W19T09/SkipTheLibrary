package com.stl.skipthelibrary;

import java.util.ArrayList;

/**
 * Created by Luke Slevinsky on 2019-02-15.
 */
public class RequestHandler {
    private State state;
    private ArrayList<User> pendingRequestors;
    private User acceptedRequestors;

    public RequestHandler(State state) {
        this(state,new ArrayList<User>(), null);

    }

    public RequestHandler(State state, ArrayList<User> pendingRequestors, User acceptedRequestors) {
        this.state = state;
        this.pendingRequestors = pendingRequestors;
        this.acceptedRequestors = acceptedRequestors;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public ArrayList<User> getPendingRequestors() {
        return pendingRequestors;
    }

    public void setPendingRequestors(ArrayList<User> pendingRequestors) {
        this.pendingRequestors = pendingRequestors;
    }

    public User getAcceptedRequestors() {
        return acceptedRequestors;
    }

    public void setAcceptedRequestors(User acceptedRequestors) {
        this.acceptedRequestors = acceptedRequestors;
    }
}
