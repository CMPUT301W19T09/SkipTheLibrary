package com.stl.skipthelibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class SignUpActivity extends AppCompatActivity {
    EditText usernameText;
    EditText passwordText;
    EditText firstNameText;
    EditText lastNameText;
    EditText emailAddressText;
    EditText phoneNumberText;


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
                if (!(new SignUpValidator(text,null,null,null,null,null).isUserNameValid())){
                    usernameText.setError("Please enter valid Username");
                }
            }
        });

        passwordText.addTextChangedListener(new TextValidator(passwordText) {
            @Override
            public void validate(TextView textView, String text) {
                if (!(new SignUpValidator(null,text,null,null,null,null).isPasswordValid())){
                    passwordText.setError("Please enter valid username");
                }
            }
        });

        firstNameText.addTextChangedListener(new TextValidator(firstNameText) {
            @Override
            public void validate(TextView textView, String text) {
                if (!(new SignUpValidator(null,null,text,null,null,null).isFirstNameValid())){
                    firstNameText.setError("Please enter valid first name");
                }
            }
        });

        lastNameText.addTextChangedListener(new TextValidator(lastNameText) {
            @Override
            public void validate(TextView textView, String text) {
                if (!(new SignUpValidator(null,null,null,text,null,null).isLastNameValid())){
                    lastNameText.setError("Please enter valid last name");
                }
            }
        });

        emailAddressText.addTextChangedListener(new TextValidator(emailAddressText) {
            @Override
            public void validate(TextView textView, String text) {
                if (!(new SignUpValidator(null,null,null,null,text,null).isEmailNameValid())){
                    emailAddressText.setError("Please enter valid email");
                }
            }
        });

        phoneNumberText.addTextChangedListener(new TextValidator(phoneNumberText) {
            @Override
            public void validate(TextView textView, String text) {
                if (!(new SignUpValidator(null,null,null,null,null,text).isPhoneNumberValid())){
                    phoneNumberText.setError("Please enter valid phone number");
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
            Toast.makeText(SignUpActivity.this, signUpValidator.getErrorMessage(), Toast.LENGTH_SHORT).show();
            return;
        }

    }

}
