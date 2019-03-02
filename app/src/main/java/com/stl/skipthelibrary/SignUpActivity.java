package com.stl.skipthelibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class SignUpActivity extends AppCompatActivity {
    private EditText usernameText;
    private EditText passwordText;
    private EditText firstNameText;
    private EditText lastNameText;
    private EditText emailAddressText;
    private EditText phoneNumberText;


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
            databaseHelper.createAccount(userName, password, firstName, lastName, emailAddress, phoneNumber);
        }
        else{
            Toast.makeText(SignUpActivity.this, "Please fix the above errors", Toast.LENGTH_SHORT).show();
            return;
        }

    }

}
