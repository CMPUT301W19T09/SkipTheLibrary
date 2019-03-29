package com.stl.skipthelibrary;

import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.robotium.solo.Solo;
import com.stl.skipthelibrary.Activities.BorrowersBooksActivity;
import com.stl.skipthelibrary.Activities.LoginActivity;
import com.stl.skipthelibrary.Activities.MapBoxActivity;
import com.stl.skipthelibrary.Activities.NotificationActivity;
import com.stl.skipthelibrary.Activities.ProfileActivity;
import com.stl.skipthelibrary.Activities.SearchActivity;
import com.stl.skipthelibrary.Activities.ViewBookActivity;
import com.stl.skipthelibrary.Entities.Book;
import com.stl.skipthelibrary.Entities.BookDescription;
import com.stl.skipthelibrary.Entities.Rating;
import com.stl.skipthelibrary.Entities.RequestHandler;
import com.stl.skipthelibrary.Entities.State;
import com.stl.skipthelibrary.Helpers.NavigationHandler;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.intent.rule.IntentsTestRule;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.assertTrue;

public class US050301Test extends IntentsTestRule<LoginActivity> {

    final public static String TAG = "US050301Test";
    private Solo solo;
    private BottomNavigationView view;
    private UITestHelper uiTestHelper;
    private static final String isbn = "1234567890123";
    private static final String bookTitle = "FelixBook";
    private static final String ownerEmail = "felix@gmail.com";
    private static final String ownerPassword = "123456";
    private static final String borrowerEmail = "felix2@gmail.com";
    private static final String borrowerPassword = "123456";

    public US050301Test() throws InterruptedException {
        super(LoginActivity.class, true, true);
        ArrayList<Book> books = new ArrayList<>();
        books.clear();

        RequestHandler requests = new RequestHandler(new State());
        BookDescription book1Description = new BookDescription(bookTitle, "Test book1", "Test Author1");
        Book book1 = new Book(isbn, book1Description, "Felix", requests, null, new Rating());

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
    public void testNotification() throws Exception {

        /**
         * Test Login functionality
         */
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.sleep(2000);

        if (solo.searchText("Notifications")) {
            enterProfile();
            if (!solo.searchText(borrowerEmail)) {
                logOutAccount();
                logInAccount(borrowerEmail, borrowerPassword);
            }
        }
        else {
            logInAccount(borrowerEmail, borrowerPassword);
        }

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
        solo.assertCurrentActivity("Wrong Activity", SearchActivity.class);
        solo.goBack();
        logOutAccount();


        /**
         * Switch account
         */
        logInAccount(ownerEmail, ownerPassword);
        solo.assertCurrentActivity("Wrong Activity", NotificationActivity.class);
        assertTrue(solo.waitForText(bookTitle));
        solo.clickInRecyclerView(0);
        solo.assertCurrentActivity("Wrong Activity", ViewBookActivity.class);
        solo.clickOnView(solo.getView(R.id.approve_button_id));
        solo.assertCurrentActivity("Wrong Activity", MapBoxActivity.class);
        solo.sleep(1000);
        solo.clickOnButton("Submit Location");
        solo.sleep(2000);
        solo.waitForText(bookTitle);
        solo.sleep(2000);
        solo.goBack();
        logOutAccount();

        logInAccount(borrowerEmail,borrowerPassword);
        solo.assertCurrentActivity("Wrong Activity", NotificationActivity.class);
        assertTrue(solo.waitForText(bookTitle));
        solo.clickInRecyclerView(0);
        solo.assertCurrentActivity("Wrong Activity", ViewBookActivity.class);
        solo.goBack();
        RecyclerView notificationList = (RecyclerView) solo.getView(R.id.notification_recycler_view);
        solo.sleep(1000);
        solo.clickOnView(solo.getView(R.id.floatingActionButton2));
        logOutAccount();

    }

    @After
    public void tearDown() throws InterruptedException {
        uiTestHelper.deleteUsersBooks("Felix");
        uiTestHelper.finish();
        solo.finishOpenedActivities();
    }

    /**
     * log out current account
     */
    public void logOutAccount() {
        enterProfile();
        solo.sleep(1000);
        solo.assertCurrentActivity("Wrong Activity", ProfileActivity.class);
        solo.sleep(3000);
        solo.scrollDown();
        solo.clickOnView(solo.getView(R.id.logoutButton));
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);

    }

    /**
     * Log in account
     *
     * @param email    account email address
     * @param password account password
     */
    public void logInAccount(String email, String password) {
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.EmailEditText), email);
        solo.enterText((EditText) solo.getView(R.id.PasswordEditText), password);
        solo.sleep(1000);
        solo.clickOnView(solo.getView(R.id.SignInButton));
    }

    /**
     * Enter borrower book activity from initial screen
     */
    public void enterBorrowersBookActivity() {
        view = (BottomNavigationView) solo.getView(R.id.bottom_navigation);
        view.setOnNavigationItemSelectedListener(new NavigationHandler(view.getContext()));
        solo.sleep(1000);
        solo.clickOnView(view.findViewById(R.id.borrow));
    }

    public void enterProfile() {
        solo.clickOnView(solo.getView(R.id.profile));
        solo.assertCurrentActivity("Wrong Activity", ProfileActivity.class);
    }
}
