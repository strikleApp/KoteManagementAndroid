package com.android.kotemanagement.superadmin.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.kotemanagement.R;
import com.android.kotemanagement.databinding.ActivitySuperAdminHomeBinding;
import com.android.kotemanagement.fragments.records.InventoryRecordsFragment;
import com.android.kotemanagement.fragments.users.ViewUserFragment;
import com.google.android.material.navigation.NavigationView;

public class SuperAdminHome extends AppCompatActivity {

    private ActivitySuperAdminHomeBinding binding;
    private ViewUserFragment viewUserFragment = new ViewUserFragment();
    private InventoryRecordsFragment inventoryRecordsFragment = new InventoryRecordsFragment();

    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySuperAdminHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.dlSideMenu), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setSupportActionBar(binding.toolbar);

        actionBarDrawerToggle =
                new ActionBarDrawerToggle(SuperAdminHome.this, binding.dlSideMenu, binding.toolbar, R.string.open, R.string.close);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        binding.dlSideMenu.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        binding.navView.setNavigationItemSelectedListener(this::onOptionsItemSelected);
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragmentLayout, viewUserFragment, "view_user")
                    .add(R.id.fragmentLayout, inventoryRecordsFragment, "inventory_records")
                    .hide(inventoryRecordsFragment)
                    .show(viewUserFragment)
                    .commit();
            setTitle("Dashboard");
        }

        binding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                String title = "";
                int id = item.getItemId();
                if(R.id.Users == id) {
                    showFragment(viewUserFragment);
                    title = "Soldiers";
                } else if(R.id.Inventory == id) {
                    showFragment(inventoryRecordsFragment);
                    title = "Inventory Records";
                }
                binding.toolbar.setTitle(title);
                binding.dlSideMenu.closeDrawers();
                return true;
            }
        });
    }

    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (fragment ==viewUserFragment) {
            fragmentTransaction.hide(inventoryRecordsFragment);
        } else if (fragment == inventoryRecordsFragment) {
            fragmentTransaction.hide(viewUserFragment);
        }
        fragmentTransaction.show(fragment).commit();
    }
}