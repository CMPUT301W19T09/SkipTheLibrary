package com.stl.skipthelibrary;

public class User {
    private String name;
    private ViewableImage image;
    private ContactInfo contactInfo;
    private Credentials credentials;

    public User(String name, ViewableImage image, ContactInfo contactInfo, Credentials credentials) {
        this.name = name;
        this.image = image;
        this.contactInfo = contactInfo;
        this.credentials = credentials;
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
}
