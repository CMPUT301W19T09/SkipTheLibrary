package com.stl.skipthelibrary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

public class BorrowersBooksActivity extends AppCompatActivity {
    public static final String TAG = BorrowersBooksActivity.class.getSimpleName();
    public static final int SEARCH = 1;

    private RecyclerView recyclerView;
    private FloatingActionButton searchBookButton;
    private ArrayList<Book> books = new ArrayList<>();
    private ArrayList<Book> filteredBooks = new ArrayList<>();
    private ArrayList<BookStatus> filters = new ArrayList<>();
    private BookRecyclerAdapter adapter;
    private Context mContext;

    private Chip requestedChip;
    private Chip acceptedChip;
    private Chip borrowedChip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrowerbooks);
        recyclerView = findViewById(R.id.borrowerBookRecyclerView);
        searchBookButton = findViewById(R.id.searchBookButton);

        mContext = getApplicationContext();

        adapter = new BookRecyclerAdapter(this, filteredBooks);

        requestedChip = findViewById(R.id.RequestedChip);
        acceptedChip = findViewById(R.id.AcceptedChip);
        borrowedChip = findViewById(R.id.BorrowedChip);
        updateFilter();

        requestedChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateFilter();
            }
        });
        acceptedChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateFilter();
            }
        });
        borrowedChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateFilter();
            }
        });

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(new NavigationHandler(this));
        navigation.setSelectedItemId(R.id.borrow);

        searchBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SearchActivity.class);
                startActivityForResult(intent, SEARCH);
            }
        });

        getBooks();
        initRecyclerView();
    }

    private void updateFilter() {
        ArrayList<BookStatus> newFilters = new ArrayList<>();
        if (requestedChip.isChecked()){
            newFilters.add(BookStatus.REQUESTED);
        }
        if (acceptedChip.isChecked()){
            newFilters.add(BookStatus.ACCEPTED);
        }
        if (borrowedChip.isChecked()){
            newFilters.add(BookStatus.BORROWED);
        }
        filters = newFilters;
        updateFilteredBooks();
    }

    private void getBooks() {
        final DatabaseHelper databaseHelper = new DatabaseHelper(this);
        databaseHelper.getDatabaseReference().child("Books").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Book book = dataSnapshot.getValue(Book.class);
                if (book.userIsInterested(CurrentUser.getInstance().getUserName())){
                    books.add(book);
                    updateFilteredBooks();
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

                // if the book already existed in our current record of books
                if (idToReplace != null){
                    // if the user is still interested in the book we update it
                    if (book.userIsInterested(CurrentUser.getInstance().getUserName())){
                        books.set(idToReplace,book);
                    }
                    // otherwise we remove it
                    else{
                        books.remove(books.get(idToReplace));
                    }
                    updateFilteredBooks();
                }
                // if the book was not in our record but the user is now interested in it
                // we add the book to their collection.
                else if(book.userIsInterested(CurrentUser.getInstance().getUserName())){
                    books.add(book);
                    updateFilteredBooks();
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
                    updateFilteredBooks();
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

    private void updateFilteredBooks(){
        ArrayList<Book> newFilteredBooks = new ArrayList<>();
        for (Book book: books){
            if (filters.contains(book.getRequests().getState().getBookStatus())){
                newFilteredBooks.add(book);
            }
        }
        filteredBooks.clear();
        filteredBooks.addAll(newFilteredBooks);

        adapter.notifyDataSetChanged();
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
        if (requestCode == SEARCH) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "BOOK SEARCHED 🤪", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "SOMETHING WONG MY FRIEND", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
    }

}
