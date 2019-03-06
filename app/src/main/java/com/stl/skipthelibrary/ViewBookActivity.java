package com.stl.skipthelibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class ViewBookActivity extends AppCompatActivity {
    final public static String TAG = ViewBookActivity.class.getSimpleName();

    private Button button;
    private View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_details);


        button = (Button) findViewById(R.id.HandOffButton);
        button.setText("lend");
    }

    public void handOffButtonOnClick(View view){
        Intent intent = new Intent(this, ScannerActivity.class);
        startActivityForResult(intent, ScannerActivity.SCAN_BOOK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if(requestCode == ScannerActivity.SCAN_BOOK) {
            if (resultCode == RESULT_OK){

            }else{
                Log.d(TAG, "onActivityResult: Something went wrong in scan");
            }
        }
    }
}
