package com.stl.skipthelibrary;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class ViewableImage {
    private String imageString;

    public ViewableImage() {
        setImageString("");
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
}
