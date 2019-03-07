package com.stl.skipthelibrary.Activities;

import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.stl.skipthelibrary.DatabaseAndAPI.DatabaseHelper;
import com.stl.skipthelibrary.R;
import com.stl.skipthelibrary.Validators.SignUpValidator;
import com.stl.skipthelibrary.Validators.TextValidator;
import com.stl.skipthelibrary.Entities.ViewableImage;

import java.io.IOException;

/**
 * This activity allows user's to register for a new account
 */
public class SignUpActivity extends AppCompatActivity {
    private EditText usernameText;
    private EditText passwordText;
    private EditText firstNameText;
    private EditText lastNameText;
    private EditText emailAddressText;
    private EditText phoneNumberText;
    private MaterialButton profilePhotoButton;
    private CircleImageView profilePhotoImageView;
    private ViewableImage profileImage;
    public static final int PICK_PROFILE_IMAGE = 0;


    /**
     * Bind UI elements and setup listeners
     * @param savedInstanceState: the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        usernameText = findViewById(R.id.SignUpUsername);
        passwordText = findViewById(R.id.SignUpPassword);
        firstNameText = findViewById(R.id.SignUpFirstName);
        lastNameText = findViewById(R.id.SignUpLastName);
        emailAddressText = findViewById(R.id.SignUpEmail);
        phoneNumberText = findViewById(R.id.SignUpPhoneNumber);
        profilePhotoButton = findViewById(R.id.addProfileImage);
        profilePhotoImageView = findViewById(R.id.newProfileImage);

        profileImage = new ViewableImage(BitmapFactory.decodeResource(getResources(), R.drawable.default_avatar));

        usernameText.addTextChangedListener(new TextValidator(usernameText) {
            /**
             * Validate the username
             * @param textView: the textview to validate
             * @param text: the text to validate
             */
            @Override
            public void validate(TextView textView, String text) {
                SignUpValidator signInValidator = new SignUpValidator();
                signInValidator.setUsername(text);
                if (!(signInValidator.isUserNameValid())){
                    usernameText.setError(signInValidator.getErrorMessage());
                }
            }
        });

        passwordText.addTextChangedListener(new TextValidator(passwordText) {
            /**
             * Validate the password
             * @param textView: the textview to validate
             * @param text: the text to validate
             */
            @Override
            public void validate(TextView textView, String text) {
                SignUpValidator signInValidator = new SignUpValidator();
                signInValidator.setPassword(text);
                if (!(signInValidator.isPasswordValid())){
                    passwordText.setError(signInValidator.getErrorMessage());
                }
            }
        });

        firstNameText.addTextChangedListener(new TextValidator(firstNameText) {
            /**
             * Validate the firstname
             * @param textView: the textview to validate
             * @param text: the text to validate
             */
            @Override
            public void validate(TextView textView, String text) {
                SignUpValidator signInValidator = new SignUpValidator();
                signInValidator.setFirstName(text);
                if (!(signInValidator.isFirstNameValid())){
                    firstNameText.setError(signInValidator.getErrorMessage());
                }
            }
        });

        lastNameText.addTextChangedListener(new TextValidator(lastNameText) {
            /**
             * Validate the lastname
             * @param textView: the textview to validate
             * @param text: the text to validate
             */
            @Override
            public void validate(TextView textView, String text) {
                SignUpValidator signInValidator = new SignUpValidator();
                signInValidator.setLastName(text);
                if (!(signInValidator.isLastNameValid())){
                    lastNameText.setError(signInValidator.getErrorMessage());
                }
            }
        });

        emailAddressText.addTextChangedListener(new TextValidator(emailAddressText) {
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
                    emailAddressText.setError(signInValidator.getErrorMessage());
                }
            }
        });

        phoneNumberText.addTextChangedListener(new TextValidator(phoneNumberText) {
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
                    phoneNumberText.setError(signInValidator.getErrorMessage());
                }
            }
        });

        profilePhotoButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Set the user's profile photo
             * @param v: the profile photo button
             */
              @Override
              public void onClick(View v) {
                  Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                  getIntent.setType("image/*");

                  Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                  pickIntent.setType("image/*");

                  Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                  chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                  startActivityForResult(chooserIntent, PICK_PROFILE_IMAGE);
              }
          }
        );
    }


    /**
     * Register the user if all of thier info is valid and thier username is unique.
     * @param view: the register button
     */
    public void RegisterOnClick(View view) {
        String userName = usernameText.getText().toString();
        String password = passwordText.getText().toString();
        String firstName = firstNameText.getText().toString();
        String lastName = lastNameText.getText().toString();
        String emailAddress = emailAddressText.getText().toString();
        String phoneNumber = phoneNumberText.getText().toString();
        SignUpValidator signUpValidator = new SignUpValidator(userName, password, firstName, lastName, emailAddress, phoneNumber);
        if (signUpValidator.isValid()){
            DatabaseHelper databaseHelper = new DatabaseHelper(this);
            databaseHelper.createAccountIfValid(userName, password, firstName, lastName, emailAddress, phoneNumber, profileImage);
        }
        else{
            Toast.makeText(SignUpActivity.this, "Please fix the above errors", Toast.LENGTH_SHORT).show();
            return;
        }

    }

    /**
     * Retrieve the selected profile image
     * @param requestCode: the request code
     * @param resultCode: the result of the call
     * @param data: the data retrieved
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_PROFILE_IMAGE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a photo.
                // The Intent's data Uri identifies which photo was selected.
                Uri imageUri = data.getData();
                profilePhotoImageView.setImageURI(imageUri);
                try {
                    profileImage = new ViewableImage(ViewableImage.getBitmapFromUri(imageUri, this));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

}
