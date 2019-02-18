package com.stl.skipthelibrary;

import android.content.Context;
import android.util.Log;

/**
 * Created by Luke Slevinsky on 2019-02-18.
 */
public class DatabaseHelper {
    public final String TAG = getClass().getSimpleName();
    private static final DatabaseHelper ourInstance = new DatabaseHelper();
    private Context mContext;

    public static DatabaseHelper getInstance() {
        return ourInstance;
    }

    private DatabaseHelper(Context context) {
        try {
//            mContext = context;
//            Firebase.setAndroidContext(context);
//            userRef = FirebaseDatabase.getInstance().getReference().child("Users");
//            roomRef = FirebaseDatabase.getInstance().getReference().child("Rooms");
            Log.d(TAG, "DatabaseHelper: Not much goin on yet it seems");
        }
        catch(Exception e){
            Log.d(TAG,"ERROR: "+e.getMessage());
        }
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
