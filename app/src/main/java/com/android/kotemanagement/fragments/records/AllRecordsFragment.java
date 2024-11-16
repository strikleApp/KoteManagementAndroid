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

import com.android.kotemanagement.adapter.ItemOffsetDecoration;
import com.android.kotemanagement.adapter.RecordsAdapter;
import com.android.kotemanagement.databinding.FragmentAllRecordsBinding;
import com.android.kotemanagement.room.viewmodel.RecordsViewModel;

public class AllRecordsFragment extends Fragment {
    private FragmentAllRecordsBinding binding;
    private RecordsAdapter adapter;
    private RecordsViewModel recordsViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAllRecordsBinding.inflate(inflater, container, false);
        recordsViewModel = new ViewModelProvider(this).get(RecordsViewModel.class);
        adapter = new RecordsAdapter(getContext());
        binding.rvAllRecords.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvAllRecords.addItemDecoration(new ItemOffsetDecoration(16));

        recordsViewModel.getAllRecords().observe(getViewLifecycleOwner(), records -> {
            if (records != null && !records.isEmpty()) {
                adapter.setRecordsList(records);
                binding.rvAllRecords.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } else {
                Log.e("All Records Fragment", "No records found.");
            }

        });

        return binding.getRoot();
    }
}
