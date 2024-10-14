package com.android.kotemanagement.room.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.android.kotemanagement.room.dao.IssueWeaponsDao;
import com.android.kotemanagement.room.database.KoteDatabase;
import com.android.kotemanagement.room.entities.IssueWeapons;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

public class IssueWeaponsRepository {

    private IssueWeaponsDao issueWeaponsDao;
    private LiveData<List<IssueWeapons>> allIssueWeaponsList;
    private KoteDatabase koteDatabase;

    public IssueWeaponsRepository(Application application) {
        koteDatabase = KoteDatabase.getDatabaseInstance(application);
        issueWeaponsDao = koteDatabase.getIssueWeaponsDao();
        allIssueWeaponsList = issueWeaponsDao.getAllIssueWeaponsList();
    }

    public void insert(IssueWeapons issueWeapons){
        Executors.newSingleThreadExecutor().execute(() -> {
            issueWeaponsDao.insert(issueWeapons);
        });
    }

    public void delete(IssueWeapons issueWeapons){
        Executors.newSingleThreadExecutor().execute(() -> {
            issueWeaponsDao.delete(issueWeapons);
        });
    }

    public IssueWeapons getWeaponBySerialNumber(String serialNumber) {
        return issueWeaponsDao.getWeaponBySerialNumber(serialNumber);
    }

    public LiveData<List<IssueWeapons>> getAllIssueWeaponsList() {
        return allIssueWeaponsList;
    }




}
