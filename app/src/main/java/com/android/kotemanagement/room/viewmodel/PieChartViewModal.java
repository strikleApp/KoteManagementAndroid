package com.android.kotemanagement.room.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;


import com.android.kotemanagement.room.entities.Records;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class PieChartViewModal extends ViewModel {

    private final MutableLiveData<List<PieEntry>> pieData = new MutableLiveData<>();

    public LiveData<List<PieEntry>> getPieData() {
        return pieData;
    }

    public void observeInventoryRecords(LiveData<List<Records>> inventoryRecordsLiveData) {
        Transformations.map(inventoryRecordsLiveData, records -> {
            if (records == null || records.isEmpty()) {
                return new ArrayList<PieEntry>();
            }

            Map<String, Integer> categoryCounts = new HashMap<>();
            for (Records record : records) {
                String category = String.valueOf(record.getType());  // Replace with actual field
                int count = categoryCounts.getOrDefault(category, 0) + 1;
                categoryCounts.put(category, count);
            }

            List<PieEntry> pieEntries = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : categoryCounts.entrySet()) {
                pieEntries.add(new PieEntry(entry.getValue(), entry.getKey()));
            }

            return pieEntries;
        }).observeForever(pieData::setValue);
    }
}

