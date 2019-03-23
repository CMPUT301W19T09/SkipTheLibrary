package com.stl.skipthelibrary.Entities;

import com.stl.skipthelibrary.Exceptions.RatingOutOfBoundsException;

import java.util.Objects;

/**
 * This class describes the concept of a rating
 */
public class Rating {
    private double averageRating;
    private int count;
    private int maxRating;
    private int minRating;

    /**
     * The empty constructor
     */
    public Rating() {
        this(0, 0, 5, 0);
    }

    /**
     * The full constructor
     * @param averageRating: the average recieved rating
     * @param count: the total number of ratings
     * @param maxRating: the max rating permitted
     * @param minRating: the min rating permitted
     */
    public Rating(double averageRating, int count, int maxRating, int minRating) {
        this.averageRating = averageRating;
        this.count = count;
        this.maxRating = maxRating;
        this.minRating = minRating;
    }

    /**
     * Get the total number of ratings
     * @return the total number of ratings
     */
    public int getCount() {
        return count;
    }

    /**
     * Set the the total number of ratings
     * @param count: the total number of ratings
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * Get the max rating permitted
     * @return the max rating permitted
     */
    public int getMaxRating() {
        return maxRating;
    }

    /**
     * Set the max rating permitted
     * @param maxRating: the max rating permitted
     */
    public void setMaxRating(int maxRating) {
        this.maxRating = maxRating;
    }

    /**
     * Get the max rating permitted
     * @return the max rating permitted
     */
    public int getMinRating() {
        return minRating;
    }

    /**
     * Set the min rating permitted
     * @param minRating: the min rating permitted
     */
    public void setMinRating(int minRating) {
        this.minRating = minRating;
    }

    /**
     * Get the average rating
     * @return the average rating
     */
    public double getAverageRating() {
        return averageRating;
    }

    /**
     * Set the average Rating
     * @param averageRating: the average rating
     */
    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    /**
     * Add a new rating
     * @param rating: the new rating to add
     * @throws RatingOutOfBoundsException : If the rating is outside [min,max] this exception is
     * thrown
     */
    public void addRating(double rating) throws RatingOutOfBoundsException{
        if (rating < minRating || rating > maxRating){
            throw new RatingOutOfBoundsException();
        }
        double totalRating = (averageRating * (double) count) + rating;
        count++;
        averageRating = totalRating / (double) count;
    }


    /**
     * Convert the rating into a string
     * @return the rating as  a string
     */
    @Override
    public String toString() {
        return "{rating = " + averageRating + ", count " + count + "}";
    }

    /**
     * Determines if the rating is identical to another object
     * @param o: An object to compare the rating to
     * @return true if o is identical to the current rating
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rating)) return false;
        Rating rating = (Rating) o;
        return Double.compare(rating.getAverageRating(), getAverageRating()) == 0 &&
                getCount() == rating.getCount() &&
                getMaxRating() == rating.getMaxRating() &&
                getMinRating() == rating.getMinRating();
    }

    /**
     * Calculate and return the rating's hashcode
     * @return the rating's hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(getAverageRating(), getCount(), getMaxRating(), getMinRating());
    }
}
