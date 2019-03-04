package com.stl.skipthelibrary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HorizontalAdapter extends RecyclerView.Adapter<BookImageViewHolder> {

    private List<ViewableImage> bookImages;
    private Context mContext;

    public HorizontalAdapter(List<ViewableImage> bookImages, Context context) {
        this.bookImages = bookImages;
        this.mContext = context;
    }

    @NonNull
    @Override
    public BookImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.book_image_list_item, parent, false);
        return new BookImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BookImageViewHolder holder, final int position) {
        holder.bookImage.setImageBitmap(bookImages.get(position).decode());
        holder.deleteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookImages.remove(position);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return bookImages.size();
    }
}
