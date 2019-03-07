package com.stl.skipthelibrary.Validators;

/**
 * This interface is user to define classes that must validate fields
 */
public interface Validatable {
    /**
     * Determines if the current fields are valid
     * @return true if valid, false otherwise
     */
    boolean isValid();

    /**
     * Get the error message
     * @return the error message
     */
    String getErrorMessage();
}
