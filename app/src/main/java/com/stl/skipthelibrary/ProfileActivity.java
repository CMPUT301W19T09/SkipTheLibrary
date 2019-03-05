package com.stl.skipthelibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    public final static String USER_NAME = "userName";
    private static final String TAG = ProfileActivity.class.getSimpleName();

    private DatabaseHelper databaseHelper;
    private CircleImageView myProfileImage;
    private TextView myProfileName;
    private TextView myProfileUsername;
    private TextView myProfileEmail;
    private TextView myProfilePhoneNumber;
    private BottomNavigationView navigation;
    private User user;

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

//        Intent intent = getIntent();
//        setUser(intent.getExtras().getString(USER_NAME));

        user.getContactInfo().setContext(this);

        myProfileImage.setImageBitmap(user.getImage().decode());

        myProfileName.setText(user.getName());
        myProfileUsername.setText("@"+ user.getUserName());
        myProfileEmail.setText("Email: "+ user.getContactInfo().getEmail());
        myProfilePhoneNumber.setText("Phone Number: "+ user.getContactInfo().getPhoneNumber().replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3"));

    }

    private void setUser(String userName) {

    }

    private void userRetrieved(User retrievedUser) {
        user = retrievedUser;
        user.getContactInfo().setContext(this);

        myProfileImage.setImageBitmap(user.getImage().decode());

        myProfileName.setText(user.getName());
        myProfileUsername.setText("@"+ user.getUserName());
        myProfileEmail.setText("Email: "+ user.getContactInfo().getEmail());
        myProfilePhoneNumber.setText("Phone Number: "+ user.getContactInfo().getPhoneNumber().replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3"));
    }


    public void logoutOnClick(View view) {
        databaseHelper.signOut();
        Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void sendEmailOnClick(View view) {
        user.getContactInfo().startEmail();
    }

    public void sendPhoneCallOnClick(View view) {
        user.getContactInfo().startCall();
    }

    @Override
    public void onBackPressed() {
    }
}
