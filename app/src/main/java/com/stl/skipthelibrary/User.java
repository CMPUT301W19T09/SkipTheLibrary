package com.stl.skipthelibrary;

import java.util.ArrayList;

public class User {
    private String name;
    private ViewableImage image;
    private ContactInfo contactInfo;
    private Credentials credentials;
    private UserIdentity ownerUserIdentity;
    private UserIdentity borrowerUserIdentity;
    private ArrayList<Notification> notifications;

    public User(){
        this.name = "";
        this.image = null;
        this.contactInfo = null;
        this.credentials = null;
        this.ownerUserIdentity = null;
        this.borrowerUserIdentity = null;
        this.notifications = new ArrayList<Notification>();
    }

    public User(String name, ViewableImage image, ContactInfo contactInfo, Credentials credentials, UserIdentity ownerUserIdentity, UserIdentity borrowerUserIdentity, ArrayList<Notification> notifications) {
        this.name = name;
        this.image = image;
        this.contactInfo = contactInfo;
        this.credentials = credentials;
        this.ownerUserIdentity = ownerUserIdentity;
        this.borrowerUserIdentity = borrowerUserIdentity;
        this.notifications = notifications;
    }

    public ArrayList<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(ArrayList<Notification> notifications) {
        this.notifications = notifications;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ViewableImage getImage() {
        return image;
    }

    public void setImage(ViewableImage image) {
        this.image = image;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public UserIdentity getOwnerUserIdentity() {
        return ownerUserIdentity;
    }

    public void setOwnerUserIdentity(UserIdentity ownerUserIdentity) {
        this.ownerUserIdentity = ownerUserIdentity;
    }

    public UserIdentity getBorrowerUserIdentity() {
        return borrowerUserIdentity;
    }

    public void setBorrowerUserIdentity(UserIdentity borrowerUserIdentity) {
        this.borrowerUserIdentity = borrowerUserIdentity;
    }


}
