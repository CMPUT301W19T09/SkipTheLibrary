package com.stl.skipthelibrary;

import java.util.regex.Pattern;

public class SignInValidator implements Validatable {
    private String emailAddress;
    private String password;
    private String errorMessage;

    public SignInValidator(String emailAddress, String password) {
        this.emailAddress = emailAddress;
        this.password = password;
        this.errorMessage = "No errors detected";
    }

    public boolean isEmailNameValid(){
        Boolean valid = Pattern.compile("[a-zA-Z_0-9.]+@[a-zA-Z]+\\.[a-zA-Z]+").matcher(emailAddress).matches();

        if (!valid){
            errorMessage = "Invalid email address format";
        }

        return valid;
    }

    public boolean isPasswordValid(){
        Boolean valid = Pattern.compile("[0-9a-zA-Z_]{6,}").matcher(password).matches();

        if (!valid){
            errorMessage = "Invalid password format";
        }
        return valid;
    }

    @Override
    public boolean isValid() {
        return isEmailNameValid() && isPasswordValid();
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
