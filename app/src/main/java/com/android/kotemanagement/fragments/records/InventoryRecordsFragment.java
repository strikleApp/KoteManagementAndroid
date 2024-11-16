package com.android.kotemanagement.fragments.records;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.android.kotemanagement.adapter.InventoryRecordsAdapter;
import com.android.kotemanagement.adapter.ItemOffsetDecoration;
import com.android.kotemanagement.databinding.FragmentInventoryRecordsBinding;
import com.android.kotemanagement.room.viewmodel.RecordsViewModel;

public class InventoryRecordsFragment extends Fragment {
    private FragmentInventoryRecordsBinding binding;
    private InventoryRecordsAdapter adapter;
    private RecordsViewModel recordsViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentInventoryRecordsBinding.inflate(inflater, container, false);

        recordsViewModel = new ViewModelProvider(this).get(RecordsViewModel.class);
        adapter = new InventoryRecordsAdapter(getContext());
        binding.rvInventoryRecords.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvInventoryRecords.addItemDecoration(new ItemOffsetDecoration(16));

        recordsViewModel.getInventoryRecords().observe(getViewLifecycleOwner(), records -> {
            if (records != null && !records.isEmpty()) {
                adapter.setRecordsList(records);
                binding.rvInventoryRecords.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } else {
                Log.e("Inventory Records Fragment", "No records found.");
            }
        });
        return binding.getRoot();

    }
}
