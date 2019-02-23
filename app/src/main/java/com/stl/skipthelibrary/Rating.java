package com.stl.skipthelibrary;

import java.util.Objects;

public class Rating {
    private double averageRating;
    private int count;
    private int maxRating;
    private int minRating;

    public Rating() {
        this(0, 0, 5, 0);
    }

    public Rating(double averageRating, int count, int maxRating, int minRating) {
        this.averageRating = averageRating;
        this.count = count;
        this.maxRating = maxRating;
        this.minRating = minRating;
    }

    public int getCount() {
        return count;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setMaxRating(int maxRating) {
        this.maxRating = maxRating;
    }

    public void setMinRating(int minRating) {
        this.minRating = minRating;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void addRating(double rating) throws RatingOutOfBoundsException{
        if (rating < minRating || rating > maxRating){
            throw new RatingOutOfBoundsException();
        }
        double totalRating = (averageRating * (double) count) + rating;
        count++;
        averageRating = totalRating / (double) count;
    }

    public int getMaxRating() {
        return maxRating;
    }

    public int getMinRating() {
        return minRating;
    }

    @Override
    public String toString() {
        return "{rating = " + averageRating + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!Rating.class.isAssignableFrom(obj.getClass())) {
            return false;
        }

        final Rating other = (Rating) obj;
        if (getAverageRating() != other.getAverageRating()) {
            return false;
        }
        if (getCount() != other.getCount()) {
            return false;
        }
        if (getMaxRating() != other.getMaxRating()) {
            return false;
        }
        if (getMinRating() != other.getMinRating()) {
            return false;
        }

        return true;
    }


    @Override
    public int hashCode() {
        return Objects.hash(averageRating, count, maxRating, minRating);
    }
}
