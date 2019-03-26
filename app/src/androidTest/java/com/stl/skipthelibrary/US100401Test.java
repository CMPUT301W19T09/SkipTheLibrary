package com.stl.skipthelibrary;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RatingBar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.robotium.solo.Solo;
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
import com.stl.skipthelibrary.Entities.Book;
import com.stl.skipthelibrary.Entities.BookDescription;
import com.stl.skipthelibrary.Entities.Rating;
import com.stl.skipthelibrary.Entities.RequestHandler;
import com.stl.skipthelibrary.Entities.State;

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

public class US100401Test extends IntentsTestRule<LoginActivity> {
    private Solo solo;
    private BottomNavigationView view;
    private UITestHelper uiTestHelper;
    private static final String isbn = "0120120120123";
    private static final String bookTitle = "BookTest1";
    private static final String ownerEmail = "uitest@email.com";
    private static final String ownerPassword = "123123";
    private static final String borrowerEmail = "uitestborrower@email.com";
    private static final String borrowerPassword = "123123";

    public US100401Test() throws InterruptedException {
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
    public void testBorrowerSearchForOnlyAvailableBooks() throws Exception {
        int ownerIndex;
        int borrowerIndex;
        RecyclerView ownerBooksList;
        RecyclerView borrowerBooksList;
        RecyclerView searchBooksList;

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

        // search for and request the book
        enterBorrowActivity();

        enterSearchText(bookTitle);

        searchBooksList = (RecyclerView) solo.getView(R.id.SearchRecyclerView);
        borrowerIndex = getBookIndex(bookTitle, searchBooksList);
        assertTrue(borrowerIndex>=0);

        solo.clickInRecyclerView(borrowerIndex);
        solo.assertCurrentActivity("Should be ViewBookActivity", ViewBookActivity.class);
        assertTrue(solo.waitForText(bookTitle));
        solo.clickOnView(solo.getView(R.id.requestButton));

        // TODO: With refresh
        solo.goBack();
        solo.sleep(1000);


        solo.assertCurrentActivity("Wrong Activity", SearchActivity.class);
        solo.goBack();
        solo.waitForText("Borrowing");
        solo.assertCurrentActivity("Wrong Activity", BorrowersBooksActivity.class);

        // switch accounts
        logOutAccount();

        logInAccount(ownerEmail, ownerPassword);

        // search for and accept request
        enterMyBookActivity();
        ownerBooksList = (RecyclerView) solo.getView(R.id.ownerBooksRecyclerView);
        ownerIndex = getBookIndex(bookTitle, ownerBooksList);
        assertTrue(ownerIndex>=0);
        solo.clickInRecyclerView(ownerIndex);
        solo.assertCurrentActivity("Wrong Activity", ViewBookActivity.class);
        solo.clickOnView(solo.getView(R.id.approve_button_id));
        solo.waitForText("Submit Location");
        solo.assertCurrentActivity("Should be Map Box Activity", MapBoxActivity.class);
        solo.sleep(1000);
        solo.clickOnButton("Submit Location");

//        // TODO: No refresh
//        solo.clickInRecyclerView(ownerIndex);

        solo.waitForText(bookTitle);
        solo.assertCurrentActivity("Should be ViewBookActivity", ViewBookActivity.class);

        // lend out the book
        Intent resultData = new Intent();
        resultData.putExtra("ISBN", isbn);
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
        intending(hasComponent(ScannerActivity.class.getName())).respondWith(result);
        solo.clickOnView(solo.getView(R.id.lendButton));
        solo.sleep(1000);


        // TODO: With refresh
        solo.goBack();


        solo.waitForText("My Books");
        solo.assertCurrentActivity("Should be my books activity", MyBooksActivity.class);

        // switch accounts
        logOutAccount();
        logInAccount(borrowerEmail, borrowerPassword);

        // borrow the book
        enterBorrowActivity();
        assertTrue(solo.waitForText(bookTitle));
        borrowerBooksList = (RecyclerView) solo.getView(R.id.borrowerBookRecyclerView);
        borrowerIndex = getBookIndex(bookTitle, borrowerBooksList);
        assertTrue(borrowerIndex>=0);

//        // reset ratings for two users
//        resetRatings(borrowerIndex, borrowerBooksList);




        solo.clickInRecyclerView(borrowerIndex);

        assertTrue(solo.waitForText(bookTitle));
        solo.assertCurrentActivity("Should be ViewBookActivity", ViewBookActivity.class);
        solo.clickOnView(solo.getView(R.id.borrowButton));
        solo.sleep(1000);


//        // TODO: No refresh
//        solo.clickInRecyclerView(borrowerIndex);
//        assertTrue(solo.waitForText(bookTitle));
//        solo.assertCurrentActivity("Should be ViewBookActivity", ViewBookActivity.class);


        // return the book
        solo.clickOnView(solo.getView(R.id.returnButton));
        solo.waitForText("rating");
        solo.assertCurrentActivity("Should be RateUserActivity", RateUserActivity.class);
        solo.setProgressBar(0, 2);

        solo.clickOnView(solo.getView(R.id.RateButton));

        //TODO: With refresh
        solo.sleep(1000);
        solo.goBack();


        solo.waitForText("Borrowing");
        solo.assertCurrentActivity("Should be BorrowersBookActivity", BorrowersBooksActivity.class);

        // switch accounts
        logOutAccount();
        logInAccount(ownerEmail, ownerPassword);

        // check the rating
        enterProfile();
        RatingBar ownerRatingBar = (RatingBar) solo.getView(R.id.ownerRatingBar);
        assertEquals(1.0, ownerRatingBar.getRating());

        // accept the return
        enterMyBookActivity();
        solo.clickInRecyclerView(ownerIndex);
        solo.sleep(1000);
        solo.assertCurrentActivity("Wrong Activity", ViewBookActivity.class);
        solo.clickOnView(solo.getView(R.id.returnedButton));
        solo.waitForText("rating");
        solo.assertCurrentActivity("Should be RateUserActivity", RateUserActivity.class);
        solo.setProgressBar(0, 8);

        solo.clickOnView(solo.getView(R.id.RateButton));

        //TODO: With refresh
        solo.sleep(1000);
        solo.goBack();

        solo.waitForText("My Books");
        solo.assertCurrentActivity("Should be MyBooksActivity", MyBooksActivity.class);


        // go and check borrower rating was received
        logOutAccount();
        logInAccount(borrowerEmail, borrowerPassword);

        enterProfile();
        RatingBar borrowerRatingBar = (RatingBar) solo.getView(R.id.ownerRatingBar);
        assertEquals(4.0, borrowerRatingBar.getRating());

        logOutAccount();
    }

    @After
    public void tearDown() throws InterruptedException {
        uiTestHelper.finish();
        solo.finishOpenedActivities();
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

    public void enterProfile() {
        solo.clickOnView(solo.getView(R.id.profile));
        solo.assertCurrentActivity("Wrong Activity", ProfileActivity.class);
    }

    public void enterMyBookActivity(){
        solo.clickOnView(solo.getView(R.id.my_books));
        solo.waitForText("My Books");
        solo.assertCurrentActivity("Wrong Activity", MyBooksActivity.class);
    }

    public void enterBorrowActivity() {
//        view = (BottomNavigationView)solo.getView(R.id.bottom_navigation);
//        view.setOnNavigationItemSelectedListener(new NavigationHandler(view.getContext()));
        solo.clickOnView(solo.getView(R.id.borrow));
        solo.waitForText("Borrowing");
        solo.assertCurrentActivity("Wrong Activity", BorrowersBooksActivity.class);
    }

    public void enterSearchText(String text){
        solo.clickOnView(solo.getView(R.id.searchBookButton));
        solo.waitForActivity(SearchActivity.class);
        solo.assertCurrentActivity("Wrong Activity", SearchActivity.class);
        solo.enterText((AutoCompleteTextView) solo.getView(R.id.SearchBar), "BookTest1");
    }

//    public void resetRatings(int index, RecyclerView bookList) {
//        BookRecyclerAdapter adapter = (BookRecyclerAdapter) bookList.getAdapter();
//        Book book = adapter.getBooks().get(index);
//        book.getRequests().getAcceptedRequestor();
//    }

    public int getBookIndex(String title, RecyclerView bookList) {
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
}
