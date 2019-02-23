package com.stl.skipthelibrary;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.view.Display;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static org.junit.Assert.*;

public class ContactInfoTest {
    private ContactInfo contactInfo;
    private String phone;
    private String email;

    @Before
    public void setUp() {
        phone = "1-780-611-8111";
        email = "test@test.com";
        contactInfo = new ContactInfo(email, phone, null);
    }

    @Test
    public void testConstructor() {
        assertEquals(email, contactInfo.getEmail());
        assertEquals(phone, contactInfo.getPhoneNumber());
    }

    @Test
    public void testSetEmail() {
        String email_two = "test@email.com";
        assertEquals(email, contactInfo.getEmail());
        contactInfo.setEmail(email_two);
        assertEquals(email_two, contactInfo.getEmail());
    }

    @Test
    public void testSetContext() {
        Context context = new LoginActivity() {
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
            }
        };
        assertEquals(null, contactInfo.getContext());
        contactInfo.setContext(context);
        assertEquals(context, contactInfo.getContext());
    }

    @Test
    public void testSetPhoneNumber() {
        String phone_two = "1-111-111-1111";
        assertEquals(phone, contactInfo.getPhoneNumber());
        contactInfo.setPhoneNumber(phone_two);
        assertEquals(phone_two, contactInfo.getPhoneNumber());
    }
}