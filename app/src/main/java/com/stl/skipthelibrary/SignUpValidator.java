package com.stl.skipthelibrary;

import java.util.regex.Pattern;

public class SignUpValidator implements Validatable {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String phoneNumber;
    private String errorMessage;

    public SignUpValidator(String username, String password, String firstName, String lastName, String emailAddress, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.errorMessage = "No errors detected";
    }

    private boolean isUserNameValid(){
        Boolean valid = Pattern.compile("[0-9a-zA-Z_]+").matcher(username).matches();

        if (!valid){
            errorMessage = "Invalid username";
        }

        return valid;
    }
    private boolean isPasswordValid(){
        Boolean valid = Pattern.compile("[0-9a-zA-Z_]+").matcher(password).matches();

        if (!valid){
            errorMessage = "Invalid password";
        }

        return valid;
    }
    private boolean isFirstNameValid(){
        Boolean valid = Pattern.compile("[a-zA-Z_]+").matcher(firstName).matches();

        if (!valid){
            errorMessage = "Invalid first name";
        }

        return valid;
    }
    private boolean isLastNameValid(){
        Boolean valid = Pattern.compile("[a-zA-Z_]+").matcher(lastName).matches();

        if (!valid){
            errorMessage = "Invalid last name";
        }

        return valid;
    }
    private boolean isEmailNameValid(){
        Boolean valid = Pattern.compile("[a-zA-Z_0-9.]+@[a-zA-Z]+\\.[a-zA-Z]+").matcher(emailAddress).matches();

        if (!valid){
            errorMessage = "Invalid email address";
        }

        return valid;
    }
    private boolean isPhoneNumberValid(){
        Boolean valid = Pattern.compile("\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}").matcher(phoneNumber).matches();

        if (!valid){
            errorMessage = "Invalid phone number";
        }

        return valid;
    }



    @Override
    public boolean isValid() {
        return isUserNameValid() && isPasswordValid() && isFirstNameValid() && isLastNameValid()
                && isEmailNameValid() && isPhoneNumberValid();
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
