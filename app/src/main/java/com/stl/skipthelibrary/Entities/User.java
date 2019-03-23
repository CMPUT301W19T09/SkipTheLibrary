package com.stl.skipthelibrary.Entities;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

/**
 * The user class defines a user
 */
public class User {
    private String name;
    private String userName;
    private String userID;
    private ViewableImage image;
    private ContactInfo contactInfo;
    private Rating ownerRating;
    private Rating borrowerRating;

    /**
     * The empty constructor
     */
    public User(){
        this.name = "";
        this.userName = "";
        this.userID = UUID.randomUUID().toString();
        this.image = null;
        this.contactInfo = null;
        this.ownerRating = new Rating();
        this.borrowerRating = new Rating();
    }

    /**
     * A partial constructor
     * @param name: the user's name
     * @param userName: the user's username
     * @param userID: the user's id
     * @param contactInfo: the user's contact info
     * @param image: the user's image
     */
    public User(String name, String userName, String userID, ContactInfo contactInfo, ViewableImage image) {
        this.name = name;
        this.userName = userName;
        this.userID = userID;
        this.contactInfo = contactInfo;
        this.image = image;
        this.ownerRating = new Rating();
        this.borrowerRating = new Rating();
    }

    /**
     * The full constructor
     * @param name: the user's name
     * @param userName: the user's username
     * @param userID: the user's user id
     * @param image: the user's image
     * @param contactInfo: the user's contact info
     * @param ownerRating: the user's owner rating
     * @param borrowerRating: the user's borrower rating
     */
    public User(String name, String userName, String userID, ViewableImage image, ContactInfo contactInfo, Rating ownerRating, Rating borrowerRating) {
        this.name = name;
        this.userName = userName;
        this.userID = userID;
        this.image = image;
        this.contactInfo = contactInfo;
        this.ownerRating = ownerRating;
        this.borrowerRating = borrowerRating;
    }


    /**
     * Get the user's username
     * @return the user's username
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Set the user's username
     * @param userName: the user's username
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Get the user's user id
     * @return the user's user id
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Set the user's user id
     * @param userID: the user's user id
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * Get the user's name
     * @return the user's name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the user's name
     * @param name: the user's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the user's image
     * @return the user's image
     */
    public ViewableImage getImage() {
        return image;
    }

    /**
     * Set the user's image
     * @param image: the user's image
     */
    public void setImage(ViewableImage image) {
        this.image = image;
    }

    /**
     * Get the user's contact info
     * @return the user's contact info
     */
    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    /**
     * Set the user's contact info
     * @param contactInfo: the user's contact info
     */
    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    /**
     * Get the user's owner rating
     * @return the user's owner rating
     */
    public Rating getOwnerRating() {
        return ownerRating;
    }

    /**
     * Sets the user's owner rating
     * @param ownerRating
     */
    public void setOwnerRating(Rating ownerRating) {
        this.ownerRating = ownerRating;
    }

    /**
     * Gets the user's borrower rating
     * @return the user's borrower rating
     */
    public Rating getBorrowerRating() {
        return borrowerRating;
    }

    /**
     * Sets the user's borrowers rating
     * @param borrowerRating: the user's borrower rating
     */
    public void setBorrowerRating(Rating borrowerRating) {
        this.borrowerRating = borrowerRating;
    }

    /**
     * Convert the user into a string
     * @return the user a string
     */
    @Override
    public String toString() {
        return "{Name = " + name + " ,userName = " + userName + " ,userID = " + userID
                + " ,contactInfo = " + contactInfo.toString() + " ,ownerRating = "
                + ownerRating.toString() + " ,borrowerRating = "
                + borrowerRating.toString() + "}";
    }

    /**
     * Determines if the user is identical to another object
     * @param o: An object to compare the user to
     * @return true if o is identical to the current user
     */
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
                Objects.equals(getBorrowerRating(), user.getBorrowerRating());
    }

    /**
     * Calculate and return the user's hashcode
     * @return the user's hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(getName(), getUserName(), getUserID(), getImage(), getContactInfo(), getOwnerRating(), getBorrowerRating());
    }
}
