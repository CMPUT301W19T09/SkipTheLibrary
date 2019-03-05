package com.stl.skipthelibrary;

import android.view.View;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class BookImageViewHolder extends RecyclerView.ViewHolder {
    public ImageView bookImage;
    public CircleImageView deleteImageButton;

    public BookImageViewHolder(@NonNull View itemView) {
        super(itemView);
        bookImage = itemView.findViewById(R.id.bookImage);
        deleteImageButton = itemView.findViewById(R.id.deleteBookImageButton);
    }

}
