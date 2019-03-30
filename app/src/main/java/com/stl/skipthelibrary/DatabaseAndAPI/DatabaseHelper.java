package com.stl.skipthelibrary.DatabaseAndAPI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.stl.skipthelibrary.Activities.ViewBookActivity;
import com.stl.skipthelibrary.Entities.Book;
import com.stl.skipthelibrary.Entities.ContactInfo;
import com.stl.skipthelibrary.Activities.LoginActivity;
import com.stl.skipthelibrary.Activities.MyBooksActivity;
import com.stl.skipthelibrary.Activities.NotificationActivity;

import com.stl.skipthelibrary.R;

import com.stl.skipthelibrary.Entities.Notification;
import com.stl.skipthelibrary.Enums.NotificationType;
import com.stl.skipthelibrary.Entities.Rating;

import com.stl.skipthelibrary.Singletons.CurrentLocation;
import com.stl.skipthelibrary.Singletons.CurrentUser;
import com.stl.skipthelibrary.Entities.User;
import com.stl.skipthelibrary.Entities.ViewableImage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.stl.skipthelibrary.Activities.ViewBookActivity.ISBN;

/**
 * The database helper. This class interacts heavily with the database to do all database heavy
 * tasks that can be abstracted outside of an activity.
 */
public class DatabaseHelper {
    public final String TAG = getClass().getSimpleName();
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private Context context;

    /**
     * The default constructor
     * @param context: the current construct
     */
    public DatabaseHelper(Context context) {
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        firebaseUser = firebaseAuth.getCurrentUser();
        this.context = context;
    }

