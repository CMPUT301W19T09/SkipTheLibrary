package com.stl.skipthelibrary;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.EditText;

import com.robotium.solo.Solo;
import com.stl.skipthelibrary.Activities.AddBooksActivity;
import com.stl.skipthelibrary.Activities.MyBooksActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.CoreMatchers.not;

public class US080101Test extends IntentsTestRule<MyBooksActivity> {

        private Solo solo;

        public US080101Test() {
            super(MyBooksActivity.class, true, true);
        }


        @Rule
        public IntentsTestRule<MyBooksActivity> rule =
                new IntentsTestRule<>(MyBooksActivity.class, true, true);

        @Before
        public void setUp() throws Exception{

            solo = new Solo(getInstrumentation(), rule.getActivity());
        }

        @Test
        public void start() throws Exception{

            Activity activity = rule.getActivity();
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

            Uri testImageUri = Uri.parse("content://com.google.android.apps.photos.contentprovider/-1/1/content%3A%2F%2Fmedia%2Fexternal%2Fimages%2Fmedia%2F25/ORIGINAL/NONE/1876073787");
            Intent resultData = new Intent();
            resultData.setData(testImageUri);
            Instrumentation.ActivityResult result =
                    new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);

            // Set up result stubbing when an intent sent to "choose photo" is seen.
            intending(not(isInternal())).respondWith(result);
            View addImageButton = solo.getView(R.id.addBookImageButton);

            solo.clickOnView(addImageButton);


//            solo.clickOnImageButton(0); //takes us back to my books activity
//
//            solo.waitForActivity(MyBooksActivity.class);
//            solo.assertCurrentActivity("Wrong Activity", MyBooksActivity.class);
        }
}
