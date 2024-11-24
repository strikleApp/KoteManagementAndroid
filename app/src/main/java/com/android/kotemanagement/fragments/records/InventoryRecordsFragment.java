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

import com.android.kotemanagement.adapter.InventoryRecordsAdapter;
import com.android.kotemanagement.adapter.ItemOffsetDecoration;
import com.android.kotemanagement.authentication.FingerprintAuthenticator;
import com.android.kotemanagement.databinding.FragmentInventoryRecordsBinding;
import com.android.kotemanagement.room.entities.Records;
import com.android.kotemanagement.room.viewmodel.RecordsViewModel;

import java.util.ArrayList;
import java.util.List;

public class InventoryRecordsFragment extends Fragment {
    private FragmentInventoryRecordsBinding binding;
    private InventoryRecordsAdapter adapter;
    private RecordsViewModel recordsViewModel;
    private List<Records> cachedRecords = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentInventoryRecordsBinding.inflate(inflater, container, false);

        recordsViewModel = new ViewModelProvider(this).get(RecordsViewModel.class);
        adapter = new InventoryRecordsAdapter(getContext());
        binding.rvInventoryRecords.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvInventoryRecords.addItemDecoration(new ItemOffsetDecoration(16));
        binding.btnVerifyFingerprint.setOnClickListener(v -> {
            fingerprintAuth();
        });

        recordsViewModel.getInventoryRecords().observe(getViewLifecycleOwner(), records -> {
            if (records != null && !records.isEmpty()) {
                cachedRecords = records;
                adapter.setRecordsList(records);
                binding.rvInventoryRecords.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } else {
                Log.e("Inventory Records Fragment", "No records found.");
            }
        });

        setupSearchFunction();

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

    private void setupSearchFunction() {
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
    }


    private void filterRecords(String query) {
        if (query == null || query.trim().isEmpty()) {
            adapter.setRecordsList(cachedRecords);
        } else {
            List<Records> filteredList = new ArrayList<>();
            for (Records record : cachedRecords) {
                if ((record.getArmyNumber() != null && record.getArmyNumber().toLowerCase().contains(query.toLowerCase())) ||
                        (record.getSno() != null && record.getSno().toLowerCase().contains(query.toLowerCase())) ||
                        (record.getWeaponName() != null && record.getWeaponName().toLowerCase().contains(query.toLowerCase())) ||
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
