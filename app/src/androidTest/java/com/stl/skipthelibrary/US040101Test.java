package com.stl.skipthelibrary;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.robotium.solo.Solo;
import com.stl.skipthelibrary.Activities.AddBooksActivity;
import com.stl.skipthelibrary.Activities.LoginActivity;
import com.stl.skipthelibrary.Activities.MyBooksActivity;
import com.stl.skipthelibrary.Activities.NotificationActivity;
import com.stl.skipthelibrary.Activities.ProfileActivity;
import com.stl.skipthelibrary.Activities.SearchActivity;
import com.stl.skipthelibrary.Activities.ViewBookActivity;
import com.stl.skipthelibrary.Entities.Notification;
import com.stl.skipthelibrary.Enums.BookStatus;
import com.stl.skipthelibrary.Helpers.NavigationHandler;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

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

    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    @Test
    public void testRequestBook(){

        //login
        logInAccount("a@a.com", "123456");

        //navigate to add book
        BottomNavigationView view = (BottomNavigationView)solo.getView(R.id.bottom_navigation);
        view.setOnNavigationItemSelectedListener(new NavigationHandler(view.getContext()));
        solo.clickOnView(view.findViewById(R.id.my_books));
        solo.assertCurrentActivity("Wrong Activity", MyBooksActivity.class);

        //add book
        solo.assertCurrentActivity("Wrong Activity", MyBooksActivity.class);
        solo.clickOnView(solo.getView(R.id.addBookButton));

        solo.waitForActivity(AddBooksActivity.class);
        solo.assertCurrentActivity("Wrong Activity", AddBooksActivity.class);

        solo.enterText((EditText) solo.getView(R.id.AddBookTitle), "Grimm Tales");
        solo.enterText((EditText) solo.getView(R.id.AddBookAuthor), "Inda Hood");
        solo.enterText((EditText) solo.getView(R.id.AddBookISBN), "122-456-889-1111");
        solo.enterText((EditText) solo.getView(R.id.AddBookDesc), "Red riding hood is rad.");

        solo.clickOnImageButton(0); //takes us back to my books activity

        //switch user


        view = (BottomNavigationView)solo.getView(R.id.bottom_navigation);
        view.setOnNavigationItemSelectedListener(new NavigationHandler(view.getContext()));
        solo.clickOnView(view.findViewById(R.id.profile));
        solo.assertCurrentActivity("Wrong Activity", ProfileActivity.class);


        //search book

        //select book

        //request book





        solo.assertCurrentActivity("Wrong Activity", ProfileActivity.class);



        /*RecyclerView searchBookList = (RecyclerView) solo.getView(R.id.SearchRecyclerView);
        int index = 0;

        View selectedBook = searchBookList.getChildAt(index);
        TextView status = selectedBook.findViewById(R.id.BookListItemStatus);

        while(!status.getText().toString().equals("AVAILABLE")){
            selectedBook = searchBookList.getChildAt(index);
            status = selectedBook.findViewById(R.id.BookListItemStatus);
            index++;
        }
        assertEquals(status.getText().toString(), "AVAILABLE");

        solo.clickOnView(selectedBook);
        solo.waitForActivity(ViewBookActivity.class);
        solo.assertCurrentActivity("Wrong Activity", ViewBookActivity.class);
        solo.clickOnView(solo.getView(R.id.requestButton));

        solo.waitForActivity(SearchActivity.class);
        View requestedBook = searchBookList.getChildAt(index);
        status = requestedBook.findViewById(R.id.BookListItemStatus);
        assertEquals(status.getText().toString(), "REQUESTED");
        */

        /*
        solo.waitForActivity(AddBooksActivity.class);
        solo.assertCurrentActivity("Wrong Activity", AddBooksActivity.class);


        solo.enterText((EditText) solo.getView(R.id.AddBookTitle), "Test Title");
        solo.enterText((EditText) solo.getView(R.id.AddBookAuthor), "Test Author");
        solo.enterText((EditText) solo.getView(R.id.AddBookISBN), "123-456-789-1011");
        solo.enterText((EditText) solo.getView(R.id.AddBookDesc), "Test Description");

        solo.clickOnImageButton(0); //takes us back to my books activity

        solo.waitForActivity(MyBooksActivity.class);
        solo.assertCurrentActivity("Wrong Activity", MyBooksActivity.class);

        assertTrue(solo.waitForText("Test Title"));
        assertTrue(solo.waitForText("Test Author"));
         */

    }

    public void logInAccount(String email, String password){
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.EmailEditText), email);
        solo.enterText((EditText) solo.getView(R.id.PasswordEditText), password);
        solo.clickOnView(solo.getView(R.id.SignInButton));
        solo.waitForActivity(NotificationActivity.class);
        solo.assertCurrentActivity("Wrong activity", NotificationActivity.class);
    }
}

