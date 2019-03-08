package com.stl.skipthelibrary.BindersAndAdapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.stl.skipthelibrary.Activities.MapBoxActivity;
import com.stl.skipthelibrary.Activities.NotificationActivity;
import com.stl.skipthelibrary.Activities.ProfileActivity;
import com.stl.skipthelibrary.Entities.Location;
import com.stl.skipthelibrary.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import static android.app.Activity.RESULT_OK;

public class RequestorRecyclerAdapter extends RecyclerView.Adapter<RequestorRecyclerAdapter.ViewHolder> {
    private final static String TAG = "RequestorRecyclerA";
    public static final int REQUEST_CODE = 1;
    private ArrayList<String> requestors;
    private Context context;

    public RequestorRecyclerAdapter(Context context, ArrayList<String> requestors) {
        this.context = context;
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


        holder.approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MapBoxActivity.class);
                ((Activity) context).startActivityForResult(intent, MapBoxActivity.SET_LOCATION);
            }
        });

        holder.denyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == MapBoxActivity.SET_LOCATION) {
            if (requestCode == RESULT_OK){
                String locationString = data.getStringExtra("Location");
                Gson gson = new Gson();
                Location location = gson.fromJson(locationString, Location.class);
                Toast.makeText(context, location.getLatitude() + " \n " + location.getLongitude(), Toast.LENGTH_SHORT).show();
            }
        }
    }



}
