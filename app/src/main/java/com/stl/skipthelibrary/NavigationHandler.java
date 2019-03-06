package com.stl.skipthelibrary;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;

public class NavigationHandler implements BottomNavigationView.OnNavigationItemSelectedListener {
    Context context;

    NavigationHandler(Context context){
        this.context = context;
    }

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
