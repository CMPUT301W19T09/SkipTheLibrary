package com.stl.skipthelibrary;

/**
 * This class is a singleton and contains the current user
 */
public class CurrentUser {

    private static final User ourInstance = new User();

    /**
     * Gets the current user
     * @return the current user
     */
    public static User getInstance() {
        return ourInstance;
    }

    /**
     * Sets the current user
     * @param user: the user to set
     */
    public static void setUser(User user) {
        getInstance().setOwnerRating(user.getOwnerRating());
        getInstance().setBorrowerRating(user.getBorrowerRating());
        getInstance().setContactInfo(user.getContactInfo());
        getInstance().setImage(user.getImage());
        getInstance().setName(user.getName());
        getInstance().setUserName(user.getUserName());
        getInstance().setNotifications(user.getNotifications());
    }
}
