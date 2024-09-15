package com.android.kotemanagement.room.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.android.kotemanagement.room.dao.SoldiersDao;
import com.android.kotemanagement.room.database.KoteDatabase;
import com.android.kotemanagement.room.entities.Soldiers;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SoldiersRepository {
    private SoldiersDao soldiersDao;

    private LiveData<List<Soldiers>> getAllSoldiersList;
    private KoteDatabase koteDatabase;

    private Executor executor;

    public SoldiersRepository(Application application) {
        executor = Executors.newSingleThreadExecutor();
        koteDatabase = KoteDatabase.getDatabaseInstance(application);
        soldiersDao = koteDatabase.getSoldiersDao();
        getAllSoldiersList = soldiersDao.getAllSoldiersList();

    }

    public void insert(Soldiers soldiers) {
        executor.execute(() -> soldiersDao.insert(soldiers));
    }

    public void update(Soldiers soldiers) {
        executor.execute(() -> soldiersDao.update(soldiers));
    }

    public void delete(Soldiers soldiers) {
        executor.execute(() -> soldiersDao.delete(soldiers));
    }

    public Soldiers getSoldierByArmyNumber(String armyNumber) {
        return soldiersDao.getSoldierByArmyNumber(armyNumber);
    }

    public LiveData<List<Soldiers>> getAllSoldiersList() {
        return getAllSoldiersList;
    }

}
