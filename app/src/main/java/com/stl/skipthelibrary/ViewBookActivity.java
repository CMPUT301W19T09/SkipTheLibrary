package com.stl.skipthelibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.RatingBar;

public class ViewBookActivity extends AppCompatActivity {
    private static String TAG = "ViewBookActivityTag";
    private DatabaseHelper databaseHelper;
    Book book;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_details);
        databaseHelper = new DatabaseHelper(this);
        getIncomingIntents();

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
                Log.d(TAG, "Author is: " + book.getDescription().getAuthor());
                Log.d(TAG, "Author is: " + book.getDescription().getSynopsis());
                initBookFields();
            }
        });
    }


    private void initBookFields(){
        EditText title_element = findViewById(R.id.title_element);
        EditText author_element = findViewById(R.id.author_element);
        RatingBar rating_element = findViewById(R.id.rating_element);
        EditText synopsis_element = findViewById(R.id.synopsis_element);

        title_element.setText(book.getDescription().getTitle());
        author_element.setText(book.getDescription().getAuthor());
        rating_element.setMax(book.getRating().getMaxRating());
        rating_element.setNumStars((int) Math.round(book.getRating().getAverageRating()));
        synopsis_element.setText(book.getDescription().getSynopsis());

        title_element.setEnabled(false);
        author_element.setEnabled(false);
        rating_element.setEnabled(false);
        synopsis_element.setEnabled(false);

    }
}
