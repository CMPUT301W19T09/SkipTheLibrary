package com.stl.skipthelibrary.BindersAndAdapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.stl.skipthelibrary.DatabaseAndAPI.DatabaseHelper;
import com.stl.skipthelibrary.Entities.Book;
import com.stl.skipthelibrary.Entities.Notification;
import com.stl.skipthelibrary.Enums.BookStatus;
import com.stl.skipthelibrary.Activities.MyBooksActivity;
import com.stl.skipthelibrary.Activities.ProfileActivity;
import com.stl.skipthelibrary.Enums.NotificationType;
import com.stl.skipthelibrary.R;
import com.stl.skipthelibrary.Activities.ViewBookActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

/**
 * The recycler adapter for books. Used to display a list of books.
 */
public class NotificationRecyclerAdapter extends RecyclerView.Adapter<NotificationRecyclerAdapter.ViewHolder>{
    final private static String TAG = "NotificationRecyclerViewAdapter";
    private Context context;
    private ArrayList<Notification> notifications;
    private Notification mRecentlyDeletedItem;
    private int mRecentlyDeletedItemPosition;
    final public static String BOOK_ID = "bookUUID";


    /**
     * The constructor
     * @param context: the context of the container
     * @param notifications: the list of notifications to display
     */
    public NotificationRecyclerAdapter(Context context, ArrayList<Notification> notifications) {
        this.context = context;
        this.notifications = notifications;
    }

    /**
     * Gets the context
     * @return the context
     */

    public Context getContext() {
        return context;
    }

    /**
     * sets the context
     * @param context: the context
     */
    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * gets the arraylist of books
     * @returnthe arraylist of books
     */
    public ArrayList<Notification> getNotifications() {
        return notifications;
    }

    /**
     * sets the arraylist of books
     * @param notifications: the arraylist of notifications
     */
    public void setBooks(ArrayList<Notification> notifications) {
        this. notifications = notifications;
    }


    /**
     * On creating the viewholder inflate the xml and return the adapter viewholder
     * @param parent: the parent viewgroup
     * @param viewType: the id of the viewtype
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_list_item, parent,false);
        return new NotificationRecyclerAdapter.ViewHolder(view);
    }

    /**
     * Bind the UI elements of the ViewHolder
     * @param holder: the viewholder which contains the UI elements
     * @param position: the book in the book array we are displaying
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        NotificationType notificationType = notifications.get(position).getNotificationType();
        String bookName = notifications.get(position).getBookName();
        String userName = notifications.get(position).getUserName();
        String notificationID = notifications.get(position).getUuid();
        String notificationBodyText = "Notification Received";


        if (notificationType.toString().equals(NotificationType.NEW_REQUEST.toString())){
            holder.notificationBubble.setBackgroundTintList(context.getColorStateList(R.color.AVAILABLE));
            notificationBodyText = "Your book has been requested";
        }
        else if (notificationType.toString().equals(NotificationType.REQUEST_ACCEPTED.toString())){
            holder.notificationBubble.setBackgroundTintList(context.getColorStateList(R.color.ACCEPTED));
            notificationBodyText = "Your request has been accepted";
        }
        else if (notificationType.toString().equals(NotificationType.REQUEST_DENIED.toString())){
            holder.notificationBubble.setBackgroundTintList(context.getColorStateList(R.color.declined_red));
            notificationBodyText = "Your request has been denied";
        }

        holder.notificationHeader.setText(bookName);
        holder.notificationBody.setText(notificationBodyText);


        holder.notificationBubble.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewBookActivity.class);
                intent.putExtra(BOOK_ID, notifications.get(position).getBookID());
                context.startActivity(intent);
                Log.d(TAG,"StartedActivity");
            }
        });


        holder.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteNotification(position);
            }
        });

    }

    /**
     * Return the number of books to display
     * @return the number of books
     */
    @Override
    public int getItemCount() {
        return notifications.size();
    }


    /**
     * Inner Class ViewHolder defines all of the elements in the corresponding xml file.
     *  it allows us to set their properties during onBind.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout parentLayout;
        LinearLayout notificationBubble;
        FloatingActionButton closeButton;
        TextView notificationHeader;
        TextView notificationBody;

        /**
         * ViewHolder constructor
         * @param itemView: the current itemview
         */
        public ViewHolder(View itemView) {
            super(itemView);
            parentLayout = itemView.findViewById(R.id.notification_recycler_view);
            closeButton = itemView.findViewById(R.id.floatingActionButton2);
            notificationHeader = itemView.findViewById(R.id.NotificationHeading);
            notificationBody = itemView.findViewById(R.id.NotificationBody);
            notificationBubble = itemView.findViewById(R.id.notification_bubble);
        }

    }

    /**
     * Delete an item from the arraylist
     * @param position: the position of the book we are deleting
     */
    public void deleteNotification(int position) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        mRecentlyDeletedItem = notifications.get(position);
        mRecentlyDeletedItemPosition = position;
        notifications.remove(position);
        notifyItemRemoved(position);
        databaseHelper.deleteNotification(mRecentlyDeletedItem);
    }



}
