package com.example.test2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.opencv.android.OpenCVLoader;

public class MainActivity extends AppCompatActivity {

    ImageView trainingPhoto;
    ImageView testPhoto;
    ImageView resultPhoto;

    Button processBtn;

    EditText resultText;

    private static final int CAMERA_REQUEST_CODE = 201;
    private static final int STORAGE_REQUEST_CODE = 202;
    private static final int IMAGE_PICK_GALLERY_CODE = 203;
    private static final int IMAGE_PICK_CEMERA_CODE = 204;

    String cameraPremission[];
    String storagePremission[];

    Uri image_uri;
    boolean img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        trainingPhoto = findViewById(R.id.trainingImage);
        testPhoto = findViewById(R.id.testImage);
        resultPhoto = findViewById(R.id.resultImage);
        processBtn = findViewById(R.id.resultBtn);
        resultText = findViewById(R.id.resultText);

        // camera premission
        cameraPremission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePremission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        trainingPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img = true;
                showImageImportDialog();
            }
        });

        testPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img = false;
                showImageImportDialog();
            }
        });

        OpenCVLoader.initDebug();
    }


    private void showImageImportDialog() {
        // items to display in dialog
        String[] items = {" Camera", " Gallery"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        // set Title
        dialog.setTitle("Select Image");
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){
                    // camera option clicked
                    if(!checkCameraPremission()){
                        // camera premission not allowed, request it
                        reqestCameraPremission();
                    }else{
                        // premission allowed, take picture
                        pickCamera();
                    }
                }else if (which == 1){
                    // gallery option clicked
                    if(!checkStoragePremission()){
                        // camera premission not allowed, request it
                        reqestStoragePremission();
                    }else{
                        // premission allowed, take picture
                        pickGallery();
                    }
                }
            }
        });
        // show dialog
        dialog.create().show();
    }

    private void pickGallery() {
        // intent to pick image from gallery
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_GALLERY_CODE);
    }

    private void pickCamera() {
        // intent to take image form camera and store it
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "NewPic"); // title of the photo
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image to text"); // description
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CEMERA_CODE);
    }

    private void reqestStoragePremission() {
        ActivityCompat.requestPermissions(this, storagePremission, STORAGE_REQUEST_CODE);
    }

    private boolean checkStoragePremission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void reqestCameraPremission() {
        ActivityCompat.requestPermissions(this, cameraPremission, CAMERA_REQUEST_CODE);
    }

    private boolean checkCameraPremission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result&&result1;
    }

    // handle permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CAMERA_REQUEST_CODE:
                if(grantResults.length > 0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted&&writeStorageAccepted){
                        pickCamera();
                    }else{
                        Toast.makeText(this,"Perrmision to access camera denied!", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case STORAGE_REQUEST_CODE:
                if(grantResults.length > 0){
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(writeStorageAccepted){
                        pickGallery();
                    }else{
                        Toast.makeText(this,"Perrmision to access gallery denied!", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                // got image from gallery to crop
                CropImage.activity(data.getData()).setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }
            if (requestCode == IMAGE_PICK_CEMERA_CODE) {
                // got image from camera to crop
                CropImage.activity(image_uri).setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }
        }
        // get cropped image
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            //Toast.makeText(this, "" + result.getUri(), Toast.LENGTH_LONG).show();
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                if(img){
                    trainingPhoto.setImageURI(resultUri);
                }else{
                    testPhoto.setImageURI(resultUri);
                }


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                // display error if image crop fails
                Exception error = result.getError();
                Toast.makeText(this, "" + error, Toast.LENGTH_LONG).show();
            }
        }
    }
}
