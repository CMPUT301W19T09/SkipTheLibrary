package com.stl.skipthelibrary;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.PointF;
import android.view.View;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

public class US010701Test extends ActivityTestRule<MyBooksActivity> {

    private Solo solo;

    public US010701Test() {
        super(MyBooksActivity.class, false, true);
    }

    @Rule
    public ActivityTestRule<MyBooksActivity> rule =
            new ActivityTestRule<>(MyBooksActivity.class, false, true);

    @Before
    public void setUp() throws Exception{

        solo = new Solo(getInstrumentation(), rule.getActivity());
    }

    @Test
    public void start() throws Exception{

        Activity activity = rule.getActivity();
    }

    @Test
    public void testDeleteBook() {

        solo.assertCurrentActivity("Wrong Activity", MyBooksActivity.class);
        RecyclerView myBooksList = (RecyclerView) solo.getView(R.id.ownerBooksRecyclerView);
        View bookToDelete = myBooksList.getChildAt(0);
        PointF start1 = new PointF(bookToDelete.getRight() - 5, bookToDelete.getTop() - bookToDelete.getHeight()/2);
        PointF start2 = new PointF(bookToDelete.getRight() - 5, bookToDelete.getTop() - bookToDelete.getHeight()/2);
        PointF end1 = new PointF(bookToDelete.getLeft() + 5, bookToDelete.getTop() - bookToDelete.getHeight()/2);
        PointF end2 = new PointF(bookToDelete.getLeft() + 5, bookToDelete.getTop() - bookToDelete.getHeight()/2);
        solo.swipe(start1, start2, end1, end2);
    }
}
