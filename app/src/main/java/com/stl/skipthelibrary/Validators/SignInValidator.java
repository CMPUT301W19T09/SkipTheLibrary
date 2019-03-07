package com.stl.skipthelibrary.Validators;

import java.util.regex.Pattern;

/**
 * Determines if a user's login details are of proper format
 */
public class SignInValidator implements Validatable {
    private String emailAddress;
    private String password;
    private String errorMessage;

    /**
     * Empty constructor
     */
    public SignInValidator() {
    }

    /**
     * Full constructor
     * @param emailAddress: the user's email adress
     * @param password:: the user's password
     */
    public SignInValidator(String emailAddress, String password) {
        this.emailAddress = emailAddress;
        this.password = password;
        this.errorMessage = "No errors detected";
    }

    /**
     * Determines if the email is valid
     * @return true if valid, false otherwise
     */
    public boolean isEmailNameValid(){
        Boolean valid = Pattern.compile("[a-zA-Z_0-9.]+@[a-zA-Z]+\\.[a-zA-Z]+").matcher(emailAddress).matches();

        if (!valid){
            errorMessage = "Invalid email address format";
        }

        return valid;
    }

    /**
     * Determines if the password is valid
     * @return true if valid, false otherwise
     */
    public boolean isPasswordValid(){
        Boolean valid = Pattern.compile("[0-9a-zA-Z_]{6,}").matcher(password).matches();

        if (!valid){
            errorMessage = "Invalid password format";
        }
        return valid;
    }

    /**
     * Get the user's email address
     * @return the user's email address
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Sets the user's email address
     * @param emailAddress: the user's email address
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * Get the user's password
     * @return the user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user's password
     * @param password: the user's password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets the error message
     * @param errorMessage: the error message
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Determines if a user's login details are of proper format
     * @return true if valid, false otherwise
     */
    @Override
    public boolean isValid() {
        return isEmailNameValid() && isPasswordValid();
    }

    /**
     * Gets the error message
     * @return the error message
     */
    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
