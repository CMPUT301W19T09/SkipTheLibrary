package com.stl.skipthelibrary.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.gson.Gson;
import com.stl.skipthelibrary.DatabaseAndAPI.DatabaseHelper;
import com.stl.skipthelibrary.Entities.Rating;
import com.stl.skipthelibrary.Entities.ViewableImage;
import com.stl.skipthelibrary.Helpers.NavigationHandler;
import com.stl.skipthelibrary.R;
import com.stl.skipthelibrary.Singletons.CurrentUser;
import com.stl.skipthelibrary.Entities.User;
import com.stl.skipthelibrary.Validators.SignUpValidator;
import com.stl.skipthelibrary.Validators.TextValidator;

import java.io.IOException;

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
    private RatingBar myOwnerRating;
    private RatingBar myBorrowerRating;
    private BottomNavigationView navigation;
    private MaterialButton logoutButton;
    private User user;
    private ProgressDialog progressDialog;
    private TextView title;
    private ImageButton edit_button;
    private ImageButton save_button;
    private CircleImageView add_image_button;
    private CircleImageView delete_image_button;
    private ChildEventListener childEventListener;
    private TextWatcher emailTextWatcher;
    private TextWatcher phoneTextWatcher;
    private Context mContext;

    private boolean isUserTheCurrentUser;
    private ViewableImage currentImage;

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

        mContext = getApplicationContext();
        myProfileImage = findViewById(R.id.profileImage);
        myProfileName = findViewById(R.id.myProfileName);
        myProfileUsername = findViewById(R.id.myProfileUsername);
        myProfileEmail = findViewById(R.id.email);
        myProfilePhoneNumber = findViewById(R.id.phoneNumber);
        myOwnerRating = findViewById(R.id.ownerRatingBar);
        myBorrowerRating = findViewById(R.id.borrowerRatingBar);
        navigation = findViewById(R.id.bottom_navigation);
        title = findViewById(R.id.MyProfileTitle);
        logoutButton = findViewById(R.id.logoutButton);
        edit_button = findViewById(R.id.profile_edit_button);
        save_button = findViewById(R.id.profile_save_button);
        delete_image_button = findViewById(R.id.deleteProfileImageButton);
        add_image_button = findViewById(R.id.addProfileImageButton);

        setContactInfoFieldsEditable(false);
        hideImageButtons();

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

        phoneTextWatcher = new TextValidator(myProfilePhoneNumber) {
            /**
             * Validate the phonenumber
             * @param textView: the textview to validate
             * @param text: the text to validate
             */
            @Override
            public void validate(TextView textView, String text) {
                SignUpValidator signInValidator = new SignUpValidator();
                signInValidator.setPhoneNumber(text);
                if (!(signInValidator.isPhoneNumberValid())){
                    myProfilePhoneNumber.setError(signInValidator.getErrorMessage());
                }
            }
        };

        emailTextWatcher = new TextValidator(myProfileEmail) {
            /**
             * Validate the email address
             * @param textView: the textview to validate
             * @param text: the text to validate
             */
            @Override
            public void validate(TextView textView, String text) {
                SignUpValidator signInValidator = new SignUpValidator();
                signInValidator.setEmailAddress(text);
                if (!(signInValidator.isEmailNameValid())){
                    myProfileEmail.setError(signInValidator.getErrorMessage());
                }
            }
        };
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
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User user = dataSnapshot.getValue(User.class);
                userRetrieved(user);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Toast.makeText(ProfileActivity.this, "This account has been modified",
                        Toast.LENGTH_SHORT).show();
                ProfileActivity.this.finish();
                startActivity(getIntent());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Toast.makeText(ProfileActivity.this, "This account has been deleted",
                        Toast.LENGTH_SHORT).show();
                ProfileActivity.this.finish();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };

        databaseHelper.getDatabaseReference().child("Users").orderByChild("userName")
                .equalTo(userName)
                .addChildEventListener(childEventListener);
    }

    /**
     * Once we get the user, bind the user data to the UI
     * @param retrievedUser: the retired user
     */
    private void userRetrieved(User retrievedUser) {
        user = retrievedUser;
        user.getContactInfo().setContext(this);

        currentImage = user.getImage();
        myProfileImage.setImageBitmap(currentImage.decode());

        myProfileName.setText(user.getName());
        myProfileUsername.setText(String.format("@%s",user.getUserName()));
        myProfileEmail.setText(String.format("Email: %s",user.getContactInfo().getEmail()));
        myProfilePhoneNumber.setText(String.format("Phone Number: %s",user.getContactInfo().getPhoneNumber())
                .replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3"));

        Rating ownerRating = user.getOwnerRating();
        myOwnerRating.setMax(ownerRating.getMaxRating());
        myOwnerRating.setStepSize((float) 0.5);
        myOwnerRating.setRating((float) ownerRating.getAverageRating());

        Rating borrowerRating = user.getBorrowerRating();
        myBorrowerRating.setMax(borrowerRating.getMaxRating());
        myBorrowerRating.setStepSize((float) 0.5);
        myBorrowerRating.setRating((float) borrowerRating.getAverageRating());

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
                    showImageButton();
                    addTextWatchers();
                    myProfileEmail.setText(CurrentUser.getInstance().getContactInfo().getEmail());
                    myProfilePhoneNumber.setText(CurrentUser.getInstance()
                            .getContactInfo().getPhoneNumber()
                            .replaceFirst("\\((\\d{3})\\) (\\d{3})-(\\d+)", "$1$2$3"));
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
                    hideImageButtons();
                    updateContactInfoFields();
                    myProfileEmail.setText(String.format("Email: %s",CurrentUser.getInstance()
                            .getContactInfo().getEmail()));
                    myProfilePhoneNumber.setText(String.format("Phone Number: %s",CurrentUser
                            .getInstance().getContactInfo().getPhoneNumber()
                            .replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3")));
                    removeTextWatchers();
                }
            });
        }

    }

    /**
     * Displays the correct image button depending on the circumstances (delete or add)
     */
    private void showImageButton() {
        if (currentImage != null) {
            delete_image_button.setVisibility(View.VISIBLE);
            add_image_button.setVisibility(View.GONE);
        } else {
            delete_image_button.setVisibility(View.GONE);
            add_image_button.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Hides both of the possible image buttons
     */
    private void hideImageButtons() {
        add_image_button.setVisibility(View.GONE);
        delete_image_button.setVisibility(View.GONE);
    }

    /**
     * Adds text validation to editable fields
     */
    private void addTextWatchers() {
        if (emailTextWatcher != null && phoneTextWatcher != null) {
            myProfileEmail.addTextChangedListener(emailTextWatcher);
            myProfilePhoneNumber.addTextChangedListener(phoneTextWatcher);
        }
    }

    /**
     * Removes text validation from editable fields
     */
    private void removeTextWatchers() {
        if (emailTextWatcher != null && phoneTextWatcher != null) {
            myProfileEmail.removeTextChangedListener(emailTextWatcher);
            myProfilePhoneNumber.removeTextChangedListener(phoneTextWatcher);
            myProfileEmail.setError(null);
            myProfilePhoneNumber.setError(null);
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
        String userName = CurrentUser.getInstance().getUserName();
        String password = "fakepass";
        String firstName = "First";
        String lastName = "Last";
        String emailAddress = myProfileEmail.getText().toString();
        String phoneNumber = myProfilePhoneNumber.getText().toString();
        // Use Gson to make a deep copy of the current user object
        Gson gson = new Gson();
        User proposedUser = gson.fromJson(gson.toJson(CurrentUser.getInstance()),User.class);

        proposedUser.getContactInfo().setEmail(emailAddress);
        proposedUser.getContactInfo().setPhoneNumber(phoneNumber);
        if (currentImage == null) {
            ViewableImage defaultImage = new ViewableImage(
                    BitmapFactory.decodeResource(getResources(), R.drawable.default_avatar)
            );
            proposedUser.setImage(defaultImage);
            myProfileImage.setImageBitmap(defaultImage.decode());
        } else {
            proposedUser.setImage(currentImage);
        }
        Log.d(TAG, "updateContactInfoFields: "+proposedUser);


        SignUpValidator signUpValidator = new SignUpValidator(userName, password, firstName, lastName, emailAddress, phoneNumber);
        if (signUpValidator.isValid()){
            databaseHelper.updateCurrentUser(proposedUser);
        }
        else{
            Toast.makeText(mContext, "Please fix the above errors", Toast.LENGTH_SHORT).show();
            return;
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
     * If the user is not viewing their own profile then allow them to send a call to the user
     * they are viewing
     * @param view: the logout button
     */
    public void deleteImageOnClick(View view) {
        if (isUserTheCurrentUser){
            currentImage = null;
            myProfileImage.setImageBitmap(null);
            showImageButton();
        }
    }

    /**
     * If the user is not viewing their own profile then allow them to send a call to the user
     * they are viewing
     * @param view: the logout button
     */
    public void addImageOnClick(View view) {
        if (isUserTheCurrentUser){
            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
            getIntent.setType("image/*");

            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setType("image/*");

            Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

            startActivityForResult(chooserIntent, AddBooksActivity.PICK_BOOK_IMAGE);
        }
    }


    /**
     * Handle requests from other activities, this includes image adding
     * @param requestCode: the request code
     * @param resultCode: the result code
     * @param data: the data returned
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == AddBooksActivity.PICK_BOOK_IMAGE) {
            if (resultCode == RESULT_OK) {
                // The user picked a photo.
                // The Intent's data Uri identifies which photo was selected.
                Uri imageUri = data.getData();
                try {
                    currentImage = new ViewableImage(ViewableImage.getBitmapFromUri(imageUri, this));
                    myProfileImage.setImageBitmap(currentImage.decode());
                    showImageButton();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                Log.d(TAG, "onActivityResult: Error in picking image");
            }
        }
    }

    /**
     * Disable the back button if the user is viewing their own profile as they have the navigation
     * bar
     */
    @Override
    public void onBackPressed() {
        if (!isUserTheCurrentUser){
            databaseHelper.getDatabaseReference().child("Users").orderByChild("userName")
                    .equalTo(user.getUserName()).removeEventListener(childEventListener);
            super.onBackPressed();
        }
    }

    /**
     * Remove the event listener and finish the activity
     */
    @Override
    public void finish() {
        databaseHelper.getDatabaseReference().child("Users").orderByChild("userName")
                .equalTo(user.getUserName()).removeEventListener(childEventListener);
        super.finish();
    }

    /**
     * Removes the child event listener when the activity is stopped
     */
    @Override
    protected void onStop() {
        databaseHelper.getDatabaseReference().child("Users").orderByChild("userName")
                .equalTo(user.getUserName()).removeEventListener(childEventListener);
        super.onStop();
    }

    /**
     * Removes the child event listener when the activity is paused
     */
    @Override
    protected void onPause() {
        databaseHelper.getDatabaseReference().child("Users").orderByChild("userName")
                .equalTo(user.getUserName()).removeEventListener(childEventListener);
        super.onPause();
    }
}
