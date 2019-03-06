package com.stl.skipthelibrary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView searchRecyclerView;
    private BookRecyclerAdapter adapter;
    private AutoCompleteTextView searchBar;
    private ImageView backButton;
    private ImageView searchButton;

    private Context mContext;

    private ArrayList<Book> searchedBooks= new ArrayList<>();
    private ArrayList<Book> books = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mContext = getApplicationContext();
        adapter = new BookRecyclerAdapter(this, searchedBooks);

        searchRecyclerView = findViewById(R.id.SearchRecyclerView);
        searchBar = findViewById(R.id.SearchBar);
        backButton = findViewById(R.id.BookListItemLeftArrow);
        searchButton = findViewById(R.id.searchBookButton);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                search();
            }
        });

        searchBooks();
        initRecyclerView();
    }


    private void searchBooks() {
        final DatabaseHelper databaseHelper = new DatabaseHelper(this);
        databaseHelper.getDatabaseReference().child("Books").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Book book = dataSnapshot.getValue(Book.class);
                if (! (book.getOwnerUserName().equals(CurrentUser.getInstance().getUserName()))){
                    books.add(book);
                    search();
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
                    search();
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
                    search();
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
        searchRecyclerView.setAdapter(adapter);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    // ON CLICK LISTENERS

    public void search() {
        ArrayList<Book> newFilteredBooks = new ArrayList<>();
        String search = searchBar.getText().toString();
        List<String> searchText = Arrays.asList(search.split(" "));

        boolean contained;

        for (Book book: books){
            if (book.getRequests().getState().getBookStatus().equals(BookStatus.ACCEPTED) ||
                    book.getRequests().getState().getBookStatus().equals(BookStatus.BORROWED)) {
                continue;
            }
            contained = true;

            // Filter based on search here
            for (String text: searchText) {
                if (!book.getDescription().getTitle().toLowerCase().contains(text.toLowerCase()) &&
                        !book.getDescription().getAuthor().toLowerCase().contains(text.toLowerCase()) &&
                        !book.getDescription().getSynopsis().toLowerCase().contains(text.toLowerCase())) {

                    contained = false;
                }
            }
            if (contained) {
                newFilteredBooks.add(book);
            }
        }
        searchedBooks.clear();
        searchedBooks.addAll(newFilteredBooks);

        adapter.notifyDataSetChanged();
    }

    public void cancelSearchOnClick(View view) {
        Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }

}
