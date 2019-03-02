package com.stl.skipthelibrary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyBooksActivity extends AppCompatActivity {
    private static final String TAG = MyBooksActivity.class.getSimpleName();
    public static final int ADD = 1;

    private ArrayList<Book> books = new ArrayList<Book>();
    private RecyclerView recyclerView;
    private FloatingActionButton addBookButton;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ownerbooks);
        recyclerView = (RecyclerView) findViewById(R.id.ownerBooksRecyclerView);
        addBookButton = (FloatingActionButton) findViewById(R.id.addBookButton);
        mContext = getApplicationContext();

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(new NavigationHandler(this));
        navigation.setSelectedItemId(R.id.my_books);

        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AddBooksActivity.class);
                startActivityForResult(intent, ADD);
            }
        });

        getBooks();
        initRecyclerView();
    }

    private void getBooks() {
        //Currently just test data as firebase is empty
        books.add(new Book("test ISBN", new BookDescription("test Title", "test Sysnopsis",
                "test author", new Rating()),"testUsername",
                new RequestHandler(new State(BookStatus.ACCEPTED,null, null)), null, new Rating()));
        books.add(new Book("test ISBN", new BookDescription("test Title", "test Sysnopsis",
                "test author", new Rating()),"testUsername",
                new RequestHandler(new State(BookStatus.REQUESTED,null, null)), null, new Rating()));
        books.add(new Book("test ISBN", new BookDescription("test Title", "test Sysnopsis",
                "test author", new Rating()),"testUsername",
                new RequestHandler(new State(BookStatus.AVAILABLE,null, null)), null, new Rating()));
        books.add(new Book("test ISBN", new BookDescription("test Title", "test Sysnopsis",
                "test author", new Rating()),"testUsername",
                new RequestHandler(new State(BookStatus.BORROWED,null, null)), null, new Rating()));
        books.add(new Book("test ISBN", new BookDescription("test Title", "test Sysnopsis",
                "test author", new Rating()),"testUsername",
                new RequestHandler(new State(BookStatus.REQUESTED,null, null)), null, new Rating()));
    }

    private void initRecyclerView(){

        Query songQuery = FirebaseDatabase.getInstance()
                .getReference()
                .child("Books");

        FirebaseRecyclerOptions<Book> options =
                new FirebaseRecyclerOptions.Builder<Book>()
                        .setQuery(songQuery, Book.class)
                        .build();

        FirebaseRecyclerAdapter adapter = new BookRecyclerAdapter(this, books, options);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * onActivitResult method to get the result from startActivityFromResult
     *
     * @param requestCode The request code that was sent with the activity
     * @param resultCode The status code for the result
     * @param resultIntent The returning intent from the activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {
        Book book;
        // Check which request it is that we're responding to
        if (requestCode == ADD) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Toast.makeText(mContext, "BOOK ADDED 🤪", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(mContext, "SOMETHING WONG MY FRIEND", Toast.LENGTH_SHORT).show();
        }
    }
}
