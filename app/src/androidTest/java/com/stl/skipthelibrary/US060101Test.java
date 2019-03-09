package com.stl.skipthelibrary;

import android.app.Activity;
import android.app.NativeActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.stl.skipthelibrary.Activities.ScannerActivity;
import com.stl.skipthelibrary.Activities.SearchActivity;
import com.stl.skipthelibrary.Activities.ViewBookActivity;
import com.stl.skipthelibrary.DatabaseAndAPI.DatabaseHelper;
import com.stl.skipthelibrary.Entities.Notification;
import com.stl.skipthelibrary.Entities.State;
import com.stl.skipthelibrary.Helpers.NavigationHandler;

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
    public ActivityTestRule<LoginActivity> rule =
            new ActivityTestRule<>(LoginActivity.class, false, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), rule.getActivity());
    }

    @Test
    public void testScanningFlow() throws Exception {
        
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.EmailEditText), "Felix@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.PasswordEditText), "123456");
        solo.assertCurrentActivity("Wrong activity", NotificationActivity.class);


        BottomNavigationView view;
        view = (BottomNavigationView)solo.getView(R.id.bottom_navigation);
        view.setOnNavigationItemSelectedListener(new NavigationHandler(view.getContext()));
        solo.clickOnView(view.findViewById(R.id.profile));
        solo.assertCurrentActivity("Wrong Activity", ProfileActivity.class);

        view = (BottomNavigationView)solo.getView(R.id.bottom_navigation);
        view.setOnNavigationItemSelectedListener(new NavigationHandler(view.getContext()));
        solo.clickOnView(view.findViewById(R.id.my_books));
        solo.assertCurrentActivity("Wrong Activity", MyBooksActivity.class);

        solo.clickOnView(solo.getView(R.id.addBookButton));

        solo.assertCurrentActivity("Wrong Activity", AddBooksActivity.class);
        solo.enterText((EditText) solo.getView(R.id.AddBookTitle), "Felix");
        solo.enterText((EditText) solo.getView(R.id.AddBookAuthor), "Felix");
        solo.enterText((EditText) solo.getView(R.id.AddBookISBN), "1234567890123");
        solo.enterText((EditText) solo.getView(R.id.AddBookDesc), "Felix");
        solo.clickOnView(solo.getView(R.id.SaveBookButton));

        solo.assertCurrentActivity("Wrong Activity", MyBooksActivity.class);

        solo.clickOnView(solo.getView(R.id.BookListItemRightArrow));
        solo.assertCurrentActivity("Wrong Activity", ViewBookActivity.class);
        solo.goBack();
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

//        assertTrue(myBooksList.getAdapter().getItemCount() == 0);

    }

    public void GetBook(){
        RecyclerView myBooksList = (RecyclerView) solo.getView(R.id.ownerBooksRecyclerView);
        View bookToDelete = myBooksList.getChildAt(0);


    }
}
