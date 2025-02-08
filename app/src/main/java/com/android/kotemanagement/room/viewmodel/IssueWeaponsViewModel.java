package com.android.kotemanagement.room.viewmodel;

import android.app.Application;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.android.kotemanagement.room.entities.IssueWeapons;
import com.android.kotemanagement.room.repository.IssueWeaponsRepository;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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

    public IssueWeapons checkIfWeaponExists(final String serialNumber) {
        Callable<IssueWeapons> task = () -> issueWeaponsRepository.getWeaponBySerialNumber(serialNumber);
        Future<IssueWeapons> future = Executors.newSingleThreadExecutor().submit(task);

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    public LiveData<List<IssueWeapons>> getAllIssuedWeaponsList() {

        return allIssuedWeaponsList;
    }


    public IssueWeapons checkWeaponByArmyNumber(String armyNumber) {
        Callable<IssueWeapons> task = () -> issueWeaponsRepository.getWeaponByArmyNumber(armyNumber);
        Future<IssueWeapons> future = Executors.newSingleThreadExecutor().submit(task);
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

}
