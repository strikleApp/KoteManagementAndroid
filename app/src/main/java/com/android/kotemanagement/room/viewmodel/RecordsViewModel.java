package com.android.kotemanagement.room.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.android.kotemanagement.room.entities.Records;
import com.android.kotemanagement.room.repository.RecordsRepository;

import java.util.List;

public class RecordsViewModel extends ViewModel {

    private final RecordsRepository repository;
    private final LiveData<List<Records>> allRecords;

    public RecordsViewModel(Application application) {
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

// Factory for creating the ViewModel
class RecordsViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;

    public RecordsViewModelFactory(Application application) {
        this.application = application;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RecordsViewModel.class)) {
            return (T) new RecordsViewModel(application);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}