package com.stl.skipthelibrary;

import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.robotium.solo.Solo;
import com.stl.skipthelibrary.Activities.AddBooksActivity;
import com.stl.skipthelibrary.Activities.MyBooksActivity;
import com.stl.skipthelibrary.Activities.ProfileActivity;
import com.stl.skipthelibrary.Activities.ViewBookActivity;
import com.stl.skipthelibrary.Entities.Book;
import com.stl.skipthelibrary.Helpers.NavigationHandler;

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
public class US020201Test extends ActivityTestRule<MyBooksActivity> {

    private Solo solo;
    private UITestHelper uiTestHelper;

    public US020201Test() throws InterruptedException {
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

        solo.sleep(1000);
        selectMenuItem(R.id.profile);
        solo.sleep(1000);
        solo.waitForActivity(ProfileActivity.class);
        solo.assertCurrentActivity("Wrong Activity", ProfileActivity.class);

        solo.clickOnView(solo.getView(R.id.profile_edit_button));

        solo.sleep(1000);
        solo.clickOnView(solo.getView(R.id.phoneNumber));
        solo.clearEditText((EditText) solo.getView(R.id.phoneNumber));
        String newPhoneNumber = "7804459972";
        solo.enterText((EditText) solo.getView(R.id.phoneNumber), newPhoneNumber);
        solo.sleep(2000);

//        solo.enterText((EditText) solo.getView(R.id.editTextPassword), "123456");
//        solo.clickOnView(solo.getView(android.R.id.button1)); //button1 is positive button, submit
//        solo.sleep(1000);
//        solo.clickOnView(solo.getView(R.id.profile_save_button));

        solo.sleep(1000);

    }

    public void selectMenuItem(Integer menuRid ){
        BottomNavigationView view = (BottomNavigationView)solo.getView(R.id.bottom_navigation);
        solo.sleep(1000);
        view.setOnNavigationItemSelectedListener(new NavigationHandler(view.getContext()));
        solo.sleep(1000);
        solo.clickOnView(view.findViewById(menuRid));
    }
}
