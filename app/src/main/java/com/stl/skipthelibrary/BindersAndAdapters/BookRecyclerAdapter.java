package com.stl.skipthelibrary.BindersAndAdapters;

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

import com.google.android.material.snackbar.Snackbar;
import com.stl.skipthelibrary.DatabaseAndAPI.DatabaseHelper;
import com.stl.skipthelibrary.Entities.Book;
import com.stl.skipthelibrary.Enums.BookStatus;
import com.stl.skipthelibrary.Activities.MyBooksActivity;
import com.stl.skipthelibrary.Activities.ProfileActivity;
import com.stl.skipthelibrary.R;
import com.stl.skipthelibrary.Activities.ViewBookActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * The recycler adapter for books. Used to display a list of books.
 */
public class BookRecyclerAdapter extends RecyclerView.Adapter<BookRecyclerAdapter.ViewHolder>{
    final public static String BOOK_ID = "bookUUID";
    final private static String TAG = "RecyclerViewAdapter";
    private Context context;
    private ArrayList<Book> books;
    private Book mRecentlyDeletedItem;
    private int mRecentlyDeletedItemPosition;

    /**
     * The constructor
     * @param context: the context of the container
     * @param books: the list of books to display
     */
    public BookRecyclerAdapter(Context context, ArrayList<Book> books) {
        this.context = context;
        this.books = books;
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
    public ArrayList<Book> getBooks() {
        return books;
    }

    /**
     * sets the arraylist of books
     * @param books: the arraylist of books
     */
    public void setBooks(ArrayList<Book> books) {
        this.books = books;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_list_item, parent,false);
        return new BookRecyclerAdapter.ViewHolder(view);
    }

    /**
     * Bind the UI elements of the ViewHolder
     * @param holder: the viewholder which contains the UI elements
     * @param position: the book in the book array we are displaying
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        String title = books.get(position).getDescription().getTitle();
        String author = books.get(position).getDescription().getAuthor();
        String status = books.get(position).getRequests().getState().getBookStatus().name();
        final String userName = books.get(position).getOwnerUserName();
        holder.title.setText(title);
        holder.author.setText(author);

        SpannableString userNameUnderLined = new SpannableString("@" + userName);
        userNameUnderLined.setSpan(new UnderlineSpan(), 0, userNameUnderLined.length(), 0);
        holder.userName.setText( userNameUnderLined);

        if (context.getClass() == MyBooksActivity.class && status.equals(BookStatus.BORROWED.name())){
            holder.status.setText(context.getString(R.string.lent).toUpperCase());
        }
        else{
            holder.status.setText(status);
        }


        if (status.equals(BookStatus.ACCEPTED.name())){
            holder.status.setBackgroundTintList(context.getColorStateList(R.color.ACCEPTED));
        }
        else if (status.equals(BookStatus.AVAILABLE.name())){
            holder.status.setBackgroundTintList(context.getColorStateList(R.color.AVAILABLE));
        }
        else if (status.equals(BookStatus.REQUESTED.name())){
            holder.status.setBackgroundTintList(context.getColorStateList(R.color.REQUESTED));
        }
        else if (status.equals(BookStatus.BORROWED.name())){
            holder.status.setBackgroundTintList(context.getColorStateList(R.color.BORROWED));
        }


        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "clicked on book arrow");
                Intent intent = new Intent(context, ViewBookActivity.class);
                intent.putExtra(BOOK_ID, books.get(position).getUuid());
                context.startActivity(intent);
            }
        });

        holder.userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra(ProfileActivity.USER_NAME,
                        userName);
                context.startActivity(intent);
            }
        });




    }

    /**
     * Return the number of books to display
     * @return the number of books
     */
    @Override
    public int getItemCount() {
        return books.size();
    }


    /**
     * Inner Class ViewHolder defines all of the elements in the corresponding xml file.
     *  it allows us to set their properties during onBind.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout parentLayout;
        ImageView rightArrow;
        TextView title;
        TextView author;
        TextView status;
        ImageView bookArrow;
        TextView userName;

        /**
         * ViewHolder constructor
         * @param itemView: the current itemview
         */
        public ViewHolder(View itemView) {
            super(itemView);
            parentLayout = itemView.findViewById(R.id.book_list_parent_view);
            rightArrow = itemView.findViewById(R.id.BookListItemRightArrow);
            title = itemView.findViewById(R.id.BookListItemTitle);
            author = itemView.findViewById(R.id.BookListItemAuthor);
            status = itemView.findViewById(R.id.BookListItemStatus);
            bookArrow = itemView.findViewById(R.id.BookListItemRightArrow);
            userName = itemView.findViewById(R.id.ownerUserName);
        }

    }

    /**
     * Delete an item from the arraylist
     * @param position: the position of the book we are deleting
     */
    public void deleteItem(int position) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        mRecentlyDeletedItem = books.get(position);
        mRecentlyDeletedItemPosition = position;
        books.remove(position);
        notifyItemRemoved(position);
        databaseHelper.deleteBook(mRecentlyDeletedItem);
        showUndoSnackbar();
    }

    /**
     * Create the undo snackbar functionality to a delete can be reversed
     */
    private void showUndoSnackbar() {
        View view = null;
        if (context.getClass() == MyBooksActivity.class) {
            view = ((Activity) context).findViewById(R.id.ownerBooksRecyclerView);
        } else {
            return;
        }

        Snackbar snackbar = Snackbar.make(view, R.string.snack_bar_text,
                Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.snack_bar_undo, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                undoDelete();
            }
        });
        snackbar.show();
    }

    /**
     * Undo a delete
     */
    private void undoDelete() {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        books.add(mRecentlyDeletedItemPosition,
                mRecentlyDeletedItem);
        databaseHelper.addBookIfValid(mRecentlyDeletedItem, false);
        notifyItemInserted(mRecentlyDeletedItemPosition);
    }

}