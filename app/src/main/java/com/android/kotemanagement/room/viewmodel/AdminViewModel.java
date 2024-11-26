package com.android.kotemanagement.room.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.android.kotemanagement.room.entities.Admin;
import com.android.kotemanagement.room.repository.AdminRepository;

public class AdminViewModel extends AndroidViewModel {
    private final AdminRepository adminRepository;

    public AdminViewModel(@NonNull Application application) {
        super(application);
        adminRepository = new AdminRepository(application);
    }

    // Method to register a new admin
    public void registerAdmin(Admin admin) {
        adminRepository.register(admin); // Calls repository to insert the admin
    }

    // Method to get an admin by username or army number
    public LiveData<Admin> getAdminByUsernameOrArmyNumber(String username, String armyNumber) {
        return adminRepository.getAdminByUsernameOrArmyNumber(username, armyNumber); // Returns LiveData to observe
    }
}
