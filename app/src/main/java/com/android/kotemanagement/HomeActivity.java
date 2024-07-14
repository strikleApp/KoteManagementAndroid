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

import com.android.kotemanagement.fragments.dashboard.DashboardActivity;
import com.android.kotemanagement.fragments.users.AddUserActivity;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity {
  private DrawerLayout drawerLayout;
  private ActionBarDrawerToggle actionBarDrawerToggle;

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
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
          .replace(R.id.fragmentLayout, DashboardActivity.class, null)
          .commit();
    }

    navView.setNavigationItemSelectedListener(
            item -> {
              Fragment selectedFragment = null;
              String title = "Dashboard";
              int id = item.getItemId();

              if (id == R.id.dashboard) {
                selectedFragment = new DashboardActivity();
              } else if (id == R.id.users) {
                selectedFragment = new AddUserActivity();
                title = "Users";
              } else if (id == R.id.inventory) {
                selectedFragment = new AddUserActivity();
                title = "Inventory";
              } else if (id == R.id.records) {
                selectedFragment = new AddUserActivity();
                title = "Records";
              }

              if (selectedFragment != null) {
                getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentLayout, selectedFragment)
                    .commit();
                toolbar.setTitle(title);
              }
              drawerLayout.closeDrawers();
              return true;
            });
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    return actionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
  }
}
