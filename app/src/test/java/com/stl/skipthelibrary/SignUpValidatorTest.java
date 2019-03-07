package com.stl.skipthelibrary;

import com.stl.skipthelibrary.Validators.SignUpValidator;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Luke Slevinsky on 2019-02-23.
 */
public class SignUpValidatorTest {
    private SignUpValidator signUpValidator;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String phoneNumber;
    private String errorMessage;

    @Before
    public void setUp() throws Exception {
        signUpValidator = new SignUpValidator();
    }

    @Test
    public void testSetUsername() {
        username = "username";
        assertEquals(null, signUpValidator.getUsername());
        signUpValidator.setUsername(username);
        assertEquals(username, signUpValidator.getUsername());
    }
    @Test
    public void testSetPassword() {
        password = "password";
        assertEquals(null, signUpValidator.getPassword());
        signUpValidator.setPassword(password);
        assertEquals(password, signUpValidator.getPassword());
    }
    @Test
    public void testSetFirstName() {
        firstName = "firstName";
        assertEquals(null, signUpValidator.getFirstName());
        signUpValidator.setFirstName(firstName);
        assertEquals(firstName, signUpValidator.getFirstName());
    }
    @Test
    public void testSetLastName() {
        lastName = "lastName";
        assertEquals(null, signUpValidator.getLastName());
        signUpValidator.setLastName(lastName);
        assertEquals(lastName, signUpValidator.getLastName());
    }
    @Test
    public void testSetEmailAddress() {
        emailAddress = "emailAddress";
        assertEquals(null, signUpValidator.getEmailAddress());
        signUpValidator.setEmailAddress(emailAddress);
        assertEquals(emailAddress, signUpValidator.getEmailAddress());
    }
    @Test
    public void testSetPhoneNumber() {
        phoneNumber = "phoneNumber";
        assertEquals(null, signUpValidator.getPhoneNumber());
        signUpValidator.setPhoneNumber(phoneNumber);
        assertEquals(phoneNumber, signUpValidator.getPhoneNumber());
    }
    @Test
    public void testIsUserNameValid() {
        String emptyName = "";
        String goodName = "good";
        signUpValidator.setUsername(emptyName);
        assertFalse(signUpValidator.isUserNameValid());
        signUpValidator.setUsername(goodName);
        assertTrue(signUpValidator.isUserNameValid());
    }
    @Test
    public void testIsPasswordValid() {
        String badPass = "123";
        String goodPass = "123456";
        signUpValidator.setPassword(badPass);
        assertFalse(signUpValidator.isPasswordValid());
        signUpValidator.setPassword(goodPass);
        assertTrue(signUpValidator.isPasswordValid());
    }
    @Test
    public void testIsFirstNameValid() {
        String badName = "";
        String goodName = "name";
        signUpValidator.setFirstName(badName);
        assertFalse(signUpValidator.isFirstNameValid());
        signUpValidator.setFirstName(goodName);
        assertTrue(signUpValidator.isFirstNameValid());
    }
    @Test
    public void testIsLastNameValid() {
        String badName = "";
        String goodName = "name";
        signUpValidator.setLastName(badName);
        assertFalse(signUpValidator.isLastNameValid());
        signUpValidator.setLastName(goodName);
        assertTrue(signUpValidator.isLastNameValid());
    }
    @Test
    public void testIsEmailNameValid() {
        String badEmail = "........";
        String goodEmail = "good.email@email.com";
        signUpValidator.setEmailAddress(badEmail);
        assertFalse(signUpValidator.isEmailNameValid());
        signUpValidator.setEmailAddress(goodEmail);
        assertTrue(signUpValidator.isEmailNameValid());
    }
    @Test
    public void testIsPhoneNumberValid() {
        String badPhone = "asd";
        String goodPhone = "7806198888";
        signUpValidator.setPhoneNumber(badPhone);
        assertFalse(signUpValidator.isPhoneNumberValid());
        signUpValidator.setPhoneNumber(goodPhone);
        assertTrue(signUpValidator.isPhoneNumberValid());
    }
    @Test
    public void testIsValid() {
        String goodPhone = "7806198888";
        String goodEmail = "good.email@email.com";
        String goodFirstName = "name";
        String goodLastName = "last";
        String goodUserName = "good";
        String goodPass = "123456";
        String badPass = "123";

        SignUpValidator signUpValidator_test = new SignUpValidator(goodUserName, goodPass, goodFirstName, goodLastName, goodEmail, goodPhone);
        assertTrue(signUpValidator_test.isPasswordValid());
        signUpValidator_test.setPassword(badPass);
        assertFalse(signUpValidator_test.isValid());
    }
}