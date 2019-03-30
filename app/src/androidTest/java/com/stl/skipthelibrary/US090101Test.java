package com.stl.skipthelibrary;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.robotium.solo.Solo;
import com.stl.skipthelibrary.Activities.BorrowersBooksActivity;
import com.stl.skipthelibrary.Activities.LoginActivity;
import com.stl.skipthelibrary.Activities.MapBoxActivity;
import com.stl.skipthelibrary.Activities.MyBooksActivity;
import com.stl.skipthelibrary.Activities.NotificationActivity;
import com.stl.skipthelibrary.Activities.ProfileActivity;
import com.stl.skipthelibrary.Activities.MapViewActivity;
import com.stl.skipthelibrary.Activities.SearchActivity;
import com.stl.skipthelibrary.Activities.ViewBookActivity;
import com.stl.skipthelibrary.BindersAndAdapters.BookRecyclerAdapter;
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

import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class US090101Test extends IntentsTestRule<LoginActivity> {
    private Solo solo;
    private BottomNavigationView view;
    private UITestHelper uiTestHelper;
    private static final String isbn = "0120120120123";
    private static final String bookTitle = "BookTest1";
    private static final String ownerEmail = "uitest@email.com";
    private static final String ownerPassword = "123123";
    private static final String borrowerEmail = "uitestborrower@email.com";
    private static final String borrowerPassword = "123123";
    private int ownerIndex = -1;
    private int searchIndex = -1;
    private int borrowerIndex = -1;

    public US090101Test() throws InterruptedException {
        super(LoginActivity.class, true, true);

        RequestHandler requests = new RequestHandler(new State());
        BookDescription book1Description = new BookDescription(bookTitle, "Test book", "Test Author");
        Book book1 = new Book(isbn, book1Description, uiTestHelper.userName, requests, null, new Rating());

        ArrayList<Book> books = new ArrayList<>();
        books.add(book1);
        uiTestHelper = new UITestHelper(true, true, books);
    }

    @Rule
    public IntentsTestRule<LoginActivity> intentsTestRule =
            new IntentsTestRule<>(LoginActivity.class);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), intentsTestRule.getActivity());
    }

    @Test
    public void testOwnerSpecifyGeoLocation() throws Exception {
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.sleep(2000);

        // borrower
        initialLoginToTestBorrower();
        enterBorrowActivity();
        searchAndRequestBook();

        // owner
        switchAccounts(ownerEmail, ownerPassword);
        enterMyBookActivity();
        acceptRequest();

        solo.clickInRecyclerView(ownerIndex);
        solo.waitForText(bookTitle);
        solo.clickOnButton("View Handoff Location");
        solo.waitForText("Handoff Location");
        solo.assertCurrentActivity("Should be Map View Activity", MapViewActivity.class);
        solo.sleep(2000);

        solo.goBack();
        solo.waitForText(bookTitle);
        solo.assertCurrentActivity("Should be ViewBookActivity", ViewBookActivity.class);

        solo.goBack();
        solo.waitForText("My Books");
        solo.assertCurrentActivity("Wrong Activity", MyBooksActivity.class);

        logOutAccount();
    }

    private void acceptRequest() {
        solo.assertCurrentActivity("Wrong Activity", MyBooksActivity.class);
        if (ownerIndex < 0) {
            ownerIndex = getBookIndex(bookTitle, R.id.ownerBooksRecyclerView);
            assertTrue(ownerIndex>=0);
        }

        solo.clickInRecyclerView(ownerIndex);
        solo.assertCurrentActivity("Wrong Activity", ViewBookActivity.class);
        solo.clickOnView(solo.getView(R.id.approve_button_id));
        solo.waitForText("Submit Location");
        solo.assertCurrentActivity("Should be Map Box Activity", MapBoxActivity.class);
        solo.sleep(2000);
        solo.clickOnButton("Submit Location");
        solo.waitForText(bookTitle);
        solo.assertCurrentActivity("Should be ViewBookActivity", ViewBookActivity.class);
        solo.goBack();
        solo.waitForText("My Books");
        solo.assertCurrentActivity("Wrong Activity", MyBooksActivity.class);
    }

    private void switchAccounts(String email, String password) {
        logOutAccount();
        logInAccount(email, password);
    }

    private void searchAndRequestBook() {
        solo.assertCurrentActivity("Wrong Activity", BorrowersBooksActivity.class);
        enterSearchText(bookTitle);

        if (searchIndex == -1) {
            searchIndex = getBookIndex(bookTitle, R.id.SearchRecyclerView);
            assertTrue(searchIndex>=0);
        }

        solo.clickInRecyclerView(searchIndex);
        solo.assertCurrentActivity("Should be ViewBookActivity", ViewBookActivity.class);
        assertTrue(solo.waitForText(bookTitle));
        solo.clickOnView(solo.getView(R.id.requestButton));

        solo.goBack();
        solo.sleep(1000);

        solo.assertCurrentActivity("Wrong Activity", SearchActivity.class);
        solo.goBack();
        solo.waitForText("Borrowing");
        solo.assertCurrentActivity("Wrong Activity", BorrowersBooksActivity.class);
    }

    public void logOutAccount(){
        enterProfile();
        solo.sleep(1000);
        solo.scrollDown();
        assertTrue(solo.waitForText("logout"));
        solo.clickOnButton("logout");
        assertTrue(solo.waitForText("Login"));
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
    }

    public void logInAccount(String email, String password){
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.EmailEditText), email);
        solo.enterText((EditText) solo.getView(R.id.PasswordEditText), password);
        solo.clickOnView(solo.getView(R.id.SignInButton));
        assertTrue(solo.waitForText("Notifications"));
        solo.assertCurrentActivity("Wrong activity", NotificationActivity.class);
    }

    private void initialLoginToTestBorrower() {
        if (solo.searchText("Notifications")) {
            enterProfile();
            if (!solo.searchText(borrowerEmail)) {
                switchAccounts(borrowerEmail, borrowerPassword);
            }
        }
        else {
            logInAccount(borrowerEmail, borrowerPassword);
        }
    }

    public void enterProfile() {
        solo.clickOnView(solo.getView(R.id.profile));
        solo.assertCurrentActivity("Wrong Activity", ProfileActivity.class);
    }

    public void enterBorrowActivity() {
        solo.clickOnView(solo.getView(R.id.borrow));
        solo.waitForText("Borrowing");
        solo.assertCurrentActivity("Wrong Activity", BorrowersBooksActivity.class);
    }

    public void enterMyBookActivity(){
        solo.clickOnView(solo.getView(R.id.my_books));
        solo.waitForText("My Books");
        solo.assertCurrentActivity("Wrong Activity", MyBooksActivity.class);
    }

    public void enterSearchText(String text){
        solo.clickOnView(solo.getView(R.id.searchBookButton));
        solo.waitForActivity(SearchActivity.class);
        solo.assertCurrentActivity("Wrong Activity", SearchActivity.class);
        solo.enterText((AutoCompleteTextView) solo.getView(R.id.SearchBar), text);
    }

    public int getBookIndex(String title, int recyclerViewID) {
        RecyclerView bookList = (RecyclerView) solo.getView(recyclerViewID);
        BookRecyclerAdapter adapter = (BookRecyclerAdapter) bookList.getAdapter();
        ArrayList<Book> books = adapter.getBooks();
        for (Book book: books) {
            if (book.getDescription().getTitle().equals(title)) {
                return adapter.getBooks().indexOf(book);
            }
        }
        // assume 0
        return 0;
    }

    @After
    public void tearDown() throws InterruptedException {
        uiTestHelper.finish();
        solo.finishOpenedActivities();
    }
}

