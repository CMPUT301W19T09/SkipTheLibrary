package com.stl.skipthelibrary;

import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MyProfileActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    private CircleImageView myProfileImage;
    private TextView myProfileName;
    private TextView myProfileUsername;
    private TextView myProfileEmail;
    private TextView myProfilePhoneNumber;
    private BottomNavigationView navigation;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        databaseHelper = new DatabaseHelper(this);

        myProfileImage = findViewById(R.id.profileImage);
        myProfileName = findViewById(R.id.myProfileName);
        myProfileUsername = findViewById(R.id.myProfileUsername);
        myProfileEmail = findViewById(R.id.email);
        myProfilePhoneNumber = findViewById(R.id.phoneNumber);
        navigation = findViewById(R.id.bottom_navigation);

        navigation.setOnNavigationItemSelectedListener(new NavigationHandler(this));
        navigation.setSelectedItemId(R.id.profile);

        currentUser = CurrentUser.getInstance();
        currentUser.getContactInfo().setContext(this);

        if (currentUser.getImage() != null) {
            myProfileImage.setImageBitmap(currentUser.getImage().decode());
        }
        myProfileName.setText(currentUser.getName());
        myProfileUsername.setText("@"+currentUser.getUserName());
        myProfileEmail.setText("Email: "+currentUser.getContactInfo().getEmail());
        myProfilePhoneNumber.setText("Phone Number: "+currentUser.getContactInfo().getPhoneNumber().replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3"));

    }


    public void logoutOnClick(View view) {
        databaseHelper.signOut();
        Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void sendEmailOnClick(View view) {
        currentUser.getContactInfo().startEmail();
    }

    public void sendPhoneCallOnClick(View view) {
        currentUser.getContactInfo().startCall();
    }
}
