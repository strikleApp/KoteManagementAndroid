package com.android.kotemanagement.fragments.users;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.kotemanagement.authentication.FingerprintAuthenticator;
import com.android.kotemanagement.databinding.FragmentDeleteUserBinding;
import com.android.kotemanagement.fragments.records.RecordFunctions;
import com.android.kotemanagement.room.entities.Soldiers;
import com.android.kotemanagement.room.viewmodel.RecordsViewModel;
import com.android.kotemanagement.room.viewmodel.SoldiersViewModel;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

public class DeleteUserFragment extends Fragment {
    FragmentDeleteUserBinding binding;

    Soldiers searchedSoldier;
    SoldiersViewModel soldiersViewModel;
    RecordsViewModel recordsViewModel;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentDeleteUserBinding.inflate(inflater, container, false);
        recordsViewModel = new ViewModelProvider(this).get(RecordsViewModel.class);


        binding.clBody.setVisibility(View.GONE);

        soldiersViewModel = new ViewModelProvider(this).get(SoldiersViewModel.class);

        binding.tlSearchUser.setEndIconOnClickListener(v -> {
            searchSoldierFunction();
        });
        binding.btnSearch.setOnClickListener(view -> {
            searchSoldierFunction();
        });

        binding.btnDelete.setOnClickListener(v -> {
            fingerprintAuth();
        });

        return binding.getRoot();
    }

    private void fingerprintAuth() {
        // Initialize FingerprintAuthenticator
        FingerprintAuthenticator fingerprintAuthenticator = new FingerprintAuthenticator(getContext());

        fingerprintAuthenticator.authenticate(getActivity(), isSuccess -> {
            if (isSuccess) {
                // Authentication success
                // Proceed with authenticated logic
                Toast.makeText(getContext(), "Authentication Successful", Toast.LENGTH_SHORT).show();
                CountDownLatch latch = new CountDownLatch(1);
                Executors.newSingleThreadExecutor().execute(() -> {
                    soldiersViewModel.delete(searchedSoldier);

                    RecordFunctions.removeUserRecord(searchedSoldier.getArmyNumber(),
                            searchedSoldier.getFirstName() + " " + searchedSoldier.getLastName(),
                            searchedSoldier.getRank(),
                            recordsViewModel);

                    getActivity().runOnUiThread(() -> {
                        binding.clBody.setVisibility(View.GONE);  // Hide content
                        Toast.makeText(requireContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                    });
                    latch.countDown();
                });

                try {
                    // Wait until the background task is completed
                    latch.await();
                } catch (InterruptedException e) {
                    Log.e("Interrupted Exception", "Error occurred while deleting user in DeleteUserFragment.java.");
                }


            } else {
                // Authentication failed
                Toast.makeText(getContext(), "Authentication Failed", Toast.LENGTH_SHORT).show();
                // Handle failure logic
            }
        });
    }

    private void searchSoldierFunction() {
        String armyNumber = Objects.requireNonNull(binding.etSearchUser.getText()).toString();

        CountDownLatch latch = new CountDownLatch(1);
        Executors.newSingleThreadExecutor().execute(() -> {
            searchedSoldier = soldiersViewModel.getSoldierByArmyNumber(armyNumber);
            latch.countDown();
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            Log.e("Interrupted Exception", "Error occurred while searching for user in ViewUserFragment.java.");
        }

        if (searchedSoldier != null) {
            binding.clBody.setVisibility(View.VISIBLE);

            binding.tvName.setText(searchedSoldier.firstName + " " + searchedSoldier.lastName);
            binding.tvRank.setText(searchedSoldier.rank);
            binding.tvDateOfJoining.setText(searchedSoldier.dob);
            binding.tvID.setText(searchedSoldier.armyNumber);
        }
    }

}
