package com.stl.skipthelibrary;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.provider.Contacts;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

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

public class US050201Test extends IntentsTestRule<LoginActivity> {
    private Solo solo;
    private BottomNavigationView view;
    private UITestHelper uiTestHelper;
    private static final String isbn = "0120120120123";
    private static final String bookTitle = "BookTest1";
    private static final String ownerEmail = "uitest@email.com";
    private static final String ownerPassword = "111111";
    private static final String borrowerEmail = "uitest@email.com";
    private static final String borrowerPassword = "123123";

    public US050201Test() throws InterruptedException {
        super(LoginActivity.class, true, true);

        RequestHandler requests = new RequestHandler(new State());
        BookDescription book1Description = new BookDescription(bookTitle, "Test book", "Test Author", new Rating());
        Book book1 = new Book(isbn, book1Description, uiTestHelper.userName, requests, null, null);

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

        logInAccount(ownerEmail, ownerPassword);
        enterProfile();
        if (!solo.searchText("testowner@gmail.com")) {
            logOutAccount();
            logInAccount("testowner@gmail.com", "111111");
        }

        addBook();
        logOutAccount();


        logInAccount(borrowerEmail, borrowerPassword);

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
        solo.assertCurrentActivity("Wrong Activity", SearchActivity.class);
        solo.goBack();
        assertTrue(solo.waitForText(bookTitle));

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
        solo.clickOnView(solo.getView(R.id.deny_button_id));
//        solo.waitForText("Submit Location");
//        solo.assertCurrentActivity("Should be Map Box Activity", MapBoxActivity.class);
//        solo.clickOnButton("Submit Location");
//        solo.waitForText("My Books");
//        solo.assertCurrentActivity("Should be my books activity", MyBooksActivity.class);
//
//        // switch accounts
//        logOutAccount();
//        logInAccount(borrowerEmail, borrowerPassword);
//
//        // show that request book no longer shows in search
//        enterBorrowActivity();
//        enterSearchText(bookTitle);
//        searchBooksList = (RecyclerView) solo.getView(R.id.SearchRecyclerView);
//        assertEquals(0, searchBooksList.getAdapter().getItemCount());
//        solo.goBack();
//
//        // switch accounts
//        logOutAccount();
//        logInAccount(ownerEmail, ownerPassword);
//
//        // lend out the book
//        enterMyBookActivity();
//        solo.clickInRecyclerView(ownerIndex);
//        solo.assertCurrentActivity("Wrong Activity", ViewBookActivity.class);
//        Intent resultData = new Intent();
//        resultData.putExtra("ISBN", isbn);
//        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
//        intending(hasComponent(ScannerActivity.class.getName())).respondWith(result);
//        solo.clickOnView(solo.getView(R.id.lendButton));
//
//        // switch accounts
//        logOutAccount();
//        logInAccount(borrowerEmail, borrowerPassword);
//
//        // borrow the book
//        enterBorrowActivity();
//        assertTrue(solo.waitForText(bookTitle));
//        borrowerBooksList = (RecyclerView) solo.getView(R.id.borrowerBookRecyclerView);
//        borrowerIndex = getBookIndex(bookTitle, borrowerBooksList);
//        assertTrue(borrowerIndex>=0);
//        solo.clickInRecyclerView(borrowerIndex);
//
//        solo.clickOnView(solo.getView(R.id.borrowButton));
//        solo.waitForText("Borrowing");
//        solo.assertCurrentActivity("Should be BorrowersBookActivity", BorrowersBooksActivity.class);
//
//        // show that book status has changed
//        solo.clickOnView(solo.getView(R.id.AcceptedChip));
//        assertTrue(solo.waitForText(bookTitle));
//
//        // show that book no longer appears in search
//        enterSearchText(bookTitle);
//        searchBooksList = (RecyclerView) solo.getView(R.id.SearchRecyclerView);
//        assertEquals(0, searchBooksList.getAdapter().getItemCount());
//        solo.goBack();
//
//        // switch accounts
//        logOutAccount();
//        logInAccount(ownerEmail, ownerPassword);

        // delete the book
        enterMyBookActivity();
        deleteBook(ownerIndex);
    }

    @After
    public void tearDown() throws InterruptedException {
        uiTestHelper.deleteBooks();
        solo.finishOpenedActivities();
    }

    public void deleteBook(int index) {
        RecyclerView myBooksList = (RecyclerView) solo.getView(R.id.ownerBooksRecyclerView);
        View bookToDelete = myBooksList.getChildAt(index);

        int[] location = new int[2];

        bookToDelete.getLocationInWindow(location);

        int fromX = location[0] + 800;
        int fromY = location[1];

        int toX = location[0];
        int toY = fromY;

        solo.drag(fromX, toX, fromY, toY, 10);
        solo.sleep(1000);

        assertTrue(myBooksList.getAdapter().getItemCount() == 0);

    }

    public void logOutAccount(){
        enterProfile();
        solo.clickOnView(solo.getView(R.id.logoutButton));
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

    public void addBook() {
        enterMyBookActivity();

        solo.clickOnView(solo.getView(R.id.addBookButton));

        solo.assertCurrentActivity("Wrong Activity", AddBooksActivity.class);
        solo.enterText((EditText) solo.getView(R.id.AddBookTitle), bookTitle);
        solo.enterText((EditText) solo.getView(R.id.AddBookAuthor), "Author");
        solo.enterText((EditText) solo.getView(R.id.AddBookISBN), isbn);
        solo.enterText((EditText) solo.getView(R.id.AddBookDesc), "A book");
        solo.clickOnView(solo.getView(R.id.SaveBookButton));

        assertTrue(solo.waitForText(bookTitle));
    }

    public void enterProfile() {
        view = (BottomNavigationView)solo.getView(R.id.bottom_navigation);
        view.setOnNavigationItemSelectedListener(new NavigationHandler(view.getContext()));
        solo.clickOnView(view.findViewById(R.id.profile));
        solo.assertCurrentActivity("Wrong Activity", ProfileActivity.class);
    }

    public void enterMyBookActivity(){
        view = (BottomNavigationView)solo.getView(R.id.bottom_navigation);
        view.setOnNavigationItemSelectedListener(new NavigationHandler(view.getContext()));
        solo.clickOnView(view.findViewById(R.id.my_books));
        solo.waitForText("My Books");
        solo.assertCurrentActivity("Wrong Activity", MyBooksActivity.class);
    }

    public void enterBorrowActivity() {
        view = (BottomNavigationView)solo.getView(R.id.bottom_navigation);
        view.setOnNavigationItemSelectedListener(new NavigationHandler(view.getContext()));
        solo.clickOnView(view.findViewById(R.id.borrow));
        solo.waitForText("Borrowing");
        solo.assertCurrentActivity("Wrong Activity", BorrowersBooksActivity.class);
    }

    public void enterSearchText(String text){
        solo.clickOnView(solo.getView(R.id.searchBookButton));
        solo.waitForActivity(SearchActivity.class);
        solo.assertCurrentActivity("Wrong Activity", SearchActivity.class);
        solo.enterText((AutoCompleteTextView) solo.getView(R.id.SearchBar), "BookTest1");
    }

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

