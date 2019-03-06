package com.stl.skipthelibrary;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class User {
    private String name;
    private String userName;
    private String userID;
    private ViewableImage image;
    private ContactInfo contactInfo;
    private Rating ownerRating;
    private Rating borrowerRating;
    private ArrayList<Notification> notifications;

    public User(){
        this.name = "";
        this.userName = "";
        this.userID = UUID.randomUUID().toString();
        this.image = null;
        this.contactInfo = null;
        this.ownerRating = new Rating();
        this.borrowerRating = new Rating();
        this.notifications = new ArrayList<>();
    }

    public User(String name, String userName, String userID, ContactInfo contactInfo, ViewableImage image) {
        this.name = name;
        this.userName = userName;
        this.userID = userID;
        this.contactInfo = contactInfo;
        this.image = image;
        this.ownerRating = new Rating();
        this.borrowerRating = new Rating();
        this.notifications = new ArrayList<>();
    }

    public User(String name, String userName, String userID, ViewableImage image, ContactInfo contactInfo, Rating ownerRating, Rating borrowerRating, ArrayList<Notification> notifications) {
        this.name = name;
        this.userName = userName;
        this.userID = userID;
        this.image = image;
        this.contactInfo = contactInfo;
        this.ownerRating = ownerRating;
        this.borrowerRating = borrowerRating;
        this.notifications = notifications;
    }



    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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

    public Rating getOwnerRating() {
        return ownerRating;
    }

    public void setOwnerRating(Rating ownerRating) {
        this.ownerRating = ownerRating;
    }

    public Rating getBorrowerRating() {
        return borrowerRating;
    }

    public void setBorrowerRating(Rating borrowerRating) {
        this.borrowerRating = borrowerRating;
    }

    @Override
    public String toString() {
        return "{Name = " + name + " ,userName = " + userName + " ,userID = " + userID
                + " ,contactInfo = " + contactInfo.toString() + " ,ownerRating = "
                + ownerRating.toString() + " ,borrowerRating = "
                + borrowerRating.toString() + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(getName(), user.getName()) &&
                Objects.equals(getUserName(), user.getUserName()) &&
                Objects.equals(getUserID(), user.getUserID()) &&
                Objects.equals(getImage(), user.getImage()) &&
                Objects.equals(getContactInfo(), user.getContactInfo()) &&
                Objects.equals(getOwnerRating(), user.getOwnerRating()) &&
                Objects.equals(getBorrowerRating(), user.getBorrowerRating()) &&
                Objects.equals(getNotifications(), user.getNotifications());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getUserName(), getUserID(), getImage(), getContactInfo(), getOwnerRating(), getBorrowerRating(), getNotifications());
    }
}
