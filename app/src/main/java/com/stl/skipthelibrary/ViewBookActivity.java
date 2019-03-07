package com.stl.skipthelibrary;

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
        user = CurrentUser.getInstance();
        bindBookDescriptionElements();
        getIncomingIntents();


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

        //Owner Requested


        //Owner Handoff


        //Owner Return


        //Borrower Request


        //Borrower Handoff


        //Borrower Return


        //Pending Screen


    }

    /**
     * This method catches the incoming data (BookUUID) that is sent via an intent on screen switch.
     */
    private void getIncomingIntents() {
        String bookID = getIntent().getExtras().getString("bookUUID");
        databaseHelper.pullBook(bookID, new MyCallBack() {
            @Override
            public void onCallBack(Book retrievedBook) {
                book = retrievedBook;
                handleBookArrival();
                Log.d("TAG", "Book received:  "+ book.getDescription().getTitle());
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
        selectBottom();
    }

    private void selectBottom() {

        BookStatus bookStatus = book.getRequests().getState().getBookStatus();
        HandoffState bookHandoffState = book.getRequests().getState().getHandoffState();


        if (user.getUserName().equals(book.getOwnerUserName())) {//user is owner
            if (bookStatus==BookStatus.REQUESTED) {
                setBottomScreen(R.layout.bookscreen_owner_requested);
            } else if (bookHandoffState==HandoffState.READY_FOR_PICKUP) {
                setBottomScreen(R.layout.bookscreen_owner_handoff);
            } else if (bookHandoffState==HandoffState.BORROWER_RETURNED) {
                setBottomScreen(R.layout.bookscreen_owner_return);
            } else{
                setBottomScreen(R.layout.bookscreen_pending);
            }
        }
        else{//user is borrower
            if (bookStatus==BookStatus.REQUESTED) {
                setBottomScreen(R.layout.bookscreen_borrower_request);
            } else if (bookHandoffState==HandoffState.READY_FOR_PICKUP){
                setBottomScreen(R.layout.bookscreen_borrower_handoff);
            } else if (bookHandoffState==HandoffState.BORROWER_RECEIVED){
                setBottomScreen(R.layout.bookscreen_borrower_return);
            } else{
                setBottomScreen(R.layout.bookscreen_pending);
            }
        }


    }

    private void setBottomScreen(int resourcefile){
        Log.d(TAG, "Screen is: "+ resourcefile);
        ViewStub stub = (ViewStub) findViewById(R.id.generic_bottom_screen_id);
        stub.setLayoutResource(resourcefile);
        View inflated = stub.inflate();
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
            rating_element.setEnabled(true);
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


}
