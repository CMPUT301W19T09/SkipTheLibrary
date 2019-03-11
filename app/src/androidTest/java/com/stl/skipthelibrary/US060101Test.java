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

        RequestHandler requests = new RequestHandler(new State());
        BookDescription book1Description = new BookDescription(bookTitle, "Test book", "Test Author", new Rating());
        Book book1 = new Book(isbn, book1Description, uiTestHelper.userName, requests, null, new Rating());

        ArrayList<Book> books = new ArrayList<>();
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
        logInAccount(ownerEmail, ownerPassword);


        /**
         * Test AddBook functionality
         */

        enterMyBookActivity();



        /**
         * log out
         */
        logOutAccount();

        /**
         * Goes into another account
         */
        logInAccount(borrowerEmail, borrowerPassword);

        enterBorrowersBookActivity();
        solo.assertCurrentActivity("Wrong Activity", BorrowersBooksActivity.class);

        solo.clickOnView(solo.getView(R.id.searchBookButton));
        solo.waitForActivity(SearchActivity.class);
        solo.assertCurrentActivity("Wrong Activity", SearchActivity.class);
        solo.enterText((AutoCompleteTextView) solo.getView(R.id.SearchBar), bookTitle);
        assertTrue(solo.waitForText(bookTitle, 1, 2000));


        solo.clickInRecyclerView(0);
        solo.assertCurrentActivity("Wrong Activity", ViewBookActivity.class);
        solo.clickOnView(solo.getView(R.id.requestButton));
        solo.assertCurrentActivity("Wrong Activity", SearchActivity.class);
        solo.goBack();
        logOutAccount();


        /**
         * login Book Owner Account
         */
        logInAccount(ownerEmail, ownerPassword);
        enterMyBookActivity();
        viewBookFromMybookActivity();
        RecyclerView requstedBookList = (RecyclerView) solo.getView(R.id.RequesterRecyclerView);
        solo.clickOnView(requstedBookList.getChildAt(0).findViewById(R.id.approve_button_id));
        solo.assertCurrentActivity("Wrong Activity", MapBoxActivity.class);
        solo.clickOnButton("Submit Location");
        solo.waitForText("My Books");
        solo.assertCurrentActivity("Wrong Activity", MyBooksActivity.class);
        logOutAccount();

        logInAccount(borrowerEmail, borrowerPassword);
        enterBorrowersBookActivity();
        logOutAccount();
        solo.waitForActivity(LoginActivity.class);

        logInAccount(ownerEmail, ownerPassword);
        enterMyBookActivity();
        viewBookFromMybookActivity();
        solo.clickInRecyclerView(0);
        solo.assertCurrentActivity("Wrong Activity", ViewBookActivity.class);
        Intent resultData = new Intent();
        resultData.putExtra("ISBN", isbn);
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
        intending(hasComponent(ScannerActivity.class.getName())).respondWith(result);
        solo.clickOnView(solo.getView(R.id.lendButton));
        solo.waitForText("My Books");
        logOutAccount();

        logInAccount(borrowerEmail, borrowerPassword);
        enterBorrowersBookActivity();
        viewBookFromBorrowerBookActivity();
        intending(hasComponent(ScannerActivity.class.getName())).respondWith(result);
        solo.clickOnView(solo.getView(R.id.borrowButton));
        solo.waitForText("Borrow");
        logOutAccount();

        logInAccount(ownerEmail,ownerPassword);
        enterMyBookActivity();
        logOutAccount();

        logInAccount(borrowerEmail, borrowerPassword);
        enterBorrowersBookActivity();
        viewBookFromBorrowerBookActivity();
        intending(hasComponent(ScannerActivity.class.getName())).respondWith(result);
        solo.clickOnView(solo.getView(R.id.returnButton));
        logOutAccount();

        logInAccount(ownerEmail, ownerPassword);
        enterMyBookActivity();
        viewBookFromMybookActivity();
        intending(hasComponent(ScannerActivity.class.getName())).respondWith(result);
        solo.clickOnView(solo.getView(R.id.returnedButton));
        solo.sleep(3000);
        deleteBook();
    }

    @After
    public void tearDown() throws InterruptedException {
        solo.finishOpenedActivities();
    }

    public void deleteBook() {
        RecyclerView myBooksList = (RecyclerView) solo.getView(R.id.ownerBooksRecyclerView);
        View bookToDelete = myBooksList.getChildAt(0);

        int[] location = new int[2];

        bookToDelete.getLocationInWindow(location);

        int fromX = location[0] + 800;
        int fromY = location[1];

        int toX = location[0];
        int toY = fromY;

        solo.drag(fromX, toX, fromY, toY, 10);
        solo.sleep(1000);

//        assertTrue(myBooksList.getAdapter().getItemCount() == 0);

    }


    public void logOutAccount(){
        /**
         * log out
         */
        view = (BottomNavigationView)solo.getView(R.id.bottom_navigation);
        view.setOnNavigationItemSelectedListener(new NavigationHandler(view.getContext()));
        solo.clickOnView(view.findViewById(R.id.profile));
        solo.assertCurrentActivity("Wrong Activity", ProfileActivity.class);
        solo.clickOnView(solo.getView(R.id.logoutButton));
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);

    }

    public void logInAccount(String email, String password){
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.EmailEditText), email);
        solo.enterText((EditText) solo.getView(R.id.PasswordEditText), password);
        solo.clickOnView(solo.getView(R.id.SignInButton));
    }

    public void enterMyBookActivity(){
        view = (BottomNavigationView)solo.getView(R.id.bottom_navigation);
        view.setOnNavigationItemSelectedListener(new NavigationHandler(view.getContext()));
        solo.clickOnView(view.findViewById(R.id.my_books));
        solo.assertCurrentActivity("Wrong Activity", MyBooksActivity.class);
    }

    public void viewBookFromMybookActivity(){
        solo.assertCurrentActivity("Wrong Activity", MyBooksActivity.class);
        RecyclerView myBooksList = (RecyclerView) solo.getView(R.id.ownerBooksRecyclerView);
        solo.clickOnView(myBooksList.getChildAt(0).findViewById(R.id.BookListItemRightArrow));
        solo.assertCurrentActivity("Wrong Activity", ViewBookActivity.class);
    }

    public void enterBorrowersBookActivity(){
        view = (BottomNavigationView) solo.getView(R.id.bottom_navigation);
        view.setOnNavigationItemSelectedListener(new NavigationHandler(view.getContext()));
        solo.clickOnView(view.findViewById(R.id.borrow));
    }

    public void viewBookFromBorrowerBookActivity(){
        solo.assertCurrentActivity("Wrong Activity", BorrowersBooksActivity.class);
        RecyclerView borrowerBooksList = (RecyclerView) solo.getView(R.id.borrowerBookRecyclerView);
        solo.clickOnView(borrowerBooksList.getChildAt(0).findViewById(R.id.BookListItemRightArrow));
        solo.assertCurrentActivity("Wrong Activity", ViewBookActivity.class);
    }
}
