package com.android.kotemanagement.superadmin.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.android.kotemanagement.R;
import com.android.kotemanagement.activities.AdminLogin;
import com.android.kotemanagement.activities.HomeActivity;
import com.android.kotemanagement.activities.LoginActivity;
import com.android.kotemanagement.authentication.FingerprintAuthenticator;
import com.android.kotemanagement.databinding.ActivitySuperAdminLoginBinding;
import com.android.kotemanagement.room.viewmodel.AdminViewModel;

import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.Executor;

public class SuperAdminLogin extends AppCompatActivity {

    private ActivitySuperAdminLoginBinding binding;
    private AdminViewModel adminViewModel;

    private int countBackPress = 0;
    private boolean hasFilledArmyNumber = false;

    private static final String SUPER_ADMIN_ARMY_NUMBER = "SUPERADMIN123";
    private static final String SUPER_ADMIN_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySuperAdminLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.tlPassword.setVisibility(View.GONE);
        binding.btnLogin.setVisibility(View.GONE);

        //On Back Pressed
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                countBackPress++;
                if (countBackPress == 1 && hasFilledArmyNumber) {
                    binding.tlPassword.setVisibility(View.GONE);
                    binding.btnLogin.setVisibility(View.GONE);
                    binding.btnVerify.setVisibility(View.VISIBLE);
                    binding.tlArmyNumber.setEnabled(true);
                    binding.etPassword.setText("");
                    binding.etArmyNumber.setText("");
                } else {
                    startActivity(new Intent(SuperAdminLogin.this, LoginActivity.class));
                    finish();
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        //AdminViewModel
        adminViewModel = new ViewModelProvider(this).get(AdminViewModel.class);

        binding.btnVerify.setOnClickListener(v -> {
            String armyNumber = Objects.requireNonNull(binding.etArmyNumber.getText()).toString().trim();
            if (armyNumber.equals(SUPER_ADMIN_ARMY_NUMBER)) {
                binding.tlPassword.setVisibility(View.VISIBLE);
                binding.btnLogin.setVisibility(View.VISIBLE);
                binding.btnVerify.setVisibility(View.GONE);
                binding.tlArmyNumber.setEnabled(false);
                hasFilledArmyNumber = true;
                Toast.makeText(SuperAdminLogin.this, "Successfully Verified", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SuperAdminLogin.this, "Wrong Credentials", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnLogin.setOnClickListener(v -> {
            String password = Objects.requireNonNull(binding.etPassword.getText()).toString().trim();
            if (SUPER_ADMIN_PASSWORD.equals(password)) {
                fingerprintAuth();
            } else {
                Toast.makeText(SuperAdminLogin.this, "Wrong Credentials", Toast.LENGTH_SHORT).show();
            }

        });
        binding.btnAdminLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, AdminLogin.class));
            finish();
        });

    }

    private void fingerprintAuth() {
        // Initialize FingerprintAuthenticator
        FingerprintAuthenticator fingerprintAuthenticator = new FingerprintAuthenticator(this);

        fingerprintAuthenticator.authenticate(this, isSuccess -> {
            if (isSuccess) {
                // Authentication success
                Toast.makeText(this, "Authentication Successful", Toast.LENGTH_SHORT).show();
                // Proceed with authenticated logic
                Intent intent = new Intent(this, SuperAdminHome.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                // Authentication failed
                Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                // Handle failure logic
            }
        });
    }


}