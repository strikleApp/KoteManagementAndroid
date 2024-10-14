package com.android.kotemanagement.room.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.android.kotemanagement.room.entities.IssueWeapons;
import com.android.kotemanagement.room.repository.IssueWeaponsRepository;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

public class IssueWeaponsViewModel extends AndroidViewModel {
    private final IssueWeaponsRepository issueWeaponsRepository;
    private final LiveData<List<IssueWeapons>> allIssuedWeaponsList;

    public IssueWeaponsViewModel(@NonNull Application application) {
        super(application);
        issueWeaponsRepository = new IssueWeaponsRepository(application);
        allIssuedWeaponsList = issueWeaponsRepository.getAllIssueWeaponsList();
    }

    public void insert(IssueWeapons issueWeapons) {
        Executors.newSingleThreadExecutor().execute(() -> {
            issueWeaponsRepository.insert(issueWeapons);
        });
    }

    public void delete(IssueWeapons issueWeapons) {
        Executors.newSingleThreadExecutor().execute(() -> {
            issueWeaponsRepository.delete(issueWeapons);
        });
    }

    public IssueWeapons getWeaponBySerialNumber(String serialNumber) {
        return issueWeaponsRepository.getWeaponBySerialNumber(serialNumber);
    }

    public LiveData<List<IssueWeapons>> getAllIssuedWeaponsList() {
        return allIssuedWeaponsList;
    }

}
