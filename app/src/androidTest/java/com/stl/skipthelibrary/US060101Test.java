package com.stl.skipthelibrary;


import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.robotium.solo.Solo;
import com.stl.skipthelibrary.Activities.AddBooksActivity;
import com.stl.skipthelibrary.Activities.BorrowersBooksActivity;
import com.stl.skipthelibrary.Activities.LoginActivity;
import com.stl.skipthelibrary.Activities.MapBoxActivity;
import com.stl.skipthelibrary.Activities.MyBooksActivity;
import com.stl.skipthelibrary.Activities.NotificationActivity;
import com.stl.skipthelibrary.Activities.ProfileActivity;
import com.stl.skipthelibrary.Activities.RateUserActivity;
import com.stl.skipthelibrary.Activities.ScannerActivity;
import com.stl.skipthelibrary.Activities.SearchActivity;
import com.stl.skipthelibrary.Activities.ViewBookActivity;
import com.stl.skipthelibrary.BindersAndAdapters.BookRecyclerAdapter;
import com.stl.skipthelibrary.DatabaseAndAPI.DatabaseHelper;
import com.stl.skipthelibrary.Entities.Book;
import com.stl.skipthelibrary.Entities.BookDescription;
import com.stl.skipthelibrary.Entities.Rating;
import com.stl.skipthelibrary.Entities.RequestHandler;
import com.stl.skipthelibrary.Entities.State;
import com.stl.skipthelibrary.Helpers.NavigationHandler;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Map;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;


public class US060101Test extends IntentsTestRule<LoginActivity> {
    private Solo solo;
    private BottomNavigationView view;
    private UITestHelper uiTestHelper;
    private static final String isbn = "0120120120123";
    private static final String bookTitle = "BookTest1";
    private static final String ownerEmail = "uitest@email.com";
    private static final String ownerPassword = "123123";
    private static final String borrowerEmail = "uitestborrower@email.com";
    private static final String borrowerPassword = "123123";

    public US060101Test() throws InterruptedException {
        super(LoginActivity.class, true, true);
        ArrayList<Book> books = new ArrayList<>();
        books.clear();

        RequestHandler requests = new RequestHandler(new State());
        BookDescription book1Description = new BookDescription(bookTitle, "Test book", "Test Author");
        Book book1 = new Book(isbn, book1Description, uiTestHelper.userName, requests, null, new Rating());

        books.add(book1);
        uiTestHelper = new UITestHelper(true, true, books);
    }


