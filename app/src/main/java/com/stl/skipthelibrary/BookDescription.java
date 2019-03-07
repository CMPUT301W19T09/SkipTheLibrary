package com.stl.skipthelibrary;

import java.util.Objects;

/**
 * This class details the description of a book.
 */
public class BookDescription {
    private String title;
    private String synopsis;
    private String author;
    private Rating rating;

    /**
     * The empty constructor
     */
    public BookDescription(){}

    /**
     * The full constructor
     * @param title: the book's title
     * @param synopsis: the book's synopsis
     * @param author: the book's author
     * @param rating: the book's rating
     */
    public BookDescription(String title, String synopsis, String author, Rating rating) {
        this.title = title;
        this.synopsis = synopsis;
        this.author = author;
        this.rating = rating;
    }


    /**
     * gets the book's title
     * @return the book's title
     */
    public String getTitle() {
        return title;
    }

    /**
     * sets the book's title
     * @param title: the book's title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * gets the book's synopsis
     * @return the book's synopsis
     */
    public String getSynopsis() {
        return synopsis;
    }

    /**
     * sets the book's synopsis
     * @param synopsis: the book's synopsis
     */
    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    /**
     * get the book's author
     * @return
     */
    public String getAuthor() {
        return author;
    }

    /**
     * sets the book's author
     * @param author: the book's author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * get the book's rating
     * @return the book's rating
     */
    public Rating getRating() {
        return rating;
    }

    /**
     * set the book's rating
     * @param rating: the book's rating
     */
    public void setRating(Rating rating) {
        this.rating = rating;
    }

    /**
     * Determines if the object o is identical to the current bookdescription
     * @param o: the object we compare to
     * @return true if o is equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookDescription)) return false;
        BookDescription that = (BookDescription) o;
        return Objects.equals(getTitle(), that.getTitle()) &&
                Objects.equals(getSynopsis(), that.getSynopsis()) &&
                Objects.equals(getAuthor(), that.getAuthor()) &&
                Objects.equals(getRating(), that.getRating());
    }

    /**
     * Calculates the hash for the current instance
     * @return the hash of the current instance
     */
    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getSynopsis(), getAuthor(), getRating());
    }
}
