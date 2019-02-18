package com.stl.skipthelibrary;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;

public class ContactInfo {
    private String email;
    private String phoneNumber;
    private Context context;

    public ContactInfo(String email, String phoneNumber, Context context) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.context = context;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void startCall() {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        context.startActivity(callIntent);
    }

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
}
