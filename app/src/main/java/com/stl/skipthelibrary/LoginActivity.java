package com.stl.skipthelibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
private EditText passwordText;
private EditText usernameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameText = findViewById(R.id.UsernameEditText);
        passwordText = findViewById(R.id.PasswordEditText);
        
    }


    public void signInOnClick(View view) {
        String email = usernameText.getText().toString();
        String password = passwordText.getText().toString();
        SignInValidator signInValidator = new SignInValidator(email, password);
        if (signInValidator.isValid()){
            DatabaseHelper databaseHelper = new DatabaseHelper(this);
            databaseHelper.signIn(email, password);
        }
        else{
            Toast.makeText(LoginActivity.this, signInValidator.getErrorMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void signUpOnClick(View view) {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }
}
