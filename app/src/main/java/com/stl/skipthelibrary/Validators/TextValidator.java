package com.stl.skipthelibrary.Validators;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;
//The abstract class was taken from this forum post:
//https://stackoverflow.com/questions/2763022/android-how-can-i-validate-edittext-input#answer-11838715

/**
 * A simple wrapper class to simplify the TextWatcher interface
 *
 */
public abstract class TextValidator implements TextWatcher {
    private final TextView textView;

    /**
     * The constructor
     * @param textView: the text to be validated
     */
    public TextValidator(TextView textView) {
        this.textView = textView;
    }

    /**
     * Validate the text in the textview
     * @param textView: the textview to be validated
     * @param text: the text to be validated
     */
    public abstract void validate(TextView textView, String text);

    /**
     * Validate the text after it is changed
     * @param s: the element which has changed
     */
    @Override
    final public void afterTextChanged(Editable s) {
        String text = textView.getText().toString();
        validate(textView, text);
    }

    /**
     * Triggers before text has changed
     * @param s: the character sequence
     * @param start: the start
     * @param count: the length
     * @param after: the after
     */
    @Override
    final public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    /**
     * Triggers after text changes
     * @param s: the character sequence
     * @param start: the start
     * @param before: the before
     * @param count: the length
     */
    @Override
    final public void onTextChanged(CharSequence s, int start, int before, int count) { }
}