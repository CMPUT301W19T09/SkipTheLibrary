package com.stl.skipthelibrary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    final public static String TAG = "LoginActivity";

    private EditText passwordText;
    private EditText emailText;
    private DatabaseHelper databaseHelper;
    private boolean permissionsAccepted;

    private static final int PERMISSIONS_RC = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailText = findViewById(R.id.EmailEditText);
        passwordText = findViewById(R.id.PasswordEditText);
        databaseHelper = new DatabaseHelper(this);

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

        permissionsAccepted = false;
        requestPermissions();
        autoLogIn();
    }

    private void requestPermissions() {
        boolean coarseLocationNeeded = ActivityCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED;
        boolean fineLocationNeeded = ActivityCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED;
        boolean cameraNeeded = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED;

        ArrayList<String> permissions = new ArrayList<>();

        if (coarseLocationNeeded){
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (fineLocationNeeded){
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (cameraNeeded){
            permissions.add(Manifest.permission.CAMERA);
        }

        if (permissions.size() > 0){
            ActivityCompat.requestPermissions(this,
                    permissions.toArray(new String[permissions.size()]),
                    PERMISSIONS_RC);
        }
        else{
            permissionsAccepted = true;
        }
    }

    private void autoLogIn(){
        if (permissionsAccepted){
            // user is already logged in
            if (databaseHelper.isUserLoggedIn()){
                try{
                    databaseHelper.pullUserSignIn(databaseHelper.getFirebaseUser().getUid());
                    Toast.makeText(this, "Automatically Signed In", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Toast.makeText(this, "Your account has been deleted.", Toast.LENGTH_SHORT).show();
                    try{
                        databaseHelper.signOut();
                    }
                    catch(Exception ex){
                        ex.printStackTrace();
                    }
                }
            }
        }
        else{
            Toast.makeText(this, "Please accept all permissions to login.", Toast.LENGTH_SHORT).show();
            requestPermissions();
        }
    }

    public void signInOnClick(View view) {
        if (permissionsAccepted){
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
        else{
            Toast.makeText(this, "Please accept all permissions to login.", Toast.LENGTH_SHORT).show();
            requestPermissions();
        }
    }

    public void signUpOnClick(View view) {
        if (permissionsAccepted){
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "Please accept all permissions to sign up.", Toast.LENGTH_SHORT).show();
            requestPermissions();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (permissions.length == grantResults.length && requestCode == PERMISSIONS_RC && grantResults.length > 0){
            for (int i = 0; i < grantResults.length; i++){
                Log.d(TAG, (grantResults[i] == PackageManager.PERMISSION_GRANTED) + "");
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                    return;
                }
            }
            permissionsAccepted = true;
            autoLogIn();
        }
    }

    @Override
    public void onBackPressed() {
    }
}
