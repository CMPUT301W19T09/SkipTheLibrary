package com.stl.skipthelibrary.BindersAndAdapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.stl.skipthelibrary.Activities.MapBoxActivity;
import com.stl.skipthelibrary.Activities.ProfileActivity;
import com.stl.skipthelibrary.DatabaseAndAPI.DatabaseHelper;
import com.stl.skipthelibrary.Entities.Book;
import com.stl.skipthelibrary.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class RequestorRecyclerAdapter extends RecyclerView.Adapter<RequestorRecyclerAdapter.ViewHolder> {
    private ArrayList<String> requestors;
    private Context context;
    private Book book;

    public RequestorRecyclerAdapter(Context context, ArrayList<String> requestors, Book book) {
        this.context = context;
        this.requestors = requestors;
        this.book = book;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.requestor_list_item, parent,false);
        return new RequestorRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String username = requestors.get(position);

        SpannableString userNameUnderLined = new SpannableString("@" + username);
        userNameUnderLined.setSpan(new UnderlineSpan(), 0, userNameUnderLined.length(), 0);

        holder.userName.setText( userNameUnderLined);

        holder.approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MapBoxActivity.class);
                intent.putExtra("username", username);
                ((Activity) context).startActivityForResult(intent, MapBoxActivity.SET_LOCATION);
            }
        });

        holder.denyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                book.getRequests().denyRequestor(username, book.getUuid(),
                        book.getDescription().getTitle());
                DatabaseHelper databaseHelper = new DatabaseHelper(context);
                databaseHelper.updateBook(book);
            }
        });

        holder.userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra(ProfileActivity.USER_NAME,
                        username);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return requestors.size();
    }

    /**
     * Inner Class ViewHolder defines all of the elements in the corresponding xml file.
     *  it allows us to set their properties during onBind.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout parentLayout;
        TextView userName;
        Button approveButton;
        Button denyButton;

        /**
         * ViewHolder constructor
         * @param itemView: the current itemview
         */
        public ViewHolder(View itemView) {
            super(itemView);
            parentLayout = itemView.findViewById(R.id.requestor_list_parent_view);
            userName = itemView.findViewById(R.id.requestor_username);
            approveButton = itemView.findViewById(R.id.approve_button_id);
            denyButton = itemView.findViewById(R.id.deny_button_id);
        }

    }
}
