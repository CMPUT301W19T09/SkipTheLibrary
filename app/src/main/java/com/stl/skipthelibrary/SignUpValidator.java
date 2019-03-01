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

    public SignUpValidator() {
        this(null,null,null,null,null,null);
    }

    public SignUpValidator(String username, String password, String firstName, String lastName, String emailAddress, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.errorMessage = "No errors detected";
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    ///////

    public boolean isUserNameValid(){
        Boolean valid = Pattern.compile("[0-9a-zA-Z_]+").matcher(username).matches();

        if (!valid){
            errorMessage = "Invalid username format";
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
    public boolean isFirstNameValid(){
        Boolean valid = Pattern.compile("[a-zA-Z_]+").matcher(firstName).matches();

        if (!valid){
            errorMessage = "Invalid first name format";
        }

        return valid;
    }
    public boolean isLastNameValid(){
        Boolean valid = Pattern.compile("[a-zA-Z_]+").matcher(lastName).matches();

        if (!valid){
            errorMessage = "Invalid last name format";
        }

        return valid;
    }
    public boolean isEmailNameValid(){
        Boolean valid = Pattern.compile("[a-zA-Z_0-9.]+@[a-zA-Z]+\\.[a-zA-Z]+").matcher(emailAddress).matches();

        if (!valid){
            errorMessage = "Invalid email address format";
        }

        return valid;
    }
    public boolean isPhoneNumberValid(){
        Boolean valid = Pattern.compile("\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}").matcher(phoneNumber).matches();

        if (!valid){
            errorMessage = "Invalid phone number format";
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
