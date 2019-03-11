package com.stl.skipthelibrary;

import android.widget.EditText;

import com.robotium.solo.Solo;
import com.stl.skipthelibrary.Activities.AddBooksActivity;
import com.stl.skipthelibrary.Activities.MyBooksActivity;
import com.stl.skipthelibrary.DatabaseAndAPI.DatabaseHelper;
import com.stl.skipthelibrary.Entities.Book;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import androidx.test.rule.ActivityTestRule;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.assertTrue;

public class US010101Test extends ActivityTestRule<MyBooksActivity>{

    private Solo solo;
    private UITestHelper uiTestHelper;

    public US010101Test() throws InterruptedException {
        super(MyBooksActivity.class, false, true);
        uiTestHelper = new UITestHelper(true, true, new ArrayList<Book>());
    }

    @Rule
    public ActivityTestRule<MyBooksActivity> rule =
            new ActivityTestRule<>(MyBooksActivity.class, false, true);

    @Before
    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), rule.getActivity());
    }

    @After
    public void tearDown() throws InterruptedException {
        uiTestHelper.finish();
        solo.finishOpenedActivities();
    }

    @Test
    public void testAddBookWithoutImages(){
        solo.assertCurrentActivity("Wrong Activity", MyBooksActivity.class);

        solo.clickOnView(solo.getView(R.id.addBookButton));

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

    }
}
