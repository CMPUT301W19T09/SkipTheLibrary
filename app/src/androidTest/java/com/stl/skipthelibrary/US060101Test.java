package com.stl.skipthelibrary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.robotium.solo.Solo;
import com.stl.skipthelibrary.Activities.AddBooksActivity;
import com.stl.skipthelibrary.Activities.BorrowersBooksActivity;
import com.stl.skipthelibrary.Activities.MyBooksActivity;
import com.stl.skipthelibrary.Activities.ScannerActivity;
import com.stl.skipthelibrary.Activities.SearchActivity;
import com.stl.skipthelibrary.Activities.ViewBookActivity;
import com.stl.skipthelibrary.DatabaseAndAPI.DatabaseHelper;
import com.stl.skipthelibrary.Entities.State;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.assertTrue;

public class US060101Test extends ActivityTestRule<ViewBookActivity> {
    private Solo solo;
    private DatabaseHelper databaseHelper;
        public US060101Test() {

            super(ViewBookActivity.class, false, false);
        }

    private class MockScanner extends ScannerActivity {
        private String isbn;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            Intent intent = new Intent();
            intent.putExtra("ISBN", isbn);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }

        @Override
        public void finish() {
            super.finish();
        }

        public void setIsbn(String isbn) {
            this.isbn = isbn;
        }
    }

    @Rule
    public ActivityTestRule<MyBooksActivity> rule =
            new ActivityTestRule<>(MyBooksActivity.class, false, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), rule.getActivity());
    }

    @Test
    public void testSingleKeywordTitleSearch() throws Exception {

        solo.assertCurrentActivity("Wrong Activity", MyBooksActivity.class);
        solo.clickOnView(solo.getView(R.id.addBookButton));

        solo.assertCurrentActivity("Wrong Activity", AddBooksActivity.class);
        solo.enterText((EditText) solo.getView(R.id.AddBookTitle), "Felix");
        solo.enterText((EditText) solo.getView(R.id.AddBookAuthor), "Felix");
        solo.enterText((EditText) solo.getView(R.id.AddBookISBN), "1234567890123");
        solo.enterText((EditText) solo.getView(R.id.AddBookDesc), "Felix");
        solo.clickOnView(solo.getView(R.id.SaveBookButton));

        solo.assertCurrentActivity("Wrong Activity", MyBooksActivity.class);
        //TODO: Uncomment this once solve the firebase problem
//        solo.clickOnView(solo.getView(R.id.BookListItemRightArrow));
//        solo.assertCurrentActivity("Wrong Activity", ViewBookActivity.class);

        deleteBook();

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

    public void GetBook(){
        RecyclerView myBooksList = (RecyclerView) solo.getView(R.id.ownerBooksRecyclerView);
        View bookToDelete = myBooksList.getChildAt(0);


    }
}
