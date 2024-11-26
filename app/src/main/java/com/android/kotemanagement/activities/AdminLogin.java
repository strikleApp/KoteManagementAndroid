package com.android.kotemanagement.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.android.kotemanagement.authentication.FingerprintAuthenticator;
import com.android.kotemanagement.authentication.LoginAuthentication;
import com.android.kotemanagement.databinding.ActivityAdminLoginBinding;
import com.android.kotemanagement.room.viewmodel.AdminViewModel;
import com.android.kotemanagement.superadmin.activities.SuperAdminLogin;


public class AdminLogin extends AppCompatActivity {

    private ActivityAdminLoginBinding adminLoginBinding;
    private AdminViewModel adminViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adminLoginBinding = ActivityAdminLoginBinding.inflate(getLayoutInflater());
        View view = adminLoginBinding.getRoot();
        setContentView(view);

        adminViewModel = new ViewModelProvider(this).get(AdminViewModel.class);

        adminLoginBinding.btnLogin.setOnClickListener(v -> loginAdmin());
        adminLoginBinding.btnSuperAdminLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, SuperAdminLogin.class));
            finish();
        });
    }

    private void loginAdmin() {
        String armyNumber = adminLoginBinding.etArmyNumberLogin.getText().toString().trim();
        String username = adminLoginBinding.etUsernameLogin.getText().toString().trim();
        String password = adminLoginBinding.etPasswordLogin.getText().toString().trim();
        adminLoginBinding.etArmyNumberLogin.setError(null);
        adminLoginBinding.etUsernameLogin.setError(null);
        adminLoginBinding.etPasswordLogin.setError(null);

        if (armyNumber.isEmpty()) {
            adminLoginBinding.etArmyNumberLogin.setError("Please enter your Army Number");
            return;
        }
        if (username.isEmpty()) {
            adminLoginBinding.etUsernameLogin.setError("Please enter your Username");
            return;
        }
        if (password.isEmpty()) {
            adminLoginBinding.etPasswordLogin.setError("Please enter your Password");
            return;
        }


        adminViewModel.getAdminByUsernameOrArmyNumber(username, armyNumber).observe(this, admin -> {
            if (admin != null) {
                if (admin.getPassword().equals(password)) {
                    FingerprintAuthenticator fingerprintAuthenticator = new FingerprintAuthenticator(this);
                    fingerprintAuthenticator.authenticate(this, isSuccess -> {
                        if (isSuccess) {
                            // Authentication success
                            Toast.makeText(this, "Authentication Successful", Toast.LENGTH_SHORT).show();
                            // Proceed with authenticated logic
                            LoginAuthentication.saveLoginInfo(this, admin.getArmyNumber(), admin.getUsername());
                            Intent intent = new Intent(this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();

                        } else {
                            // Authentication failed
                            Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                            // Handle failure logic
                        }
                    });


                } else {
                    adminLoginBinding.etPasswordLogin.setError("Incorrect password");
                }
            } else {
                adminLoginBinding.etUsernameLogin.setError("Incorrect Username or Army Number");
                adminLoginBinding.etArmyNumberLogin.setError("Incorrect Username or Army Number");
            }
        });
    }


}
