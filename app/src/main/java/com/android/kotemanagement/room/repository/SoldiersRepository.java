package com.android.kotemanagement.room.repository;

import android.app.Application;

import com.android.kotemanagement.room.dao.SoldiersDao;
import com.android.kotemanagement.room.database.KoteDatabase;
import com.android.kotemanagement.room.entities.Soldiers;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SoldiersRepository {
    private SoldiersDao soldiersDao;
    private List<Soldiers> getAllSoldiersList;

    private Executor executor;

    public SoldiersRepository(Application application) {
        KoteDatabase database = KoteDatabase.getDatabaseInstance(application);
        soldiersDao = database.getSoldiersDao();
        getAllSoldiersList = soldiersDao.getAllSoldiersList();

        executor = Executors.newSingleThreadExecutor();
    }

    public void Insert(Soldiers soldiers) {
        executor.execute(() -> soldiersDao.insert(soldiers));
    }

    public void Update(Soldiers soldiers) {
        executor.execute(() -> soldiersDao.update(soldiers));
    }

    public void Delete(Soldiers soldiers) {
        executor.execute(() -> soldiersDao.delete(soldiers));
    }

    public Soldiers getSoldierByArmyNumber(String armyNumber) {
        return soldiersDao.getSoldierByArmyNumber(armyNumber);
    }

    public List<Soldiers> getAllSoldiersList() {
        return getAllSoldiersList;
    }


}
