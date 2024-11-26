package com.android.kotemanagement.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.kotemanagement.R;
import com.android.kotemanagement.databinding.ActivityLoginBinding;
import com.android.kotemanagement.fragments.dashboard.DashboardActivity;
import com.android.kotemanagement.superadmin.activities.SuperAdminLogin;
//import com.android.kotemanagement.superAdmin.SuperAdminHome;

import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding bindingLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        bindingLogin = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = bindingLogin.getRoot();
        setContentView(view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cl_registerLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bindingLogin.btnAdminLogin.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, AdminLogin.class));
        });

        bindingLogin.btnSuperAdminLogin.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SuperAdminLogin.class));
        });


    }

}