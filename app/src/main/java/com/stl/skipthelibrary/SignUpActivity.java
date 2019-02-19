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
            createAccount(userName, password, firstName, lastName, emailAddress, phoneNumber);
        }
        else{
            Toast.makeText(SignUpActivity.this, signUpValidator.getErrorMessage(), Toast.LENGTH_SHORT).show();
            return;
        }

    }

    private void createAccount(final String userName, String password, final String firstName, final String lastName, final String emailAddress, final String phoneNumber){
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(emailAddress, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Authentication Succeeded.", Toast.LENGTH_SHORT).show();
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            accountCreated(new User(firstName + " " + lastName, userName, firebaseUser.getUid()
                                    , new ContactInfo(emailAddress, phoneNumber, null)));
                        }
                        else {
                            Toast.makeText(SignUpActivity.this, "Registration failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void accountCreated(User user){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference();
        databaseReference.child("Users").child(user.getUserID()).setValue(user);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}
