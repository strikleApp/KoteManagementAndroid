package com.android.kotemanagement.fragments.inventory;

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

public class InventoryActivity extends Fragment
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    private final Fragment issueInventoryFragment = new IssueInventoryFragment();
    private final Fragment returnInventoryFragment = new ReturnInventoryFragment();
    private static final int RETURN_INVENTORY = R.id.return_inventory;
    private static final int ISSUE_INVENTORY = R.id.issue_inventory;



    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_inventory, container, false);
        bottomNavigationView = view.findViewById(R.id.bnvBottomNavBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);


        if (savedInstanceState == null) {
            // Initialize all fragments and hide the others
            FragmentManager fm = getChildFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.flFragment, issueInventoryFragment, "issueInventoryFragment")
                    .add(R.id.flFragment, returnInventoryFragment, "returnInventoryFragment")
                    .hide(returnInventoryFragment)
                    .commit();

            // Set the default fragment
            bottomNavigationView.setSelectedItemId(R.id.issue_inventory);
            showFragment(issueInventoryFragment);
        }


        return view;
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == RETURN_INVENTORY) {
            showFragment(returnInventoryFragment);
            return true;
        } else if (itemId == ISSUE_INVENTORY) {
            showFragment(issueInventoryFragment);
            return true;
        }

        return false;
    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        // Hide all fragments first
        transaction.hide(issueInventoryFragment);
        transaction.hide(returnInventoryFragment);


        // Show the selected fragment
        transaction.show(fragment).commit();
    }
}
