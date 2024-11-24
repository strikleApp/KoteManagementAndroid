package com.android.kotemanagement.fragments.users;

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
import com.android.kotemanagement.authentication.FingerprintAuthenticator;
import com.android.kotemanagement.databinding.FragmentViewUserBinding;
import com.android.kotemanagement.room.entities.Soldiers;
import com.android.kotemanagement.room.viewmodel.SoldiersViewModel;

import java.util.ArrayList;
import java.util.List;


public class ViewUserFragment extends Fragment {
    private FragmentViewUserBinding binding;
    private ViewUserAdapter adapter;
    SoldiersViewModel soldiersViewModel;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentViewUserBinding.inflate(inflater, container, false);

        adapter = new ViewUserAdapter(requireContext());
        binding.rvUsers.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvUsers.addItemDecoration(new ItemOffsetDecoration(16));


        soldiersViewModel = new ViewModelProvider(this).get(SoldiersViewModel.class);

        soldiersViewModel.getAllSoldiersList().observe(getViewLifecycleOwner(), soldiersList -> {
            if (soldiersList != null && !soldiersList.isEmpty()) {
                adapter.setSoldiersList(soldiersList);
                binding.rvUsers.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } else {
                Log.e("ViewUser Fragment", "No soldiers found.");
            }
        });

        binding.btnVerifyFingerprint.setOnClickListener(v -> {
            fingerprintAuth();
        });

        binding.etSearchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.e("before", "Called");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filterSoldiers(editable.toString());
                Log.e("after", editable.toString());
            }
        });


//        // User search code
//        binding.tlSearchUser .setEndIconOnClickListener(v -> {
//            String armyNumber = binding.etSearchUser .getText().toString();
//            if (!armyNumber.isEmpty()) {
//                soldiersViewModel.getSoldierByArmyNumber(armyNumber).observe(getViewLifecycleOwner(), searchedSoldier -> {
//                    if (searchedSoldier != null) {
//                        Bundle bundle = new Bundle();
//                        bundle.putString("image", searchedSoldier.image);
//                        bundle.putString("firstName", searchedSoldier.firstName);
//                        bundle.putString("lastName", searchedSoldier.lastName);
//                        bundle.putString("rank", searchedSoldier.rank);
//                        bundle.putString("armyNumber", searchedSoldier.armyNumber);
//                        bundle.putString("dob", searchedSoldier.dob);
//
//                        // Handle the bundle (e.g., pass it to another fragment or activity)
//                    } else {
//                        Log.e("Search Error", "Soldier not found with army number: " + armyNumber);
//                    }
//                });
//            }
//        });

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

    private void filterSoldiers(String query) {
        List<Soldiers> soldiersList = soldiersViewModel.getAllSoldiersList().getValue();
        if (query.isEmpty()) {
            // If the query is empty, show all soldiers
            Log.e("empty", "called");
            adapter.setSoldiersList(soldiersList);
        } else {
            // Otherwise, filter based on the query
            List<Soldiers> filteredList = new ArrayList<>();
            String lowerCaseQuery = query.toLowerCase();

            for (Soldiers soldier : soldiersList) {
                // Check if any of the soldier's fields match the query
                if (soldier.getFirstName().toLowerCase().contains(lowerCaseQuery) ||
                        soldier.getLastName().toLowerCase().contains(lowerCaseQuery) ||
                        soldier.getRank().toLowerCase().contains(lowerCaseQuery) ||
                        soldier.getArmyNumber().toLowerCase().contains(lowerCaseQuery) ||
                        soldier.getDob().toLowerCase().contains(lowerCaseQuery)) {
                    filteredList.add(soldier);
                }
            }

            // Update the adapter with the filtered list
            adapter.setSoldiersList(filteredList);
        }
        // Notify the adapter that the data has changed
        adapter.notifyDataSetChanged();
    }

}