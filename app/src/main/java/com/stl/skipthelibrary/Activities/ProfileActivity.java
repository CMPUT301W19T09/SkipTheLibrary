package com.stl.skipthelibrary.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.stl.skipthelibrary.DatabaseAndAPI.DatabaseHelper;
import com.stl.skipthelibrary.Helpers.NavigationHandler;
import com.stl.skipthelibrary.R;
import com.stl.skipthelibrary.Singletons.CurrentUser;
import com.stl.skipthelibrary.Entities.User;

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
    private EditText myProfileEmail;
    private EditText myProfilePhoneNumber;
    private BottomNavigationView navigation;
    private MaterialButton logoutButton;
    private User user;
    private ProgressDialog progressDialog;
    private TextView title;
    private ImageButton edit_button;
    private ImageButton save_button;

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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        databaseHelper = new DatabaseHelper(this);

        myProfileImage = findViewById(R.id.profileImage);
        myProfileName = findViewById(R.id.myProfileName);
        myProfileUsername = findViewById(R.id.myProfileUsername);
        myProfileEmail = findViewById(R.id.email);
        myProfilePhoneNumber = findViewById(R.id.phoneNumber);
        navigation = findViewById(R.id.bottom_navigation);
        title = findViewById(R.id.MyProfileTitle);
        logoutButton = findViewById(R.id.logoutButton);
        edit_button = findViewById(R.id.profile_edit_button);
        save_button = findViewById(R.id.profile_save_button);

        setContactInfoFieldsEditable(false);

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
        myProfileUsername.setText(String.format("@%s",user.getUserName()));
        myProfileEmail.setText(String.format("Email: %s",user.getContactInfo().getEmail()));
        myProfilePhoneNumber.setText(String.format("Phone Number: %s",user.getContactInfo().getPhoneNumber())
                .replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3"));
        progressDialog.hide();
        progressDialog.dismiss();

        if (!isUserTheCurrentUser){
            title.setText(String.format("%s's Profile",user.getUserName()));
            edit_button.setVisibility(View.GONE);
            save_button.setVisibility(View.GONE);
        } else {
            edit_button.setVisibility(View.VISIBLE);
            save_button.setVisibility(View.GONE);

            edit_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setContactInfoFieldsEditable(true);
                    myProfileEmail.setText(CurrentUser.getInstance().getContactInfo().getEmail());
                    myProfilePhoneNumber.setText(CurrentUser.getInstance()
                            .getContactInfo().getPhoneNumber()
                            .replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3"));
                    save_button.setVisibility(View.VISIBLE);
                    edit_button.setVisibility(View.GONE);
                    logoutButton.setVisibility(View.GONE);
                }
            });

            save_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setContactInfoFieldsEditable(false);
                    save_button.setVisibility(View.GONE);
                    edit_button.setVisibility(View.VISIBLE);
                    logoutButton.setVisibility(View.VISIBLE);
                    updateContactInfoFields();
                    myProfileEmail.setText(String.format("Email: %s",CurrentUser.getInstance()
                            .getContactInfo().getEmail()));
                    myProfilePhoneNumber.setText(String.format("Phone Number: %s",CurrentUser
                            .getInstance().getContactInfo().getPhoneNumber()
                            .replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3")));
                }
            });
        }

    }

    /**
     * Toggle the ability of fields to be editable
     * @param isEditable: should the fields be allowed to be edited
     */
    private void setContactInfoFieldsEditable(Boolean isEditable) {
        if (isEditable) {
            myProfileEmail.setEnabled(true);
            myProfilePhoneNumber.setEnabled(true);
        } else {
            myProfileEmail.setEnabled(false);
            myProfilePhoneNumber.setEnabled(false);
        }
    }

    /**
     * Update the current book by changing its fields
     */
    private void updateContactInfoFields(){
        User user = CurrentUser.getInstance();
        user.getContactInfo().setEmail(myProfileEmail.getText().toString());
        user.getContactInfo().setPhoneNumber(
                myProfilePhoneNumber.getText().toString()
                        .replaceFirst("\\((\\d{3})\\) (\\d{3})-(\\d+)", "$1$2$3")
        );
        Log.d(TAG, "updateContactInfoFields: "+user);
        databaseHelper.updateCurrentUser();
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
