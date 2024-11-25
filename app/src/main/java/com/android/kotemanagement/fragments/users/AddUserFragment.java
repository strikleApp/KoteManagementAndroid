package com.android.kotemanagement.fragments.users;


import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.kotemanagement.R;
import com.android.kotemanagement.authentication.FingerprintAuthenticator;
import com.android.kotemanagement.databinding.FragmentAddUserBinding;
import com.android.kotemanagement.exceptions.UserFieldBlankException;
import com.android.kotemanagement.exceptions.UserFieldException;
import com.android.kotemanagement.exceptions.UsersExistsException;
import com.android.kotemanagement.fragments.records.RecordFunctions;
import com.android.kotemanagement.room.entities.Soldiers;
import com.android.kotemanagement.room.viewmodel.RecordsViewModel;
import com.android.kotemanagement.room.viewmodel.SoldiersViewModel;
import com.android.kotemanagement.utilities.CheckingUserInput;
import com.android.kotemanagement.utilities.ConvertImage;
import com.android.kotemanagement.utilities.PermissionCheck;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AddUserFragment extends Fragment {


    FragmentAddUserBinding binding;
    SoldiersViewModel soldiersViewModel;
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    private RecordsViewModel recordsViewModel;

    private Bitmap selectedImage = null;
    private boolean hasSelectedImage = false;
    private Soldiers soldier;
    private boolean isImageLessThan1MB;

    private String imageAsString;
    private String armyNumber;
    private String firstName;
    private String lastName;
    private String rank;
    private String dob;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pickVisualMediaResultLauncher();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentAddUserBinding.inflate(inflater, container, false);


        soldiersViewModel = new ViewModelProvider(this).get(SoldiersViewModel.class);
        recordsViewModel = new ViewModelProvider(this).get(RecordsViewModel.class);

        //spinner
        binding.etDob.setEnabled(false);

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.army_ranks, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        binding.spinnerRank.setAdapter(arrayAdapter);

        binding.tlDob.setEndIconOnClickListener(v -> materialDatePicker());


        binding.btnUpload.setOnClickListener(v -> {

            if (!PermissionCheck.checkPermissions(requireContext())) {
                Snackbar snackbar = Snackbar.make(binding.getRoot(), "Storage Permission is required for this app to function. Allow this in settings.", Snackbar.LENGTH_SHORT);
                snackbar.setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red));
                snackbar.show();
            } else {
                binding.btnUpload.setEnabled(false);
                pickMedia.launch(new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build());
            }

        });


        binding.btnAddUser.setOnClickListener(v -> {
            try {
                getAndCheckDataFromUser();
                fingerprintAuth();
            } catch (UserFieldBlankException e) {

                Snackbar snackbar = Snackbar.make(binding.getRoot(), e.message(), Snackbar.LENGTH_SHORT);
                snackbar.setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red));
                snackbar.show();
            } catch (UserFieldException e) {
                Snackbar snackbar = Snackbar.make(binding.getRoot(), e.message(), Snackbar.LENGTH_SHORT);
                snackbar.setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red));
                snackbar.show();
            } catch (UsersExistsException e) {
                Snackbar snackbar = Snackbar.make(binding.getRoot(), e.message(), Snackbar.LENGTH_SHORT);
                snackbar.setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red));
                snackbar.show();
            }

        });


        return binding.getRoot();
    }

    private void fingerprintAuth() {
        // Initialize FingerprintAuthenticator
        FingerprintAuthenticator fingerprintAuthenticator = new FingerprintAuthenticator(getContext());

        fingerprintAuthenticator.authenticate(getActivity(), isSuccess -> {
            if (isSuccess) {
                // Authentication success
                Toast.makeText(getContext(), "Authentication Successful", Toast.LENGTH_SHORT).show();
                try {
                    insertDataToDatabase();
                    RecordFunctions.addUserRecord(armyNumber, firstName + " " + lastName, rank, recordsViewModel);
                    binding.ivUpload.setImageResource(R.drawable.soldierimage);
                    binding.etArmyNumber.setText("");
                    binding.etDob.setText("");
                    binding.etFirstName.setText("");
                    binding.etLastName.setText("");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }


            } else {
                // Authentication failed
                Toast.makeText(getContext(), "Authentication Failed", Toast.LENGTH_SHORT).show();
                // Handle failure logic
            }
        });
    }

    private String formatDate(long timeInMillis) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return formatter.format(timeInMillis);
    }

    private void materialDatePicker() {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Date Of Birth").setSelection(MaterialDatePicker.todayInUtcMilliseconds()).setInputMode(MaterialDatePicker.INPUT_MODE_TEXT).setTextInputFormat(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())).build();
        datePicker.addOnPositiveButtonClickListener(selection -> {
            binding.etDob.setText(formatDate(selection));
        });

        datePicker.show(requireActivity().getSupportFragmentManager(), "DATE_PICKER");
    }

    private void insertDataToDatabase() throws InterruptedException {
        Executor executor = Executors.newSingleThreadExecutor();
        CountDownLatch latch = new CountDownLatch(1);
        executor.execute(() -> {

            imageAsString = ConvertImage.convertToString(selectedImage, requireContext());

            Soldiers soldier = new Soldiers(imageAsString, armyNumber, firstName, lastName, rank, dob);
            soldiersViewModel.insert(soldier);
            latch.countDown();
        });
        latch.await();
        binding.btnAddUser.setEnabled(true);
        Toast.makeText(requireContext(), "Data Inserted", Toast.LENGTH_SHORT).show();

    }


    private void getAndCheckDataFromUser() throws UserFieldBlankException, UserFieldException, UsersExistsException, NullPointerException {
        armyNumber = (Objects.requireNonNull(binding.etArmyNumber.getText())).toString().trim();
        firstName = (Objects.requireNonNull(binding.etFirstName.getText())).toString().trim();
        lastName = (Objects.requireNonNull(binding.etLastName.getText())).toString().trim();
        rank = binding.spinnerRank.getSelectedItem().toString().trim();
        dob = Objects.requireNonNull(binding.etDob.getText()).toString().trim();

        if (armyNumber.isBlank() || firstName.isBlank() || lastName.isBlank() || rank.isBlank() || dob.isBlank() || !hasSelectedImage) {
            throw new UserFieldBlankException();
        } else if (CheckingUserInput.isArmyNumberHaveWhiteSpace(armyNumber) || CheckingUserInput.isArmyNumberHaveSpecialCharacters(armyNumber)) {
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
        } else {
            try {
                // Age validation
                if (!isUserAbove18(dob)) {
                    Toast.makeText(requireContext(), "Soldier should be 18 years or more.", Toast.LENGTH_SHORT).show();
                    throw new UserFieldException();
                }

                if (doesUserExists()) {
                    throw new UsersExistsException();
                }
            } catch (InterruptedException e) {
                Toast.makeText(requireContext(), "Insertion Interrupted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isUserAbove18(String dob) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            LocalDate birthDate = LocalDate.parse(dob, formatter);
            LocalDate currentDate = LocalDate.now();
            Period period = Period.between(birthDate, currentDate);
            int age = period.getYears();
            return age >= 18;
        } catch (DateTimeParseException e) {
            Toast.makeText(requireContext(), "Invalid Date Format. Please use dd/MM/yyyy.", Toast.LENGTH_SHORT).show();
            return false;
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

    private void pickVisualMediaResultLauncher() {
        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                //Log.d("Photo Picker", "Selected URI: " + uri);
                if (Build.VERSION.SDK_INT >= 28) {
                    ImageDecoder.Source imageSource = ImageDecoder.createSource(requireActivity().getContentResolver(), uri);

                    Executor executor = Executors.newSingleThreadExecutor();
                    CountDownLatch latch = new CountDownLatch(1);
                    executor.execute(() -> {
                        try {
                            selectedImage = ImageDecoder.decodeBitmap(imageSource);
                        } catch (IOException e) {
                            Log.d("Photo Picker", "Exception Occurred");
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
                    binding.btnUpload.setEnabled(true);
                } else {
                    Executor executor = Executors.newSingleThreadExecutor();
                    CountDownLatch latch = new CountDownLatch(1);
                    executor.execute(() -> {
                        try {
                            selectedImage = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri);
                            latch.countDown();
                        } catch (IOException e) {
                            Log.d("Photo Picker", "Exception Occurred");
                        }
                    });
                    try {
                        latch.await();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    binding.btnUpload.setEnabled(true);
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
                    binding.ivUpload.setImageBitmap(selectedImage);
                    hasSelectedImage = true;
                    binding.btnUpload.setEnabled(true);
                } else {
                    Toast.makeText(requireContext(), "Image Size should be less than 1MB", Toast.LENGTH_SHORT).show();
                    binding.btnUpload.setEnabled(true);
                }
            } else {
                Log.d("Photo Picker", "No media selected");
                Toast.makeText(requireContext(), "No Images selected", Toast.LENGTH_SHORT).show();
                binding.btnUpload.setEnabled(true);
            }
        });
    }
}
