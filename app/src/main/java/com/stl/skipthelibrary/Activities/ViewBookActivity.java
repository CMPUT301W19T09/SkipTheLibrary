package com.stl.skipthelibrary.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;

import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.gson.Gson;
import com.stl.skipthelibrary.BindersAndAdapters.BookRecyclerAdapter;
import com.stl.skipthelibrary.BindersAndAdapters.HorizontalAdapter;
import com.stl.skipthelibrary.BindersAndAdapters.RequestorRecyclerAdapter;
import com.stl.skipthelibrary.DatabaseAndAPI.DatabaseHelper;
import com.stl.skipthelibrary.Entities.Book;
import com.stl.skipthelibrary.Entities.Location;
import com.stl.skipthelibrary.Entities.RequestHandler;
import com.stl.skipthelibrary.Entities.User;
import com.stl.skipthelibrary.Entities.ViewableImage;
import com.stl.skipthelibrary.Enums.BookStatus;
import com.stl.skipthelibrary.Enums.HandoffState;
import com.stl.skipthelibrary.Enums.NotificationType;
import com.stl.skipthelibrary.Enums.UserIdentity;
import com.stl.skipthelibrary.R;
import com.stl.skipthelibrary.Singletons.CurrentUser;
import com.stl.skipthelibrary.Validators.BookValidator;
import com.stl.skipthelibrary.Validators.TextValidator;

import java.io.IOException;
import java.util.ArrayList;
/**
 * This activity allows both both borrowers and owners to view books.
 * Owners can also edit books.
 * Owners and borrowers can also change the state of the book (requesting it, lending it out, etc.)
 */
public class ViewBookActivity extends AppCompatActivity {
    final public static String TAG = "ViewBookActivityTag";
    final public static String ISBN = "ISBN";
    final public static String UUID = "UUID";
    final public static String UNAME = "UserName";
    final public static String USER_IDENTITY = "UserIdentity";
    private DatabaseHelper databaseHelper;


    private Book book;
    private User user;
    private EditText title_element;
    private EditText author_element;
    private TextView owner_user_name_element;
    private RatingBar rating_element;
    private EditText synopsis_element;
    private RecyclerView images_element;
    private TextView noImages;
    private ArrayList<ViewableImage> bookImages  = new ArrayList<>();
    private HorizontalAdapter horizontalAdapter;
    private ImageButton edit_button;
    private ImageButton save_button;
    private ViewStub stub;
    private View inflated;
    private ChildEventListener childEventListener;
    private MaterialButton addNewBookImageButton;
    private String isbn_code;
    private ProgressDialog progressDialog;
    private TextWatcher titleTextWatcher;
    private TextWatcher authorTextWatcher;
    private TextWatcher synopsisTextWatcher;
    private Context mContext;


