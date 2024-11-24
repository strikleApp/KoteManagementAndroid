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
import com.android.kotemanagement.activities.HomeActivity;
import com.android.kotemanagement.activities.LoginActivity;
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
    private Executor biometricExecutor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

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
                if(countBackPress == 1 && hasFilledArmyNumber) {
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

        // setting up biometric
        setBiometricExecutor();
        setBiometricPromptInfo();

        //AdminViewModel
        adminViewModel = new ViewModelProvider(this).get(AdminViewModel.class);

        binding.btnVerify.setOnClickListener(v-> {
            String armyNumber = Objects.requireNonNull(binding.etArmyNumber.getText()).toString().trim();
            if(armyNumber.equals(SUPER_ADMIN_ARMY_NUMBER)) {
                binding.tlPassword.setVisibility(View.VISIBLE);
                binding.btnLogin.setVisibility(View.VISIBLE);
                binding.btnVerify.setVisibility(View.GONE);
                binding.tlArmyNumber.setEnabled(false);
                hasFilledArmyNumber = true;
                Toast.makeText(SuperAdminLogin.this,"Successfully Verified", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SuperAdminLogin.this, "Wrong Credentials", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnLogin.setOnClickListener(v-> {
            String password = Objects.requireNonNull(binding.etPassword.getText()).toString().trim();
            if(SUPER_ADMIN_PASSWORD.equals(password)) {
                Toast.makeText(SuperAdminLogin.this, "Successfully Logged In", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SuperAdminLogin.this, SuperAdminHome.class));
                finish();
            } else {
                Toast.makeText(SuperAdminLogin.this, "Wrong Credentials", Toast.LENGTH_SHORT).show();
            }

            //biometricPrompt.authenticate(promptInfo);
        });

    }

    private void setBiometricExecutor() {
        biometricExecutor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(SuperAdminLogin.this, biometricExecutor,
                new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                        Toast.makeText(getApplicationContext(),
                                        "Authentication error: " + errString, Toast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        Toast.makeText(getApplicationContext(), "Authentication succeeded!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SuperAdminLogin.this, HomeActivity.class));
                        finish();
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        Toast.makeText(SuperAdminLogin.this, "Failed to authenticate.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setBiometricPromptInfo() {
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Cancel")
                .build();
    }



}