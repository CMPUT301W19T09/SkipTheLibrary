package com.stl.skipthelibrary.BindersAndAdapters;

import android.view.View;
import android.widget.ImageView;

import com.stl.skipthelibrary.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * The view holder for the arraylist of book images
 */
public class BookImageViewHolder extends RecyclerView.ViewHolder {
    public ImageView bookImage;
    public CircleImageView deleteImageButton;

    /**
     * The constructor, binds the UI items
     * @param itemView: the current itemview
     */
    public BookImageViewHolder(@NonNull View itemView) {
        super(itemView);
        bookImage = itemView.findViewById(R.id.bookImage);
        deleteImageButton = itemView.findViewById(R.id.deleteBookImageButton);
        deleteImageButton.setVisibility(View.GONE);
    }

}
