package com.stl.skipthelibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    final public static String TAG = "LoginActivity";

    private EditText passwordText;
    private EditText emailText;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailText = findViewById(R.id.EmailEditText);
        passwordText = findViewById(R.id.PasswordEditText);
        databaseHelper = new DatabaseHelper(this);

        // user is already logged in
        Log.d(TAG, "onCreate: "+databaseHelper.isUserLoggedIn());
        if (databaseHelper.isUserLoggedIn()){
            try{
                databaseHelper.pullUserSignIn(databaseHelper.getFirebaseUser().getUid());
                Toast.makeText(this, "Automatically Signed In", Toast.LENGTH_SHORT).show();
            }
            catch (Exception e){
                Toast.makeText(this, "Your account has been deleted.", Toast.LENGTH_SHORT).show();
            }
        }

        emailText.addTextChangedListener(new TextValidator(emailText) {
            @Override
            public void validate(TextView textView, String text) {
                if (!(new SignInValidator(text,null).isEmailNameValid())){
                    emailText.setError("Please enter valid email");
                }
            }
        });
        passwordText.addTextChangedListener(new TextValidator(passwordText) {
            @Override
            public void validate(TextView textView, String text) {
                if (!(new SignInValidator(null,text).isPasswordValid())){
                    passwordText.setError("Please enter valid password");
                }
            }
        });

    }


    public void signInOnClick(View view) {
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        SignInValidator signInValidator = new SignInValidator(email, password);
        if (signInValidator.isValid()){
            databaseHelper.signIn(email, password);
        }
        else{
            Toast.makeText(LoginActivity.this, "Please fix the above errors", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void signUpOnClick(View view) {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }
}
