package com.stl.skipthelibrary.BindersAndAdapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stl.skipthelibrary.R;
import com.stl.skipthelibrary.Entities.ViewableImage;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.stl.skipthelibrary.Activities.ViewBookActivity.TAG;

/**
 * The Horizontal RecyclerView Adapter used to display images for books
 */
public class HorizontalAdapter extends RecyclerView.Adapter<BookImageViewHolder> {

    private List<ViewableImage> bookImages;
    private Context mContext;
    private TextView noImages;
    private boolean editMode;

    /**
     * The constructor
     * @param bookImages: the list of images
     * @param context: the current context
     * @param noImages: the no image textview to display
     * @param editMode: whether we are in editing mode or not
     */
    public HorizontalAdapter(List<ViewableImage> bookImages, Context context, TextView noImages, boolean editMode) {
        this.bookImages = bookImages;
        this.mContext = context;
        this.noImages = noImages;
        this.editMode = editMode;
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
        if (editMode){
            holder.deleteImageButton.setVisibility(View.VISIBLE);
            holder.deleteImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bookImages.remove(position);
                    if (noImages!=null) {
                        if(bookImages.size() == 0){
                            noImages.setVisibility(View.VISIBLE);
                        }
                        else {
                            noImages.setVisibility(View.GONE);
                        }
                    }
                    notifyDataSetChanged();
                }
            });
        }
        else{
            holder.deleteImageButton.setVisibility(View.GONE);
        }
    }

    /**
     * Determines and returns the size of the recycler view
     * @return the number of elements in the recycler view
     */
    @Override
    public int getItemCount() {
        return bookImages.size();
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }
}
