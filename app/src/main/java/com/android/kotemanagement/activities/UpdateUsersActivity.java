package com.android.kotemanagement.activities;

import android.Manifest;
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
import com.android.kotemanagement.databinding.ActivityUpdateUsersBinding;
import com.android.kotemanagement.exceptions.UserFieldBlankException;
import com.android.kotemanagement.exceptions.UserFieldException;
import com.android.kotemanagement.room.entities.Soldiers;
import com.android.kotemanagement.room.viewmodel.SoldiersViewModel;
import com.android.kotemanagement.utilities.CheckingUserInput;
import com.android.kotemanagement.utilities.ConvertImage;
import com.android.kotemanagement.utilities.PermissionCheck;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

public class UpdateUsersActivity extends AppCompatActivity {
    private ActivityUpdateUsersBinding binding;
    private SoldiersViewModel soldiersViewModel;
    private Soldiers soldier;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    private Bitmap selectedImage;
    private ArrayAdapter<CharSequence> adapter;

    private String armyNumber;
    private String firstName;
    private String lastName;
    private String rank;
    private String dob;
    private String imageAsString;
    private boolean hasUpdatedImage = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityUpdateUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.etArmyNumber.setEnabled(false);

        adapter = ArrayAdapter.createFromResource(this, R.array.army_ranks, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerRank.setAdapter(adapter);

        binding.etDob.setEnabled(false);
        soldiersViewModel = new ViewModelProvider(UpdateUsersActivity.this).get(SoldiersViewModel.class);

        try {
            getData();
        } catch (InterruptedException e) {
            Log.e("Get Data", "Error in getting data");
        }

        pickVisualMediaResultLauncher();

        binding.tlDob.setEndIconOnClickListener(v -> materialDatePicker());

        binding.btnUpload.setOnClickListener(v-> {
            binding.btnUpload.setEnabled(false);
            checkingPermissions();
        });

        binding.btnUpdateUser.setOnClickListener(v-> {
            try{
                getAndCheckDataFromUser();
                updateUserData();
            } catch(UserFieldBlankException e) {
                Snackbar snackbar = Snackbar.make(binding.getRoot(), e.message(), Snackbar.LENGTH_SHORT);
                snackbar.setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.red));
                snackbar.show();
            } catch (UserFieldException e) {
                Snackbar snackbar = Snackbar.make(binding.getRoot(), e.message(), Snackbar.LENGTH_SHORT);
                snackbar.setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.red));
                snackbar.show();
            }catch (NullPointerException e) {
                Log.d("Update User", "Null Pointer Exception Occurred");
            }
        });




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
            binding.etDob.setText(formatDate(selection));
        });

        datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
    }

    private void checkingPermissions() {
        if(!PermissionCheck.checkPermissions(this)) {
            if(Build.VERSION.SDK_INT >= 34) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES , Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED},
                        1);
            } else if(Build.VERSION.SDK_INT == 33) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 1);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        } else {
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            checkingPermissions();
        }
    }

    private void pickVisualMediaResultLauncher() {
        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {

            if(uri != null) {
                if(Build.VERSION.SDK_INT >= 28) {
                    ImageDecoder.Source imageSource = ImageDecoder.createSource(this.getContentResolver(), uri);
                    CountDownLatch latch = new CountDownLatch(1);
                    Executors.newSingleThreadExecutor().execute(() -> {
                        try {
                            selectedImage = ImageDecoder.decodeBitmap(imageSource);
                        } catch (IOException e) {
                            Log.d("Photo Picker", "IO Exception Occurred");
                        } finally {
                            latch.countDown();
                        }
                    });
                    try{
                        latch.await();
                    } catch (InterruptedException e) {
                        Log.d("Photo Picker", "Interrupted Exception Occurred");
                    }
                } else {
                    CountDownLatch latch = new CountDownLatch(1);
                    Executors.newSingleThreadExecutor().execute(() -> {
                        try{
                            selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        } catch (FileNotFoundException e) {
                            Log.d("Photo Picker", "File not found");
                        } catch (IOException e) {
                            Log.d("Photo Picker", "IO Exception Occurred");
                        } finally {
                            latch.countDown();
                        }
                    });
                    try {
                        latch.await();
                    } catch (InterruptedException e) {
                        Log.d("Photo Picker", "Interrupted Exception Occurred");
                    }
                }

                boolean isImageLessThan1MB = ConvertImage.isImageLessThan1MB(selectedImage);
                if(isImageLessThan1MB) {
                    binding.ivUpload.setImageBitmap(selectedImage);
                    binding.btnUpload.setEnabled(true);
                    hasUpdatedImage = true;
                } else {
                    binding.btnUpload.setEnabled(true);
                    Toast.makeText(this, "Image size should be less than 1MB.", Toast.LENGTH_SHORT).show();
                }

            } else {
                binding.btnUpload.setEnabled(true);
                Toast.makeText(this, "No Image has been selected.", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void getData() throws InterruptedException {
        String armyNumber = getIntent().getStringExtra("army_number");
        if(armyNumber != null) {
            CountDownLatch latch = new CountDownLatch(1);
            Executors.newSingleThreadExecutor().execute(() ->  {
                soldier = soldiersViewModel.getSoldierByArmyNumber(armyNumber);
                latch.countDown();
            });
            latch.await();
            setDataToView();
        }
    }

    private void  setDataToView() {
        Bitmap selectedImage = ConvertImage.convertToBitmap(soldier.image);
        binding.ivUpload.setImageBitmap(selectedImage);
        binding.etArmyNumber.setText(soldier.armyNumber);
        binding.etFirstName.setText(soldier.firstName);
        binding.etLastName.setText(soldier.lastName);

        int pos = adapter.getPosition(soldier.rank);

        binding.spinnerRank.setSelection(pos);
        binding.etDob.setText(soldier.dob);
    }

    private void updateUserData() {
        CountDownLatch latch = new CountDownLatch(1);
        Executors.newSingleThreadExecutor().execute(() -> {
            if(hasUpdatedImage) {
                imageAsString = ConvertImage.convertToString(selectedImage, this);
            } else {
                imageAsString = soldier.image;
            }

            Soldiers soldier = new Soldiers(
                    imageAsString,
                    armyNumber,
                    firstName,
                    lastName,
                    rank,
                    dob
            );

            soldiersViewModel.update(soldier);
            latch.countDown();

        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            Log.d("Update User", "Interrupted Exception Occurred");
        }
        Toast.makeText(this, "Data Updated", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void getAndCheckDataFromUser() throws UserFieldBlankException, UserFieldException, NullPointerException{
        armyNumber = (Objects.requireNonNull(binding.etArmyNumber.getText())).toString().trim();
        firstName = (Objects.requireNonNull(binding.etFirstName.getText())).toString().trim();
        lastName = (Objects.requireNonNull(binding.etLastName.getText())).toString().trim();
        rank = binding.spinnerRank.getSelectedItem().toString().trim();
        dob = Objects.requireNonNull(binding.etDob.getText()).toString().trim();

        if(armyNumber.isBlank() || firstName.isBlank() || lastName.isBlank() || rank.isBlank() || dob.isBlank()) {
            throw new UserFieldBlankException();
        } else if(CheckingUserInput.isArmyNumberHaveWhiteSpace(armyNumber) || CheckingUserInput.isArmyNumberHaveSpecialCharacters(armyNumber)) {
            binding.etArmyNumber.setError("Army Number should not contain any space or special characters.");
            binding.etArmyNumber.requestFocus();
            throw new UserFieldException();
        } else if (CheckingUserInput.isFirstNameHaveNumber(firstName) || CheckingUserInput.isFirstNameHaveSpecialCharacters(firstName)) {
            binding.etFirstName.setError("First Name should not contain any number or special characters.");
            binding.etFirstName.requestFocus();
            throw new UserFieldException();
        } else if (CheckingUserInput.isLastNameHaveNumber(lastName) || CheckingUserInput.isLastNameHaveSpecialCharacters(lastName)) {
            binding.etLastName.setError("Last Name should not contain any number or special characters.");
            binding.etLastName.requestFocus();
            throw new UserFieldException();
        }
    }


}