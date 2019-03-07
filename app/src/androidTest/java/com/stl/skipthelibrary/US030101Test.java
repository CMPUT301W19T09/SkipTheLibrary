package com.stl.skipthelibrary;

import android.app.Activity;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.robotium.solo.Solo;
import com.stl.skipthelibrary.Activities.BorrowersBooksActivity;
import com.stl.skipthelibrary.Activities.SearchActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.rule.ActivityTestRule;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.assertTrue;

public class US030101Test extends ActivityTestRule<BorrowersBooksActivity> {

    private Solo solo;

    public US030101Test() {
        super(BorrowersBooksActivity.class, false, true);
    }

    @Rule
    public ActivityTestRule<BorrowersBooksActivity> rule =
            new ActivityTestRule<>(BorrowersBooksActivity.class, false, true);

    @Before
    public void setUp() throws Exception {

        solo = new Solo(getInstrumentation(), rule.getActivity());
    }

    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
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
}
