package com.stl.skipthelibrary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyBooksActivity extends AppCompatActivity {
    private static final String TAG = MyBooksActivity.class.getSimpleName();
    public static final int ADD = 1;

    ArrayList<Book> books = new ArrayList<Book>();
    RecyclerView recyclerView;
    FloatingActionButton addBookButton;
    Context mContext;

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
        BookRecyclerAdapter adapter = new BookRecyclerAdapter(this, books);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}
