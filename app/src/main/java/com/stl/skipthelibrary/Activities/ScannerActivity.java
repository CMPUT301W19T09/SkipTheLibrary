package com.stl.skipthelibrary.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.stl.skipthelibrary.R;



import java.util.List;

/**
 * The scanner activity launches the scanner and returns the ISBN of the scanned barcode
 */
public class ScannerActivity extends AppCompatActivity {
    public static final String TAG = ScannerActivity.class.getSimpleName();

    public static final int SCAN_BOOK = 3;
    public static final int SCAN = 4;

    private Uri imageUri;

    /**
     * Bind UI elements and setup listners and begin scanning
     * @param savedInstanceState: the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        startScan();
    }

    /**
     * Start the scan
     */
    public void startScan() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, ScannerActivity.SCAN);
    }

    /**
     * Processes the image received from the camera. Detect any barcodes found and send them to be
     * processed. Adapted from https://firebase.google.com/docs/ml-kit/android/read-barcodes
     * @param bitmap: the bitmap to process
     */
    private void processBitMap(Bitmap bitmap){
        Log.d(TAG, "processBitMap: PROCESS");
        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionBarcodeDetectorOptions options = new FirebaseVisionBarcodeDetectorOptions.Builder()
                .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_ALL_FORMATS).build();
        FirebaseVisionBarcodeDetector firebaseVisionBarcodeDetector = FirebaseVision.getInstance()
                .getVisionBarcodeDetector(options);
        firebaseVisionBarcodeDetector.detectInImage(firebaseVisionImage)
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionBarcode>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionBarcode> firebaseVisionBarcodes) {
                        Log.d(TAG, "onSuccess: NICE");
                        processResult(firebaseVisionBarcodes);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ScannerActivity.this, "Please try again", Toast.LENGTH_SHORT).show();

                    }
        });
    }

    /**
     * Process the result of the barcode, if the ISBN if found return the ISBN code to the calling
     * activity. Otherwise, display the correct error message. Adapted from
     * https://firebase.google.com/docs/ml-kit/android/read-barcodes
     * @param firebaseVisionBarcodes: the list of barcodes
     */
    private void processResult(List<FirebaseVisionBarcode> firebaseVisionBarcodes) {
        if (firebaseVisionBarcodes.size() == 0){
            Toast.makeText(this, "Nothing found to scan. Please try again", Toast.LENGTH_SHORT).show();
        }

        for (FirebaseVisionBarcode barcode : firebaseVisionBarcodes){
            if (barcode.getValueType() == FirebaseVisionBarcode.TYPE_ISBN){
                Intent intent = new Intent();
                intent.putExtra("ISBN", barcode.getDisplayValue());
                setResult(Activity.RESULT_OK, intent);
                finish();
                return;
            }
        }
        Toast.makeText(this, "Found improper barcode. Please try again", Toast.LENGTH_SHORT).show();
        startScan();
    }

    /**
     * Retrieve request's made to other activities.
     * @param requestCode: the request code made
     * @param resultCode: the returned code
     * @param data: the intent passed back
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if the request is from the scanner
        if (requestCode == ScannerActivity.SCAN) {
            // if successful
            if (resultCode == RESULT_OK) {
                // get the scanned ISBN
                try {
                    Bitmap thumbnail = MediaStore.Images.Media.getBitmap(
                            getContentResolver(), imageUri);
                    getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            MediaStore.Images.ImageColumns.DATA + "=?" ,
                            new String[]{ getRealPathFromURI(imageUri) });
                    processBitMap(thumbnail);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.d(TAG, "onActivityResult: Something went wrong in scan");
                finish();
            }
        } else {
            Log.d(TAG, "onActivityResult: Error in picking image");
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    /**
     * On back pressed go to MyBooksActivity.
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MyBooksActivity.class);
        startActivity(intent);
    }

}
