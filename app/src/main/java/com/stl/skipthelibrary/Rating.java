package com.stl.skipthelibrary;

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
}
