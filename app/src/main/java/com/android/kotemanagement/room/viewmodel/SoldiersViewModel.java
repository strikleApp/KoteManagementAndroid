package com.android.kotemanagement.room.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.android.kotemanagement.room.entities.Soldiers;
import com.android.kotemanagement.room.repository.SoldiersRepository;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SoldiersViewModel extends AndroidViewModel {

    private final SoldiersRepository soldiersRepository;
    private LiveData<List<Soldiers>> getAllSoldiersList;
    private final Executor executor;

    public SoldiersViewModel(@NonNull Application application) {
        super(application);
        executor = Executors.newSingleThreadExecutor();
        soldiersRepository = new SoldiersRepository(application);
        getAllSoldiersList = soldiersRepository.getAllSoldiersList();
    }

    public void insert(Soldiers soldiers) {
        executor.execute(() -> {
            soldiersRepository.insert(soldiers);
            getAllSoldiersList = soldiersRepository.getAllSoldiersList();
        });
    }

    public void update(Soldiers soldiers) {
        executor.execute(() -> {
            soldiersRepository.update(soldiers);
            getAllSoldiersList = soldiersRepository.getAllSoldiersList();
        });
    }

    public void delete(Soldiers soldiers) {
        executor.execute(() -> {
            soldiersRepository.delete(soldiers);
            getAllSoldiersList = soldiersRepository.getAllSoldiersList();
        });
    }

    public Soldiers getSoldierByArmyNumber(String armyNumber) {
        return soldiersRepository.getSoldierByArmyNumber(armyNumber);
    }

    public LiveData<List<Soldiers>> getAllSoldiersList() {
        return getAllSoldiersList;
    }
}