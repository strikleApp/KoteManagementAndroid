package com.android.kotemanagement.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.kotemanagement.R;
import com.android.kotemanagement.databinding.ActivityAddUsersBinding;
import com.android.kotemanagement.utilities.PermissionCheck;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class AddUsersActivity extends AppCompatActivity {

    ActivityAddUsersBinding addUsersBinding;
    ActivityResultLauncher<Intent> activityResultLauncher;
    private Bitmap selectedImage;
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        addUsersBinding = ActivityAddUsersBinding.inflate(getLayoutInflater());
        setContentView(addUsersBinding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.army_ranks,
                android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(com.google.android.material.R.layout.support_simple_spinner_dropdown_item);
        addUsersBinding.spinnerRank.setAdapter(arrayAdapter);

        addUsersBinding.btnUpload.setOnClickListener(v -> checkingPermissions());

        addUsersBinding.tlDob.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker();
            }
        });


        //registerForActivityResultLauncher();
        pickVisualMediaResultLauncher();

    }

    private void checkingPermissions() {

        if(!PermissionCheck.checkPermissions(this)) {
            if(Build.VERSION.SDK_INT >= 34) {
                ActivityCompat.requestPermissions(AddUsersActivity.this,
                        new String[]{Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED, Manifest.permission.READ_MEDIA_IMAGES}, 1);
            } else if(Build.VERSION.SDK_INT >= 33) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 1);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
        } else {
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
            //activityResultLauncher.launch(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
        }


    }

    private String formatDate(long timeInMillis) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return formatter.format(timeInMillis);
    }

    private void materialDatePicker() {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Date Of Birth")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();
        datePicker.addOnPositiveButtonClickListener(selection -> {
            addUsersBinding.etDob.setText(formatDate(selection));
        });
        datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            checkingPermissions();
        }
    }

    private void registerForActivityResultLauncher() {
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result ->{
            int resultCode = result.getResultCode();
            Intent imageData = result.getData();

            if(resultCode == RESULT_OK && imageData != null) {
                Uri imageUri = imageData.getData();
                if (imageUri != null) {
                    if (Build.VERSION.SDK_INT >= 28) {
                        ImageDecoder.Source imageSource = ImageDecoder.createSource(this.getContentResolver(), imageUri);
                        try {
                            selectedImage = ImageDecoder.decodeBitmap(imageSource);
                            addUsersBinding.ivUpload.setImageBitmap(selectedImage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                            addUsersBinding.ivUpload.setImageBitmap(selectedImage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        });

    }

    private void pickVisualMediaResultLauncher() {
        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri-> {
            if(uri != null) {
                //Log.d("Photo Picker", "Selected URI: " + uri);
                if(Build.VERSION.SDK_INT >= 28) {
                    ImageDecoder.Source imageSource = ImageDecoder.createSource(this.getContentResolver(), uri);
                    try {
                        selectedImage = ImageDecoder.decodeBitmap(imageSource);
                        addUsersBinding.ivUpload.setImageBitmap(selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        addUsersBinding.ivUpload.setImageBitmap(selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                Log.d("Photo Picker", "No media selected");
                Toast.makeText(this, "No Images selected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}