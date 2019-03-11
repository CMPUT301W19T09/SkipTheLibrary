package com.stl.skipthelibrary;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.robotium.solo.Solo;
import com.stl.skipthelibrary.Activities.AddBooksActivity;
import com.stl.skipthelibrary.Activities.MyBooksActivity;
import com.stl.skipthelibrary.Activities.ViewBookActivity;
import com.stl.skipthelibrary.BindersAndAdapters.BookRecyclerAdapter;
import com.stl.skipthelibrary.Entities.Book;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class US010401Test extends ActivityTestRule<MyBooksActivity> {
    private Solo solo;
    private int index;
    private UITestHelper uiTestHelper;

    public US010401Test() throws InterruptedException {
        super(MyBooksActivity.class, true, true);
        uiTestHelper = new UITestHelper(true, true, new ArrayList<Book>());
    }

    @Rule
    public ActivityTestRule<MyBooksActivity> rule =
            new ActivityTestRule<>(MyBooksActivity.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), rule.getActivity());
    }

    @Test
    public void viewOwnBooks() {
        solo.assertCurrentActivity("Wrong Activity", MyBooksActivity.class);

        addBookWithoutImages();

        View availableChip = solo.getView(R.id.AvailableChip);

        solo.clickOnView(availableChip);
        assertFalse(solo.waitForText("Test Title", 1, 500));

        solo.clickOnView(availableChip);
        assertTrue(solo.waitForText("Test Title"));

        RecyclerView myBooksList = (RecyclerView) solo.getView(R.id.ownerBooksRecyclerView);
        BookRecyclerAdapter adapter = (BookRecyclerAdapter) myBooksList.getAdapter();
        ArrayList<Book> books = adapter.getBooks();
        for (Book book: books) {
            if (book.getDescription().getTitle().equals("Test Title")) {
                index = adapter.getBooks().indexOf(book);
            }
        }
        assertTrue(index>=0);
        solo.clickInRecyclerView(index);
        solo.assertCurrentActivity("Should be ViewBookActivity", ViewBookActivity.class);
        assertTrue(solo.waitForText("Test Title"));
        solo.sleep(2000);
        solo.goBack();

        deleteBook();
    }

    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        uiTestHelper.finish();
    }

    public void addBookWithoutImages(){
        solo.assertCurrentActivity("Wrong Activity", MyBooksActivity.class);

        solo.clickOnView(solo.getView(R.id.addBookButton));

        solo.waitForActivity(AddBooksActivity.class);
        solo.assertCurrentActivity("Wrong Activity", AddBooksActivity.class);


        solo.enterText((EditText) solo.getView(R.id.AddBookTitle), "Test Title");
        solo.enterText((EditText) solo.getView(R.id.AddBookAuthor), "Test Author");
        solo.enterText((EditText) solo.getView(R.id.AddBookISBN), "123-456-789-1011");
        solo.enterText((EditText) solo.getView(R.id.AddBookDesc), "Test Description for the book.");

        solo.clickOnImageButton(0); //takes us back to my books activity

        solo.waitForActivity(MyBooksActivity.class);
        solo.assertCurrentActivity("Wrong Activity", MyBooksActivity.class);

        assertTrue(solo.waitForText("Test Title"));
        assertTrue(solo.waitForText("Test Author"));

    }

    public void deleteBook() {
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


}



