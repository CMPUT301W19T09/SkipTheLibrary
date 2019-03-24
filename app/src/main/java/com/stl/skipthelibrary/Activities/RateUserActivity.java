package com.stl.skipthelibrary.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.stl.skipthelibrary.R;

public class RateUserActivity extends AppCompatActivity {
    private TextView RatingUsername;
    private RatingBar RatingBar;
    private MaterialButton RateButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rateuser);

        RatingUsername = findViewById(R.id.RatingUsername);
        RatingBar = findViewById(R.id.ratingBar);
        RateButton = findViewById(R.id.RateButton);
        Intent intent = getIntent();
        String userName = intent.getStringExtra(ViewBookActivity.UNAME);
        
        RateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double value = RatingBar.getRating();

            }
        });
    }
}
