package com.stl.skipthelibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ViewBookActivity extends AppCompatActivity {
    private Button button;
    private View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_details);


        button = (Button) findViewById(R.id.HandOffButton);
        button.setText("lend");
    }
}
