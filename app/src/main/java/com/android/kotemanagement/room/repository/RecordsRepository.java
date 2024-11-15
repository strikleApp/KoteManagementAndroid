package com.android.kotemanagement.room.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.android.kotemanagement.room.dao.RecordsDao;
import com.android.kotemanagement.room.database.KoteDatabase;
import com.android.kotemanagement.room.entities.Records;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RecordsRepository {

    private final RecordsDao recordsDao;
    private final LiveData<List<Records>> allRecords;
    private final Executor executor;

    public RecordsRepository(Application application) {
        KoteDatabase db = KoteDatabase.getDatabaseInstance(application);
        recordsDao = db.getRecordsDao();
        allRecords = recordsDao.getAllRecords();
        executor = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Records>> getAllRecords() {
        return allRecords;
    }

    public void insert(Records record) {
        executor.execute(() -> recordsDao.insert(record));
    }

    public void update(Records record) {
        executor.execute(() -> recordsDao.update(record));
    }

    public void delete(Records record) {
        executor.execute(() -> recordsDao.delete(record));
    }

    public Records getRecordByArmyNumber(String armyNumber) {
        return recordsDao.getRecordByArmyNumber(armyNumber);
    }
}