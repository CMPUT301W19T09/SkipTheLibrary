package com.stl.skipthelibrary.DatabaseAndAPI;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.stl.skipthelibrary.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class is used to get the book's rating from goodreads api
 */
public class GoodReadsAPIHelper {
    private static final String TAG = GoodReadsAPIHelper.class.getSimpleName();
    private static final String BASE_URL = "https://www.goodreads.com/book/review_counts.json?";
    private static final String KEY_QUERY_PARAM = "key";
    private static final String ISBN_QUERY_PARAM = "isbns";
    private static final String METHOD = "GET";

    /**
     * Constructor
     */
    public GoodReadsAPIHelper() {}

    /**
     * Get the book's JSON rating info by making an API call.
     * Sets up the HTTP connection and then retrieves the response
     * @param ISBN: the book's ISBN
     * @return the book's details in JSON
     */
    public static String getBookInfo(Context context, String ISBN){
        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        String bookReviewJSON = null;

        try {
            // construct the URL
            Uri uri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(KEY_QUERY_PARAM, context.getResources().getString(R.string.goodreads_api_key))
                    .appendQueryParameter(ISBN_QUERY_PARAM, ISBN).build();

            URL url = new URL(uri.toString());
            Log.d(TAG, "getBookInfo: "+uri.toString());

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

            bookReviewJSON = stringBuffer.toString();
            Log.d(TAG, bookReviewJSON);

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

            return bookReviewJSON;
        }

    }
}
