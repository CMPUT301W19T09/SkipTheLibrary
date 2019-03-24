package com.stl.skipthelibrary.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.stl.skipthelibrary.DatabaseAndAPI.DatabaseHelper;
import com.stl.skipthelibrary.Entities.Rating;
import com.stl.skipthelibrary.Entities.User;
import com.stl.skipthelibrary.Enums.UserIdentity;
import com.stl.skipthelibrary.R;

public class RateUserActivity extends AppCompatActivity {
    public final static int RATEOWNER = 6;
    public final static int RATEBORROWER = 7;

    private TextView RatingUsername;
    private RatingBar RatingBar;
    private MaterialButton RateButton;
    private Intent intent;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rateuser);

        RatingUsername = findViewById(R.id.RatingUsername);
        RatingBar = findViewById(R.id.ratingBar);
        RateButton = findViewById(R.id.RateButton);
        intent = getIntent();
        String userName = intent.getStringExtra(ViewBookActivity.UNAME);
        databaseHelper = new DatabaseHelper(this);

        getUser(userName);
    }

    private void getUser(String userName) {
        databaseHelper.getDatabaseReference().child("Users").orderByChild("userName")
                .equalTo(userName)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        User user = dataSnapshot.getValue(User.class);
                        userRetrieved(user);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }

    private void userRetrieved(final User user) {
        RateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double rating = RatingBar.getRating();
                Intent returnIntent = new Intent();
                if((intent.getSerializableExtra(ViewBookActivity.UBO)).equals(UserIdentity.OWNER)){
                    user.getOwnerRating().addRating(rating);
                    databaseHelper.getDatabaseReference().child("Users").child(user.getUserID())
                            .child("ownerRating").setValue(user.getOwnerRating());
                    setResult(Activity.RESULT_OK, returnIntent);
                }else if ((intent.getSerializableExtra(ViewBookActivity.UBO)).equals(UserIdentity.BORROWER)){
                    user.getBorrowerRating().addRating(rating);
                    databaseHelper.getDatabaseReference().child("Users").child(user.getUserID())
                            .child("borrowerRating").setValue(user.getBorrowerRating());
                    setResult(Activity.RESULT_OK, returnIntent);
                }else{
                    setResult(Activity.RESULT_CANCELED, returnIntent);
                }
                finish();
            }
        });
    }
}
