package com.stl.skipthelibrary;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.stl.skipthelibrary.Entities.ViewableImage;

import org.junit.Before;
import org.junit.Test;


import java.util.Base64;

import static org.junit.Assert.*;

public class ViewableImageTest {
    private ViewableImage viewableImage;

    @Before
    public void setUp() {

        viewableImage = new ViewableImage();
    }

    @Test
    public void testSetImageString(){
        viewableImage.setImageString("test image string");
        assertEquals("test image string", viewableImage.getImageString());
    }

    @Test
    public void testEquals() {
        ViewableImage secondImage = new ViewableImage();
        assertTrue(viewableImage.equals(secondImage));
        secondImage.setImageString("test image string");
        assertFalse(viewableImage.equals(secondImage));
        secondImage = viewableImage;
        assertTrue(viewableImage.equals(secondImage));
        secondImage = null;
        assertFalse(viewableImage.equals(secondImage));
    }
}
