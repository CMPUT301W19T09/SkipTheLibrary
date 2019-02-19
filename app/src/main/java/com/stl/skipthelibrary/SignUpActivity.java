package com.stl.skipthelibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class SignUpActivity extends AppCompatActivity {
    public String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    public void RegisterOnClick(View view) {
        String userName = ((EditText) findViewById(R.id.SignUpUsername)).getText().toString();
        String password = ((EditText) findViewById(R.id.SignUpPassword)).getText().toString();
        String firstName = ((EditText) findViewById(R.id.SignUpFirstName)).getText().toString();
        String lastName = ((EditText) findViewById(R.id.SignUpLastName)).getText().toString();
        String emailAddress = ((EditText) findViewById(R.id.SignUpEmail)).getText().toString();
        String phoneNumber = ((EditText) findViewById(R.id.SignUpPhoneNumber)).getText().toString();
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
