package com.stl.skipthelibrary.BindersAndAdapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stl.skipthelibrary.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RequestorRecyclerAdapter extends RecyclerView.Adapter<RequestorRecyclerAdapter.ViewHolder>{
    private final static String TAG = "RequestorRecyclerA";
    private ArrayList<String> requestors;

    public RequestorRecyclerAdapter(ArrayList<String> requestors) {
        this.requestors = requestors;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.requestor_list_item, parent,false);
        return new RequestorRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String username = requestors.get(position);
        Log.d(TAG, username );
        holder.userName.setText(username);
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
        LinearLayout parentLayout;
        TextView userName;

        /**
         * ViewHolder constructor
         * @param itemView: the current itemview
         */
        public ViewHolder(View itemView) {
            super(itemView);
            parentLayout = itemView.findViewById(R.id.requestor_list_parent_view);
            userName = itemView.findViewById(R.id.requestor_username);
        }

    }



}
