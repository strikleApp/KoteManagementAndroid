package com.android.kotemanagement.fragments.records;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.kotemanagement.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class RecordsActivity extends Fragment
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    private final Fragment allRecordsFragment = new AllRecordsFragment();
    private final Fragment userRecordsFragment = new UserRecordsFragment();
    private final Fragment inventoryRecordsFragment = new InventoryRecordsFragment();
    private static final int ALL_RECORDS = R.id.all_records;
    private static final int USER_RECORDS = R.id.user_records;
    private static final int INVENTORY_RECORDS = R.id.inventory_records;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_records, container, false);
        bottomNavigationView = view.findViewById(R.id.bnvBottomNavBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            // Initialize all fragments and hide the others
            FragmentManager fm = getChildFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.flFragment, allRecordsFragment, "allRecordsFragment")
                    .add(R.id.flFragment, userRecordsFragment, "userRecordsFragment")
                    .add(R.id.flFragment, inventoryRecordsFragment, "inventoryRecordsFragment")
                    .hide(userRecordsFragment).hide(inventoryRecordsFragment)
                    .commit();

            // Set the default fragment
            bottomNavigationView.setSelectedItemId(R.id.all_records);
            showFragment(allRecordsFragment);
        }
        return view;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == ALL_RECORDS) {
            showFragment(allRecordsFragment);
            return true;
        } else if (itemId == INVENTORY_RECORDS) {
            showFragment(inventoryRecordsFragment);
            return true;
        } else if (itemId == USER_RECORDS) {
            showFragment(userRecordsFragment);
            return true;
        }
        return false;
    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        // Hide all fragments first
        transaction.hide(userRecordsFragment);
        transaction.hide(inventoryRecordsFragment);
        transaction.hide(allRecordsFragment);

        // Show the selected fragment
        transaction.show(fragment).commit();
    }
}
