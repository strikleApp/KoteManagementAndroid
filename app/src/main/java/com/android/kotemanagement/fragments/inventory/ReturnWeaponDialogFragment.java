package com.android.kotemanagement.fragments.inventory;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.kotemanagement.R;
import com.android.kotemanagement.databinding.FragmentReturnWeaponDialogBinding;
import com.android.kotemanagement.fragments.records.RecordFunctions;
import com.android.kotemanagement.room.entities.IssueWeapons;
import com.android.kotemanagement.room.viewmodel.IssueWeaponsViewModel;
import com.android.kotemanagement.room.viewmodel.RecordsViewModel;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

public class ReturnWeaponDialogFragment extends DialogFragment {

    FragmentReturnWeaponDialogBinding binding;
    String serialNumber;
    IssueWeaponsViewModel issueWeaponsViewModel;
    IssueWeapons issuedWeapon;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentReturnWeaponDialogBinding.inflate(inflater, container, false);

        Bundle arguments = getArguments();
        if (arguments != null) {
            serialNumber = arguments.getString("serial_number");
        }

        issueWeaponsViewModel = new ViewModelProvider(requireActivity()).get(IssueWeaponsViewModel.class);

        getIssuedWeapon();

        if (issuedWeapon != null) {
            binding.tvSerial.setText(issuedWeapon.serialNumber);
            binding.tvWeaponType.setText(issuedWeapon.weaponType);
            binding.tvWeaponName.setText(issuedWeapon.weaponName);
            binding.tvSoldier.setText(issuedWeapon.armyNumber);
        }

        binding.btnReturn.setOnClickListener(v -> {
            CountDownLatch latch = new CountDownLatch(1);
            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    issueWeaponsViewModel.delete(issuedWeapon);
                    latch.countDown();

                }
            });
                try {
                latch.await();
                Toast.makeText(requireContext(), "Successfully returned weapon", Toast.LENGTH_SHORT).show();
            } catch (InterruptedException e) {
                Toast.makeText(requireContext(), "Error in returning weapon", Toast.LENGTH_SHORT).show();
            }

        });


        return binding.getRoot();
    }

    private void getIssuedWeapon() {
        CountDownLatch latch = new CountDownLatch(1);
        Executors.newSingleThreadExecutor().execute(() -> {
            issuedWeapon = issueWeaponsViewModel.getWeaponBySerialNumber(serialNumber);
            latch.countDown();
        });
        try {
            latch.await();
            ;
        } catch (InterruptedException e) {
            Toast.makeText(requireContext(), "Error in retrieving weapon details", Toast.LENGTH_SHORT).show();
        }
    }
}