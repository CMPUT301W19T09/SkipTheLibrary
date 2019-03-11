package com.stl.skipthelibrary;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.robotium.solo.Solo;
import com.stl.skipthelibrary.Activities.AddBooksActivity;
import com.stl.skipthelibrary.Activities.MyBooksActivity;
import com.stl.skipthelibrary.Entities.Book;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.CoreMatchers.not;

public class US080101Test extends IntentsTestRule<MyBooksActivity> {

        private Solo solo;
        private UITestHelper uiTestHelper;

        public US080101Test() throws InterruptedException {
            super(MyBooksActivity.class, true, true);
            uiTestHelper = new UITestHelper(true, true, new ArrayList<Book>());
        }


        @Rule
        public IntentsTestRule<MyBooksActivity> rule =
                new IntentsTestRule<>(MyBooksActivity.class, true, true);

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
        public void addPhotoToBook() throws Exception {

            solo.assertCurrentActivity("Wrong Activity", MyBooksActivity.class);

            solo.clickOnView(solo.getView(R.id.addBookButton));

            solo.waitForActivity(AddBooksActivity.class);
            solo.assertCurrentActivity("Wrong Activity", AddBooksActivity.class);


            solo.enterText((EditText) solo.getView(R.id.AddBookTitle), "Test Title");
            solo.enterText((EditText) solo.getView(R.id.AddBookAuthor), "Test Author");
            solo.enterText((EditText) solo.getView(R.id.AddBookISBN), "123-456-789-1011");
            solo.enterText((EditText) solo.getView(R.id.AddBookDesc), "Test Description");

            Resources resources = solo.getCurrentActivity().getApplicationContext().getResources();
            Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                    resources.getResourcePackageName(R.mipmap.ic_launcher) + '/' +
                    resources.getResourceTypeName(R.mipmap.ic_launcher) + '/' +
                    resources.getResourceEntryName(R.mipmap.ic_launcher));

            Intent resultData = new Intent();
            resultData.setData(imageUri);
            Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(
                    Activity.RESULT_OK, resultData);

            // Set up result stubbing when an intent sent to "choose photo" is seen.
            intending(not(isInternal())).respondWith(result);

            View addImageButton = solo.getView(R.id.addBookImageButton);
            solo.clickOnView(addImageButton);

            solo.sleep(1000);
            solo.clickOnImageButton(0); //takes us back to my books activity

            solo.waitForActivity(MyBooksActivity.class);
            solo.assertCurrentActivity("Wrong Activity", MyBooksActivity.class);
        }
}
