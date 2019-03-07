package com.stl.skipthelibrary.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.stl.skipthelibrary.R;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.util.List;

/**
 * The scanner activity launches the scanner and returns the ISBN of the scanned barcode
 */
public class ScannerActivity extends AppCompatActivity {
    public static final int SCAN_BOOK = 1;

    private CameraView cameraView;

    /**
     * Bind UI elements and setup listners and begin scanning
     * @param savedInstanceState: the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        cameraView = findViewById(R.id.scanner_camera_view);
        cameraView.start();

        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {

            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            /**
             * On retrieving an image send the corresponding bitmap to be processed
             * @param cameraKitImage: the cameraKitImage which can be used to get the bitmap
             */
            @Override
            public void onImage(CameraKitImage cameraKitImage) {
                Bitmap bitmap = Bitmap.createScaledBitmap(cameraKitImage.getBitmap(), cameraView.getWidth(),
                        cameraView.getHeight(), false);
                processBitMap(bitmap);
            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });
    }

    /**
     * When the scan now button is pressed we the current image is captured
     * @param view: the scan now button
     */
    public void confirmScanOnClick(View view) {
        cameraView.captureImage();
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
                finish();
                return;
            }
        }
        Toast.makeText(this, "Found improper barcode. Please try again", Toast.LENGTH_SHORT).show();
    }


}
