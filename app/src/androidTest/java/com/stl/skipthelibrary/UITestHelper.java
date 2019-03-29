package com.stl.skipthelibrary;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.stl.skipthelibrary.DatabaseAndAPI.DatabaseHelper;
import com.stl.skipthelibrary.Entities.Book;
import com.stl.skipthelibrary.Entities.Rating;
import com.stl.skipthelibrary.Entities.User;
import com.stl.skipthelibrary.Singletons.CurrentUser;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * See US010101Test for example on how to use. USE FOR EVERY UI TEST
 */
public class UITestHelper {
    private static final String testUserID = "XavBff02PYPykAxmnBFoy3moBTY2";
    public static final String userName = "UITest";
    public static final String borrowerUserId = "5OlDDyriXkQXwtlpSZALozbpB972";
    private User testUser;
    private ArrayList<Book> books;
    private DatabaseHelper databaseHelper = new DatabaseHelper(null);
    private ChildEventListener childEventListener;
    private UITestSemaphore uiTestSemaphore;

    public UITestHelper(final boolean loadTestUser, final boolean loadBooks, @NonNull final ArrayList<Book> books) throws InterruptedException {
        databaseHelper.getDatabaseReference().child("TestSemaphore").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uiTestSemaphore = dataSnapshot.getValue(UITestSemaphore.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Thread.sleep(2000);

        if (!uiTestSemaphore.isInUse()){
            uiTestSemaphore.setInUse(true);
            databaseHelper.getDatabaseReference().child("TestSemaphore").setValue(uiTestSemaphore);
            start(loadTestUser, loadBooks, books);
        }
        else{
            throw new RuntimeException("A test is currently running");
        }
    }

    private void start(boolean loadTestUser, boolean loadBooks, @NonNull ArrayList<Book> books) throws InterruptedException {
        refreshRatings();
        deleteNotifcations();
        Thread.sleep(2000);
        if(loadTestUser){
            loadTestUser();
        }
        if (loadBooks){
            this.books = books;
            loadBooks();
        }
    }

    public void loadTestUser() throws InterruptedException {
        databaseHelper.getDatabaseReference().child("Users").child(testUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                testUser = dataSnapshot.getValue(User.class);
                if (testUser == null){
                    throw new RuntimeException("TEST USER HAS BEEN DELETED");
                }
                CurrentUser.setUser(testUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Thread.sleep(2000);
    }

    private void refreshRatings() {
        databaseHelper.getDatabaseReference().child("Users").child(testUserID)
                .child("borrowerRating").setValue(new Rating());
        databaseHelper.getDatabaseReference().child("Users").child(testUserID)
                .child("ownerRating").setValue(new Rating());
        databaseHelper.getDatabaseReference().child("Users").child(borrowerUserId)
                .child("borrowerRating").setValue(new Rating());
        databaseHelper.getDatabaseReference().child("Users").child(borrowerUserId)
                .child("ownerRating").setValue(new Rating());
    }

    public void loadBooks() throws InterruptedException {
        deleteBooks();
        for (Book book: books){
            databaseHelper.getDatabaseReference().child("Books").child(book.getUuid()).setValue(book);
        }
        Thread.sleep(2000);
    }

    public void finish() throws InterruptedException {
        deleteBooks();
        deleteNotifcations();
        refreshRatings();
        uiTestSemaphore.setInUse(false);
        databaseHelper.getDatabaseReference().child("TestSemaphore").setValue(uiTestSemaphore);
    }

    private void deleteNotifcations() {
        databaseHelper.getDatabaseReference().child("Notifications").removeValue();
    }


    private void deleteBooks() throws InterruptedException {
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Book book = dataSnapshot.getValue(Book.class);
                databaseHelper.getDatabaseReference().child("Books").child(book.getUuid()).removeValue();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseHelper.getDatabaseReference().child("Books")
                .orderByChild("ownerUserName")
                .equalTo(userName).addChildEventListener(childEventListener);

        Thread.sleep(2500);

        databaseHelper.getDatabaseReference().child("Books")
                .orderByChild("ownerUserName")
                .equalTo(userName).removeEventListener(childEventListener);
    }

    public void deleteUsersBooks(String userName) throws InterruptedException {
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Book book = dataSnapshot.getValue(Book.class);
                databaseHelper.getDatabaseReference().child("Books").child(book.getUuid()).removeValue();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseHelper.getDatabaseReference().child("Books")
                .orderByChild("ownerUserName")
                .equalTo(userName).addChildEventListener(childEventListener);

        Thread.sleep(2500);

        databaseHelper.getDatabaseReference().child("Books")
                .orderByChild("ownerUserName")
                .equalTo(userName).removeEventListener(childEventListener);
    }

}
