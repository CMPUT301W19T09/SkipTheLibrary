package com.stl.skipthelibrary;

/**
 * Created by Luke Slevinsky on 2019-03-01.
 */
public class CurrentUser {

    private static final User ourInstance = new User();

    public static User getInstance() {
        return ourInstance;
    }

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
