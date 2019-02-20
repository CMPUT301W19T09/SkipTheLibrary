package com.stl.skipthelibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText passwordText;
    private EditText usernameText;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameText = findViewById(R.id.UsernameEditText);
        passwordText = findViewById(R.id.PasswordEditText);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        // user is already logged in
        if (firebaseUser != null){
            DatabaseHelper databaseHelper = new DatabaseHelper(this);
            databaseHelper.pullUserSignIn(firebaseUser.getUid());
            Toast.makeText(this, "Automatically Signed In", Toast.LENGTH_SHORT).show();
        }

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
