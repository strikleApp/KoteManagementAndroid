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
import com.android.kotemanagement.adapter.UserRecordsAdapter;
import com.android.kotemanagement.databinding.FragmentUserRecordsBinding;
import com.android.kotemanagement.room.viewmodel.RecordsViewModel;

public class UserRecordsFragment extends Fragment {
    private FragmentUserRecordsBinding binding;
    private UserRecordsAdapter adapter;
    private RecordsViewModel recordsViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUserRecordsBinding.inflate(inflater, container, false);

        recordsViewModel = new ViewModelProvider(this).get(RecordsViewModel.class);
        adapter = new UserRecordsAdapter(getContext());
        binding.rvUserRecords.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvUserRecords.addItemDecoration(new ItemOffsetDecoration(16));

        recordsViewModel.getUserRecords().observe(getViewLifecycleOwner(), records -> {
            if (records != null && !records.isEmpty()) {
                adapter.setRecordsList(records);
                binding.rvUserRecords.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } else {
                Log.e("User Records Fragment", "No records found.");
            }
        });

        return binding.getRoot();
    }
}
