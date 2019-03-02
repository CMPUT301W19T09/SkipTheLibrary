package com.stl.skipthelibrary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class BorrowersBooksActivity extends AppCompatActivity {
    public static final String TAG = BorrowersBooksActivity.class.getSimpleName();

    private ArrayList<Book> books = new ArrayList<Book>();
    private RecyclerView recyclerView;
    private FloatingActionButton searchBookButton;
    private Context mContext;
    private FirebaseRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrowerbooks);
        recyclerView = (RecyclerView) findViewById(R.id.borrowerBookRecyclerView);
        searchBookButton = (FloatingActionButton) findViewById(R.id.searchBookButton);
        mContext = getApplicationContext();

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(new NavigationHandler(this));
        navigation.setSelectedItemId(R.id.borrow);

//        getBooks();
        initRecyclerView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
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
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        Query bookQuery = FirebaseDatabase.getInstance()
                .getReference()
                .child("Books");


        FirebaseRecyclerOptions<Book> options =
                new FirebaseRecyclerOptions.Builder<Book>()
                        .setQuery(bookQuery, Book.class)
                        .build();

        mAdapter = new BookRecyclerAdapter(this, books, options);
        recyclerView.setAdapter(mAdapter);
    }
}
