package com.android.kotemanagement.room.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.android.kotemanagement.room.dao.ArmoryDao;
import com.android.kotemanagement.room.database.KoteDatabase;
import com.android.kotemanagement.room.entities.Armory;

import java.util.List;
import java.util.concurrent.Executors;

public class ArmoryRepository {
    private ArmoryDao armoryDao;
    private LiveData<List<Armory>> allWeaponsList;
    private KoteDatabase koteDatabase;

    public ArmoryRepository(Application application) {
        koteDatabase = KoteDatabase.getDatabaseInstance(application);
        armoryDao = koteDatabase.getArmoryDao();
        allWeaponsList = armoryDao.getAllWeaponList();
    }

    public void insert(Armory weapon) {
        Executors.newSingleThreadExecutor().execute(() -> {
            armoryDao.insert(weapon);
        });
    }

    public void delete(Armory weapon) {
        Executors.newSingleThreadExecutor().execute(() -> {
            armoryDao.delete(weapon);
        });
    }

    public void getWeaponBySerialNumber(String serialNumber) {
        Executors.newSingleThreadExecutor().execute(() -> {
            armoryDao.getWeaponBySerialNumber(serialNumber);
        });
    }

    public LiveData<List<Armory>> getAllWeaponsList() {
        return allWeaponsList;
    }
}
