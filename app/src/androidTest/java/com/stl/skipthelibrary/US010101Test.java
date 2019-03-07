package com.stl.skipthelibrary;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;

import com.robotium.solo.Solo;
import com.stl.skipthelibrary.Activities.AddBooksActivity;
import com.stl.skipthelibrary.Activities.MyBooksActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

@RunWith(AndroidJUnit4.class)
public class US010101Test extends ActivityTestRule<MyBooksActivity>{

    private Solo solo;

    public US010101Test() {
        super(MyBooksActivity.class, false, true);
    }

    @Rule
    public ActivityTestRule<MyBooksActivity> rule =
            new ActivityTestRule<>(MyBooksActivity.class, false, true);

    @Before
    public void setUp() throws Exception{

        solo = new Solo(getInstrumentation(), rule.getActivity());
    }

    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
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
