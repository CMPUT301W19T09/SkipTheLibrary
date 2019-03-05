package com.stl.skipthelibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
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
    private ProgressDialog progressDialog;
    private TextView title;
    private boolean isUserTheCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initProgressDialog();

        databaseHelper = new DatabaseHelper(this);

        myProfileImage = findViewById(R.id.profileImage);
        myProfileName = findViewById(R.id.myProfileName);
        myProfileUsername = findViewById(R.id.myProfileUsername);
        myProfileEmail = findViewById(R.id.email);
        myProfilePhoneNumber = findViewById(R.id.phoneNumber);
        navigation = findViewById(R.id.bottom_navigation);
        title = findViewById(R.id.MyProfileTitle);

        navigation.setOnNavigationItemSelectedListener(new NavigationHandler(this));
        navigation.setSelectedItemId(R.id.profile);

        Intent intent = getIntent();
        String userName = intent.getExtras().getString(USER_NAME);
        setUser(userName);
        isUserTheCurrentUser = userName.equals(CurrentUser.getInstance().getUserName());
    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.setTitle("Getting Profile");
        progressDialog.setIndeterminate(false);
        progressDialog.show();
    }

    private void setUser(String userName) {
        databaseHelper.getDatabaseReference().child("Users").orderByChild("userName")
                .equalTo(userName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data: dataSnapshot.getChildren()){
                    User user = data.getValue(User.class);
                    userRetrieved(user);
                    return;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void userRetrieved(User retrievedUser) {
        user = retrievedUser;
        user.getContactInfo().setContext(this);

        myProfileImage.setImageBitmap(user.getImage().decode());

        myProfileName.setText(user.getName());
        myProfileUsername.setText("@"+ user.getUserName());
        myProfileEmail.setText("Email: "+ user.getContactInfo().getEmail());
        myProfilePhoneNumber.setText("Phone Number: "+ user.getContactInfo().getPhoneNumber().replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3"));
        progressDialog.hide();

        if (!isUserTheCurrentUser){
            title.setText(user.getUserName() + "'s Profile");
        }

    }

    public void logoutOnClick(View view) {
        databaseHelper.signOut();
        Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void sendEmailOnClick(View view) {
        if (!isUserTheCurrentUser){
            user.getContactInfo().startEmail();
        }
    }

    public void sendPhoneCallOnClick(View view) {
        if (!isUserTheCurrentUser){
            user.getContactInfo().startCall();
        }
    }

    @Override
    public void onBackPressed() {
    }
}
