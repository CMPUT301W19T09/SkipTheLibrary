package com.stl.skipthelibrary;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

/**
 * A simple wrapper class to simplify the TextWatcher interface
 *
 * The abstract class was taken from this forum post:
 *      https://stackoverflow.com/questions/2763022/android-how-can-i-validate-edittext-input#answer-11838715
 */
public abstract class TextValidator implements TextWatcher {
    private final TextView textView;

    public TextValidator(TextView textView) {
        this.textView = textView;
    }

    public abstract void validate(TextView textView, String text);

    @Override
    final public void afterTextChanged(Editable s) {
        String text = textView.getText().toString();
        validate(textView, text);
    }

    @Override
    final public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    final public void onTextChanged(CharSequence s, int start, int before, int count) { }
}