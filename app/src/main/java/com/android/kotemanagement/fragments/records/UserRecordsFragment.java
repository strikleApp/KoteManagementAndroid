package com.android.kotemanagement.fragments.records;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.kotemanagement.adapter.ItemOffsetDecoration;
import com.android.kotemanagement.adapter.UserRecordsAdapter;
import com.android.kotemanagement.authentication.FingerprintAuthenticator;
import com.android.kotemanagement.databinding.FragmentUserRecordsBinding;
import com.android.kotemanagement.room.entities.Records;
import com.android.kotemanagement.room.viewmodel.RecordsViewModel;

import java.util.ArrayList;
import java.util.List;

public class UserRecordsFragment extends Fragment {
    private FragmentUserRecordsBinding binding;
    private UserRecordsAdapter adapter;
    private RecordsViewModel recordsViewModel;
    private List<Records> cachedRecords = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUserRecordsBinding.inflate(inflater, container, false);

        recordsViewModel = new ViewModelProvider(this).get(RecordsViewModel.class);
        adapter = new UserRecordsAdapter(getContext());
        binding.rvUserRecords.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvUserRecords.addItemDecoration(new ItemOffsetDecoration(16));
        binding.btnVerifyFingerprint.setOnClickListener(v -> {
            fingerprintAuth();
        });

        // Observe records and cache them
        recordsViewModel.getUserRecords().observe(getViewLifecycleOwner(), records -> {
            if (records != null && !records.isEmpty()) {
                cachedRecords = new ArrayList<>(records); // Cache the original list
                adapter.setRecordsList(records);
                binding.rvUserRecords.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } else {
                Log.e("User Records Fragment", "No records found.");
            }
        });

        // Add search functionality
        binding.etSearchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterRecords(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return binding.getRoot();
    }

    private void fingerprintAuth() {
        // Initialize FingerprintAuthenticator
        FingerprintAuthenticator fingerprintAuthenticator = new FingerprintAuthenticator(getContext());

        fingerprintAuthenticator.authenticate(getActivity(), isSuccess -> {
            if (isSuccess) {
                // Authentication success
                Toast.makeText(getContext(), "Authentication Successful", Toast.LENGTH_SHORT).show();
                // Proceed with authenticated logic
                binding.clMainContent.setVisibility(View.VISIBLE);
                binding.clFingerprintPrompt.setVisibility(View.INVISIBLE);

            } else {
                // Authentication failed
                Toast.makeText(getContext(), "Authentication Failed", Toast.LENGTH_SHORT).show();
                binding.tvAuthText.setText("Verification failed. Please verify again");
                // Handle failure logic
            }
        });
    }

    private void filterRecords(String query) {
        if (query == null || query.trim().isEmpty()) {
            // Reset to show all records if the query is empty
            adapter.setRecordsList(cachedRecords);
        } else {
            List<Records> filteredList = new ArrayList<>();
            for (Records record : cachedRecords) {
                if ((record.getArmyNumber() != null && record.getArmyNumber().toLowerCase().contains(query.toLowerCase())) ||
                        (record.getName() != null && record.getName().toLowerCase().contains(query.toLowerCase())) ||
                        (record.getRank() != null && record.getRank().toLowerCase().contains(query.toLowerCase())) ||
                        (record.getDate() != null && record.getDate().toLowerCase().contains(query.toLowerCase())) ||
                        (record.getAction() != null && record.getAction().toString().toLowerCase().contains(query.toLowerCase()))) {
                    filteredList.add(record);
                }
            }
            adapter.setRecordsList(filteredList);
        }
        adapter.notifyDataSetChanged();
    }
}
