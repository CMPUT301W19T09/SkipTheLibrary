package com.stl.skipthelibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;

public class ViewBookActivity extends AppCompatActivity {
    private static String TAG = "ViewBookActivityTag";
    private DatabaseHelper databaseHelper;
    Book book;
    User user;
    EditText title_element;
    EditText author_element ;
    RatingBar rating_element;
    EditText synopsis_element;
    ImageButton edit_button;
    ImageButton save_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_details);
        databaseHelper = new DatabaseHelper(this);
        user = CurrentUser.getInstance();
        initBookFields();
        save_button.setVisibility(View.GONE);
        getIncomingIntents();
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFieldsEditable(true);
                save_button.setVisibility(View.VISIBLE);
                edit_button.setVisibility(View.GONE);
            }
        });

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFieldsEditable(false);
                save_button.setVisibility(View.GONE);
                edit_button.setVisibility(View.VISIBLE);
                updateBookFields();
            }
        });

    }


    /**
     * This method catches the incoming data (BookUUID) that is sent via an intent on screen switch.
     */
    private void getIncomingIntents(){
        String bookID = getIntent().getExtras().getString("bookUUID");
        Log.d("TAG", "In getIncomingIntents ");
        databaseHelper.pullBook(bookID, new MyCallBack() {
            @Override
            public void onCallBack(Book retrievedBook) { ;
                book = retrievedBook;
                handleBookArrival();
            }
        });
    }

    private void handleBookArrival(){

        fillBookFields();
        //If user is owner of book, allow for edittability
        if (user.getUserName().equals(book.getOwnerUserName())){
            edit_button.setVisibility(View.VISIBLE);
            Log.d(TAG, "VISIBLE is true for edit");
            save_button.setVisibility(View.GONE);
        }
    }

    private void setFieldsEditable(Boolean isEditable){
        if(isEditable){
            title_element.setEnabled(true);
            author_element.setEnabled(true);
            rating_element.setEnabled(true);
            synopsis_element.setEnabled(true);
        }
        else{
            title_element.setEnabled(false);
            author_element.setEnabled(false);
            rating_element.setEnabled(false);
            synopsis_element.setEnabled(false);
        }
    }

    private void initBookFields() {
        title_element = findViewById(R.id.title_element);
        author_element = findViewById(R.id.author_element);
        rating_element = findViewById(R.id.rating_bar_element);
        synopsis_element = findViewById(R.id.synopsis_element);
        edit_button = findViewById(R.id.edit_button);
        save_button = findViewById(R.id.save_button);

        save_button.setVisibility(View.GONE);
        edit_button.setVisibility(View.GONE);
        setFieldsEditable(false);
    }

    private void fillBookFields(){
        title_element.setText(book.getDescription().getTitle());
        author_element.setText(book.getDescription().getAuthor());
        rating_element.setMax(book.getRating().getMaxRating());
        rating_element.setNumStars((int) Math.round(book.getRating().getAverageRating()));
        synopsis_element.setText(book.getDescription().getSynopsis());
    }

    private void updateBookFields(){
        book.getDescription().setTitle(title_element.getText().toString());
        book.getDescription().setAuthor(author_element.getText().toString());
        book.getDescription().setSynopsis(synopsis_element.getText().toString());
        book.getRating().addRating( (double) rating_element.getNumStars());
        databaseHelper.updateBook(book);
    }

}
