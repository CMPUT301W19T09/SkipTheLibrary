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
    private UserIdentity ownerUserIdentity;
    private UserIdentity borrowerUserIdentity;
    private ArrayList<Notification> notifications;

    public User(){
        this.name = "";
        this.userName = "";
        this.userID = UUID.randomUUID().toString();
        this.image = null;
        this.contactInfo = null;
        this.ownerUserIdentity = null;
        this.borrowerUserIdentity = null;
        this.notifications = new ArrayList<Notification>();
    }

    public User(String name, String userName, String userID, ContactInfo contactInfo) {
        this.name = name;
        this.userName = userName;
        this.userID = userID;
        this.contactInfo = contactInfo;
        this.image = null;
        this.ownerUserIdentity = new UserIdentity(UserMode.OWNER);
        this.borrowerUserIdentity = new UserIdentity(UserMode.BORROWER);
        this.notifications = new ArrayList<Notification>();
    }

    public User(String name, String userName, String userID, ViewableImage image, ContactInfo contactInfo, UserIdentity ownerUserIdentity, UserIdentity borrowerUserIdentity, ArrayList<Notification> notifications) {
        this.name = name;
        this.userName = userName;
        this.userID = userID;
        this.image = image;
        this.contactInfo = contactInfo;
        this.ownerUserIdentity = ownerUserIdentity;
        this.borrowerUserIdentity = borrowerUserIdentity;
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

    @Override
    public String toString() {
        return "{Name = " + name + " ,userName = " + userName + " ,userID = " + userID
                + " ,contactInfo = " + contactInfo.toString() + " ,ownerUserIdentity = "
                + ownerUserIdentity.toString() + " ,borrowerUserIdentity = "
                + borrowerUserIdentity.toString() + "}";
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
                Objects.equals(getOwnerUserIdentity(), user.getOwnerUserIdentity()) &&
                Objects.equals(getBorrowerUserIdentity(), user.getBorrowerUserIdentity()) &&
                Objects.equals(getNotifications(), user.getNotifications());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getUserName(), getUserID(), getImage(), getContactInfo(), getOwnerUserIdentity(), getBorrowerUserIdentity(), getNotifications());
    }
}
