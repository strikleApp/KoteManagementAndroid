package com.android.kotemanagement.fragments.inventory;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.kotemanagement.R;
import com.android.kotemanagement.databinding.IssueInventoryFragmentBinding;
import com.android.kotemanagement.room.entities.IssueWeapons;
import com.android.kotemanagement.room.viewmodel.IssueWeaponsViewModel;
import com.android.kotemanagement.room.viewmodel.RecordsViewModel;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

public class IssueInventoryFragment extends Fragment {

    private IssueInventoryFragmentBinding binding;
    private IssueWeaponsViewModel issueWeaponsViewModel;
    private RecordsViewModel recordsViewModel;
    StringBuilder selectedWeaponType = new StringBuilder();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = IssueInventoryFragmentBinding.inflate(inflater, container, false);

        //creating an array adapter for weapons type
        ArrayAdapter<CharSequence> weaponTypeAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.weapon_type, android.R.layout.simple_dropdown_item_1line);
        binding.mtvWeaponType.setAdapter(weaponTypeAdapter);


        binding.mtvWeaponType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedWeaponType.append(parent.getItemAtPosition(position).toString());
                checkSelectedWeaponType(selectedWeaponType.toString());
                selectedWeaponType.delete(0, selectedWeaponType.length());
            }
        });


        issueWeaponsViewModel = new ViewModelProvider(this).get(IssueWeaponsViewModel.class);
        recordsViewModel = new ViewModelProvider(this).get(RecordsViewModel.class);
        binding.btnIssue.setOnClickListener(v -> {
            issueWeapons();
        });

        return binding.getRoot();
    }

    private void checkSelectedWeaponType(String weaponType) {

        binding.mtvWeapon.setText("");
        binding.etSerialNumber.setText("");

        if (weaponType.equalsIgnoreCase("assault rifle")) {
            ArrayAdapter<CharSequence> assaultRifleAdapter = ArrayAdapter.createFromResource(requireContext(),
                    R.array.assault_rifle, android.R.layout.simple_dropdown_item_1line);
            binding.mtvWeapon.setAdapter(assaultRifleAdapter);
        } else if (weaponType.equalsIgnoreCase("sniper rifle")) {
            ArrayAdapter<CharSequence> sniperRifleAdapter = ArrayAdapter.createFromResource(requireContext(),
                    R.array.sniper_rifles, android.R.layout.simple_dropdown_item_1line);
            binding.mtvWeapon.setAdapter(sniperRifleAdapter);
        } else if (weaponType.equalsIgnoreCase("pistol")) {
            ArrayAdapter<CharSequence> pistolAdapter = ArrayAdapter.createFromResource(requireContext(),
                    R.array.pistols, android.R.layout.simple_dropdown_item_1line);
            binding.mtvWeapon.setAdapter(pistolAdapter);
        } else if (weaponType.equalsIgnoreCase("smg")) {
            ArrayAdapter<CharSequence> smgAdapter = ArrayAdapter.createFromResource(requireContext(),
                    R.array.sub_machine_guns, android.R.layout.simple_dropdown_item_1line);
            binding.mtvWeapon.setAdapter(smgAdapter);
        } else if (weaponType.equalsIgnoreCase("lmg")) {
            ArrayAdapter<CharSequence> lmgAdapter = ArrayAdapter.createFromResource(requireContext(),
                    R.array.light_machine_guns, android.R.layout.simple_dropdown_item_1line);
            binding.mtvWeapon.setAdapter(lmgAdapter);
        }
    }

    private void issueWeapons() {
        String weaponType = binding.mtvWeaponType.getText().toString();
        String weapon = binding.mtvWeapon.getText().toString();
        String serialNumber = Objects.requireNonNull(binding.etSerialNumber.getText()).toString();
        String armyNumber = Objects.requireNonNull(binding.etArmyNumber.getText()).toString();

        CountDownLatch latch = new CountDownLatch(1);
        Executors.newSingleThreadExecutor().execute(() -> {
            IssueWeapons issue = new IssueWeapons(serialNumber, weaponType, weapon, armyNumber);
            issueWeaponsViewModel.insert(issue);
            latch.countDown();
        });

        try {
            latch.await();
            Toast.makeText(requireContext(), "Weapon Issued", Toast.LENGTH_SHORT).show();
            binding.mtvWeaponType.setText("");
            binding.mtvWeapon.setText("");
            binding.etSerialNumber.setText("");
            binding.etArmyNumber.setText("");
        } catch (InterruptedException e) {
            Toast.makeText(requireContext(), "Error in inserting data", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
