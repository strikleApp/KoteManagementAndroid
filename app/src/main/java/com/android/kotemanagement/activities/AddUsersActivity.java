package com.android.kotemanagement.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
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
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.android.kotemanagement.R;
import com.android.kotemanagement.databinding.ActivityAddUsersBinding;
import com.android.kotemanagement.exceptions.UserFieldBlankException;
import com.android.kotemanagement.exceptions.UserFieldException;
import com.android.kotemanagement.exceptions.UsersExistsException;
import com.android.kotemanagement.room.entities.Soldiers;
import com.android.kotemanagement.room.viewmodel.SoldiersViewModel;
import com.android.kotemanagement.utilities.CheckingUserInput;
import com.android.kotemanagement.utilities.ConvertImage;
import com.android.kotemanagement.utilities.PermissionCheck;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AddUsersActivity extends AppCompatActivity {

    ActivityAddUsersBinding addUsersBinding;
    ActivityResultLauncher<Intent> activityResultLauncher;
    private Bitmap selectedImage = null;
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    SoldiersViewModel soldiersViewModel;
    private Executor executor = Executors.newSingleThreadExecutor();
    private boolean hasSelectedImage = false;
    private Soldiers soldier;

    private String imageAsString;
    private String armyNumber;
    private String firstName;
    private String lastName;
    private String rank;
    private String dob;

    private boolean isImageLessThan1MB;

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

        addUsersBinding.etDob.setEnabled(false);

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.army_ranks,
                android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        addUsersBinding.spinnerRank.setAdapter(arrayAdapter);

        addUsersBinding.btnUpload.setOnClickListener(v -> {
            addUsersBinding.btnUpload.setEnabled(false);
            checkingPermissions();
        });

        addUsersBinding.tlDob.setEndIconOnClickListener(v ->
                materialDatePicker());


        

        //registerForActivityResultLauncher();
        pickVisualMediaResultLauncher();

        //Initializing Soldiers View Model
        soldiersViewModel = new ViewModelProvider(AddUsersActivity.this).get(SoldiersViewModel.class);
        //inserting data
        addUsersBinding.btnAddUser.setOnClickListener(v -> {
            try {
                getAndCheckDataFromUser();
                insertDataToDatabase();
            } catch (UserFieldBlankException e) {
                Snackbar snackbar = Snackbar.make(addUsersBinding.getRoot(), e.message(), Snackbar.LENGTH_SHORT);
                snackbar.setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.red));
                snackbar.show();
            } catch (UserFieldException e) {
                Snackbar snackbar = Snackbar.make(addUsersBinding.getRoot(), e.message(), Snackbar.LENGTH_SHORT);
                snackbar.setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.red));
                snackbar.show();
            } catch (UsersExistsException e) {
                Snackbar snackbar = Snackbar.make(addUsersBinding.getRoot(), e.message(), Snackbar.LENGTH_SHORT);
                snackbar.setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.red));
                snackbar.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    private void checkingPermissions() {

        if (!PermissionCheck.checkPermissions(this)) {
            if (Build.VERSION.SDK_INT >= 34) {
                ActivityCompat.requestPermissions(AddUsersActivity.this,
                        new String[]{Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED, Manifest.permission.READ_MEDIA_IMAGES}, 1);
            } else if (Build.VERSION.SDK_INT >= 33) {
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
                .setInputMode(MaterialDatePicker.INPUT_MODE_TEXT)
                .setTextInputFormat(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()))
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
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            checkingPermissions();
        }
    }

    private void pickVisualMediaResultLauncher() {
        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                //Log.d("Photo Picker", "Selected URI: " + uri);
                if (Build.VERSION.SDK_INT >= 28) {
                    ImageDecoder.Source imageSource = ImageDecoder.createSource(this.getContentResolver(), uri);

                    Executor executor = Executors.newSingleThreadExecutor();
                    CountDownLatch latch = new CountDownLatch(1);
                    executor.execute(() -> {
                        try {
                            selectedImage = ImageDecoder.decodeBitmap(imageSource);
                        } catch (IOException e) {
                            Log.d("Photo Picker", "Exception Ocurred");
                            e.printStackTrace();
                        } finally {
                            latch.countDown();
                        }
                    });

                    try {
                        latch.await();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    addUsersBinding.btnUpload.setEnabled(true);
                } else {
                    Executor executor = Executors.newSingleThreadExecutor();
                    CountDownLatch latch = new CountDownLatch(1);
                    executor.execute(() -> {
                        try {
                            selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                            latch.countDown();
                        } catch (IOException e) {
                            Log.d("Photo Picker", "Exception Ocurred");
                        }
                    });
                    try {
                        latch.await();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    addUsersBinding.btnUpload.setEnabled(true);
                }

                Executor executor = Executors.newSingleThreadExecutor();
                CountDownLatch latch = new CountDownLatch(1);

                executor.execute(() -> {
                    isImageLessThan1MB = ConvertImage.isImageLessThan1MB(selectedImage);
                    latch.countDown();
                });
                try {
                    latch.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (isImageLessThan1MB) {
                    addUsersBinding.ivUpload.setImageBitmap(selectedImage);
                    hasSelectedImage = true;
                    addUsersBinding.btnUpload.setEnabled(true);
                } else {
                    Toast.makeText(this, "Image Size should be less than 1MB", Toast.LENGTH_SHORT).show();
                    addUsersBinding.btnUpload.setEnabled(true);
                }
            } else {
                Log.d("Photo Picker", "No media selected");
                Toast.makeText(this, "No Images selected", Toast.LENGTH_SHORT).show();
                addUsersBinding.btnUpload.setEnabled(true);
            }
        });
    }

    private void insertDataToDatabase() throws InterruptedException {
        Executor executor = Executors.newSingleThreadExecutor();
        CountDownLatch latch = new CountDownLatch(1);
        executor.execute(() -> {

            imageAsString = ConvertImage.convertToString(selectedImage, this);

            Soldiers soldier = new Soldiers(
                    imageAsString,
                    armyNumber,
                    firstName,
                    lastName,
                    rank,
                    dob
            );
            soldiersViewModel.insert(soldier);
            latch.countDown();
        });
        latch.await();
        addUsersBinding.btnAddUser.setEnabled(true);
        Toast.makeText(this, "Data Inserted", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(AddUsersActivity.this, ViewSoldiersActivity.class);
        startActivity(intent);
        finish();
    }

    private void getAndCheckDataFromUser() throws UserFieldBlankException, UserFieldException, UsersExistsException, NullPointerException {
        armyNumber = (Objects.requireNonNull(addUsersBinding.etArmyNumber.getText())).toString().trim();
        firstName = (Objects.requireNonNull(addUsersBinding.etFirstName.getText())).toString().trim();
        lastName = (Objects.requireNonNull(addUsersBinding.etLastName.getText())).toString().trim();
        rank = addUsersBinding.spinnerRank.getSelectedItem().toString().trim();
        dob = Objects.requireNonNull(addUsersBinding.etDob.getText()).toString().trim();

        if (armyNumber.isBlank() || firstName.isBlank() || lastName.isBlank() || rank.isBlank() || dob.isBlank() || !hasSelectedImage) {
            throw new UserFieldBlankException();
        } else if (CheckingUserInput.isArmyNumberHaveWhiteSpace(armyNumber) || CheckingUserInput.isArmyNumberHaveSpecialCharacters(armyNumber)) {
            addUsersBinding.etArmyNumber.setError("Army Number should not contain any space or special characters.");
            addUsersBinding.etArmyNumber.requestFocus();
            throw new UserFieldException();
        } else if (CheckingUserInput.isFirstNameHaveNumber(firstName) || CheckingUserInput.isFirstNameHaveSpecialCharacters(firstName)) {
            addUsersBinding.etFirstName.setError("First Name should not contain any number or special characters.");
            addUsersBinding.etFirstName.requestFocus();
            throw new UserFieldException();
        } else if (CheckingUserInput.isLastNameHaveNumber(lastName) || CheckingUserInput.isLastNameHaveSpecialCharacters(lastName)) {
            addUsersBinding.etLastName.setError("Last Name should not contain any number or special characters.");
            addUsersBinding.etLastName.requestFocus();
            throw new UserFieldException();
        } else {
            try {
                if (doesUserExists()) {
                    throw new UsersExistsException();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                Toast.makeText(this, "Insertion Interrupted", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private boolean doesUserExists() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            soldier = soldiersViewModel.getSoldierByArmyNumber(armyNumber);
            latch.countDown();
        });
        latch.await();
        return soldier != null;
    }

}