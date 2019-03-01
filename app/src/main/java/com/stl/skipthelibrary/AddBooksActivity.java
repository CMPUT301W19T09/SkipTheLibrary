package com.stl.skipthelibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddBooksActivity extends AppCompatActivity {
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
            Book newBook = new Book(bookDescription,databaseHelper.getFirebaseUser().getDisplayName());
//            databaseHelper.addBook(newBook);
            Toast.makeText(mContext, "We should add a book here", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(mContext, bookValidator.getErrorMessage(), Toast.LENGTH_SHORT).show();
            return;
        }

    }
}
