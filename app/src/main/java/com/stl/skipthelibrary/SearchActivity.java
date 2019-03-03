package com.stl.skipthelibrary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView searchRecyclerView;
    private AutoCompleteTextView searchBar;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchRecyclerView = findViewById(R.id.SearchRecyclerView);
        searchBar = findViewById(R.id.SearchBar);
        backButton = findViewById(R.id.BookListItemLeftArrow);

    }

    public void cancelSearchOnClick() {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
}
