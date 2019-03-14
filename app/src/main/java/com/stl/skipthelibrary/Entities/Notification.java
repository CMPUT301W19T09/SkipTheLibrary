package com.stl.skipthelibrary.Entities;

import com.stl.skipthelibrary.Enums.NotificationType;

import java.util.Objects;
import java.util.UUID;

/**
 * This class describes a notification
 */
public class Notification {
    private NotificationType notificationType;
    private String userName;
    private String bookID;
    private String bookName;
    private String uuid;

    /**
     * Empty constructor
     */
    public Notification() {
        this.uuid = UUID.randomUUID().toString();
    }

    /**
     * Constructor will most parameters
     * @param notificationType: the type of notification
     * @param userName: the userName of the user this notification is for
     * @param bookID: the book's ID
     * @param bookName: the book's name
     */
    public Notification(NotificationType notificationType, String userName, String bookID, String bookName) {
        this.notificationType = notificationType;
        this.userName = userName;
        this.bookID = bookID;
        this.bookName = bookName;
        this.uuid = UUID.randomUUID().toString();
    }

    /**
     * Constructor will all parameters
     * @param notificationType: the type of notification
     * @param userName: the userName of the user this notification is for
     * @param bookID: the book's ID
     * @param bookName: the book's name
     * @param uuid: the notification's uuid
     */
    public Notification(NotificationType notificationType, String userName, String bookID, String bookName, String uuid) {
        this.notificationType = notificationType;
        this.userName = userName;
        this.bookID = bookID;
        this.bookName = bookName;
        this.uuid = uuid;
    }

    /**
     * Set the notification type
     * @param notificationType: the notification type to set
     */
    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    /**
     * Get the notification type
     * @return the notification type
     */
    public NotificationType getNotificationType() {
        return notificationType;
    }

    /**
     * Get the userName
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Set the userName
     * @param userName: the userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }


    /**
     * Get the bookID
     * @return the bookID
     */
    public String getBookID() {
        return bookID;
    }

    /**
     * Set the bookID
     * @param bookID: the bookID
     */
    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    /**
     * Get the book's name
     * @return the book's name
     */
    public String getBookName() {
        return bookName;
    }

    /**
     * Set the book's name
     * @param bookName: the book's name
     */
    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    /**
     * Get the uuid
     * @return the uuid
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * Set the uuid
     * @param uuid the uuid
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /**
     * Determines if the notification is identical to another object
     * @param o: An object to compare the notification to
     * @return true if o is identical to the current notification
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Notification)) return false;
        Notification that = (Notification) o;
        return getNotificationType() == that.getNotificationType() &&
                Objects.equals(getUserName(), that.getUserName()) &&
                Objects.equals(getBookID(), that.getBookID()) &&
                Objects.equals(getBookName(), that.getBookName());
    }


    /**
     * Calculate and return the notification's hashcode
     * @return the notification's hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(getNotificationType(), getUserName(), getBookID(), getBookName());
    }
}
