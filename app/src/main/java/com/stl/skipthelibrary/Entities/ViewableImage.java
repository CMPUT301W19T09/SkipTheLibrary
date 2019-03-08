package com.stl.skipthelibrary.Entities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Objects;

/**
 * This class defines a viewable image
 */
public class ViewableImage {

    private String imageString;

    /**
     * The empty constructor
     */
    public ViewableImage() {
        setImageString("default string");
    }

    /**
     * The constructor that takes in a bitmap
     * @param bitmap: the bitmap of the image
     */
    public ViewableImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        encode(b);
    }


    /**
     * Gets the image string
     * @return the image string
     */
    public String getImageString() {
        return imageString;
    }

    /**
     * Sets the image string
     * @param imageString: the image string
     */
    public void setImageString(String imageString) {
        this.imageString = imageString;
    }

    /**
     * Encode the byte image array
     * @param byteArrayImage: the byte image array to encode
     */
    public void encode(byte[] byteArrayImage){
        setImageString(Base64.encodeToString(byteArrayImage, Base64.DEFAULT));
    }

    /**
     * Decode the byte array into a bitmap
     * @return the bitmap
     */
    public Bitmap decode(){
        byte[] byteArray = Base64.decode(getImageString(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        return bitmap;
    }

    /**
     * Get a bitmap from URI
     * @param uri: the URI to get the bitmap from
     * @param context: the current context
     * @return the bitmap
     * @throws IOException: An IO Exception
     */
    public static Bitmap getBitmapFromUri(Uri uri, Context context) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                context.getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    /**
     * Determines if the viewable image is identical to another object
     * @param o: An object to compare the viewable image to
     * @return true if o is identical to the current viewable image
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ViewableImage)) return false;
        ViewableImage that = (ViewableImage) o;
        return Objects.equals(getImageString(), that.getImageString());
    }

    /**
     * Calculate and return the viewable image's hashcode
     * @return the viewable image's hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(getImageString());
    }
}
