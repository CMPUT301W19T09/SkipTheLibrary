package com.stl.skipthelibrary;

import com.stl.skipthelibrary.Validators.SignInValidator;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SignInValidatorTest {
    private SignInValidator signInValidator;

    @Before
    public void setUp(){
        signInValidator = new SignInValidator();
    }

    @Test
    public void testValidEmail() {
        signInValidator.setEmailAddress("example123@test.com");
        assertTrue(signInValidator.isEmailNameValid());
        assertNull(signInValidator.getErrorMessage());
    }

    @Test
    public void testInvalidEmail() {
        signInValidator.setEmailAddress("wrong email format at bad dot com");
        assertFalse(signInValidator.isEmailNameValid());
        assertNotNull(signInValidator.getErrorMessage());
    }

    @Test
    public void testValidPassword() {
        signInValidator.setPassword("ag12sjb_w28");
        assertTrue(signInValidator.isPasswordValid());
        assertNull(signInValidator.getErrorMessage());
    }

    @Test
    public void testInvalidPassword() {
        signInValidator.setPassword("wrong password format");
        assertFalse(signInValidator.isPasswordValid());
        assertNotNull(signInValidator.getErrorMessage());
    }

    @Test
    public void testisValid() {
        signInValidator.setEmailAddress("wrong email format at bad dot com");
        signInValidator.setPassword("wrong password format");
        assertFalse(signInValidator.isValid());

        signInValidator.setEmailAddress("wrong email format at bad dot com");
        signInValidator.setPassword("ag12sjb_w28");
        assertFalse(signInValidator.isValid());

        signInValidator.setEmailAddress("example123@test.com");
        signInValidator.setPassword("wrong password format");
        assertFalse(signInValidator.isValid());

        signInValidator.setEmailAddress("example123@test.com");
        signInValidator.setPassword("ag12sjb_w28");
        assertTrue(signInValidator.isValid());
    }

    @Test
    public void getErrorMessage() {
    }
}