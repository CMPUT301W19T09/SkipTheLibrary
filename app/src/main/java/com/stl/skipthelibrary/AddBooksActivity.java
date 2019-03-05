package com.stl.skipthelibrary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddBooksActivity extends AppCompatActivity {
    final public static String TAG = AddBooksActivity.class.getSimpleName();
    final public static int PICK_BOOK_IMAGE = 2;

    private Context mContext;
    private EditText bookTitle;
    private EditText bookAuthor;
    private EditText bookISBN;
    private EditText bookDesc;
    private RecyclerView bookImageRecyclerview;
    private HorizontalAdapter horizontalAdapter;
    private List<ViewableImage> bookImages = new ArrayList<>();
    private MaterialButton addBookImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbooks);
        mContext = getApplicationContext();

        bookTitle = findViewById(R.id.AddBookTitle);
        bookAuthor = findViewById(R.id.AddBookAuthor);
        bookISBN = findViewById(R.id.AddBookISBN);
        bookDesc = findViewById(R.id.AddBookDesc);
        addBookImageButton = findViewById(R.id.addBookImageButton);

        bookImageRecyclerview = findViewById(R.id.bookImagesRecyclerview);
        horizontalAdapter=new HorizontalAdapter(bookImages, getApplicationContext());
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(AddBooksActivity.this, LinearLayoutManager.HORIZONTAL, false);
        bookImageRecyclerview.setLayoutManager(horizontalLayoutManager);
        bookImageRecyclerview.setAdapter(horizontalAdapter);

        bookTitle.addTextChangedListener(new TextValidator(bookTitle) {
            @Override
            public void validate(TextView textView, String text) {
                if (!(new BookValidator(text,null,null,null).isTitleValid())){
                    bookTitle.setError("Please enter valid title");
                }
            }
        });

        bookAuthor.addTextChangedListener(new TextValidator(bookAuthor) {
            @Override
            public void validate(TextView textView, String text) {
                if (!(new BookValidator(null,text,null,null).isAuthorValid())){
                    bookAuthor.setError("Please enter valid author");
                }
            }
        });

        bookISBN.addTextChangedListener(new TextValidator(bookISBN) {
            @Override
            public void validate(TextView textView, String text) {
                if (!(new BookValidator(null,null,text,null).isISBNValid())){
                    bookISBN.setError("Please enter valid isbn");
                }
            }
        });

        bookDesc.addTextChangedListener(new TextValidator(bookDesc) {
            @Override
            public void validate(TextView textView, String text) {
                if (!(new BookValidator(null,null,null,text).isDescriptionValid())){
                    bookDesc.setError("Please enter valid description");
                }
            }
        });

        addBookImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bookImages.size() < 5) {
                    Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    getIntent.setType("image/*");

                    Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickIntent.setType("image/*");

                    Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                    startActivityForResult(chooserIntent, PICK_BOOK_IMAGE);
                }
                else {
                    Toast.makeText(mContext, "Maximum number of images added", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    public void saveBookOnClick(View view) {
        String title = bookTitle.getText().toString();
        String author = bookAuthor.getText().toString();
        String isbn = bookISBN.getText().toString();
        String description = bookDesc.getText().toString();

        BookValidator bookValidator = new BookValidator(title,author,isbn,description);
        if (bookValidator.isValid()){
            DatabaseHelper databaseHelper = new DatabaseHelper(this);
            BookDescription bookDescription = new BookDescription(title,description,author,new Rating());
            Book newBook = new Book(bookDescription, isbn, CurrentUser.getInstance().getUserName(), (ArrayList<ViewableImage>) bookImages);
            databaseHelper.addBookIfValid(newBook);
        }
        else{
            Toast.makeText(mContext, bookValidator.getErrorMessage(), Toast.LENGTH_SHORT).show();
            return;
        }

    }

    public void scanBookOnClick(View view) {
        Intent intent = new Intent(this, ScannerActivity.class);
        startActivityForResult(intent, ScannerActivity.SCAN_BOOK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ScannerActivity.SCAN_BOOK) {
            if (resultCode == RESULT_OK) {
                String ISBN = data.getStringExtra("ISBN");
                bookISBN.setText(ISBN);
                new BookDescriptionReceiver(ISBN, bookTitle, bookAuthor, bookDesc).execute(ISBN);
            } else {
                Log.d(TAG, "onActivityResult: Something went wrong in scan");
            }
        }
        else if(requestCode == PICK_BOOK_IMAGE){
            if ( resultCode == RESULT_OK) {
                // The user picked a photo.
                // The Intent's data Uri identifies which photo was selected.
                Uri imageUri = data.getData();
                try {
                    ViewableImage newBookImage = new ViewableImage(ViewableImage.getBitmapFromUri(imageUri, this));
                    bookImages.add(newBookImage);
                    horizontalAdapter.notifyDataSetChanged();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Log.d(TAG, "onActivityResult: Error in picking image");
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MyBooksActivity.class);
        startActivity(intent);
    }
}
