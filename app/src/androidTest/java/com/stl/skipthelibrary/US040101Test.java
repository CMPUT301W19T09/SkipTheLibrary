package com.stl.skipthelibrary;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.robotium.solo.Solo;
import com.stl.skipthelibrary.Activities.AddBooksActivity;
import com.stl.skipthelibrary.Activities.BorrowersBooksActivity;
import com.stl.skipthelibrary.Activities.LoginActivity;
import com.stl.skipthelibrary.Activities.MyBooksActivity;
import com.stl.skipthelibrary.Activities.NotificationActivity;
import com.stl.skipthelibrary.Activities.ProfileActivity;
import com.stl.skipthelibrary.Activities.SearchActivity;
import com.stl.skipthelibrary.Activities.ViewBookActivity;
import com.stl.skipthelibrary.Entities.Notification;
import com.stl.skipthelibrary.Enums.BookStatus;
import com.stl.skipthelibrary.Helpers.NavigationHandler;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Random;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class US040101Test extends ActivityTestRule<LoginActivity>{

    private Solo solo;

    public US040101Test() {
        super(LoginActivity.class, false, true);
    }

    @Rule
    public ActivityTestRule<LoginActivity> rule =
            new ActivityTestRule<>(LoginActivity.class, false, true);

    @Before
    public void setUp() throws Exception{

        solo = new Solo(getInstrumentation(), rule.getActivity());
    }

    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    @Test
    public void testRequestBook(){

        //login
        logInAccount("uitest@email.com","123123");

        //navigate to add book
        selectMenuItem(R.id.my_books);
        solo.waitForActivity(MyBooksActivity.class);
        solo.assertCurrentActivity("Wrong Activity", MyBooksActivity.class);

        //add book
        solo.clickOnView(solo.getView(R.id.addBookButton));
        solo.waitForActivity(AddBooksActivity.class);
        solo.assertCurrentActivity("Wrong Activity", AddBooksActivity.class);

        solo.enterText((EditText) solo.getView(R.id.AddBookTitle), "Grimm Tales");
        solo.enterText((EditText) solo.getView(R.id.AddBookAuthor), "Inda Hood");
        solo.enterText((EditText) solo.getView(R.id.AddBookISBN), "112-456-883-1235");
        solo.enterText((EditText) solo.getView(R.id.AddBookDesc), "Red riding hood is rad.");

        solo.clickOnImageButton(0); //takes us back to my books activity

        //switch user

        selectMenuItem(R.id.profile);
        solo.waitForActivity(ProfileActivity.class);
        solo.assertCurrentActivity("Wrong Activity", ProfileActivity.class);

        logOutAccount();

        logInAccount("uitestborrower@email.com", "123123");

        //search book
        selectMenuItem(R.id.borrow);
        solo.waitForActivity(BorrowersBooksActivity.class);
        solo.assertCurrentActivity("Wrong Activity", BorrowersBooksActivity.class);

        solo.clickOnView(solo.getView(R.id.searchBookButton));
        solo.waitForActivity(SearchActivity.class);
        solo.assertCurrentActivity("Wrong Activity", SearchActivity.class);
        solo.enterText((AutoCompleteTextView) solo.getView(R.id.SearchBar), "Grimm");

        //select book
        RecyclerView searchBookList = (RecyclerView) solo.getView(R.id.SearchRecyclerView);
        solo.clickOnView(searchBookList.getChildAt(0));

        //request book
        solo.clickOnView(solo.getView(R.id.requestButton));

        //logout
        solo.goBack();
        solo.waitForActivity(BorrowersBooksActivity.class);
        selectMenuItem(R.id.profile);
        solo.waitForActivity(ProfileActivity.class);
        solo.assertCurrentActivity("Wrong Activity", ProfileActivity.class);

        logOutAccount();

        //login and navigate to owner's books
        logInAccount("uitest@email.com","123123");
        selectMenuItem(R.id.my_books);
        solo.waitForActivity(MyBooksActivity.class);
        solo.assertCurrentActivity("Wrong Activity", MyBooksActivity.class);

        searchBookList = (RecyclerView) solo.getView(R.id.ownerBooksRecyclerView);
        View selectedBook = searchBookList.getChildAt(0);

        //delete book
        deleteBook();

    }


    public void selectMenuItem(Integer menuRid ){
        BottomNavigationView view = (BottomNavigationView)solo.getView(R.id.bottom_navigation);
        view.setOnNavigationItemSelectedListener(new NavigationHandler(view.getContext()));
        solo.clickOnView(view.findViewById(menuRid));
    }


    public void logInAccount(String email, String password){
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.EmailEditText), email);
        solo.enterText((EditText) solo.getView(R.id.PasswordEditText), password);
        solo.clickOnView(solo.getView(R.id.SignInButton));
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


    }

    public void logOutAccount(){
        BottomNavigationView view = (BottomNavigationView)solo.getView(R.id.bottom_navigation);
        view.setOnNavigationItemSelectedListener(new NavigationHandler(view.getContext()));
        solo.clickOnView(view.findViewById(R.id.profile));
        solo.assertCurrentActivity("Wrong Activity", ProfileActivity.class);

        solo.clickOnView(solo.getView(R.id.logoutButton));
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);

    }
}



