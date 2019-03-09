package com.stl.skipthelibrary.Entities;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

/**
 * The book class. This entity contains the state of a book.
 */
public class Book{
    final public static String TAG = "Book";

    private String uuid;
    private String ISBN;
    private BookDescription description;
    private String ownerUserName;
    private RequestHandler requests;
    private ArrayList<ViewableImage> images;
    private Rating rating;

    /**
     * Empty constructor
     */
    public Book() {
        this.uuid = UUID.randomUUID().toString();
    }

    /**
     * Constructor with some fields.
     * @param description: the book's description
     * @param ISBN: the book's ISBN
     * @param ownerUserName: the book's owner's username
     * @param images: an arraylist of images of the book
     */
    public Book(BookDescription description, String ISBN, String ownerUserName, ArrayList<ViewableImage> images) {
        this(ISBN, description, ownerUserName, new RequestHandler(), images, new Rating());
    }

    /**
     * The full constructor.
     * @param ISBN: the book's ISBN
     * @param description: the book's description
     * @param ownerUserName: the book's owner's username
     * @param requests: the book's requesthandler
     * @param images: an arraylist of images of the book.
     * @param rating: the book's rating
     */
    public Book(String ISBN, BookDescription description, String ownerUserName, RequestHandler requests, ArrayList<ViewableImage> images, Rating rating) {
        this.uuid = UUID.randomUUID().toString();
        this.ISBN = ISBN;
        this.description = description;
        this.ownerUserName = ownerUserName;
        this.requests = requests;
        this.images = images;
        this.rating = rating;
    }

    /**
     * Determines if a user is interested in the current book
     * @param userName: the owner's username
     * @return true if the user is interested, false otherwise.
     */
    public boolean userIsInterested(String userName){
        if (userName.equals("")){
            return false;
        }
        if (requests == null){
            return false;
        }

        if (requests.getRequestors() != null && requests.getRequestors().contains(userName)){
            return true;
        }

        if (requests.getAcceptedRequestor() != null && requests.getAcceptedRequestor().equals(userName)){
            return true;
        }

        return false;
    }

    /**
     * Get the book's ISBN
     * @return ISBN
     */
    public String getISBN() {
        return ISBN;
    }

    /**
     * Sets the book's ISBN
     * @param ISBN: the book's ISBN
     */
    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    /**
     * Get the book's Unique ID
     * @return the book's Unique ID
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * Sets the book's Unique ID
     * @param uuid: the book's Unique ID
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /**
     * Gets the book's request handler
     * @return the book's request handler
     */
    public RequestHandler getRequests() {
        return requests;
    }

    /**
     * Sets the book's request handler
     * @param requests: the book's request handler
     */
    public void setRequests(RequestHandler requests) {
        this.requests = requests;
    }

    /**
     * Gets the book's rating
     * @return the book's rating
     */
    public Rating getRating() {
        return rating;
    }

    /**
     * Sets the book's rating
     * @param rating: the book's rating
     */
    public void setRating(Rating rating) {
        this.rating = rating;
    }

    /**
     * Get all the images that the book has
     * @return an arraylist of images the book has
     */
    public ArrayList<ViewableImage> getImages() {
        if (images == null){
            return new ArrayList<>();
        }
        return images;
    }

    /**
     * Sets the books images
     * @param images: an arraylist of images of the book.
     */
    public void setImages(ArrayList<ViewableImage> images) {
        this.images = images;
    }

    /**
     * Gets the book's description
     * @return the book's description
     */
    public BookDescription getDescription() {
        return description;
    }

    /**
     * Sets the book's description
     * @param description: the book's description
     */
    public void setDescription(BookDescription description) {
        this.description = description;
    }


    /**
     * Gets the book's owner's username
     * @return the book's owner's username
     */
    public String getOwnerUserName() {
        return ownerUserName;
    }

    /**
     * Set the book's owner's username
     * @param ownerUserName:
     */
    public void setOwnerUserName(String ownerUserName) {
        this.ownerUserName = ownerUserName;
    }

    /**
     * Determines if the book is identical to another object
     * @param o: An object to compare the book to
     * @return true if o is identical to the current book
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return Objects.equals(getUuid(), book.getUuid()) &&
                Objects.equals(getISBN(), book.getISBN()) &&
                Objects.equals(getDescription(), book.getDescription()) &&
                Objects.equals(getOwnerUserName(), book.getOwnerUserName()) &&
                Objects.equals(getRequests(), book.getRequests()) &&
                Objects.equals(getImages(), book.getImages()) &&
                Objects.equals(getRating(), book.getRating());
    }

    /**
     * Calculate and return the book's hashcode
     * @return the book's hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(getUuid(), getISBN(), getDescription(), getOwnerUserName(), getRequests(), getImages(), getRating());
    }
}
