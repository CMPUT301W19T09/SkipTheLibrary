package com.stl.skipthelibrary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * The Horizontal RecyclerView Adapter used to display images for books
 */
public class HorizontalAdapter extends RecyclerView.Adapter<BookImageViewHolder> {

    private List<ViewableImage> bookImages;
    private Context mContext;

    /**
     * The constructor
     * @param bookImages: the list of images to display
     * @param context: the current context
     */
    public HorizontalAdapter(List<ViewableImage> bookImages, Context context) {
        this.bookImages = bookImages;
        this.mContext = context;
    }

    /**
     * Create the view holder and return in
     * @param parent: the parent viewgroup
     * @param viewType: the id of the viewtype
     * @return a new bookimage viewholder
     */
    @NonNull
    @Override
    public BookImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.book_image_list_item, parent, false);
        return new BookImageViewHolder(v);
    }

    /**
     * Bind the UI elements and allow deletion of images
     * @param holder: the viewholder we use to bind data to
     * @param position: the position of the current element in the list of images
     */
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

    /**
     * Determines and returns the size of the recycler view
     * @return the number of elements in the recycler view
     */
    @Override
    public int getItemCount() {
        return bookImages.size();
    }
}
