package com.stl.skipthelibrary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class BorrowersBooksActivity extends AppCompatActivity {
    ArrayList<Book> books = new ArrayList<Book>();
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrowerbooks);
        recyclerView = findViewById(R.id.borrowerBookRecyclerView);

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(new NavigationHandler(this));
        navigation.setSelectedItemId(R.id.borrow);

        getBooks();
        initRecyclerView();
    }

    private void getBooks() {
        //Currently just test data as firebase is empty
        books.add(new Book(new BookDescription("test Title", "test Sysnopsis",
                "test author", new Rating()),"testUsername",
                new RequestHandler(new State(BookStatus.ACCEPTED,null, null)), null, new Rating()));
        books.add(new Book(new BookDescription("test Title", "test Sysnopsis",
                "test author", new Rating()),"testUsername",
                new RequestHandler(new State(BookStatus.REQUESTED,null, null)), null, new Rating()));
        books.add(new Book(new BookDescription("test Title", "test Sysnopsis",
                "test author", new Rating()),"testUsername",
                new RequestHandler(new State(BookStatus.AVAILABLE,null, null)), null, new Rating()));
        books.add(new Book(new BookDescription("test Title", "test Sysnopsis",
                "test author", new Rating()),"testUsername",
                new RequestHandler(new State(BookStatus.BORROWED,null, null)), null, new Rating()));
        books.add(new Book(new BookDescription("test Title", "test Sysnopsis",
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

        BookRecyclerAdapter adapter = new BookRecyclerAdapter(this, books, options);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
