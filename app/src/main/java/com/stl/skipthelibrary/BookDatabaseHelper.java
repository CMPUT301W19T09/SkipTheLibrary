package com.stl.skipthelibrary;

import android.net.Uri;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BookDatabaseHelper {
    private static final String TAG = "BookDatabaseHelper";
    private static final String BASE_URL = "https://www.googleapis.com/books/v1/volumes?";
    private static final String QUERY_PARAM = "q";
    private static final String QUERY_BASE = "=isbn:";
    private static final String PRINT_TYPE = "printType";
    private static final String PRINT_VALUE = "books";
    private static final String METHOD = "GET";

    public BookDatabaseHelper() {}

    public static String getBookInfo(String ISBN){
        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        String bookDescriptionJSON = null;

        try {
            Uri uri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, QUERY_BASE + ISBN)
                    .appendQueryParameter(PRINT_TYPE, PRINT_VALUE).build();

            URL url = new URL(uri.toString());

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(METHOD);
            httpURLConnection.connect();

            InputStream inputStream = httpURLConnection.getInputStream();
            StringBuffer stringBuffer = new StringBuffer();

            if (inputStream == null){
                return null;
            }

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line;

            while ((line = bufferedReader.readLine()) != null){
                stringBuffer.append(line + "\n");
            }
            if (stringBuffer.length() == 0){
                return null;
            }

            bookDescriptionJSON = stringBuffer.toString();
            Log.d(TAG, bookDescriptionJSON);

        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
        finally {
            if (httpURLConnection != null){
                httpURLConnection.disconnect();
            }

            if (bufferedReader!=null){
                try{
                    bufferedReader.close();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            return bookDescriptionJSON;
        }

    }
}
