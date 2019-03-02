package com.stl.skipthelibrary;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseError;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BookRecyclerAdapter extends FirebaseRecyclerAdapter<Book, BookRecyclerAdapter.ViewHolder> {
    final static public String TAG = "BookRecyclerAdapter";
    private Context context;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public BookRecyclerAdapter(Context context, @NonNull FirebaseRecyclerOptions<Book> options) {
        super(options);
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_list_item, parent,false);
        return new BookRecyclerAdapter.ViewHolder(view);
    }


    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Book book) {
        String title = book.getDescription().getTitle();
        String author = book.getDescription().getAuthor();
        String status = book.getRequests().getState().getBookStatus().name();

        holder.title.setText(title);
        holder.author.setText(author);
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
    }

    @Override
    public void onDataChanged() {
        // Called each time there is a new data snapshot. You may want to use this method
        // to hide a loading spinner or check for the "no documents" state and update your UI.
        // ...
        //TODO:Add loading spinner here
    }

    @Override
    public void onError(DatabaseError e) {
        // Called when there is an error getting data. You may want to update
        // your UI to display an error message to the user.
        // ...
        Log.d(TAG, "onError: DATABASE ERROR");
    }

    // Inner Class ViewHolder defines all of the elements in the corresponding xml file.
    // it allows us to set their properties during onBind.
    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout parentLayout;
        ImageView rightArrow;
        TextView title;
        TextView author;
        TextView status;

        // ViewHolder constructor
        public ViewHolder(View itemView) {
            super(itemView);
            parentLayout = itemView.findViewById(R.id.book_list_parent_view);
            rightArrow = itemView.findViewById(R.id.BookListItemRightArrow);
            title = itemView.findViewById(R.id.BookListItemTitle);
            author = itemView.findViewById(R.id.BookListItemAuthor);
            status = itemView.findViewById(R.id.BookListItemStatus);
        }

    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
