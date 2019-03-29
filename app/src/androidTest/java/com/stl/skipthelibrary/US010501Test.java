package com.stl.skipthelibrary;

import android.view.View;
import android.widget.EditText;

import com.robotium.solo.Solo;
import com.stl.skipthelibrary.Activities.AddBooksActivity;
import com.stl.skipthelibrary.Activities.MyBooksActivity;
import com.stl.skipthelibrary.Activities.ViewBookActivity;
import com.stl.skipthelibrary.BindersAndAdapters.BookRecyclerAdapter;
import com.stl.skipthelibrary.Entities.Book;
import com.stl.skipthelibrary.Entities.BookDescription;
import com.stl.skipthelibrary.Entities.RequestHandler;
import com.stl.skipthelibrary.Entities.State;
import com.stl.skipthelibrary.Enums.BookStatus;
import com.stl.skipthelibrary.Enums.HandoffState;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class US010501Test extends ActivityTestRule<MyBooksActivity> {
    private Solo solo;
    private int index;
    private UITestHelper uiTestHelper;

    public US010501Test() throws InterruptedException {
        super(MyBooksActivity.class, true, true);

        ArrayList<Book> books = new ArrayList<>();
        Book book1 = new Book(
                "4366725582347",
                new BookDescription("Requested Book", "This book is requested", "ReReQ"),
                "UITest",
                new RequestHandler(new State(BookStatus.REQUESTED, null, HandoffState.NULL_STATE)),
                null, null);

        Book book2 = new Book(
                "0848537783834",
                new BookDescription("Accepted Book", "This book is Accepted", "ACAC"),
                "UITest",
                new RequestHandler(new State(BookStatus.ACCEPTED, null, HandoffState.NULL_STATE)),
                null, null);

        Book book3 = new Book(
                "4454640963581",
                new BookDescription("Lent Book", "This book is Lent", "LeLent"),
                "UITest",
                new RequestHandler(new State(BookStatus.BORROWED, null, HandoffState.NULL_STATE)),
                null, null);

        Book book4 = new Book(
                "6776361482697",
                new BookDescription("Available Book", "This book is available", "AVAVail"),
                "UITest",
                new RequestHandler(new State(BookStatus.AVAILABLE, null, HandoffState.NULL_STATE)),
                null, null);

        books.add(book1);
        books.add(book2);
        books.add(book3);
        books.add(book4);

        uiTestHelper = new UITestHelper(true, true, books);
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

        View availableChip = solo.getView(R.id.AvailableChip);
        View requestedChip = solo.getView(R.id.RequestedChip);
        View acceptedChip = solo.getView(R.id.AcceptedChip);
        View lentChip = solo.getView(R.id.LentChip);

        assertTrue(solo.waitForText("Requested Book", 1, 500));
        assertTrue(solo.waitForText("Accepted Book", 1, 500));
        assertTrue(solo.waitForText("Lent Book", 1, 500));
        assertTrue(solo.waitForText("Available Book", 1, 500));

        solo.clickOnView(requestedChip);
        solo.sleep(1000);

        assertFalse(solo.waitForText("Requested Book", 1, 500));
        assertTrue(solo.waitForText("Accepted Book", 1, 500));
        assertTrue(solo.waitForText("Lent Book", 1, 500));
        assertTrue(solo.waitForText("Available Book", 1, 500));

        solo.clickOnView(requestedChip);
        solo.clickOnView(acceptedChip);
        solo.sleep(1000);

        assertTrue(solo.waitForText("Requested Book", 1, 500));
        assertFalse(solo.waitForText("Accepted Book", 1, 500));
        assertTrue(solo.waitForText("Lent Book", 1, 500));
        assertTrue(solo.waitForText("Available Book", 1, 500));

        solo.clickOnView(acceptedChip);
        solo.clickOnView(lentChip);
        solo.sleep(1000);

        assertTrue(solo.waitForText("Requested Book", 1, 500));
        assertTrue(solo.waitForText("Accepted Book", 1, 500));
        assertFalse(solo.waitForText("Lent Book", 1, 500));
        assertTrue(solo.waitForText("Available Book", 1, 500));

        solo.clickOnView(lentChip);
        solo.clickOnView(availableChip);
        solo.sleep(1000);

        assertTrue(solo.waitForText("Requested Book", 1, 500));
        assertTrue(solo.waitForText("Accepted Book", 1, 500));
        assertTrue(solo.waitForText("Lent Book", 1, 500));
        assertFalse(solo.waitForText("Available Book", 1, 500));

        solo.clickOnView(availableChip);
        solo.sleep(1000);

        assertTrue(solo.waitForText("Requested Book", 1, 500));
        assertTrue(solo.waitForText("Accepted Book", 1, 500));
        assertTrue(solo.waitForText("Lent Book", 1, 500));
        assertTrue(solo.waitForText("Available Book", 1, 500));
    }

    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        uiTestHelper.finish();
    }

}