    /**
     * Create an Account in FireBase if the username is unique
     * @param userName: the user's username
     * @param password: the user's password
     * @param firstName: the user's first name
     * @param lastName: the user's last name
     * @param emailAddress: the user's email address
     * @param phoneNumber: the user's phone number
     * @param image: the user's profile picture
     */
    public void createAccountIfValid(final String userName, final String password, final String firstName, final String lastName, final String emailAddress, final String phoneNumber, final ViewableImage image){
        databaseReference.child("Users").orderByChild("userName").equalTo(userName)
                .addListenerForSingleValueEvent(new ValueEventListener() {

            /**
             * Determines if the username is already taken, if not creates the account
             * @param dataSnapshot: the current data snapshot
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Toast.makeText(context, "That username already exists. Please choose another",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    createAccount(userName, password, firstName, lastName, emailAddress, phoneNumber, image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Creates a user's account
     * @param userName: the user's username
     * @param password: the user's password
     * @param firstName: the user's first name
     * @param lastName: the user's last name
     * @param emailAddress: the user's email address
     * @param phoneNumber: the user's phone number
     * @param image: the user's profile picture
     */
    private void createAccount(final String userName, String password, final String firstName, final String lastName, final String emailAddress, final String phoneNumber, final ViewableImage image){
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(emailAddress, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Authentication Succeeded.", Toast.LENGTH_SHORT).show();
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            accountCreated(new User(firstName + " " + lastName, userName, firebaseUser.getUid()
                                    , new ContactInfo(emailAddress, phoneNumber, null), image));
                        }
                        else {
                            Toast.makeText(context, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * After an account is created we update the database and go back to the login screen
     * @param user: the created user
     */
    private void accountCreated(User user){
        databaseReference.child("Users").child(user.getUserID()).setValue(user);
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    /**
     * Sign a user in, if not successful display why
     * @param email: the user's email address
     * @param password: the user's password
     */
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

    /**
     * Pull a signed in user from the database through their userID
     * @param userID: the user's Unique ID
     */
    public void pullUserSignIn(String userID) {
        databaseReference.child("Users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            /**
             * Get the user from the data snapshot
             * @param dataSnapshot: the current data snapshot
             */
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

    /**
     * Once a user is retrieved from the database we can set them to be the current user
     * @param user: the current user
     */
    private void UserRetrieved(User user){
        if (user == null){
            Toast.makeText(context, "Your account has been deleted.", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "User Recieved: username = " + user.toString());

        CurrentUser.setUser(user);

        Gson gson = new Gson();
        Intent intent = new Intent(context, NotificationActivity.class);
        context.startActivity(intent);

        CurrentLocation.getInstance().updateLocation(context);
    }

    /**
     * Determines if the user is currently logged in
     * @return true if the user is logged in, false otherwise.
     */
    public boolean isUserLoggedIn(){
        return firebaseUser != null;
    }

    /**
     * Sign the user out
     */
    public void signOut(){
        firebaseAuth.signOut();
    }

    /**
     * Add a book to the database if the user does not have any other identical books
     * @param book: the book to add
     * @param displayMessageAndFinish: whether or not to print a display message and finish
     */
    public void addBookIfValid(final Book book, final boolean displayMessageAndFinish){
        databaseReference.child("Books")
                .orderByChild("ownerUserName")
                .equalTo(CurrentUser.getInstance().getUserName())
                .addListenerForSingleValueEvent(new ValueEventListener() {
            /**
             * Determines if the user already has a book with the same ISBN, if not then adds it
             * @param dataSnapshot: the current snap shot
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean validISBN = true;
                for (DataSnapshot data: dataSnapshot.getChildren()){
                    Book currentBook = data.getValue(Book.class);
                    if (currentBook.getISBN().equals(book.getISBN())){
                        Toast.makeText(context, "You already have this book!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                addValidBook(book, displayMessageAndFinish);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Adds a book to the user's collection in firebase
     * @param book: the book to add
     * @param displayMessageAndFinish: whether or not to print a display message and finish
     */
    private void addValidBook(Book book, boolean displayMessageAndFinish){
        getDatabaseReference().child("Books").child(book.getUuid())
                .setValue(book);

        if(displayMessageAndFinish){
            Toast.makeText(context, "Book Added!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context.getApplicationContext(), MyBooksActivity.class);
            intent.putExtra(ISBN,book.getISBN());
            intent.putExtra(ViewBookActivity.UUID,book.getUuid());
            ((Activity) context).setResult(Activity.RESULT_OK, intent);
            ((Activity) context).finish();
        }
    }


    /**
     * Updates a book in the user's collection in firebase
     * @param newbook: the book to update
     */
    public void updateBook(Book newbook){
        databaseReference.child("Books").child(newbook.getUuid()).setValue(newbook);
        Log.d("Updating book", "new book should be replaced");
    }

    /**
     * Updates the current user in firebase
     */
    public boolean updateCurrentUser(User user){
        Log.d(TAG, "updateCurrentUser: "+user);
        String email = user.getContactInfo().getEmail();
        Log.d(TAG, "updateCurrentUser: "+email +" "+getFirebaseAuth().getCurrentUser().getEmail());
        if (!email.equals(getFirebaseAuth().getCurrentUser().getEmail())) {
            promptPassword(user);
            Log.d(TAG, "updateCurrentUser: UPDATING IN FIREBASE");
        } else {
            CurrentUser.setUser(user);
            getDatabaseReference().child("Users").child(user.getUserID()).setValue(user);
        }
        Log.d("Updating user", "new user should be replaced");
        return true;
    }

    /**
     * Updates a users email information in firebase
     * @param password: the password of the account in question
     */
    private void updateEmail(String password, final User proposedUser) {
        String oldEmail = getFirebaseAuth().getCurrentUser().getEmail();
        final String newEmail = proposedUser.getContactInfo().getEmail();

        // Get auth credentials from the user for re-authentication
        AuthCredential credential = EmailAuthProvider
                .getCredential(oldEmail, password); // Current Login Credentials \\
        // Prompt the user to re-provide their sign-in credentials
        firebaseUser.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "User re-authenticated.");
                        //Now change your email address \\
                        //----------------Code for Changing Email Address----------\\
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        user.updateEmail(newEmail)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "User email address updated.");
                                            getDatabaseReference().child("Users")
                                                    .child(CurrentUser.getInstance().getUserID()).
                                                    setValue(proposedUser);
                                            CurrentUser.setUser(proposedUser);
                                            Toast.makeText(getContext(),"Updated Email", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Toast.makeText(getContext(), "Update Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                        //----------------------------------------------------------\\
                    }
                });
    }

    /**
     * Prompts a user to input their password in order to update authentication related contact
     *  info
     */
    public void promptPassword(final User proposedUser) {
        LayoutInflater li = LayoutInflater.from(getContext());
        View promptsView = li.inflate(R.layout.password_prompt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextPassword);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                Log.d(TAG, "onClick: " + userInput.getText().toString());
                                String password = userInput.getText().toString();
                                updateEmail(password,proposedUser);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    /**
     * Updates a books rating in firebase based off of the book UUID
     * @param uuid: the book to update
     * @param rating: The new rating
     */
    public void updateRating(String uuid, Rating rating){
        databaseReference.child("Books").child(uuid).child("rating").setValue(rating);
        Log.d("Updating book", "rating should be replaced");
    }


    /**
     * Delete a book from firebase
     * @param book: the book to delete
     */
    public void deleteBook(Book book){
        getDatabaseReference().child("Books").child(book.getUuid()).removeValue();

        deleteAllNotifications(book.getUuid());

    }

    private void deleteAllNotifications(String bookID) {
        getDatabaseReference().child("Notifications").orderByChild("bookID")
                .equalTo(bookID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Notification notification = dataSnapshot.getValue(Notification.class);
                databaseReference.child("Notifications").child(notification.getUuid()).removeValue();
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
        });
    }


    public void sendNotification(NotificationType notificationType, String user, String bookID, String bookName) {
        Notification notification = new Notification(notificationType, user, bookID, bookName);
        getDatabaseReference().child("Notifications").child(notification.getUuid()).setValue(notification);
    }


    /**
     * Delete a notification from firebase
     * @param notification: the notification to delete
     */
    public void deleteNotification(Notification notification){
        getDatabaseReference().child("Notifications").child(notification.getUuid()).removeValue();
    }


    /**
     * Gets the current FireBaseAuth instance
     * @return the current FireBaseAuth instance
     */
    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    /**
     * Sets the current FireBaseAuth instance
     * @param firebaseAuth: the current FireBaseAuth instance
     */
    public void setFirebaseAuth(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    /**
     * Gets the current FireBaseDatabase instance
     * @return the current FireBaseDatabase instance
     */
    public FirebaseDatabase getDatabase() {
        return database;
    }

    /**
     * Sets the current FireBaseDatabase instance
     * @param database: the current FireBaseDatabase instance
     */
    public void setDatabase(FirebaseDatabase database) {
        this.database = database;
    }

    /**
     * Gets the current FireBaseReference instance
     * @return the current FireBaseReference instance
     */
    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    /**
     * Sets the current FireBaseReference instance
     * @param databaseReference: the current FireBaseReference instance
     */
    public void setDatabaseReference(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    /**
     * Sets the current context
     * @return the current context
     */
    public Context getContext() {
        return context;
    }

    /**
     * Sets the context
     * @param context: the current context
     */
    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * Gets the current FirebaseUser instance
     * @return the current FirebaseUser instance
     */
    public FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }

    /**
     * Sets the current FirebaseUser instance
     * @param firebaseUser:the current FirebaseUser instance
     */
    public void setFirebaseUser(FirebaseUser firebaseUser) {
        this.firebaseUser = firebaseUser;
    }

}
