package com.stl.skipthelibrary;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class ViewableImage {
    private String imageString;

    public ViewableImage(){
        this.imageString = "default string";
    }

    public ViewableImage(Uri uri) {
        Bitmap bm = BitmapFactory.decodeFile(uri.toString());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
    }


    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }

    public void encode(byte[] byteArrayImage){
        setImageString(Base64.encodeToString(byteArrayImage, Base64.DEFAULT));
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void decode(){
//        Base64.encode(getImageString(),Base64.DEFAULT);
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ViewableImage)) return false;
        ViewableImage that = (ViewableImage) o;
        return Objects.equals(getImageString(), that.getImageString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getImageString());
    }
}
