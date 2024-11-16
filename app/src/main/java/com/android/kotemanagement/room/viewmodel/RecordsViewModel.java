package com.android.kotemanagement.room.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.android.kotemanagement.room.entities.Records;
import com.android.kotemanagement.room.repository.RecordsRepository;

import java.util.List;

public class RecordsViewModel extends AndroidViewModel {

    private final RecordsRepository repository;
    private final LiveData<List<Records>> allRecords;

    public RecordsViewModel(Application application) {
        super(application);
        repository = new RecordsRepository(application);
        allRecords = repository.getAllRecords();
    }

    public LiveData<List<Records>> getAllRecords() {
        return allRecords;
    }

    public void insert(Records record) {
        repository.insert(record);
    }

    public void update(Records record) {
        repository.update(record);
    }

    public void delete(Records record) {
        repository.delete(record);
    }

    public Records getRecordByArmyNumber(String armyNumber) {
        return repository.getRecordByArmyNumber(armyNumber);
    }
}