    /**
     * Bind UI Elements
     * @param savedInstanceState: the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initProgressDialog();
        setContentView(R.layout.book_details);
        databaseHelper = new DatabaseHelper(this);
        mContext = getApplicationContext();
        stub = findViewById(R.id.generic_bottom_screen_id);
        user = CurrentUser.getInstance();
        title_element = findViewById(R.id.title_element);
        author_element = findViewById(R.id.author_element);
        owner_user_name_element = findViewById(R.id.owner_user_name_element);
        rating_element = findViewById(R.id.rating_bar_element);
        synopsis_element = findViewById(R.id.synopsis_element);
        edit_button = findViewById(R.id.edit_button);
        save_button = findViewById(R.id.save_button);
        save_button.setVisibility(View.GONE);
        edit_button.setVisibility(View.GONE);
        noImages = findViewById(R.id.noImageTextView);
        addNewBookImageButton = findViewById(R.id.addNewBookImageButton);
        images_element = findViewById(R.id.bookImageRecyclerview);
        setBookDescriptionFieldsEditable(false);

        getIncomingIntents();


        titleTextWatcher = new TextValidator(title_element) {
            /**
             * Validates the input
             * @param textView: the textView to validate
             * @param text: the text to validate
             */
            @Override
            public void validate(TextView textView, String text) {
                if (!(new BookValidator(text,null,null,null).isTitleValid())){
                    title_element.setError("Please enter valid title");
                }
            }
        };
        authorTextWatcher = new TextValidator(author_element) {
            /**
             * Validates the input
             * @param textView: the textView to validate
             * @param text: the text to validate
             */
            @Override
            public void validate(TextView textView, String text) {
                if (!(new BookValidator(null,text,null,null).isAuthorValid())){
                    author_element.setError("Please enter valid author");
                }
            }
        };
        synopsisTextWatcher = new TextValidator(synopsis_element) {
            @Override
            public void validate(TextView textView, String text) {
                if (!(new BookValidator(null,null,null,text).isDescriptionValid())){
                    synopsis_element.setError("Please enter valid description");
                }
            }
        };
    }
    /**
     * Turn on the progress dialog just incase it takes a while to get the book
     */
    private void initProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.setTitle("Getting Book");
        progressDialog.setIndeterminate(false);
        progressDialog.show();
    }

    /**
     * This method catches the incoming data (BookUUID) that is sent via an intent on screen switch.
     */
    private void getIncomingIntents() {
        String bookID = getIntent().getExtras().getString(BookRecyclerAdapter.BOOK_ID);

        childEventListener = databaseHelper.getDatabaseReference()
                .child("Books").orderByChild("uuid").equalTo(bookID)
                .addChildEventListener(new ChildEventListener() {
                    /**
                     * When a new child is added add it to the list of books
                     * @param dataSnapshot: the current snapshot
                     * @param s: the ID
                     */
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        book = dataSnapshot.getValue(Book.class);
                        handleBookArrival();
                    }

                    /**
                     * When a child changes update them
                     * @param dataSnapshot: the current snapshot
                     * @param s: the ID
                     */
                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Toast.makeText(ViewBookActivity.this, "This book has been modified.",
                                Toast.LENGTH_SHORT).show();
                        ViewBookActivity.this.finish();
                        startActivity(getIntent());

                    }

                    /**
                     * If a child is deleted delete them from the list of our books
                     * @param dataSnapshot: the current snapshot
                     */
                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        Toast.makeText(ViewBookActivity.this,
                                "The book you're looking at has been deleted.", Toast.LENGTH_SHORT).show();
                        ViewBookActivity.this.finish();
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
     * When we receive a book, load its fields and then setup the edit and save button if needed.
     */
    private void handleBookArrival() {
        fillBookDescriptionFields();

        //If user is owner of book, allow for edittability
        if (user.getUserName().equals(book.getOwnerUserName())) {
            edit_button.setVisibility(View.VISIBLE);
            save_button.setVisibility(View.GONE);

            edit_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setBookDescriptionFieldsEditable(true);
                    addTextWatchers();
                    author_element.setText(book.getDescription().getAuthor());
                    horizontalAdapter.setEditMode(true);
                    save_button.setVisibility(View.VISIBLE);
                    edit_button.setVisibility(View.GONE);
                    addNewBookImageButton.setVisibility(View.VISIBLE);
                    addNewBookImageButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (bookImages.size() < 5) {
                                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                getIntent.setType("image/*");

                                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                pickIntent.setType("image/*");

                                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                                startActivityForResult(chooserIntent, AddBooksActivity.PICK_BOOK_IMAGE);
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Maximum number of images added", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    });
                    horizontalAdapter.notifyDataSetChanged();
                }
            });

            save_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setBookDescriptionFieldsEditable(false);
                    save_button.setVisibility(View.GONE);
                    edit_button.setVisibility(View.VISIBLE);
                    addNewBookImageButton.setVisibility(View.GONE);
                    horizontalAdapter.notifyDataSetChanged();
                    updateBookDesriptionFields();
                    title_element.setText(book.getDescription().getTitle());
                    synopsis_element.setText(book.getDescription().getSynopsis());
                    author_element.setText(String.format(
                            "Author: %s",book.getDescription().getAuthor())
                    );
                    removeTextWatchers();
                }
            });
        } else {
            edit_button.setVisibility(View.GONE);
            save_button.setVisibility(View.GONE);
        }
        selectBottom();
    }

    /**
     * Determine which bottom screen to load
     */
    private void selectBottom() {
        BookStatus bookStatus = book.getRequests().getState().getBookStatus();
        HandoffState bookHandoffState = book.getRequests().getState().getHandoffState();

        if (user.getUserName().equals(book.getOwnerUserName())) {//user is owner
            if (bookStatus == BookStatus.REQUESTED) {
                setBottomScreen(R.layout.bookscreen_owner_requested);
                configureOwnerRequested();
            } else if (bookHandoffState == HandoffState.READY_FOR_PICKUP) {
                setBottomScreen(R.layout.bookscreen_owner_handoff);
                configureLocationDisplay(R.id.ownerViewLocationButton);
                configureOwnerHandOff();
            } else if (bookHandoffState == HandoffState.BORROWER_RETURNED) {
                setBottomScreen(R.layout.bookscreen_owner_return);
                configureOwnerReturn();
            } else {
                setBottomScreen(R.layout.bookscreen_pending);
                configureOwnerPending();
            }
        } else {//user is borrower
            if ((!book.userIsInterested(user.getUserName()) && bookStatus == BookStatus.REQUESTED) ||
                    bookStatus == BookStatus.AVAILABLE) {
                setBottomScreen(R.layout.bookscreen_borrower_request);
                configureBorrowerRequest();
            } else if (bookHandoffState == HandoffState.OWNER_LENT) {
                setBottomScreen(R.layout.bookscreen_borrower_handoff);
                configureLocationDisplay(R.id.borrowerViewLocationButton);
                configureBorrowerHandoff();
            } else if (bookHandoffState == HandoffState.BORROWER_RECEIVED) {
                setBottomScreen(R.layout.bookscreen_borrower_return);
                configureBorrowerReturn();
            } else if (bookHandoffState == HandoffState.READY_FOR_PICKUP) {
                setBottomScreen(R.layout.bookscreen_handoff_location);
                configureLocationDisplay(R.id.genericViewLocationButton);
            }else {
                setBottomScreen(R.layout.bookscreen_pending);
                configureBorrowerPending();
            }
        }
    }

    /**
     * Load the correct bottom screen and disable the progress dialog
     * @param resourcefile: the resource file to load
     */
    private void setBottomScreen(int resourcefile) {
        stub.setLayoutResource(resourcefile);
        inflated = stub.inflate();
        progressDialog.hide();
        progressDialog.dismiss();
    }

    /**
     * If the user is a requestor and its thier turn to request the book, allow them to request it
     */
    private void configureBorrowerRequest() {
        Button button = inflated.findViewById(R.id.requestButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestHandler handler = book.getRequests();
                handler.addRequestor(user.getUserName());
                handler.getState().setBookStatus(BookStatus.REQUESTED);
                databaseHelper.updateBook(book);
                databaseHelper.sendNotification(NotificationType.NEW_REQUEST, book.getOwnerUserName(),
                        book.getUuid(), book.getDescription().getTitle());
            }
        });
    }

    /**
     * If the user is a borrower and it's their turn to borrow it, let them borrow it by scanning
     * the book
     *
     */
    private void configureBorrowerHandoff() {
        inflated.findViewById(R.id.borrowButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewBookActivity.this, ScannerActivity.class);
                startActivityForResult(intent, ScannerActivity.SCAN_BOOK);
            }

        });

    }

    /**
     * Allow the user to return a book
     */
    private void configureBorrowerReturn() {
        inflated.findViewById(R.id.returnButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewBookActivity.this, ScannerActivity.class);
                startActivityForResult(intent, ScannerActivity.SCAN_BOOK);
            }

        });
    }

    /**
     * Configure the pending sceeen for the borrower screen
     */
    private void configureBorrowerPending() {

    }

    /**
     * Configure the requested screen for an owner
     */
    private void configureOwnerRequested(){
        RecyclerView recyclerView = inflated.findViewById(R.id.RequesterRecyclerView);
        RequestorRecyclerAdapter adapter = new RequestorRecyclerAdapter(this, book.getRequests().getRequestors(), book);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * Configure the lend screen for an owner
     */
    private void configureOwnerHandOff() {
        inflated.findViewById(R.id.lendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewBookActivity.this, ScannerActivity.class);
                startActivityForResult(intent, ScannerActivity.SCAN_BOOK);
            }

        });
    }

    /**
     * Configure the returned screen for an owner
     * Owner get the book and scan the book to confirm the book is returned
     */
    private void configureOwnerReturn() {
        inflated.findViewById(R.id.returnedButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewBookActivity.this, ScannerActivity.class);
                startActivityForResult(intent, ScannerActivity.SCAN_BOOK);
            }

        });
    }

    /**
     * Configure the pending screen for an owner
     */
    private void configureOwnerPending() {

    }

    private void configureLocationDisplay(int buttonID){
        MaterialButton viewLocationButton = findViewById(buttonID);
        viewLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewBookActivity.this, MapViewActivity.class);
                intent.putExtra("latitude", book.getRequests().getState().getLocation().getLatitude());
                intent.putExtra("longitude", book.getRequests().getState().getLocation().getLongitude());
                startActivity(intent);
            }
        });
    }

    private void initImageRecyclerView() {
        horizontalAdapter = new HorizontalAdapter(bookImages, getApplicationContext(), noImages, false);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(ViewBookActivity.this, LinearLayoutManager.HORIZONTAL, false);
        images_element.setAdapter(horizontalAdapter);
        images_element.setLayoutManager(horizontalLayoutManager);
    }

    /**
     * Fill in the details of a book and display it to the user
     */
    private void fillBookDescriptionFields(){
        title_element.setText(book.getDescription().getTitle());
        author_element.setText("Author: " + book.getDescription().getAuthor());

        SpannableString userNameUnderLined = new SpannableString("Owned by: @" + book.getOwnerUserName());
        userNameUnderLined.setSpan(new ForegroundColorSpan(Color.WHITE), 0,9, 0);
        userNameUnderLined.setSpan(new UnderlineSpan(), 10, userNameUnderLined.length(), 0);
        owner_user_name_element.setText(userNameUnderLined);
        owner_user_name_element.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewBookActivity.this, ProfileActivity.class);
                intent.putExtra(ProfileActivity.USER_NAME,
                        book.getOwnerUserName());
                startActivity(intent);
            }
        });

        rating_element.setMax(book.getRating().getMaxRating());
        rating_element.setStepSize((float) 0.5);
        rating_element.setRating((float) book.getRating().getAverageRating());

        Log.d(TAG, "fillBookDescriptionFields: rating "+book.getRating());
        Log.d(TAG, "fillBookDescriptionFields: ratings "+ rating_element.getRating() + " " + rating_element.getNumStars() + " " + rating_element.getStepSize());

        synopsis_element.setText(book.getDescription().getSynopsis());
        bookImages.addAll(book.getImages());
        Log.d("BOOK PICS: ", bookImages.toString());
        if(book.getImages().size() == 0){
            noImages.setVisibility(View.VISIBLE);
        }
        else {
            noImages.setVisibility(View.GONE);
        }

        initImageRecyclerView();
        horizontalAdapter.notifyDataSetChanged();
    }

    /**
     * Toggle the ability of fields to be editable
     * @param isEditable: should the fields be allowed to be edited
     */
    private void setBookDescriptionFieldsEditable(Boolean isEditable) {
        if (isEditable) {
            title_element.setEnabled(true);
            author_element.setEnabled(true);
            synopsis_element.setEnabled(true);
        } else {
            title_element.setEnabled(false);
            author_element.setEnabled(false);
            synopsis_element.setEnabled(false);
        }
    }

    /**
     * Adds text validation to editable fields
     */
    private void addTextWatchers() {
        if (titleTextWatcher != null && authorTextWatcher != null && synopsisTextWatcher != null ){
            title_element.addTextChangedListener(titleTextWatcher);
            author_element.addTextChangedListener(authorTextWatcher);
            synopsis_element.addTextChangedListener(synopsisTextWatcher);
        }
    }

    /**
     * Removes text validation from editable fields
     */
    private void removeTextWatchers() {
        title_element.removeTextChangedListener(titleTextWatcher);
        author_element.removeTextChangedListener(authorTextWatcher);
        synopsis_element.removeTextChangedListener(synopsisTextWatcher);

        title_element.setError(null);
        author_element.setError(null);
        synopsis_element.setError(null);
    }

    /**
     * Update the current book by changing its fields
     */
    private void updateBookDesriptionFields(){
        String title = title_element.getText().toString();
        String author = author_element.getText().toString();
        String description = synopsis_element.getText().toString();

        // Use Gson to make a deep copy of the current user object
        Gson gson = new Gson();
        Book proposedBook = gson.fromJson(gson.toJson(book),Book.class);

        BookValidator bookValidator = new BookValidator(title,author,book.getISBN(),description);
        if (bookValidator.isValid()){
            proposedBook.getDescription().setTitle(title_element.getText().toString());
            proposedBook.getDescription().setAuthor(author_element.getText().toString());
            proposedBook.getDescription().setSynopsis(synopsis_element.getText().toString());
            proposedBook.setImages(bookImages);
            if (CurrentUser.getInstance().getUserName().isEmpty()){
                Toast.makeText(mContext, "How did you get here?", Toast.LENGTH_SHORT).show();
            }
            else{
                book = proposedBook;
                databaseHelper.updateBook(book);
            }
        }
        else{
            Toast.makeText(mContext, bookValidator.getErrorMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
    }


    /**
     * Handle requests from other activities, this includes the scanner and the location activities
     * @param requestCode: the request code
     * @param resultCode: the result code
     * @param data: the data returned
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == ScannerActivity.SCAN_BOOK) {
            if (resultCode == RESULT_OK) {
                isbn_code = data.getStringExtra(ISBN);
                if (isbn_code.equals(book.getISBN()) && CurrentUser.getInstance().getUserName().equals(book.getOwnerUserName())){
                    switch (book.getRequests().getState().getHandoffState()) {
                        case READY_FOR_PICKUP:
                            book.getRequests().lendBook();
                            Toast.makeText(this, "The Book is Lent", Toast.LENGTH_SHORT).show();
                            databaseHelper.updateBook(book);
                            break;
                        case BORROWER_RETURNED:
                            String borrowerUserName = book.getRequests().getAcceptedRequestor();
                            book.getRequests().confirmReturned();
                            Toast.makeText(this, "The Book is Returned", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ViewBookActivity.this, RateUserActivity.class);
                            intent.putExtra(UNAME,borrowerUserName);
                            intent.putExtra(USER_IDENTITY,UserIdentity.BORROWER);
                            startActivityForResult(intent,RateUserActivity.RATEBORROWER);
                            break;
                    }
                }
                else if(isbn_code.equals(book.getISBN()) && !CurrentUser.getInstance().getUserName().equals(book.getOwnerUserName())){
                    switch (book.getRequests().getState().getHandoffState()) {
                        case OWNER_LENT:
                            book.getRequests().confirmBorrowed();
                            Toast.makeText(this, "The Book is Borrowed", Toast.LENGTH_SHORT).show();
                            databaseHelper.updateBook(book);
                            break;
                        case BORROWER_RECEIVED:
                            book.getRequests().returnBook();
                            Toast.makeText(this, "The Book is Returned", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ViewBookActivity.this, RateUserActivity.class);
                            intent.putExtra(UNAME,book.getOwnerUserName());
                            intent.putExtra(USER_IDENTITY,UserIdentity.OWNER);
                            startActivityForResult(intent,RateUserActivity.RATEOWNER);
                            break;
                    }
                }
                else{
                    Toast.makeText(this, "Scanning ISBN does not Match the Book ISBN", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Book ISBN not match the scanning ISBN");
                }

            }
            else {
                Log.d(TAG, "onActivityResult: Something went wrong in scan");
            }
        }
        else if(requestCode == AddBooksActivity.PICK_BOOK_IMAGE) {
            if (resultCode == RESULT_OK) {
                // The user picked a photo.
                // The Intent's data Uri identifies which photo was selected.
                Uri imageUri = data.getData();
                try {
                    ViewableImage newBookImage = new ViewableImage(ViewableImage.getBitmapFromUri(imageUri, this));
                    bookImages.add(newBookImage);
                    if(bookImages.size() == 0){
                        noImages.setVisibility(View.VISIBLE);
                    }
                    else {
                        noImages.setVisibility(View.GONE);
                    }
                    horizontalAdapter.notifyDataSetChanged();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                Log.d(TAG, "onActivityResult: Error in picking image");
            }
        }
        else if (requestCode == MapBoxActivity.SET_LOCATION){
            if (resultCode == RESULT_OK){
                String locationString = data.getStringExtra("Location");
                String username = data.getStringExtra("username");
                Gson gson = new Gson();
                Location location = gson.fromJson(locationString, Location.class);
                book.getRequests().getState().setLocation(location);
                book.getRequests().acceptRequestor(username, book.getUuid(),
                        book.getDescription().getTitle());
                databaseHelper.updateBook(book);
            }
        }
        else if (requestCode == RateUserActivity.RATEOWNER) {
            databaseHelper.updateBook(book);
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "Rate owner received");
            }
            else {
                Log.d(TAG, "Error in rating owner received");
            }

        }
        else if (requestCode == RateUserActivity.RATEBORROWER) {
            databaseHelper.updateBook(book);
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "Rate borrower received");
            }
            else {
                Log.d(TAG, "Error in borrower owner received");
            }
        }
    }


    /**
     * Remove the listener and then finish
     */
    @Override
    public void finish() {
        if (childEventListener!=null){
            databaseHelper.getDatabaseReference().child("Books").orderByChild("uuid")
                    .equalTo(getIntent().getExtras().getString("bookUUID"))
                    .removeEventListener(childEventListener);
        }
        super.finish();
    }
}