package com.stl.skipthelibrary;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BookDescriptionReceiver extends AsyncTask<String, Void, String> {
    private final String TAG = getClass().getSimpleName();
    private String ISBN;
    private TextView title;
    private TextView author;
    private TextView description;

    public BookDescriptionReceiver(String ISBN, TextView title, TextView author, TextView description) {
        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
        this.description = description;
    }

    @Override
    protected String doInBackground(String... strings) {
        return BookDatabaseHelper.getBookInfo(strings[0]);
    }

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

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public TextView getTitle() {
        return title;
    }

    public void setTitle(TextView title) {
        this.title = title;
    }

    public TextView getAuthor() {
        return author;
    }

    public void setAuthor(TextView author) {
        this.author = author;
    }

    public TextView getDescription() {
        return description;
    }

    public void setDescription(TextView description) {
        this.description = description;
    }
}
