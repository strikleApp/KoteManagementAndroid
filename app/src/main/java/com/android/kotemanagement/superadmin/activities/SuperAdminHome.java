package com.android.kotemanagement.superadmin.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.kotemanagement.R;
import com.android.kotemanagement.databinding.ActivitySuperAdminHomeBinding;
import com.android.kotemanagement.superadmin.activities.fragments.InventorySuperAdminFragment;
import com.android.kotemanagement.superadmin.activities.fragments.MaintenanceSuperAdminFragment;
import com.android.kotemanagement.superadmin.activities.fragments.RegisterAdminFragment;
import com.android.kotemanagement.superadmin.activities.fragments.UserSuperAdminFragment;
import com.google.android.material.navigation.NavigationView;

public class SuperAdminHome extends AppCompatActivity {

    private ActivitySuperAdminHomeBinding binding;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    private final Fragment registerAdminFragment = new RegisterAdminFragment();
    private final Fragment maintenanceSuperAdminFragment = new MaintenanceSuperAdminFragment();
    private final Fragment userSuperAdminFragment = new UserSuperAdminFragment();
    private final Fragment inventorySuperAdminFragment = new InventorySuperAdminFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySuperAdminHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        // Drawer setup
        drawerLayout = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        // Add fragments to FragmentManager initially
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentLayoutSuperAdmin, registerAdminFragment, "REGISTER_ADMIN");
        fragmentTransaction.add(R.id.fragmentLayoutSuperAdmin, maintenanceSuperAdminFragment, "MAINTENANCE");
        fragmentTransaction.add(R.id.fragmentLayoutSuperAdmin, userSuperAdminFragment, "USER");
        fragmentTransaction.add(R.id.fragmentLayoutSuperAdmin, inventorySuperAdminFragment, "INVENTORY");
        fragmentTransaction.hide(maintenanceSuperAdminFragment);
        fragmentTransaction.hide(userSuperAdminFragment);
        fragmentTransaction.hide(inventorySuperAdminFragment);
        fragmentTransaction.commit();

        // Show default fragment
        showFragment(registerAdminFragment);

        // Navigation item click handling
        navigationView.setNavigationItemSelectedListener(item -> {
            String title = "Register Admin";
            if (item.getItemId() == R.id.RegisterAdmin) {
                showFragment(registerAdminFragment);
                title = "Register Admin";

            } else if (item.getItemId() == R.id.Maintenance) {
                showFragment(maintenanceSuperAdminFragment);
                title = "Maintenance";
            } else if (item.getItemId() == R.id.Users) {
                showFragment(userSuperAdminFragment);
                title = "Users";
            } else if (item.getItemId() == R.id.Inventory) {
                showFragment(inventorySuperAdminFragment);
                title = "Inventory";
            }
            binding.toolbar.setTitle(title);
            drawerLayout.closeDrawers();
            return true;
        });
    }

    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Hide all fragments
        fragmentTransaction.hide(registerAdminFragment);
        fragmentTransaction.hide(maintenanceSuperAdminFragment);
        fragmentTransaction.hide(userSuperAdminFragment);
        fragmentTransaction.hide(inventorySuperAdminFragment);

        // Show the selected fragment
        fragmentTransaction.show(fragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
