package com.android.kotemanagement.fragments.inventory;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.android.kotemanagement.databinding.FragmentIssueWeaponDetailsBinding;
import com.android.kotemanagement.room.entities.IssueWeapons;
import com.android.kotemanagement.room.viewmodel.IssueWeaponsViewModel;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

public class IssueWeaponDetailsFragment extends DialogFragment {
    private FragmentIssueWeaponDetailsBinding binding;
    private String serialNumber;
    private IssueWeapons issuedWeapon;
    private IssueWeaponsViewModel viewModel;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentIssueWeaponDetailsBinding.inflate(inflater ,container, false);
        viewModel = new ViewModelProvider(this).get(IssueWeaponsViewModel.class);

        getWeaponDetails();

        binding.btnReturn.setOnClickListener(v-> {
            CountDownLatch latch = new CountDownLatch(1);
            Executors.newSingleThreadExecutor().execute(() -> {
                viewModel.delete(issuedWeapon);
                latch.countDown();
            });
            try {
                latch.await();
                dismiss();
                Toast.makeText(requireContext(), "Weapon returned successfully.", Toast.LENGTH_SHORT).show();
            } catch (InterruptedException e) {
                Log.e("ReturnError", "Error returning weapon in IssueWeaponDetailsFragment.java class.");
            }
        });
        return binding.getRoot();
    }

    private void getWeaponDetails(){
        Bundle args = getArguments();
        if(args != null) {
            serialNumber = args.getString("serialNumber");
            setWeaponDetails();
        }
    }

    private void setWeaponDetails() {
        CountDownLatch latch = new CountDownLatch(1);
        Executors.newSingleThreadExecutor().execute(() -> {
            issuedWeapon = viewModel.getWeaponBySerialNumber(serialNumber);
            latch.countDown();
        });

        try{
            latch.await();
            binding.tvName.setText(issuedWeapon.weaponName);
            binding.tvSerialNum.setText(issuedWeapon.serialNumber);
            binding.tvWeaaponType.setText(issuedWeapon.weaponType);
            binding.tvIssuedTo.setText(issuedWeapon.armyNumber);
        } catch (InterruptedException e) {
            Log.e("GetWeaponError", "Error getting weapon details in IssueWeaponDetailsFragment.java class.");
        }

    }
}