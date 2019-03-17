package com.stl.skipthelibrary.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.stl.skipthelibrary.DatabaseAndAPI.DatabaseHelper;
import com.stl.skipthelibrary.Entities.Book;
import com.stl.skipthelibrary.Entities.Rating;
import com.stl.skipthelibrary.Enums.BookStatus;
import com.stl.skipthelibrary.BindersAndAdapters.BookRecyclerAdapter;
import com.stl.skipthelibrary.Helpers.BookRatingReceiver;
import com.stl.skipthelibrary.Helpers.NavigationHandler;
import com.stl.skipthelibrary.R;
import com.stl.skipthelibrary.Singletons.CurrentUser;
import com.stl.skipthelibrary.Helpers.SwipeToDeleteCallback;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Display all of the books a user has and allow the user to filter them by book status
 */
public class MyBooksActivity extends AppCompatActivity {
    private static final String TAG = MyBooksActivity.class.getSimpleName();
    public static final int ADD = 1;

    private RecyclerView recyclerView;
    private FloatingActionButton addBookButton;
    private ArrayList<Book> books = new ArrayList<>();
    private ArrayList<Book> filteredBooks = new ArrayList<>();
    private ArrayList<BookStatus> filters = new ArrayList<>();
    private BookRecyclerAdapter adapter;
    private Context mContext;

    private Chip requestedChip;
    private Chip acceptedChip;
    private Chip borrowedChip;
    private Chip availableChip;

    /**
     * OnCreate bind all UI elements and set listeners
     * @param savedInstanceState: the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ownerbooks);
        recyclerView = findViewById(R.id.ownerBooksRecyclerView);
        addBookButton = findViewById(R.id.addBookButton);
        mContext = getApplicationContext();

        adapter = new BookRecyclerAdapter(this, filteredBooks);

        requestedChip = findViewById(R.id.RequestedChip);
        acceptedChip = findViewById(R.id.AcceptedChip);
        borrowedChip = findViewById(R.id.LentChip);
        availableChip = findViewById(R.id.AvailableChip);
        updateFilter();

        requestedChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /**
             * Update the filter when the chip is checked or unchecked
             * @param compoundButton: the compound button
             * @param b: which state the button was clicked in
             */
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateFilter();
            }
        });
        acceptedChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /**
             * Update the filter when the chip is checked or unchecked
             * @param compoundButton: the compound button
             * @param b: which state the button was clicked in
             */
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateFilter();
            }
        });
        borrowedChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /**
             * Update the filter when the chip is checked or unchecked
             * @param compoundButton: the compound button
             * @param b: which state the button was clicked in
             */
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateFilter();
            }
        });
        availableChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /**
             * Update the filter when the chip is checked or unchecked
             * @param compoundButton: the compound button
             * @param b: which state the button was clicked in
             */
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateFilter();
            }
        });


        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(new NavigationHandler(this));
        navigation.setSelectedItemId(R.id.my_books);

        addBookButton.setOnClickListener(new View.OnClickListener() {
            /**
             * When the user selects this button they will be taken to the add books screen
             * @param view: the current view
             */
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AddBooksActivity.class);
                startActivityForResult(intent, ADD);
            }
        });

        getBooks();
        initRecyclerView();
    }

    /**
     * Update the filter condition depending on which chips are checked
     */
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
        if (availableChip.isChecked()){
            newFilters.add(BookStatus.AVAILABLE);
        }
        filters = newFilters;
        updateFilteredBooks();
    }

    /**
     * Update the which books to display based on the filters present
     */
    private void updateFilteredBooks() {
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

    /**
     * get all books the user is interested in from firebase
     */
    private void getBooks() {
        final DatabaseHelper databaseHelper = new DatabaseHelper(this);
        databaseHelper.getDatabaseReference().child("Books")
                .orderByChild("ownerUserName")
                .equalTo(CurrentUser.getInstance().getUserName())
                .addChildEventListener(new ChildEventListener() {
            /**
             * When a new child is added add it to the list of books if the user is the owner
             * @param dataSnapshot: the current snapshot
             * @param s: the ID
             */
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Book book = dataSnapshot.getValue(Book.class);
                books.add(book);
                updateFilteredBooks();
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
                    updateFilteredBooks();
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


    /**
     * Initialize the recycler view
     */
    private void initRecyclerView(){
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallback(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
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
                Log.d(TAG, "Book Added Successfully!");
                String isbn = resultIntent.getStringExtra(ViewBookActivity.ISBN);
                String uuid = resultIntent.getStringExtra(ViewBookActivity.UUID);
                new BookRatingReceiver(mContext,new Rating(),uuid).execute(isbn);
            }
        } else {
            Log.d(TAG, "Book Was not Added Successfully.");
        }
    }

    /**
     * Disable the back button on screens with the navigation bar
     */
    @Override
    public void onBackPressed() {
    }
}
