package com.stl.skipthelibrary.DatabaseAndAPI;

import android.net.Uri;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class is used to get the book's description from google's book api
 */
public class BookWebAPIHelper {
    private static final String TAG = "BookWebAPIHelper";
    private static final String BASE_URL = "https://www.googleapis.com/books/v1/volumes?";
    private static final String QUERY_PARAM = "q";
    private static final String QUERY_BASE = "=isbn:";
    private static final String PRINT_TYPE = "printType";
    private static final String PRINT_VALUE = "books";
    private static final String METHOD = "GET";

    /**
     * Empty constructor
     */
    public BookWebAPIHelper() {}

    /**
     * Get the book's JSON info by making an API call.
     * Sets up the HTTP connection and then retrieves the response
     * @param ISBN: the book's ISBN
     * @return the book's details in JSON
     */
    public static String getBookInfo(String ISBN){
        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        String bookDescriptionJSON = null;

        try {
            // construct the URL
            Uri uri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, QUERY_BASE + ISBN)
                    .appendQueryParameter(PRINT_TYPE, PRINT_VALUE).build();

            URL url = new URL(uri.toString());

            // start the connection
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(METHOD);
            httpURLConnection.connect();

            // get the response
            InputStream inputStream = httpURLConnection.getInputStream();
            StringBuffer stringBuffer = new StringBuffer();

            if (inputStream == null){
                return null;
            }

            // format the response
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
            // return the response after disconnecting safely
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
