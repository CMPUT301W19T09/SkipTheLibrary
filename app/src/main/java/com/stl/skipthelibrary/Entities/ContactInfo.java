package com.stl.skipthelibrary.Entities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.util.Objects;

/**
 * This class outlines a user's contact info
 */
public class ContactInfo {
    private String email;
    private String phoneNumber;
    private Context context;

    /**
     * The empty constructor
     */
    public ContactInfo() {
        this.email = "";
        this.phoneNumber = "";
        this.context = null;
    }

    /**
     * The full constructor
     * @param email
     * @param phoneNumber
     * @param context
     */
    public ContactInfo(String email, String phoneNumber, Context context) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.context = context;
    }

    /**
     * Get the current context
     * @return the current context
     */
    public Context getContext() {
        return context;
    }


    /**
     * Set the current context
     * @param context: the current context
     */
    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * Get the user's email
     * @return the user's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the user's email
     * @param email: the user's email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get the user's phone number
     * @return the user's phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Set the user's phone number
     * @param phoneNumber: the user's phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Start a call to the user
     */
    public void startCall() {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        context.startActivity(callIntent);
    }

    /**
     * Start an email to the user
     */
    public void startEmail(){
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra("sms_body"  , "Hello! I am interested in a book of yours!");

        String recipients[] = {email};

        emailIntent.putExtra(Intent.EXTRA_EMAIL, recipients);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Info about your book");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello, I would like to know more about your book.\n\n");
        context.startActivity(emailIntent);
    }

    /**
     * Convert the class to a string
     * @return the string representing the class
     */
    @Override
    public String toString() {
        return "{email= " + email + " ,phonenumber= " + phoneNumber + "}";
    }

    /**
     * Determines if the contact info is identical to another object
     * @param o: An object to compare the contact info to
     * @return true if o is identical to the current contact info
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContactInfo)) return false;
        ContactInfo that = (ContactInfo) o;
        return Objects.equals(getEmail(), that.getEmail()) &&
                Objects.equals(getPhoneNumber(), that.getPhoneNumber()) &&
                Objects.equals(getContext(), that.getContext());
    }

    /**
     * Calculate and return the contact info's hashcode
     * @return the contact info's hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(getEmail(), getPhoneNumber(), getContext());
    }
}
