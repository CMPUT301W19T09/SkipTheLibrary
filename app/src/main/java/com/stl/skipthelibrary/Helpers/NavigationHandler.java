package com.stl.skipthelibrary.Helpers;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.stl.skipthelibrary.Activities.BorrowersBooksActivity;
import com.stl.skipthelibrary.Activities.MyBooksActivity;
import com.stl.skipthelibrary.Activities.NotificationActivity;
import com.stl.skipthelibrary.Activities.ProfileActivity;
import com.stl.skipthelibrary.R;
import com.stl.skipthelibrary.Singletons.CurrentUser;

import androidx.annotation.NonNull;

/**
 * Handles the navigation between screens with a navigation bar
 */
public class NavigationHandler implements BottomNavigationView.OnNavigationItemSelectedListener {
    Context context;

    /**
     * The Constructor
     * @param context: the current context
     */
    public NavigationHandler(Context context){
        this.context = context;
    }

    /**
     * When an item is selected navigate to the corresponding screen unless we are already on the
     * screen for that button
     * @param menuItem: which button was clicked
     * @return true
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Intent intent;
        switch (menuItem.getItemId()) {
            case R.id.home:
                if (context.getClass() == NotificationActivity.class){
                    break;
                }
                intent = new Intent(context, NotificationActivity.class);
                context.startActivity(intent);
                break;
            case R.id.profile:
                if (context.getClass() == ProfileActivity.class){
                    break;
                }
                intent = new Intent(context, ProfileActivity.class);
                intent.putExtra(ProfileActivity.USER_NAME,
                        CurrentUser.getInstance().getUserName());
                context.startActivity(intent);
                break;
            case R.id.my_books:
                if (context.getClass() == MyBooksActivity.class){
                    break;
                }
                intent = new Intent(context, MyBooksActivity.class);
                context.startActivity(intent);
                break;
            case R.id.borrow:
                if (context.getClass() == BorrowersBooksActivity.class){
                    break;
                }
                intent = new Intent(context, BorrowersBooksActivity.class);
                context.startActivity(intent);
                break;
        }
        return true;
    }
}
