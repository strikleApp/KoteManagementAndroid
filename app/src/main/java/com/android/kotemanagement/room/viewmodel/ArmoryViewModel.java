package com.android.kotemanagement.room.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.android.kotemanagement.room.entities.Armory;
import com.android.kotemanagement.room.repository.ArmoryRepository;

import java.util.List;
import java.util.concurrent.Executors;

public class ArmoryViewModel extends AndroidViewModel {

    private ArmoryRepository armoryRepository;
    private LiveData<List<Armory>> allWeaponsList;

    public ArmoryViewModel(@NonNull Application application) {
        super(application);
        armoryRepository = new ArmoryRepository(application);
        allWeaponsList = armoryRepository.getAllWeaponsList();
    }

    public void insert(Armory weapon) {
        Executors.newSingleThreadExecutor().execute(() -> {
            armoryRepository.insert(weapon);
        });
    }

    public void delete(Armory weapon) {
        Executors.newSingleThreadExecutor().execute(() -> {
            armoryRepository.delete(weapon);
        });
    }

    public void getWeaponBySerialNumber(String serialNumber) {
        Executors.newSingleThreadExecutor().execute(() -> {
            armoryRepository.getWeaponBySerialNumber(serialNumber);
        });
    }

    public LiveData<List<Armory>> getAllWeaponsList() {
        return allWeaponsList;
    }
}
