package com.android.kotemanagement.room.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.android.kotemanagement.room.entities.Admin;
import com.android.kotemanagement.room.repository.AdminRepository;

import java.util.concurrent.Executors;

public class AdminViewModel extends AndroidViewModel {
    private AdminRepository adminRepository;

    public AdminViewModel(@NonNull Application application) {
        super(application);
        adminRepository = new AdminRepository(application);
    }

    public void insert(Admin admin) {
        Executors.newSingleThreadExecutor().execute(() -> {
            adminRepository.insert(admin);
        });
    }

    public void remove(Admin admin) {
        Executors.newSingleThreadExecutor().execute(() -> {
            adminRepository.remove(admin);
        });
    }
}
