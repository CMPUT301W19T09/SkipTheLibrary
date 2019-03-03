package com.stl.skipthelibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;

public class AddBooksActivity extends AppCompatActivity {
    final public static String TAG = AddBooksActivity.class.getSimpleName();

    private Context mContext;
    private EditText bookTitle;
    private EditText bookAuthor;
    private EditText bookISBN;
    private EditText bookDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbooks);
        mContext = getApplicationContext();

        bookTitle = findViewById(R.id.AddBookTitle);
        bookAuthor = findViewById(R.id.AddBookAuthor);
        bookISBN = findViewById(R.id.AddBookISBN);
        bookDesc = findViewById(R.id.AddBookDesc);

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
            Book newBook = new Book(bookDescription, isbn,CurrentUser.getInstance().getUserName());
            databaseHelper.addBook(newBook);
            Toast.makeText(mContext, "Book Added", Toast.LENGTH_SHORT).show();

            Gson gson = new Gson();
            Intent intent = new Intent(getApplicationContext(), MyBooksActivity.class);

            intent.putExtra("Book", gson.toJson(newBook));
            setResult(Activity.RESULT_OK, intent);
            finish();
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
    }
}
