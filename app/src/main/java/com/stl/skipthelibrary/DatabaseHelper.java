package com.stl.skipthelibrary;

import android.util.Log;

/**
 * Created by Luke Slevinsky on 2019-02-18.
 */
public class DatabaseHelper {
    public final String TAG = getClass().getSimpleName();
//    public DatabaseReference myRef;

    private DatabaseHelper() {
//        myRef = FirebaseDatabase.getInstance();
    }


    public User pullUser(String userName) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public BookDescription pullBookDescription(String isbn) {
        Log.d(TAG, "pullBookDescription: Here we should pull the book desciption");
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public Rating pullBookRating(String isbn) {
        Log.d(TAG, "pullBookDescription: Here we should pull the book desciption");
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void sendNotificaition(String userName, Notification notification) {
        Log.d(TAG, "sendNotificaition: Here we should send notifications");
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public Boolean authenticateUser(String userName, Notification notification) {
        Log.d(TAG, "sendNotificaition: Here we should send notifications");
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
