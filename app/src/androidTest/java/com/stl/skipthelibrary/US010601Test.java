package com.stl.skipthelibrary;

import android.app.Activity;
import android.graphics.PointF;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.robotium.solo.Solo;
import com.stl.skipthelibrary.Activities.AddBooksActivity;
import com.stl.skipthelibrary.Activities.MyBooksActivity;
import com.stl.skipthelibrary.Activities.ViewBookActivity;
import com.stl.skipthelibrary.Entities.Book;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Created by Luke Slevinsky on 2019-03-07.
 *
 * Owner view + edit book description
 *
 */
public class US010601Test extends ActivityTestRule<MyBooksActivity> {

    private Solo solo;
    private UITestHelper uiTestHelper;

    public US010601Test() throws InterruptedException {
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
    public void testEditBookDescription(){
        solo.assertCurrentActivity("Wrong Activity", MyBooksActivity.class);
        addBookWithoutImages();

        solo.clickInRecyclerView(0);
        solo.clickOnView(solo.getView(R.id.addBookButton));
        solo.waitForActivity(ViewBookActivity.class);
        solo.assertCurrentActivity("Wrong Activity", ViewBookActivity.class);
        solo.sleep(1000);
        solo.clickOnView(solo.getView(R.id.edit_button));
        solo.sleep(1000);
        solo.clearEditText((EditText) solo.getView(R.id.synopsis_element));
        solo.enterText((EditText) solo.getView(R.id.synopsis_element), "I edited this text");
        solo.clickOnView(solo.getView(R.id.save_button));
        solo.sleep(1000);
        solo.goBack();

        solo.assertCurrentActivity("Wrong Activity", MyBooksActivity.class);
        solo.sleep(1000);
        solo.clickInRecyclerView(0);
        solo.sleep(1000);
        solo.clickOnView(solo.getView(R.id.addBookButton));
        solo.assertCurrentActivity("Wrong Activity", ViewBookActivity.class);
        solo.sleep(1000);
        final TextView synopsis =
                (TextView) solo.getCurrentActivity().findViewById(R.id.synopsis_element);
        assertEquals("I edited this text", synopsis.getText().toString());
        solo.sleep(1000);
        solo.goBack();
        solo.sleep(1000);
        deleteBook();

    }

    public void addBookWithoutImages(){
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

        assertTrue(myBooksList.getAdapter().getItemCount() == 0);

    }
}
