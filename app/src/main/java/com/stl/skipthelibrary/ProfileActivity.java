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
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Displays a user's profile, can display either the current user's profile or the profile of
 * another user
 */
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
    private MaterialButton logoutButton;
    private User user;
    private ProgressDialog progressDialog;
    private TextView title;
    private boolean isUserTheCurrentUser;

    /**
     * Bind UI elements, get the user to display, determine whether or not the user to display is
     * the current user
     * @param savedInstanceState
     */
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
        logoutButton = findViewById(R.id.logoutButton);

        Intent intent = getIntent();
        String userName = intent.getExtras().getString(USER_NAME);
        isUserTheCurrentUser = userName.equals(CurrentUser.getInstance().getUserName());
        if (!isUserTheCurrentUser){
            logoutButton.setVisibility(View.GONE);
            navigation.setVisibility(View.GONE);
        }
        else{
            navigation.setOnNavigationItemSelectedListener(new NavigationHandler(this));
            navigation.setSelectedItemId(R.id.profile);
        }
        setUser(userName);
    }

    /**
     * Turn on the progress dialog just incase it takes a while to get the user
     */
    private void initProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.setTitle("Getting Profile");
        progressDialog.setIndeterminate(false);
        progressDialog.show();
    }

    /**
     * Get the user who's profile we want to display
     * @param userName: the user's username
     */
    private void setUser(String userName) {
        databaseHelper.getDatabaseReference().child("Users").orderByChild("userName")
                .equalTo(userName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            /**
             * Get the specified user
             * @param dataSnapshot: the current snapshot
             */
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

    /**
     * Once we get the user, bind the user data to the UI
     * @param retrievedUser: the retired user
     */
    private void userRetrieved(User retrievedUser) {
        user = retrievedUser;
        user.getContactInfo().setContext(this);

        myProfileImage.setImageBitmap(user.getImage().decode());

        myProfileName.setText(user.getName());
        myProfileUsername.setText("@"+ user.getUserName());
        myProfileEmail.setText("Email: "+ user.getContactInfo().getEmail());
        myProfilePhoneNumber.setText("Phone Number: "+ user.getContactInfo().getPhoneNumber().replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3"));
        progressDialog.hide();
        progressDialog.dismiss();

        if (!isUserTheCurrentUser){
            title.setText(user.getUserName() + "'s Profile");
        }

    }

    /**
     * If the user is viewing their own profile then allow them to logout
     * @param view: the logout button
     */
    public void logoutOnClick(View view) {
        if (isUserTheCurrentUser){
            databaseHelper.signOut();
            Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    /**
     * If the user is not viewing their own profile then allow them to send an email to the user
     * they are viewing
     * @param view: the logout button
     */
    public void sendEmailOnClick(View view) {
        if (!isUserTheCurrentUser){
            user.getContactInfo().startEmail();
        }
    }

    /**
     * If the user is not viewing their own profile then allow them to send a call to the user
     * they are viewing
     * @param view: the logout button
     */
    public void sendPhoneCallOnClick(View view) {
        if (!isUserTheCurrentUser){
            user.getContactInfo().startCall();
        }
    }

    /**
     * Disable the back button if the user is viewing their own profile as they have the navigation
     * bar
     */
    @Override
    public void onBackPressed() {
        if (!isUserTheCurrentUser){
            super.onBackPressed();
        }
    }
}
