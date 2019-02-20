package com.stl.skipthelibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import androidx.annotation.NonNull;

/**
 * Created by Luke Slevinsky on 2019-02-18.
 */
public class DatabaseHelper {
    public final String TAG = getClass().getSimpleName();
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private Context context;

    public DatabaseHelper(Context context) {
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        this.context = context;
    }


    public void createAccount(final String userName, String password, final String firstName, final String lastName, final String emailAddress, final String phoneNumber){
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(emailAddress, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Authentication Succeeded.", Toast.LENGTH_SHORT).show();
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            accountCreated(new User(firstName + " " + lastName, userName, firebaseUser.getUid()
                                    , new ContactInfo(emailAddress, phoneNumber, null)));
                        }
                        else {
                            Toast.makeText(context, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void accountCreated(User user){
        databaseReference.child("Users").child(user.getUserID()).setValue(user);
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public void signIn(String email, String password){
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            Toast.makeText(context, "Authentication successful.", Toast.LENGTH_SHORT).show();
                            pullUserSignIn(firebaseUser.getUid());
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(context, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void pullUserSignIn(String userID) {
        databaseReference.child("Users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                UserRetrieved(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void UserRetrieved(User user){
        Log.d(TAG, "User Recieved: username = " + user.toString());
        Gson gson = new Gson();
        Intent intent = new Intent(context, NotificationActivity.class);
        intent.putExtra("User", gson.toJson(user));
        context.startActivity(intent);
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


    //getters and setters

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    public void setFirebaseAuth(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    public FirebaseDatabase getDatabase() {
        return database;
    }

    public void setDatabase(FirebaseDatabase database) {
        this.database = database;
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public void setDatabaseReference(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
