package com.stl.skipthelibrary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyBooksActivity extends AppCompatActivity {
    private static final String TAG = MyBooksActivity.class.getSimpleName();
    public static final int ADD = 1;

    private RecyclerView recyclerView;
    private FloatingActionButton addBookButton;
    private ArrayList<Book> books = new ArrayList<>();
    private BookRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ownerbooks);
        recyclerView = findViewById(R.id.ownerBooksRecyclerView);
        addBookButton = findViewById(R.id.addBookButton);

        adapter = new BookRecyclerAdapter(this, books);


        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(new NavigationHandler(this));
        navigation.setSelectedItemId(R.id.my_books);

        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyBooksActivity.this, AddBooksActivity.class);
                startActivityForResult(intent, ADD);
            }
        });

        getBooks();
        initRecyclerView();
    }


    private void getBooks() {
        final DatabaseHelper databaseHelper = new DatabaseHelper(this);
        databaseHelper.getDatabaseReference().child("Books").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Book book = dataSnapshot.getValue(Book.class);
                if (book.getOwnerUserName().equals(CurrentUser.getInstance().getUserName())){
                    books.add(book);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Book book = dataSnapshot.getValue(Book.class);
                Integer idToReplace = null;
                for (int i = 0; i < books.size(); i++){
                    if (books.get(i).getUuid().equals(book.getUuid())){
                        idToReplace = i;
                        break;
                    }
                }

                if (idToReplace != null){
                    books.set(idToReplace,book);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Book book = dataSnapshot.getValue(Book.class);
                Integer idToRemove = null;
                for (int i = 0; i < books.size(); i++){
                    if (books.get(i).getUuid().equals(book.getUuid())){
                        idToRemove = i;
                        break;
                    }
                }

                if (idToRemove != null){
                    books.remove(books.get(idToRemove));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }



    private void initRecyclerView(){
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
        // Check which request it is that we're responding to
        if (requestCode == ADD) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "BOOK ADDED 🤪", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "SOMETHING WONG MY FRIEND", Toast.LENGTH_SHORT).show();
        }
    }
}
