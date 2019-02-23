package com.stl.skipthelibrary;

import java.util.Objects;

public class BookDescription {
    private String title;
    private String synopsis;
    private String author;
    private Rating rating;

    public BookDescription(String title, String synopsis, String author, Rating rating) {
        this.title = title;
        this.synopsis = synopsis;
        this.author = author;
        this.rating = rating;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

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

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getSynopsis(), getAuthor(), getRating());
    }
}
