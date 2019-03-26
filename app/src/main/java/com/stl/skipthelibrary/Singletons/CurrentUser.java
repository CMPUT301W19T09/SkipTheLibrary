package com.stl.skipthelibrary.Singletons;

import com.stl.skipthelibrary.Entities.User;

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
        getInstance().setUserID(user.getUserID());
        getInstance().setImage(user.getImage());
        getInstance().setName(user.getName());
        getInstance().setUserName(user.getUserName());
    }
}
