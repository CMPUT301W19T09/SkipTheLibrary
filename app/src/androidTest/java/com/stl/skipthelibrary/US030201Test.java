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
import com.stl.skipthelibrary.Helpers.NavigationHandler;

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

public class US030201Test extends IntentsTestRule<LoginActivity> {
    private Solo solo;
    private BottomNavigationView view;
    private DatabaseHelper databaseHelper;
    private static final String isbn = "0120120120123";
    private static final String bookTitle = "BookTest1";

    public US030201Test() {
        super(LoginActivity.class, true, true);
    }

    private class MockScannerActivity extends ScannerActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_scanner);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            Intent intent = new Intent();
            intent.putExtra("ISBN", isbn);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }


    @Rule
    public IntentsTestRule<LoginActivity> intentsTestRule =
            new IntentsTestRule<>(LoginActivity.class);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), intentsTestRule.getActivity());
    }

    @Test
    public void testScanningFlow() throws Exception {
        int ownerIndex;
        int borrowerIndex;


        /**
         * Test Login functionality
         */
        logInAccount("testowner@gmail.com", "111111");

        enterProfile();
        if (!solo.searchText("testowner@gmail.com")) {
            logOutAccount();
            logInAccount("testowner@gmail.com", "111111");
        }

        /**
         * Test AddBook functionality
         */

        enterMyBookActivity();

        solo.clickOnView(solo.getView(R.id.addBookButton));

        solo.assertCurrentActivity("Wrong Activity", AddBooksActivity.class);
        solo.enterText((EditText) solo.getView(R.id.AddBookTitle), "BookTest1");
        solo.enterText((EditText) solo.getView(R.id.AddBookAuthor), "Author");
        solo.enterText((EditText) solo.getView(R.id.AddBookISBN), "0120120120123");
        solo.enterText((EditText) solo.getView(R.id.AddBookDesc), "A book");
        solo.clickOnView(solo.getView(R.id.SaveBookButton));

        assertTrue(solo.waitForText("Test"));

        /**
         * log out
         */
        logOutAccount();

        /**
         * Goes into another account
         */
        logInAccount("lmac@thegoal.com", "123456");

        // search for and request the book
        enterBorrowActivity();

        enterSearchText("BookTest1");

        RecyclerView searchBooksList = (RecyclerView) solo.getView(R.id.SearchRecyclerView);
        borrowerIndex = getBookIndex("BookTest1", searchBooksList);
        assertTrue(borrowerIndex>=0);
        solo.clickInRecyclerView(borrowerIndex);
        solo.assertCurrentActivity("Should be ViewBookActivity", ViewBookActivity.class);
        assertTrue(solo.waitForText("BookTest1"));
        solo.clickOnView(solo.getView(R.id.requestButton));
        solo.assertCurrentActivity("Wrong Activity", SearchActivity.class);
        solo.goBack();
        assertTrue(solo.waitForText("BookTest1"));

        // switch accounts
        logOutAccount();
        logInAccount("testowner@gmail.com", "111111");

        enterMyBookActivity();

        // accept the request
        RecyclerView myBooksList = (RecyclerView) solo.getView(R.id.ownerBooksRecyclerView);
        ownerIndex = getBookIndex("BookTest1", myBooksList);
        assertTrue(ownerIndex>=0);
        solo.clickInRecyclerView(ownerIndex);
        solo.assertCurrentActivity("Wrong Activity", ViewBookActivity.class);
        solo.clickOnView(solo.getView(R.id.approve_button_id));
        solo.waitForText("Submit Location");
        solo.assertCurrentActivity("Should be Map Box Activity", MapBoxActivity.class);
        solo.clickOnButton("Submit Location");
        solo.waitForText("My Books");
        solo.assertCurrentActivity("Should be my books activity", MyBooksActivity.class);

        // switch accounts
        logOutAccount();
        logInAccount("lmac@thegoal.com", "123456");

        enterBorrowActivity();

        enterSearchText("BookTest1");
        searchBooksList = (RecyclerView) solo.getView(R.id.SearchRecyclerView);
        assertEquals(0, searchBooksList.getAdapter().getItemCount());
        solo.goBack();

        // switch accounts
        logOutAccount();
        logInAccount("testowner@gmail.com", "111111");

        enterMyBookActivity();

        //TODO: Remove these lines
        myBooksList = (RecyclerView) solo.getView(R.id.ownerBooksRecyclerView);
        ownerIndex = getBookIndex("BookTest1", myBooksList);


        solo.clickInRecyclerView(ownerIndex);
        solo.assertCurrentActivity("Wrong Activity", ViewBookActivity.class);

        Intent resultData = new Intent();
        resultData.putExtra("ISBN", isbn);
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
        intending(hasComponent(ScannerActivity.class.getName())).respondWith(result);

        solo.clickOnView(solo.getView(R.id.lendButton));

        logOutAccount();
        logInAccount("lmac@thegoal.com", "123456");

        enterBorrowActivity();
        assertTrue(solo.waitForText(bookTitle));
        RecyclerView borrowerBooksList = (RecyclerView) solo.getView(R.id.borrowerBookRecyclerView);
        borrowerIndex = getBookIndex(bookTitle, borrowerBooksList);
        assertTrue(borrowerIndex>=0);
        solo.clickInRecyclerView(borrowerIndex);

        solo.clickOnView(solo.getView(R.id.borrowButton));
        solo.waitForText("Borrowing");
        solo.assertCurrentActivity("Should be BorrowersBookActivity", BorrowersBooksActivity.class);

        solo.clickOnView(solo.getView(R.id.AcceptedChip));
        assertTrue(solo.waitForText(bookTitle));

        enterSearchText(bookTitle);

        searchBooksList = (RecyclerView) solo.getView(R.id.SearchRecyclerView);
        assertEquals(0, searchBooksList.getAdapter().getItemCount());
        solo.goBack();

        logOutAccount();
        logInAccount("testowner@gmail.com", "111111");

        enterMyBookActivity();

//        deleteBook(ownerIndex);
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
            if (book.getDescription().getTitle().equals("BookTest1")) {
                return adapter.getBooks().indexOf(book);
            }
        }
        return -1;
    }
}
