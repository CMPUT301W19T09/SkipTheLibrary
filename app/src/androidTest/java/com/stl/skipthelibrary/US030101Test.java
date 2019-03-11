package com.stl.skipthelibrary;

import android.app.Activity;
import android.widget.AutoCompleteTextView;

import com.robotium.solo.Solo;
import com.stl.skipthelibrary.Activities.BorrowersBooksActivity;
import com.stl.skipthelibrary.Activities.SearchActivity;
import com.stl.skipthelibrary.Entities.Book;
import com.stl.skipthelibrary.Entities.BookDescription;
import com.stl.skipthelibrary.Entities.Rating;
import com.stl.skipthelibrary.Entities.RequestHandler;
import com.stl.skipthelibrary.Entities.State;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import androidx.test.rule.ActivityTestRule;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.assertTrue;

public class US030101Test extends ActivityTestRule<BorrowersBooksActivity> {

    private Solo solo;
    private static UITestHelper uiTestHelper;

    public US030101Test() throws InterruptedException {
        super(BorrowersBooksActivity.class, false, true);

        RequestHandler requests = new RequestHandler(new State());
        
        BookDescription book1Description = new BookDescription("SpecificTitleToTest", "Test book", "Test Author");
        Book book1 = new Book("123-456-7891011", book1Description, "testyBoi", requests, null, new Rating());
        BookDescription book2Description = new BookDescription("Test Title", "Test book", "SpecificAuthorToTest");
        Book book2 = new Book("123-456-7891012", book2Description, "testyBoi", requests, null, new Rating());
        BookDescription book3Description = new BookDescription("Test Title", "SpecificDescriptionToTest", "Test Author");
        Book book3 = new Book("123-456-7891013", book3Description, "testyBoi", requests, null, new Rating());
        BookDescription book4Description = new BookDescription("Artemis Fowl", "Test book", "Neil Young");
        Book book4 = new Book("123-456-7891014", book4Description, "testyBoi", requests, null, new Rating());

        ArrayList<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);
        books.add(book3);
        books.add(book4);

        uiTestHelper = new UITestHelper(true, true, books);
    }

    @Rule
    public ActivityTestRule<BorrowersBooksActivity> rule =
            new ActivityTestRule<>(BorrowersBooksActivity.class, false, true);

    @Before
    public void setUp() throws Exception {

        solo = new Solo(getInstrumentation(), rule.getActivity());
    }

    @Test
    public void testSingleKeywordTitleSearch() throws Exception {

        solo.assertCurrentActivity("Wrong Activity", BorrowersBooksActivity.class);

        solo.clickOnView(solo.getView(R.id.searchBookButton));

        solo.waitForActivity(SearchActivity.class);
        solo.assertCurrentActivity("Wrong Activity", SearchActivity.class);

        solo.enterText((AutoCompleteTextView) solo.getView(R.id.SearchBar), "SpecificT");

        assertTrue(solo.waitForText("SpecificTitleToTest"));

    }

    @Test
    public void testSingleKeywordAuthorSearch() throws Exception {

        solo.assertCurrentActivity("Wrong Activity", BorrowersBooksActivity.class);

        solo.clickOnView(solo.getView(R.id.searchBookButton));

        solo.waitForActivity(SearchActivity.class);
        solo.assertCurrentActivity("Wrong Activity", SearchActivity.class);

        solo.enterText((AutoCompleteTextView) solo.getView(R.id.SearchBar), "SpecificA");

        assertTrue(solo.waitForText("SpecificAuthorToTest"));

    }

    @Test
    public void testSingleKeywordDescriptionSearch() throws Exception {

        solo.assertCurrentActivity("Wrong Activity", BorrowersBooksActivity.class);

        solo.clickOnView(solo.getView(R.id.searchBookButton));

        solo.waitForActivity(SearchActivity.class);
        solo.assertCurrentActivity("Wrong Activity", SearchActivity.class);

        solo.enterText((AutoCompleteTextView) solo.getView(R.id.SearchBar), "SpecificD");

        assertTrue(solo.waitForText("Test", 2, 2000));

    }

    @Test
    public void testMultipleKeywordSearch() throws Exception {

        solo.assertCurrentActivity("Wrong Activity", BorrowersBooksActivity.class);

        solo.clickOnView(solo.getView(R.id.searchBookButton));

        solo.waitForActivity(SearchActivity.class);
        solo.assertCurrentActivity("Wrong Activity", SearchActivity.class);

        solo.enterText((AutoCompleteTextView) solo.getView(R.id.SearchBar), "artemis young");

        assertTrue(solo.waitForText("Artemis Fowl"));
        assertTrue(solo.waitForText("Neil Young"));
    }

    @After
    public void tearDown() throws InterruptedException {
        uiTestHelper.finish();
        uiTestHelper.deleteUsersBooks("testyBoi");
        solo.finishOpenedActivities();
    }

}
