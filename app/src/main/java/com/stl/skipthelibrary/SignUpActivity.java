package com.stl.skipthelibrary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.io.FileDescriptor;
import java.io.IOException;


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
            databaseHelper.createAccount(userName, password, firstName, lastName, emailAddress, phoneNumber, profileImage);
        }
        else{
            Toast.makeText(SignUpActivity.this, "Please fix the above errors", Toast.LENGTH_SHORT).show();
            return;
        }

    }

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
