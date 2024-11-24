package com.android.kotemanagement.room.repository;

import android.app.Application;

import com.android.kotemanagement.room.dao.AdminDao;
import com.android.kotemanagement.room.database.KoteDatabase;
import com.android.kotemanagement.room.entities.Admin;

import java.util.concurrent.Executors;

public class AdminRepository {
    private AdminDao adminDao;
    private KoteDatabase koteDatabase;

    public AdminRepository(Application application) {
        koteDatabase = KoteDatabase.getDatabaseInstance(application);
        adminDao = koteDatabase.getAdminDao();;
    }

    public void insert(Admin admin) {
        Executors.newSingleThreadExecutor().execute(() -> {
            adminDao.insert(admin);
        });
    }

    public void remove(Admin admin) {
        Executors.newSingleThreadExecutor().execute(() -> {
            adminDao.remove(admin);
        });
    }

}