    @Rule
    public IntentsTestRule<LoginActivity> rule =
            new IntentsTestRule<>(LoginActivity.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), rule.getActivity());
    }

    @Test
    public void testScanningFlow() throws Exception {
        /**
         * Test Login functionality
         */
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.sleep(5000);

        if (solo.searchText("Notifications")) {
            enterProfile();
            if (!solo.searchText(ownerEmail)) {
                logOutAccount();
                logInAccount(ownerEmail, ownerPassword);
            }
        }
        else {
            logInAccount(ownerEmail, ownerPassword);
        }


        /**
         * Check the owner's book
         */
        enterMyBookActivity();



        /**
         * Log out
         */
        logOutAccount();

        /**
         * Switch account
         */
        logInAccount(borrowerEmail, borrowerPassword);

        /**
         * Search the request book and send the request
         */
        enterBorrowersBookActivity();
        solo.assertCurrentActivity("Wrong Activity", BorrowersBooksActivity.class);
        solo.sleep(1000);
        solo.clickOnView(solo.getView(R.id.searchBookButton));
        solo.waitForActivity(SearchActivity.class);
        solo.assertCurrentActivity("Wrong Activity", SearchActivity.class);
        solo.enterText((AutoCompleteTextView) solo.getView(R.id.SearchBar), bookTitle);
        assertTrue(solo.waitForText(bookTitle, 1, 2000));

        solo.sleep(1000);
        solo.clickInRecyclerView(0);
        solo.assertCurrentActivity("Wrong Activity", ViewBookActivity.class);
        solo.sleep(1000);
        solo.clickOnView(solo.getView(R.id.requestButton));

        solo.goBack();
        solo.sleep(1000);
        solo.assertCurrentActivity("Wrong Activity", SearchActivity.class);

        solo.goBack();
        logOutAccount();


        /**
         * Switch account
         */
        logInAccount(ownerEmail, ownerPassword);
        enterMyBookActivity();

        /**
         * Owner accept the borrowing request
         */
        viewBookFromMybookActivity();
        RecyclerView requstedBookList = (RecyclerView) solo.getView(R.id.RequesterRecyclerView);
        solo.sleep(1000);
        solo.clickOnView(requstedBookList.getChildAt(0).findViewById(R.id.approve_button_id));
        solo.assertCurrentActivity("Wrong Activity", MapBoxActivity.class);
        solo.sleep(1000);
        solo.clickOnButton("Submit Location");
        solo.waitForText(bookTitle);
        solo.assertCurrentActivity("Should be ViewBookActivity", ViewBookActivity.class);

        solo.goBack();
        solo.waitForText("My Books");
        solo.assertCurrentActivity("Wrong Activity", MyBooksActivity.class);
        logOutAccount();

        /**
         * Switch account
         */
        logInAccount(borrowerEmail, borrowerPassword);

        /**
         * Book status in borrower's side
         */
        enterBorrowersBookActivity();
        logOutAccount();
        solo.waitForActivity(LoginActivity.class);

        /**
         * Switch account
         */
        logInAccount(ownerEmail, ownerPassword);

        /**
         * Owner scan the isbn code and lend the book
         */
        enterMyBookActivity();
        viewBookFromMybookActivity();
        solo.assertCurrentActivity("Wrong Activity", ViewBookActivity.class);
        Intent resultData = new Intent();
        resultData.putExtra("ISBN", isbn);
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
        intending(hasComponent(ScannerActivity.class.getName())).respondWith(result);
        solo.sleep(1000);
        solo.clickOnView(solo.getView(R.id.lendButton));
        solo.sleep(1000);
        solo.goBack();
        solo.waitForText("My Books");
        solo.assertCurrentActivity("Wrong Activity", MyBooksActivity.class);
        logOutAccount();

        /**
         * Switch account
         */
        logInAccount(borrowerEmail, borrowerPassword);

        /**
         * Borrower scan the isbn code and confirm the book is borrowed
         */
        enterBorrowersBookActivity();
        viewBookFromBorrowerBookActivity();
        intending(hasComponent(ScannerActivity.class.getName())).respondWith(result);
        solo.sleep(1000);
        solo.clickOnView(solo.getView(R.id.borrowButton));
        solo.sleep(1000);
        solo.goBack();

        solo.waitForText("Borrowing");
        solo.assertCurrentActivity("Should be BorrowersBookActivity", BorrowersBooksActivity.class);
        logOutAccount();

        /**
         * Switch account
         */
        logInAccount(ownerEmail,ownerPassword);
        /**
         * Book status in owner's side
         */
        enterMyBookActivity();
        logOutAccount();

        /**
         * Switch account
         */
        logInAccount(borrowerEmail, borrowerPassword);
        /**
         * Borrower scan the isbn and return the book
         */
        enterBorrowersBookActivity();
        viewBookFromBorrowerBookActivity();
        intending(hasComponent(ScannerActivity.class.getName())).respondWith(result);
        solo.sleep(1000);
        solo.clickOnView(solo.getView(R.id.returnButton));
        solo.waitForText("rating");
        solo.assertCurrentActivity("Should be RateUserActivity", RateUserActivity.class);
        solo.setProgressBar(0, 6);

        solo.clickOnView(solo.getView(R.id.RateButton));

        solo.sleep(1000);
        solo.goBack();

        solo.waitForText("Borrowing");
        solo.assertCurrentActivity("Should be BorrowersBookActivity", BorrowersBooksActivity.class);
        logOutAccount();

        /**
         * Switch account
         */
        logInAccount(ownerEmail, ownerPassword);

        /**
         * Owner scan the isbn to confirm the book is returned.
         */
        enterMyBookActivity();
        viewBookFromMybookActivity();
        intending(hasComponent(ScannerActivity.class.getName())).respondWith(result);
        solo.sleep(1000);
        solo.clickOnView(solo.getView(R.id.returnedButton));
        solo.waitForText("rating");
        solo.assertCurrentActivity("Should be RateUserActivity", RateUserActivity.class);
        solo.setProgressBar(0, 4);

        solo.clickOnView(solo.getView(R.id.RateButton));
        solo.sleep(1000);
        solo.goBack();

        solo.waitForText("My Books");
        solo.assertCurrentActivity("Should be MyBooksActivity", MyBooksActivity.class);

        logOutAccount();
    }

    @After
    public void tearDown() throws InterruptedException {
        uiTestHelper.finish();
        solo.finishOpenedActivities();
    }


    /**
     * log out current account
     */
    public void logOutAccount(){
        enterProfile();
        solo.sleep(1000);
        solo.scrollDown();
        assertTrue(solo.waitForText("logout"));
        solo.clickOnButton("logout");
        assertTrue(solo.waitForText("Login"));
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
    }

    /**
     * Log in account
     * @param email account email address
     * @param password account password
     */
    public void logInAccount(String email, String password){
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.EmailEditText), email);
        solo.enterText((EditText) solo.getView(R.id.PasswordEditText), password);
        solo.sleep(1000);
        solo.clickOnView(solo.getView(R.id.SignInButton));
    }

    /**
     * Enter My book activity from initial screen
     */
    public void enterMyBookActivity(){
        view = (BottomNavigationView)solo.getView(R.id.bottom_navigation);
        view.setOnNavigationItemSelectedListener(new NavigationHandler(view.getContext()));
        solo.sleep(1000);
        solo.clickOnView(view.findViewById(R.id.my_books));
        solo.assertCurrentActivity("Wrong Activity", MyBooksActivity.class);
    }

    /**
     * View the first book in owner book list
     */
    public void viewBookFromMybookActivity(){
        solo.assertCurrentActivity("Wrong Activity", MyBooksActivity.class);
        solo.sleep(1000);
        solo.clickInRecyclerView(0);
        solo.assertCurrentActivity("Wrong Activity", ViewBookActivity.class);
    }

    /**
     * Enter borrower book activity from initial screen
     */
    public void enterBorrowersBookActivity(){
        view = (BottomNavigationView) solo.getView(R.id.bottom_navigation);
        view.setOnNavigationItemSelectedListener(new NavigationHandler(view.getContext()));
        solo.sleep(1000);
        solo.clickOnView(view.findViewById(R.id.borrow));
    }

    /**
     * View the first book in borrowing book list
     */
    public void viewBookFromBorrowerBookActivity(){
        solo.assertCurrentActivity("Wrong Activity", BorrowersBooksActivity.class);
        RecyclerView borrowerBooksList = (RecyclerView) solo.getView(R.id.borrowerBookRecyclerView);
        solo.sleep(1000);
        solo.clickOnView(borrowerBooksList.getChildAt(0).findViewById(R.id.BookListItemRightArrow));
        solo.assertCurrentActivity("Wrong Activity", ViewBookActivity.class);
    }

    public void enterProfile() {
        solo.clickOnView(solo.getView(R.id.profile));
        solo.assertCurrentActivity("Wrong Activity", ProfileActivity.class);
    }
}
