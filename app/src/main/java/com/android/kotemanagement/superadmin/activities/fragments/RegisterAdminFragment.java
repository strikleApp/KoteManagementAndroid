package com.android.kotemanagement.superadmin.activities.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.kotemanagement.R;
import com.android.kotemanagement.authentication.FingerprintAuthenticator;
import com.android.kotemanagement.databinding.FragmentRegisterSuperAdminBinding;
import com.android.kotemanagement.room.entities.Admin;
import com.android.kotemanagement.room.viewmodel.AdminViewModel;
import com.android.kotemanagement.superadmin.activities.SuperAdminHome;
import com.android.kotemanagement.utilities.ConvertImage;
import com.android.kotemanagement.utilities.PermissionCheck;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.Objects;

public class RegisterAdminFragment extends Fragment {
    private String image;
    private FragmentRegisterSuperAdminBinding binding;
    private AdminViewModel adminViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterSuperAdminBinding.inflate(inflater, container, false);

        adminViewModel = new ViewModelProvider(this).get(AdminViewModel.class);
        checkingPermissions();
        binding.btnUploadButton.setOnClickListener(v -> {
            // Check if permission is granted first
            if (PermissionCheck.checkPermissions(getActivity())) {
                // Open the gallery
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");  // For picking images
                startActivityForResult(intent, 100);
            } else {
                checkingPermissions();
            }
        });


        binding.submitButton.setOnClickListener(v -> handleSubmit());

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                binding.photoImageView.setImageURI(selectedImageUri);
                Bitmap bitmap = getBitmapFromUri(selectedImageUri);
                image = ConvertImage.convertToString(bitmap, getContext());
            }
        }
    }


    public Bitmap getBitmapFromUri(Uri selectedImageUri) {
        Bitmap bitmap = null;
        if (selectedImageUri != null) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    // For Android 9 (Pie) and above (using ImageDecoder)
                    ImageDecoder.Source source = ImageDecoder.createSource(requireActivity().getContentResolver(), selectedImageUri);
                    bitmap = ImageDecoder.decodeBitmap(source);
                } else {
                    // For Android Oreo (API 27) and below (using MediaStore)
                    bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImageUri);
                }
                // Optionally, log or use the bitmap (e.g., display it in an ImageView)
                Log.d("Bitmap", "Image decoded successfully.");
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Bitmap Error", "Failed to decode image.");
            }
        } else {
            Log.e("Bitmap Error", "The selected image URI is null.");
        }

        return bitmap;
    }

    private void handleSubmit() {
        String name = binding.etName.getText().toString().trim();
        String rank = binding.etRank.getText().toString().trim();
        String armyNumber = binding.etArmyNumber.getText().toString().trim();
        String username = binding.etUsername.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        // Regex patterns
        String noSpecialCharsRegex = "^[a-zA-Z0-9 ]+$"; // Alphanumeric and spaces only
        String onlyLettersRegex = "^[a-zA-Z ]+$";      // Letters and spaces only
        String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"; // Minimum 8 characters, at least one letter, one number, and one special character

        boolean valid = true;

        binding.etName.setError(null);
        binding.etRank.setError(null);
        binding.etArmyNumber.setError(null);
        binding.etUsername.setError(null);
        binding.etPassword.setError(null);

        if (TextUtils.isEmpty(name)) {
            binding.etName.setError("Name is required!");
            valid = false;
        } else if (!name.matches(onlyLettersRegex)) {
            binding.etName.setError("Name must contain only letters and spaces!");
            valid = false;
        }

        if (TextUtils.isEmpty(rank)) {
            binding.etRank.setError("Rank is required!");
            valid = false;
        } else if (!rank.matches(noSpecialCharsRegex)) {
            binding.etRank.setError("Rank must not contain special characters!");
            valid = false;
        }

        if (TextUtils.isEmpty(armyNumber)) {
            binding.etArmyNumber.setError("Army Number is required!");
            valid = false;
        } else if (!armyNumber.matches(noSpecialCharsRegex)) {
            binding.etArmyNumber.setError("Army Number must not contain special characters!");
            valid = false;
        }

        if (TextUtils.isEmpty(username)) {
            binding.etUsername.setError("Username is required!");
            valid = false;
        }

        if (TextUtils.isEmpty(password)) {
            binding.etPassword.setError("Password is required!");
            valid = false;
        } else if (!password.matches(passwordRegex)) {
            binding.etPassword.setError("Password must be at least 8 characters long, contain one letter, one number, and one special character!");
            valid = false;
        }
        if (image.isEmpty()) {
            valid = false;
        }

        if (!valid) {
            return;
        }

        // Check if the username or army number already exists
        adminViewModel.getAdminByUsernameOrArmyNumber(username, armyNumber).observe(getViewLifecycleOwner(), admin -> {
            if (admin != null) {
                // If admin with same username or army number exists
                binding.etArmyNumber.setError("Username or Army Number already exists!");
                binding.etUsername.setError("Username or Army Number already exists!");
            } else {
                // If no admin exists with the same username or army number, register the new admin
                FingerprintAuthenticator fingerprintAuthenticator = new FingerprintAuthenticator(getContext());

                fingerprintAuthenticator.authenticate(requireActivity(), isSuccess -> {
                    if (isSuccess) {
                        // Authentication success
                        Toast.makeText(getContext(), "Authentication Successful", Toast.LENGTH_SHORT).show();
                        // Proceed with authenticated logic
                        Admin newAdmin = new Admin(armyNumber, password, username, name, rank, image);
                        adminViewModel.registerAdmin(newAdmin);
                        binding.etName.setError(null);
                        binding.etRank.setError(null);
                        binding.etArmyNumber.setError(null);
                        binding.etUsername.setError(null);
                        binding.etPassword.setError(null);
                        Toast.makeText(getContext(), "Admin successfully registered!", Toast.LENGTH_SHORT).show();
                        clearFields();
                    } else {
                        // Authentication failed
                        Toast.makeText(getContext(), "Authentication Failed", Toast.LENGTH_SHORT).show();
                        // Handle failure logic
                    }
                });
            }
        });

    }


    private void clearFields() {
        binding.photoImageView.setImageResource(R.drawable.kote1);
        binding.etName.setText("");
        binding.etRank.setText("");
        binding.etArmyNumber.setText("");
        binding.etUsername.setText("");
        binding.etPassword.setText("");
    }

    private void checkingPermissions() {

        if (!PermissionCheck.checkPermissions(getContext())) {
            if (Build.VERSION.SDK_INT >= 34) {
                requestPermissions(new String[]{
                        Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED,
                        Manifest.permission.READ_MEDIA_IMAGES
                }, 1);
            } else if (Build.VERSION.SDK_INT >= 33) {
                requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 1);
            } else {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // Show Snackbar when permission is denied
                Snackbar snackbar = Snackbar.make(binding.getRoot(),
                        "Storage Permission is required for this app to function. Allow this in settings.", Snackbar.LENGTH_SHORT);
                snackbar.setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.setBackgroundTint(ContextCompat.getColor(getContext(), R.color.red));  // Use getContext() for fragment
                snackbar.show();
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }





}
