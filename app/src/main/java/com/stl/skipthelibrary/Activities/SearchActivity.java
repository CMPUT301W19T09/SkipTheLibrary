package com.stl.skipthelibrary.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.stl.skipthelibrary.DatabaseAndAPI.DatabaseHelper;
import com.stl.skipthelibrary.Entities.Book;
import com.stl.skipthelibrary.Enums.BookStatus;
import com.stl.skipthelibrary.BindersAndAdapters.BookRecyclerAdapter;
import com.stl.skipthelibrary.R;
import com.stl.skipthelibrary.Singletons.CurrentUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This activity is where borrowers can filterSearchedBooks for all available books they can request.
 * This includes books they have requested, are currently borrowing, or have been approved to borrow
 */
public class SearchActivity extends AppCompatActivity {

    private RecyclerView searchRecyclerView;
    private BookRecyclerAdapter adapter;
    private AutoCompleteTextView searchBar;

    private ArrayList<Book> searchedBooks= new ArrayList<>();
    private ArrayList<Book> books = new ArrayList<>();

    /**
     * Bind UI elements, initialize all listeners, and start the initial filterSearchedBooks
     * @param savedInstanceState: the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        adapter = new BookRecyclerAdapter(this, searchedBooks);

        searchRecyclerView = findViewById(R.id.SearchRecyclerView);
        searchBar = findViewById(R.id.SearchBar);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            /**
             * Search the database when text changes
             * @param editable: the editable text
             */
            @Override
            public void afterTextChanged(Editable editable) {
                filterSearchedBooks();
            }
        });

        searchBooks();
        initRecyclerView();
    }


    /**
     * Get all books which are not the users. And then filter them.
     */
    private void searchBooks() {
        final DatabaseHelper databaseHelper = new DatabaseHelper(this);
        databaseHelper.getDatabaseReference().child("Books").addChildEventListener(new ChildEventListener() {
            /**
             * When a new child is added add it to the list of books
             * @param dataSnapshot: the current snapshot
             * @param s: the ID
             */
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Book book = dataSnapshot.getValue(Book.class);
                if (! (book.getOwnerUserName().equals(CurrentUser.getInstance().getUserName()))){
                    books.add(book);
                    filterSearchedBooks();
                }
            }

            /**
             * When a child is changes update them
             * @param dataSnapshot: the current snapshot
             * @param s: the ID
             */
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
                    filterSearchedBooks();
                }
            }

            /**
             * If a child is deleted delete them from the list of our books
             * @param dataSnapshot: the current snapshot
             */
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
                    filterSearchedBooks();
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


    /**
     * Initialize the recycler view
     */
    private void initRecyclerView(){
        searchRecyclerView.setAdapter(adapter);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    /**
     * Filter the books to ensure they match all keywords specified by the user.
     */
    public void filterSearchedBooks() {
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

            // Filter based on filterSearchedBooks here
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

    /**
     * Return to the borrower's books screen
     * @param view: the back button
     */
    public void cancelSearchOnClick(View view) {
        Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }

}
