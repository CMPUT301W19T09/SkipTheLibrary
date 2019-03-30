package com.stl.skipthelibrary;

import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.robotium.solo.Solo;
import com.stl.skipthelibrary.Activities.AddBooksActivity;
import com.stl.skipthelibrary.Activities.BorrowersBooksActivity;
import com.stl.skipthelibrary.Activities.LoginActivity;
import com.stl.skipthelibrary.Activities.MyBooksActivity;
import com.stl.skipthelibrary.Activities.NotificationActivity;
import com.stl.skipthelibrary.Activities.ProfileActivity;
import com.stl.skipthelibrary.Activities.SearchActivity;
import com.stl.skipthelibrary.Entities.Book;
import com.stl.skipthelibrary.Helpers.NavigationHandler;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.assertTrue;

public class US040101Test extends ActivityTestRule<NotificationActivity>{

    private Solo solo;
    private UITestHelper uiTestHelper;

    public US040101Test() throws InterruptedException{
        super(NotificationActivity.class, false, true);
        uiTestHelper = new UITestHelper(true, true, new ArrayList<Book>());

    }

    @Rule
    public ActivityTestRule<NotificationActivity> rule =
            new ActivityTestRule<>(NotificationActivity.class, false, true);

    @Before
    public void setUp() throws Exception{

        solo = new Solo(getInstrumentation(), rule.getActivity());
    }

    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        uiTestHelper.finish();
    }

    @Test
    public void testRequestBook(){
        solo.sleep(1000);
        //navigate to add book
        selectMenuItem(R.id.my_books);
        solo.sleep(1000);
        solo.waitForActivity(MyBooksActivity.class);
        solo.assertCurrentActivity("Wrong Activity", MyBooksActivity.class);

        //add book
        solo.clickOnView(solo.getView(R.id.addBookButton));
        solo.sleep(1000);
        solo.waitForActivity(AddBooksActivity.class);
        solo.assertCurrentActivity("Wrong Activity", AddBooksActivity.class);


        solo.enterText((EditText) solo.getView(R.id.AddBookTitle), "Grimm Tales");
        solo.enterText((EditText) solo.getView(R.id.AddBookAuthor), "Inda Hood");
        solo.enterText((EditText) solo.getView(R.id.AddBookISBN), "112-456-883-1235");
        solo.enterText((EditText) solo.getView(R.id.AddBookDesc), "Red riding hood is rad.");

        solo.sleep(1000);
        solo.clickOnImageButton(0); //takes us back to my books activity

        //switch user
        solo.sleep(1000);
        selectMenuItem(R.id.profile);
        solo.sleep(1000);
        solo.waitForActivity(ProfileActivity.class);
        solo.assertCurrentActivity("Wrong Activity", ProfileActivity.class);

        logOutAccount();

        solo.sleep(2000);
        logInAccount("uitestborrower@email.com", "123123");

        solo.sleep(2000);
        //search book
        selectMenuItem(R.id.borrow);
        solo.sleep(1000);
        solo.waitForActivity(BorrowersBooksActivity.class);
        solo.assertCurrentActivity("Wrong Activity", BorrowersBooksActivity.class);

        solo.sleep(1000);
        solo.clickOnView(solo.getView(R.id.searchBookButton));
        solo.sleep(1000);
        solo.waitForActivity(SearchActivity.class);
        solo.assertCurrentActivity("Wrong Activity", SearchActivity.class);
        solo.sleep(1000);
        solo.enterText((AutoCompleteTextView) solo.getView(R.id.SearchBar), "Grimm");

        solo.sleep(1000);
        //select book
        RecyclerView searchBookList = (RecyclerView) solo.getView(R.id.SearchRecyclerView);
        solo.sleep(1000);
        solo.clickOnView(searchBookList.getChildAt(0));
        solo.sleep(1000);

        //request book
        solo.clickOnView(solo.getView(R.id.requestButton));
        solo.sleep(1000);

        //logout
        solo.goBack();
        solo.sleep(1000);
        solo.goBack();
        solo.sleep(1000);
        solo.waitForActivity(BorrowersBooksActivity.class);

        selectMenuItem(R.id.profile);
        solo.sleep(1000);
        solo.waitForActivity(ProfileActivity.class);
        solo.assertCurrentActivity("Wrong Activity", ProfileActivity.class);

        solo.sleep(1000);
        logOutAccount();

        solo.sleep(2000);
        //login and navigate to owner's books
        logInAccount("uitest@email.com","123123");
        solo.sleep(2000);
        selectMenuItem(R.id.my_books);
        solo.sleep(2000);
        solo.waitForActivity(MyBooksActivity.class);
        solo.assertCurrentActivity("Wrong Activity", MyBooksActivity.class);

        solo.sleep(1000);

        assertTrue(solo.searchText("REQUESTED"));

        solo.sleep(1000);
        //delete book
        deleteBook();
    }


    public void selectMenuItem(Integer menuRid ){
        BottomNavigationView view = (BottomNavigationView)solo.getView(R.id.bottom_navigation);
        solo.sleep(1000);
        view.setOnNavigationItemSelectedListener(new NavigationHandler(view.getContext()));
        solo.sleep(1000);
        solo.clickOnView(view.findViewById(menuRid));
    }


    public void logInAccount(String email, String password){
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.EmailEditText), email);
        solo.sleep(1000);
        solo.enterText((EditText) solo.getView(R.id.PasswordEditText), password);
        solo.sleep(1000);
        solo.clickOnView(solo.getView(R.id.SignInButton));
        solo.sleep(1000);
    }


    public void deleteBook() {
        RecyclerView myBooksList = (RecyclerView) solo.getView(R.id.ownerBooksRecyclerView);
        solo.sleep(1000);
        View bookToDelete = myBooksList.getChildAt(0);

        int[] location = new int[2];

        bookToDelete.getLocationInWindow(location);
        solo.sleep(1000);

        int fromX = location[0] + 800;
        int fromY = location[1];

        int toX = location[0];
        int toY = fromY;

        solo.drag(fromX, toX, fromY, toY, 10);
        solo.sleep(1000);


    }

    public void logOutAccount(){
        solo.sleep(1000);
        solo.scrollDown();
        assertTrue(solo.waitForText("logout"));
        solo.clickOnButton("logout");
        assertTrue(solo.waitForText("Login"));
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
    }
}



