package com.android.kotemanagement.room.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.android.kotemanagement.room.dao.AdminDao;
import com.android.kotemanagement.room.database.KoteDatabase;
import com.android.kotemanagement.room.entities.Admin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AdminRepository {
    private final AdminDao adminDao;
    private final ExecutorService executorService;

    public AdminRepository(Application application) {
        KoteDatabase koteDatabase = KoteDatabase.getDatabaseInstance(application);
        adminDao = koteDatabase.getAdminDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void register(Admin admin) {
        executorService.execute(() -> adminDao.insert(admin)); // Inserts the new admin into the database
    }

    public LiveData<Admin> getAdminByUsernameOrArmyNumber(String username, String armyNumber) {
        return adminDao.getAdminByUsernameOrArmyNumber(username, armyNumber); // This will observe the result
    }

}
