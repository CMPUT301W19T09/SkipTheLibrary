package com.stl.skipthelibrary;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BookRecyclerAdapter extends RecyclerView.Adapter<BookRecyclerAdapter.ViewHolder>{
    private static String TAG = "RecyclerViewAdapter";
    private Context context;
    private ArrayList<Book> books;

    public BookRecyclerAdapter(Context context, ArrayList<Book> books) {
        this.context = context;
        this.books = books;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_list_item, parent,false);
        return new BookRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        String title = books.get(position).getDescription().getTitle();
        String author = books.get(position).getDescription().getAuthor();
        String status = books.get(position).getRequests().getState().getBookStatus().name();

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



        holder.bookArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "clicked on book arrow");
                Intent intent = new Intent(context, ViewBookActivity.class);
                intent.putExtra("bookUUID", books.get(position).getUuid());
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return books.size();
    }


    // Inner Class ViewHolder defines all of the elements in the corresponding xml file.
    // it allows us to set their properties during onBind.
    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout parentLayout;
        ImageView rightArrow;
        TextView title;
        TextView author;
        TextView status;
        ImageButton bookArrow;

        // ViewHolder constructor
        public ViewHolder(View itemView) {
            super(itemView);
            parentLayout = itemView.findViewById(R.id.book_list_parent_view);
            rightArrow = itemView.findViewById(R.id.BookListItemRightArrow);
            title = itemView.findViewById(R.id.BookListItemTitle);
            author = itemView.findViewById(R.id.BookListItemAuthor);
            status = itemView.findViewById(R.id.BookListItemStatus);
            bookArrow = itemView.findViewById(R.id.BookListItemRightArrow);
        }

    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void setBooks(ArrayList<Book> books) {
        this.books = books;
    }
}