package com.stl.skipthelibrary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;


public class ViewBookActivity extends AppCompatActivity {
    final public static String TAG = "ViewBookActivityTag";
    private DatabaseHelper databaseHelper;

    //Book Description Elements & Fields
    private Book book;
    private User user;
    private EditText title_element;
    private EditText author_element;
    private RatingBar rating_element;
    private EditText synopsis_element;
    private ImageButton edit_button;
    private ImageButton save_button;
    private ViewStub stub;
    private View inflated;
    ChildEventListener childEventListener;

    //Owner Requested Fields


    //Owner Handoff Elements & Fields
    private Button button;
    private View view;
    private String isbn_code;

    //Owner Return Elements & Fields


    //Borrower Request Elements & Fields


    //Borrower Handoff Elements & Fields


    //Borrower Return Elements & Fields


    //Pending Screen Elements & Fields


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Book Description
        setContentView(R.layout.book_details);
        databaseHelper = new DatabaseHelper(this);
        stub = findViewById(R.id.generic_bottom_screen_id);
        user = CurrentUser.getInstance();
        bindBookDescriptionElements();
        getIncomingIntents();
    }

    /**
     * This method catches the incoming data (BookUUID) that is sent via an intent on screen switch.
     */
    private void getIncomingIntents() {
        String bookID = getIntent().getExtras().getString("bookUUID");

        childEventListener = databaseHelper.getDatabaseReference()
                .child("Books").orderByChild("uuid").equalTo(bookID)
                .addChildEventListener(new ChildEventListener() {
                    /**
                     * When a new child is added add it to the list of books
                     * @param dataSnapshot: the current snapshot
                     * @param s: the ID
                     */
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        book = dataSnapshot.getValue(Book.class);
                        handleBookArrival();
                    }

                    /**
                     * When a child is changes update them
                     * @param dataSnapshot: the current snapshot
                     * @param s: the ID
                     */
                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Toast.makeText(ViewBookActivity.this, "This book has been modified.",
                                Toast.LENGTH_SHORT).show();
                        ViewBookActivity.this.finish();

                    }

                    /**
                     * If a child is deleted delete them from the list of our books
                     * @param dataSnapshot: the current snapshot
                     */
                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        Toast.makeText(ViewBookActivity.this,
                                "The book you're looking at has been deleted.", Toast.LENGTH_SHORT).show();
                        ViewBookActivity.this.finish();
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }

    private void handleBookArrival() {

        fillBookDescriptionFields();

        //If user is owner of book, allow for edittability
        if (user.getUserName().equals(book.getOwnerUserName())) {
            edit_button.setVisibility(View.VISIBLE);
            save_button.setVisibility(View.GONE);
        }
        else {
            edit_button.setVisibility(View.GONE);
            save_button.setVisibility(View.GONE);
        }
        selectBottom();
    }

    private void selectBottom() {
        BookStatus bookStatus = book.getRequests().getState().getBookStatus();
        HandoffState bookHandoffState = book.getRequests().getState().getHandoffState();

        if (user.getUserName().equals(book.getOwnerUserName())) {//user is owner
            if (bookStatus==BookStatus.REQUESTED) {
                setBottomScreen(R.layout.bookscreen_owner_requested);
                configureOwnerRequested();
            } else if (bookHandoffState==HandoffState.READY_FOR_PICKUP) {
                setBottomScreen(R.layout.bookscreen_owner_handoff);
                configureOwnerHandOff();
            } else if (bookHandoffState==HandoffState.BORROWER_RETURNED) {
                setBottomScreen(R.layout.bookscreen_owner_return);
                configureOwnerReturn();
            } else{
                setBottomScreen(R.layout.bookscreen_pending);
                configureOwnerPending();
            }
            edit_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setBookDescriptionFieldsEditable(true);
                    save_button.setVisibility(View.VISIBLE);
                    edit_button.setVisibility(View.GONE);
                }
            });

            save_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setBookDescriptionFieldsEditable(false);
                    save_button.setVisibility(View.GONE);
                    edit_button.setVisibility(View.VISIBLE);
                    updateBookDesriptionFields();
                }
            });
        }
        else{//user is borrower
            if ((!book.userIsInterested(user.getUserName()) && bookStatus==BookStatus.REQUESTED) ||
                    bookStatus==BookStatus.AVAILABLE) {
                setBottomScreen(R.layout.bookscreen_borrower_request);
                configureBorrowerRequest();
            } else if (bookHandoffState==HandoffState.READY_FOR_PICKUP){
                setBottomScreen(R.layout.bookscreen_borrower_handoff);
                configureBorrowerHandoff();
            } else if (bookHandoffState==HandoffState.BORROWER_RECEIVED){
                setBottomScreen(R.layout.bookscreen_borrower_return);
                configureBorrowerReturn();
            } else{
                setBottomScreen(R.layout.bookscreen_pending);
                configureBorrowerPending();
            }
        }
    }

    private void setBottomScreen(int resourcefile){
        stub.setLayoutResource(resourcefile);
        inflated = stub.inflate();
    }

    //Borrower Request
    private void configureBorrowerRequest() {
        Button button = inflated.findViewById(R.id.requestButton);
        button.setText(R.string.requestbutton);
        button.setBackgroundColor(getColor(R.color.REQUESTED));
        button.setTextColor(getColor(R.color.black));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestHandler handler = book.getRequests();
                handler.addRequestor(user.getUserName());
                handler.getState().setBookStatus(BookStatus.REQUESTED);
                databaseHelper.updateBook(book);
            }
        });
    }

    //Borrower Handoff
    private void configureBorrowerHandoff() {

    }

    //Borrower Return
    private void configureBorrowerReturn() {

    }

    //Pending Screen
    private void configureBorrowerPending() {

    }

    //Owner Requested
    private void configureOwnerRequested() {

    }

    //Owner HandOff
    private void configureOwnerHandOff() {

    }

    //Owner Return
    private void configureOwnerReturn() {

    }

    //Owner Pending
    private void configureOwnerPending() {

    }

    private void bindBookDescriptionElements() {
        title_element = findViewById(R.id.title_element);
        author_element = findViewById(R.id.author_element);
        rating_element = findViewById(R.id.rating_bar_element);
        synopsis_element = findViewById(R.id.synopsis_element);
        edit_button = findViewById(R.id.edit_button);
        save_button = findViewById(R.id.save_button);

        save_button.setVisibility(View.GONE);
        edit_button.setVisibility(View.GONE);
        setBookDescriptionFieldsEditable(false);
    }

    private void fillBookDescriptionFields(){
        title_element.setText(book.getDescription().getTitle());
        author_element.setText(book.getDescription().getAuthor());
        rating_element.setMax(book.getRating().getMaxRating());
        rating_element.setNumStars((int) Math.round(book.getRating().getAverageRating()));
        synopsis_element.setText(book.getDescription().getSynopsis());
    }

    private void setBookDescriptionFieldsEditable(Boolean isEditable) {
        if (isEditable) {
            title_element.setEnabled(true);
            author_element.setEnabled(true);
            rating_element.setEnabled(false);
            synopsis_element.setEnabled(true);
        } else {
            title_element.setEnabled(false);
            author_element.setEnabled(false);
            rating_element.setEnabled(false);
            synopsis_element.setEnabled(false);
        }
    }

    private void updateBookDesriptionFields(){
        book.getDescription().setTitle(title_element.getText().toString());
        book.getDescription().setAuthor(author_element.getText().toString());
        book.getDescription().setSynopsis(synopsis_element.getText().toString());
        book.getRating().addRating((double) rating_element.getNumStars());
        databaseHelper.updateBook(book);
    }


    //Owner Requested


    //Owner Return and Owner Handoff
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == ScannerActivity.SCAN_BOOK) {
            if (resultCode == RESULT_OK) {
                RequestHandler requestHandler = new RequestHandler();
                isbn_code = data.getStringExtra("ISBN");

                if (isbn_code.equals(book.getISBN()) && CurrentUser.getInstance().getUserName().equals(book.getOwnerUserName())){
                    switch (book.getRequests().getState().getHandoffState()) {
                        case READY_FOR_PICKUP:
                            requestHandler.lendBook();
                            Toast.makeText(this, "The Book is Lent", Toast.LENGTH_SHORT).show();
                            break;
                        case BORROWER_RETURNED:
                            requestHandler.confirmReturned();
                            Toast.makeText(this, "The Book is Returned", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }else if(isbn_code.equals(book.getISBN()) && !CurrentUser.getInstance().getUserName().equals(book.getOwnerUserName())){
                    switch (book.getRequests().getState().getHandoffState()) {
                        case OWNER_LENT:
                            requestHandler.confirmBorrowed();
                            Toast.makeText(this, "The Book is Borrowed", Toast.LENGTH_SHORT).show();
                            break;
                        case BORROWER_RECEIVED:
                            requestHandler.returnBook();
                            Toast.makeText(this, "The Book is Returned", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }else{
                    Toast.makeText(this, "Scanning ISBN does not Match the Book ISBN", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Book ISBN not match the scanning ISBN");
                }

            } else {
                Log.d(TAG, "onActivityResult: Something went wrong in scan");
            }
        }
    }


    //Borrower Request


    //Borrower Handoff


    //Borrower Return


    //Pending Screen


    @Override
    public void finish() {
        if (childEventListener!=null){
            databaseHelper.getDatabaseReference().child("Books").orderByChild("uuid")
                    .equalTo(getIntent().getExtras().getString("bookUUID"))
                    .removeEventListener(childEventListener);
        }
        super.finish();
    }
}