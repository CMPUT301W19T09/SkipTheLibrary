package com.stl.skipthelibrary.Helpers;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.stl.skipthelibrary.DatabaseAndAPI.BookWebAPIHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is used to get the books description, it works with the BookWebAPIHelper
 */
public class BookDescriptionReceiver extends AsyncTask<String, Void, String> {
    private final String TAG = getClass().getSimpleName();
    private String ISBN;
    private TextView title;
    private TextView author;
    private TextView description;

    /**
     * The constructor
     * @param ISBN: the book's ISBN
     * @param title: the book's title
     * @param author: the book's author
     * @param description: the book's description
     */
    public BookDescriptionReceiver(String ISBN, TextView title, TextView author, TextView description) {
        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
        this.description = description;
    }

    /**
     * Start the task that will be done in the background
     * @param strings: an unspecified amount of parameters, in this case it is the ISBN
     * @return the book info in JSON
     */
    @Override
    protected String doInBackground(String... strings) {
        return BookWebAPIHelper.getBookInfo(strings[0]);
    }

    /**
     * Converts the results of the API call in JSON to separate strings and binds them to UI
     * @param s: The results in JSON of the API call
     */
    @Override
    protected void onPostExecute(String s) {
        Log.d(TAG, s);
        super.onPostExecute(s);
        try {
            JSONObject jsonObject = new JSONObject(s);
            Log.d(TAG, jsonObject.toString());
            JSONArray jsonArray = jsonObject.getJSONArray("items");
            Log.d(TAG, jsonArray.toString());
            JSONObject volumeInfo = jsonArray.getJSONObject(0).getJSONObject("volumeInfo");
            title.setText(volumeInfo.getString("title"));
            author.setText(volumeInfo.getJSONArray("authors").getString(0));
            description.setText(volumeInfo.getString("description"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * Get the ISBN
     * @return the ISBN
     */
    public String getISBN() {
        return ISBN;
    }

    /**
     * Set the ISBN
     * @param ISBN: the ISBN
     */
    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    /**
     * Get the title
     * @return the title
     */
    public TextView getTitle() {
        return title;
    }

    /**
     * Set the title
     * @param title: the title
     */
    public void setTitle(TextView title) {
        this.title = title;
    }

    /**
     * Get the author
     * @return the author
     */
    public TextView getAuthor() {
        return author;
    }

    /**
     * Set the author
     * @param author: the author
     */
    public void setAuthor(TextView author) {
        this.author = author;
    }

    /**
     * Get the description
     * @return the description
     */
    public TextView getDescription() {
        return description;
    }

    /**
     * Sets the description
     * @param description: the description
     */
    public void setDescription(TextView description) {
        this.description = description;
    }
}
