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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.kotemanagement.R;
import com.android.kotemanagement.databinding.IssueInventoryFragmentBinding;
import com.android.kotemanagement.exceptions.UserDoesNotExistsException;
import com.android.kotemanagement.exceptions.UserFieldBlankException;
import com.android.kotemanagement.exceptions.UserFieldException;
import com.android.kotemanagement.exceptions.UsersExistsException;
import com.android.kotemanagement.fragments.records.RecordFunctions;
import com.android.kotemanagement.room.entities.IssueWeapons;
import com.android.kotemanagement.room.entities.Soldiers;
import com.android.kotemanagement.room.viewmodel.IssueWeaponsViewModel;
import com.android.kotemanagement.room.viewmodel.RecordsViewModel;
import com.android.kotemanagement.room.viewmodel.SoldiersViewModel;
import com.android.kotemanagement.utilities.CheckingUserInput;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

public class IssueInventoryFragment extends Fragment {

    private IssueInventoryFragmentBinding binding;
    private IssueWeaponsViewModel issueWeaponsViewModel;
    private SoldiersViewModel soldiersViewModel;
    StringBuilder selectedWeaponType = new StringBuilder();
    RecordsViewModel recordsViewModel;
    String weaponType;
    String weapon;
    String serialNumber;
    String armyNumber;
    Soldiers soldier;


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
        recordsViewModel = new ViewModelProvider(requireActivity()).get(RecordsViewModel.class);
        issueWeaponsViewModel = new ViewModelProvider(this).get(IssueWeaponsViewModel.class);
        soldiersViewModel = new ViewModelProvider(this).get(SoldiersViewModel.class);

        binding.btnIssue.setOnClickListener(v -> {
            try {
                weaponType = binding.mtvWeaponType.getText().toString();
                weapon = binding.mtvWeapon.getText().toString();
                serialNumber = Objects.requireNonNull(binding.etSerialNumber.getText()).toString();
                armyNumber = Objects.requireNonNull(binding.etArmyNumber.getText()).toString();

                isAllFieldsCorrect();
                issueWeapons(recordsViewModel);
            } catch (UserFieldBlankException e) {
                Snackbar snackbar = Snackbar.make(binding.getRoot(), e.message(), Snackbar.LENGTH_SHORT);
                snackbar.setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red));
                snackbar.show();
            } catch (UserDoesNotExistsException e) {
                Snackbar snackbar = Snackbar.make(binding.getRoot(), e.message(), Snackbar.LENGTH_SHORT);
                snackbar.setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red));
                snackbar.show();
            } catch (UserFieldException e) {
                Snackbar snackbar = Snackbar.make(binding.getRoot(), e.message(), Snackbar.LENGTH_SHORT);
                snackbar.setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red));
                snackbar.show();
            }

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

    private void issueWeapons(RecordsViewModel recordsViewModel) {
        RecordFunctions.addInventoryRecord(armyNumber, serialNumber, weapon, recordsViewModel);
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

    private void isAllFieldsCorrect() throws UserFieldBlankException, UserDoesNotExistsException, UserFieldException {

        if (weaponType.isBlank() || weapon.isBlank() || serialNumber.isBlank() || armyNumber.isBlank()) {
            throw new UserFieldBlankException();
        } else if (CheckingUserInput.isArmyNumberHaveWhiteSpace(armyNumber) || CheckingUserInput.isArmyNumberHaveSpecialCharacters(armyNumber)) {
            binding.etArmyNumber.setError("Army Number should not contain any space or special characters.");
            binding.etArmyNumber.requestFocus();
            throw new UserFieldException();
        } else if (CheckingUserInput.isArmyNumberHaveWhiteSpace(serialNumber) || CheckingUserInput.isArmyNumberHaveSpecialCharacters(serialNumber)) {
            binding.etSerialNumber.setError("Serial Number should not contain any space or special characters.");
            binding.etSerialNumber.requestFocus();
            throw new UserFieldException();
        } else {
            CountDownLatch latch = new CountDownLatch(1);
            Executors.newSingleThreadExecutor().execute(() -> {
                soldier = soldiersViewModel.getSoldierByArmyNumber(armyNumber);
                latch.countDown();
            });

            try {
                latch.await();
                if (soldier == null) {
                    throw new UserDoesNotExistsException();
                }
            } catch (InterruptedException e) {
                Log.e("Interrupted Exception", " Error in finding soldier.");
                //e.printStackTrace();
            }
        }

    }
}
