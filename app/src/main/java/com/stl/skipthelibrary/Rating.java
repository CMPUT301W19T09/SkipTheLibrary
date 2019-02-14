package com.stl.skipthelibrary;

public class Rating {
    private int rating;
    private int maxRating;
    private int minRating;

    public Rating(int rating) {
        this.rating = rating;
        this.minRating = 0;
        this.maxRating = 5;
    }

    public Rating(int rating, int maxRating, int minRating) {
        this.rating = rating;
        this.maxRating = maxRating;
        this.minRating = minRating;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getMaxRating() {
        return maxRating;
    }

    public void setMaxRating(int maxRating) {
        this.maxRating = maxRating;
    }

    public int getMinRating() {
        return minRating;
    }

    public void setMinRating(int minRating) {
        this.minRating = minRating;
    }
}
