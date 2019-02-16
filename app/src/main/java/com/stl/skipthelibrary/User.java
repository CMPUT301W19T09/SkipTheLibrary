package com.stl.skipthelibrary;

public class User {
    private String name;
    private ViewableImage image;
    private ContactInfo contactInfo;
    private Credentials credentials;
    private Identity ownerIdentity;
    private Identity borrowerIdentity;


    public User(String name, ViewableImage image, ContactInfo contactInfo, Credentials credentials, Identity ownerIdentity, Identity borrowerIdentity) {
        this.name = name;
        this.image = image;
        this.contactInfo = contactInfo;
        this.credentials = credentials;
        this.ownerIdentity = ownerIdentity;
        this.borrowerIdentity = borrowerIdentity;
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

    public Identity getOwnerIdentity() {
        return ownerIdentity;
    }

    public void setOwnerIdentity(Identity ownerIdentity) {
        this.ownerIdentity = ownerIdentity;
    }

    public Identity getBorrowerIdentity() {
        return borrowerIdentity;
    }

    public void setBorrowerIdentity(Identity borrowerIdentity) {
        this.borrowerIdentity = borrowerIdentity;
    }


}
