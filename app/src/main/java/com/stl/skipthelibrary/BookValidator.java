package com.stl.skipthelibrary;

import java.util.regex.Pattern;

/**
 * Created by Luke Slevinsky on 2019-02-20.
 */
public class BookValidator implements Validatable{

    private String title;
    private String author;
    private String isbn;
    private String desc;
    private String errorMessage;

    public BookValidator(String title, String author, String isbn, String desc) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.desc = desc;
        this.errorMessage = "No errors detected";
    }

    public boolean isTitleValid(){
        Boolean valid = Pattern.compile("[0-9a-zA-Z_]+").matcher(title).matches();

        if (!valid){
            errorMessage = "Invalid title format";
        }

        return valid;
    }
    public boolean isAuthorValid(){
        Boolean valid = Pattern.compile("[a-zA-Z_]+").matcher(author).matches();

        if (!valid){
            errorMessage = "Invalid author format";
        }

        return valid;
    }
    public boolean isISBNValid(){
        Boolean valid = Pattern.compile("[a-zA-Z_]+").matcher(isbn).matches();

        if (!valid){
            errorMessage = "Invalid ISBN format";
        }

        return valid;
    }
    public boolean isDescriptionValid(){
        Boolean valid = Pattern.compile("[0-9a-zA-Z_]+").matcher(desc).matches();

        if (!valid){
            errorMessage = "Invalid description format";
        }

        return valid;
    }


    @Override
    public boolean isValid() {
        return isTitleValid() && isAuthorValid() && isISBNValid() && isDescriptionValid();
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}


