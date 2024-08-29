package com.android.kotemanagement;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.android.kotemanagement.fragments.dashboard.DashboardActivity;
import com.android.kotemanagement.fragments.inventory.InventoryActivity;
import com.android.kotemanagement.fragments.records.RecordsActivity;
import com.android.kotemanagement.fragments.users.AddUserFragment;
import com.android.kotemanagement.fragments.users.UsersActivity;
import com.android.kotemanagement.roomdatabase.viewmodel.SoldiersEntityViewModel;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity {

  private DrawerLayout drawerLayout;
  private ActionBarDrawerToggle actionBarDrawerToggle;
  private Toolbar toolbar;
  private Fragment dashboardActivity = new DashboardActivity();
  private Fragment usersActivity = new UsersActivity();
  private Fragment inventoryActivity = new InventoryActivity();
  private Fragment recordsActivity = new RecordsActivity();

  //Room
  private SoldiersEntityViewModel soldiersEntityViewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // EdgeToEdge.enable(this);
    setContentView(R.layout.activity_home);
    ViewCompat.setOnApplyWindowInsetsListener(
        findViewById(R.id.main),
        (v, insets) -> {
          Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
          v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
          return insets;
        });
    toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    drawerLayout = findViewById(R.id.dlSideMenu);
    actionBarDrawerToggle =
        new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
    actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
    drawerLayout.addDrawerListener(actionBarDrawerToggle);
    actionBarDrawerToggle.syncState();

    NavigationView navView = findViewById(R.id.navView);
    navView.setNavigationItemSelectedListener(this::onOptionsItemSelected);
    if (savedInstanceState == null) {
      getSupportFragmentManager()
          .beginTransaction()
          .add(R.id.fragmentLayout, dashboardActivity, "dashboard")
          .add(R.id.fragmentLayout, usersActivity, "users")
          .add(R.id.fragmentLayout, inventoryActivity, "inventory")
          .add(R.id.fragmentLayout, recordsActivity, "records")
          .hide(usersActivity)
          .hide(inventoryActivity)
          .hide(recordsActivity)
          .show(dashboardActivity)
          .commit();
      setTitle("Dashboard");
    }

    navView.setNavigationItemSelectedListener(
        item -> {
          String title = "Dashboard";
          int id = item.getItemId();

          if (id == R.id.dashboard) {
            showFragment(dashboardActivity);

          } else if (id == R.id.users) {
            showFragment(usersActivity);
            title = "Users";
          } else if (id == R.id.inventory) {
            showFragment(inventoryActivity);
            title = "Inventory";
          } else if (id == R.id.records) {
            showFragment(recordsActivity);
            title = "Records";
          }
          toolbar.setTitle(title);

          drawerLayout.closeDrawers();
          return true;
        });

    //initializng view model
      soldiersEntityViewModel = new ViewModelProvider(this).get(SoldiersEntityViewModel.class);





  }

  private void showFragment(Fragment fragment) {
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    if (fragment == dashboardActivity) {
      fragmentTransaction.hide(usersActivity);
      fragmentTransaction.hide(inventoryActivity);
      fragmentTransaction.hide(recordsActivity);
    } else if (fragment == usersActivity) {
      fragmentTransaction.hide(dashboardActivity);
      fragmentTransaction.hide(inventoryActivity);
      fragmentTransaction.hide(recordsActivity);
    } else if (fragment == inventoryActivity) {
      fragmentTransaction.hide(usersActivity);
      fragmentTransaction.hide(dashboardActivity);
      fragmentTransaction.hide(recordsActivity);
    } else if (fragment == recordsActivity) {
      fragmentTransaction.hide(usersActivity);
      fragmentTransaction.hide(inventoryActivity);
      fragmentTransaction.hide(dashboardActivity);
    }
    fragmentTransaction.show(fragment).commit();
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    return actionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
  }
}
