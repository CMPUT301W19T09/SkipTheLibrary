package com.stl.skipthelibrary;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyBooksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ownerbooks);

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(new NavigationHandler(this));
        navigation.setSelectedItemId(R.id.my_books);

        initRecyclerView();
    }

    private void initRecyclerView(){
        ArrayList<Book> books = new ArrayList<Book>();
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
        books.add(new Book(new BookDescription("test Title", "test Sysnopsis",
                "test author", new Rating()),"testUsername",
                new RequestHandler(new State(BookStatus.REQUESTED,null, null)), null, new Rating()));
        books.add(new Book(new BookDescription("test Title", "test Sysnopsis",
                "test author", new Rating()),"testUsername",
                new RequestHandler(new State(BookStatus.REQUESTED,null, null)), null, new Rating()));
        books.add(new Book(new BookDescription("test Title", "test Sysnopsis",
                "test author", new Rating()),"testUsername",
                new RequestHandler(new State(BookStatus.REQUESTED,null, null)), null, new Rating()));
        books.add(new Book(new BookDescription("test Title", "test Sysnopsis",
                "test author", new Rating()),"testUsername",
                new RequestHandler(new State(BookStatus.REQUESTED,null, null)), null, new Rating()));

        RecyclerView recyclerView = findViewById(R.id.ownerBooksRecyclerView);
        BookRecyclerAdapter adapter = new BookRecyclerAdapter(this, books);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}
