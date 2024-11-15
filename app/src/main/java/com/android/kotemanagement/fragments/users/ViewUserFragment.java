package com.android.kotemanagement.fragments.users;

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
import com.android.kotemanagement.databinding.FragmentViewUserBinding;
import com.android.kotemanagement.room.viewmodel.SoldiersViewModel;

import java.util.List;

public class ViewUserFragment extends Fragment {
    private FragmentViewUserBinding binding;
    private ViewUserAdapter adapter;

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


        SoldiersViewModel soldiersViewModel = new ViewModelProvider(this).get(SoldiersViewModel.class);

        soldiersViewModel.getAllSoldiersList().observe(getViewLifecycleOwner(), soldiersList -> {
            if (soldiersList != null && !soldiersList.isEmpty()) {
                adapter.setSoldiersList(soldiersList);
                binding.rvUsers.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } else {
                Log.e("ViewUser Fragment", "No soldiers found.");
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
}