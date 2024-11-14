package com.android.kotemanagement.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.android.kotemanagement.R;
import com.android.kotemanagement.databinding.ActivityRegisterBinding;
import com.android.kotemanagement.room.entities.Soldiers;
import com.android.kotemanagement.room.viewmodel.SoldiersViewModel;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding registerBinding;
    SoldiersViewModel soldiersViewModel;
    Soldiers soldier;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        registerBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = registerBinding.getRoot();
        setContentView(view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cl_registerLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        registerBinding.tlNewPasswordRegister.setVisibility(View.GONE);
        registerBinding.tlConfirmPasswordRegsiter.setVisibility(View.GONE);
        registerBinding.btnRegisterPage.setVisibility(View.GONE);

        soldiersViewModel = new ViewModelProvider(this).get(SoldiersViewModel.class);

        registerBinding.btnLoginRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        registerBinding.btnVerify.setOnClickListener(V-> {
            String armyNumber = Objects.requireNonNull(registerBinding.etArmyNumber.getText()).toString();
            CountDownLatch latch = new CountDownLatch(1);
            Executors.newSingleThreadExecutor().execute(() -> {
                soldier = soldiersViewModel.getSoldierByArmyNumber(armyNumber);
                latch.countDown();
            });

            try {
                latch.await();
            } catch (InterruptedException e) {
                Log.e("Soldier Finding Error", "Register Activity soldier didn't get.");
            }

            if(soldier != null) {
                Toast.makeText(this, "Successfully Verified", Toast.LENGTH_SHORT).show();
                registerBinding.tlNewPasswordRegister.setVisibility(View.VISIBLE);
                registerBinding.tlConfirmPasswordRegsiter.setVisibility(View.VISIBLE);
                registerBinding.btnRegisterPage.setVisibility(View.VISIBLE);
                registerBinding.btnVerify.setVisibility(View.GONE);
            } else {
                Toast.makeText(this, "Soldier doesn't exist.", Toast.LENGTH_SHORT).show();
            }
        });


    }
}