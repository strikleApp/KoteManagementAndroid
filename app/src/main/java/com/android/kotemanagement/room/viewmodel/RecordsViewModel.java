package com.android.kotemanagement.room.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.android.kotemanagement.room.entities.RecordType;
import com.android.kotemanagement.room.entities.Records;
import com.android.kotemanagement.room.repository.RecordsRepository;

import java.util.ArrayList;
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
        return Transformations.map(allRecords, records -> {
            if (records == null) {
                return new ArrayList<>();
            }
            // Sort all records in descending order by date
            records.sort((r1, r2) -> r2.getDate().compareTo(r1.getDate()));
            return records;
        });
    }

    public LiveData<List<Records>> getInventoryRecords() {
        return Transformations.map(allRecords, records -> {
            if (records == null) {
                return new ArrayList<>();
            }
            // Filter and sort inventory records in descending order by date
            List<Records> filteredRecords = new ArrayList<>();
            for (Records record : records) {
                if (record.getType() == RecordType.INVENTORY_RECORDS) {
                    filteredRecords.add(record);
                }
            }
            filteredRecords.sort((r1, r2) -> r2.getDate().compareTo(r1.getDate()));
            return filteredRecords;
        });
    }

    public LiveData<List<Records>> getUserRecords() {
        return Transformations.map(allRecords, records -> {
            if (records == null) {
                return new ArrayList<>();
            }
            // Filter and sort user records in descending order by date
            List<Records> filteredRecords = new ArrayList<>();
            for (Records record : records) {
                if (record.getType() == RecordType.USERS_RECORDS) {
                    filteredRecords.add(record);
                }
            }
            filteredRecords.sort((r1, r2) -> r2.getDate().compareTo(r1.getDate()));
            return filteredRecords;
        });
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
