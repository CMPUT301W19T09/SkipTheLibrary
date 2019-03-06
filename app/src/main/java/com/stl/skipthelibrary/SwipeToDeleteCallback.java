package com.stl.skipthelibrary;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Luke Slevinsky on 2019-03-03.
 */
public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {
    final public static String TAG = SwipeToDeleteCallback.class.getSimpleName();
    private BookRecyclerAdapter mAdapter;

    private Drawable icon;
    private final ColorDrawable background;

    public SwipeToDeleteCallback(BookRecyclerAdapter adapter) {
        super(0,ItemTouchHelper.LEFT);
        mAdapter = adapter;
        icon = ContextCompat.getDrawable(mAdapter.getContext(),
                android.R.drawable.ic_menu_delete);
        background = new ColorDrawable(Color.RED);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 10;


        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        if (dX < 0) { // Swiping to the left
            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            int newLeft = itemView.getRight() + ((int) dX) - backgroundCornerOffset;
//            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
            Log.d(TAG, "onChildDraw: "+newLeft + " " + iconLeft);
            if (newLeft <= iconLeft) {
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
            } else {
                icon.setBounds(0, 0, 0, 0);
            }

            background.setBounds(newLeft, itemView.getTop(), itemView.getRight(), itemView.getBottom());
        } else { // view is unSwiped
            background.setBounds(0, 0, 0, 0);
        }

        background.draw(c);
        icon.draw(c);
    }

    // Moving items up and down
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                          @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        mAdapter.deleteItem(position);
        mAdapter.notifyItemRemoved(position);
    }


}
