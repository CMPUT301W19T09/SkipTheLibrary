package com.stl.skipthelibrary.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.camerakit.CameraKitView;
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
    public static final int SCAN_BOOK = 3;

    private CameraKitView cameraKitView;

    /**
     * Bind UI elements and setup listners and begin scanning
     * @param savedInstanceState: the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        cameraKitView = findViewById(R.id.scanner_camera_view);
        cameraKitView.onStart();
    }

    /**
     * When the scan now button is pressed we the current image is captured
     * @param view: the scan now button
     */
    public void confirmScanOnClick(View view) {
        cameraKitView.captureImage(new CameraKitView.ImageCallback() {
            @Override
            public void onImage(CameraKitView cameraKitView, byte[] bytes) {
                processBitMap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            }
        });
    }

    /**
     * Processes the image received from the camera. Detect any barcodes found and send them to be
     * processed.
     * @param bitmap: the bitmap to process
     */
    private void processBitMap(Bitmap bitmap){
        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionBarcodeDetectorOptions options = new FirebaseVisionBarcodeDetectorOptions.Builder()
                .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_ALL_FORMATS).build();
        FirebaseVisionBarcodeDetector firebaseVisionBarcodeDetector = FirebaseVision.getInstance()
                .getVisionBarcodeDetector(options);
        firebaseVisionBarcodeDetector.detectInImage(firebaseVisionImage)
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionBarcode>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionBarcode> firebaseVisionBarcodes) {
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
     * activity. Otherwise, display the correct error message.
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
                cameraKitView.onStop();
                finish();
                return;
            }
        }
        Toast.makeText(this, "Found improper barcode. Please try again", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void finish() {
        cameraKitView.onStop();
        super.finish();
    }
}
