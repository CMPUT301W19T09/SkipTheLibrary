package com.stl.skipthelibrary;


import android.net.Uri;


import org.junit.Before;
import org.junit.Test;


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


}
