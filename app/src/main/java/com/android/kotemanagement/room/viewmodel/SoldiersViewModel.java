package com.android.kotemanagement.room.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.android.kotemanagement.room.entities.Soldiers;
import com.android.kotemanagement.room.repository.SoldiersRepository;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SoldiersViewModel extends AndroidViewModel {

    private SoldiersRepository soldiersRepository;
    private List<Soldiers> getAllSoldiersList;
    private Executor executor;

    public SoldiersViewModel(@NonNull Application application) {
        super(application);
        soldiersRepository = new SoldiersRepository(application);
        getAllSoldiersList = soldiersRepository.getAllSoldiersList();
        executor = Executors.newSingleThreadExecutor();
    }

    public void Insert(Soldiers soldiers) {
        executor.execute(() -> soldiersRepository.Insert(soldiers));
    }

    public void Update(Soldiers soldiers) {
        executor.execute(()-> soldiersRepository.Update(soldiers));
    }

    public void Delete(Soldiers soldiers) {
        executor.execute(() -> soldiersRepository.Delete(soldiers));
    }

    public Soldiers getSoldierByArmyNumber(String armyNumber) {
        return soldiersRepository.getSoldierByArmyNumber(armyNumber);
    }

    public List<Soldiers> getAllSoldiersList() {
        return getAllSoldiersList;
    }

}
