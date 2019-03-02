package com.stl.skipthelibrary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyBooksActivity extends AppCompatActivity {
    private static final String TAG = MyBooksActivity.class.getSimpleName();
    public static final int ADD = 1;

    private RecyclerView recyclerView;
    private FloatingActionButton addBookButton;
    private Context mContext;
    private FirebaseRecyclerAdapter mAdapter;

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

    private void initRecyclerView(){
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        Query bookQuery = FirebaseDatabase.getInstance()
                .getReference()
                .child("Books")
                .orderByChild("ownerUserName")
                .equalTo(CurrentUser.getInstance().getUserName());

        FirebaseRecyclerOptions<Book> options =
                new FirebaseRecyclerOptions.Builder<Book>()
                        .setQuery(bookQuery, Book.class)
                        .build();

        mAdapter = new BookRecyclerAdapter(this, options);
        recyclerView.setAdapter(mAdapter);
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
