package com.stl.skipthelibrary;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class ViewableImage {

    private String imageString;

    public ViewableImage(){
        this.imageString = "default string";
    }

    public ViewableImage(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        encode(b);
    }


    public String getImageString() {

        return imageString;
    }

    public void setImageString(String imageString) {

        this.imageString = imageString;
    }

    public void encode(byte[] byteArrayImage){

        setImageString(Base64.encodeToString(byteArrayImage, Base64.DEFAULT));
    }

    public Bitmap decode(){

        byte[] byteArray = Base64.decode(getImageString(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        return bitmap;
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
