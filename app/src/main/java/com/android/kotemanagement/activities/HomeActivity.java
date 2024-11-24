package com.android.kotemanagement.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.kotemanagement.R;
import com.android.kotemanagement.databinding.ActivityHomeBinding;
import com.android.kotemanagement.fragments.dashboard.DashboardActivity;
import com.android.kotemanagement.fragments.inventory.InventoryActivity;
import com.android.kotemanagement.fragments.records.RecordsActivity;
import com.android.kotemanagement.fragments.users.UsersActivity;
import com.android.kotemanagement.utilities.ConvertImage;
import com.android.kotemanagement.utilities.PermissionCheck;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HomeActivity extends AppCompatActivity {
  private DrawerLayout drawerLayout;
  private ActionBarDrawerToggle actionBarDrawerToggle;
  private Toolbar toolbar;
  private Fragment dashboardActivity = new DashboardActivity();
  private Fragment usersActivity = new UsersActivity();
  private Fragment inventoryActivity = new InventoryActivity();
  private Fragment recordsActivity = new RecordsActivity();


  ActivityHomeBinding binding;
  ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
  private Bitmap selectedImage;
  private boolean isImageLessThan1MB;
  String imageAsString;
  private boolean hasSelectedImage = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding = ActivityHomeBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    // EdgeToEdge.enable(this);
    //setContentView(R.layout.activity_home);
    ViewCompat.setOnApplyWindowInsetsListener(
        findViewById(R.id.main),
        (v, insets) -> {
          Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
          v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
          return insets;
        });

    //Checking Permissions
    checkingPermissions();

    //Picking Media
    pickVisualMediaResultLauncher();

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

  private void checkingPermissions() {

        if (!PermissionCheck.checkPermissions(this)) {
            if (Build.VERSION.SDK_INT >= 34) {
                ActivityCompat.requestPermissions(HomeActivity.this,
                        new String[]{Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED, Manifest.permission.READ_MEDIA_IMAGES}, 1);
            } else if (Build.VERSION.SDK_INT >= 33) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 1);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            Snackbar snackbar = Snackbar.make(binding.getRoot(),"Storage Permission is required for this app to function. Allow this in settings.", Snackbar.LENGTH_SHORT);
            snackbar.setAction("Dismiss", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
            snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.red));
            snackbar.show();
        }
    }

    public String pickVisualMediaResultLauncher() {

        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {


            if (uri != null) {
                //Log.d("Photo Picker", "Selected URI: " + uri);
                if (Build.VERSION.SDK_INT >= 28) {
                    ImageDecoder.Source imageSource = ImageDecoder.createSource(this.getContentResolver(), uri);

                    Executor executor = Executors.newSingleThreadExecutor();
                    CountDownLatch latch = new CountDownLatch(1);
                    executor.execute(() -> {
                        try {
                            selectedImage = ImageDecoder.decodeBitmap(imageSource);
                        } catch (IOException e) {
                            Log.e("Photo Picker", "Exception Ocurred line 208.");
                            //e.printStackTrace();
                        } finally {
                            latch.countDown();
                        }
                    });

                    try {
                        latch.await();
                        hasSelectedImage = true;
                    } catch (Exception e) {
                        Log.e("Image Selection Error", "HomeActivity.java line 219.");
                        //e.printStackTrace();
                    }

                } else {
                    Executor executor = Executors.newSingleThreadExecutor();
                    CountDownLatch latch = new CountDownLatch(1);
                    executor.execute(() -> {
                        try {
                            selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                            latch.countDown();
                        } catch (IOException e) {
                            Log.d("Photo Picker", "Exception Ocurred");
                        }
                    });
                    try {
                        latch.await();
                        hasSelectedImage = true;
                    } catch (Exception e) {
                        Log.e("Image Selection Error", "HomeActivity.java line 235");
                    }

                }

                // Checking Image Size & Converting it into String;
                Executor executor = Executors.newSingleThreadExecutor();
                CountDownLatch latch = new CountDownLatch(1);

                executor.execute(() -> {
                    isImageLessThan1MB = ConvertImage.isImageLessThan1MB(selectedImage);
                    latch.countDown();
                });
                try {
                    latch.await();
                } catch (Exception e) {
                    Log.e("Error Checking Image Size", " HomeActivity.java line 252.");
                }

                if (isImageLessThan1MB) {
                    CountDownLatch latch1 = new CountDownLatch(1);
                    Executors.newSingleThreadExecutor().execute(() -> {
                        imageAsString = ConvertImage.convertToString(selectedImage, this);
                        latch1.countDown();
                    });
                    try {
                        latch.await();
                    } catch (InterruptedException e) {
                        Log.d("Interrupted Exception", "Exception ocurred while converting image to string inHomeActivity.java line 267.");
                    }
                } else {
                    Toast.makeText(this, "Image Size should be less than 1MB", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d("Photo Picker", "No media selected");
                Toast.makeText(this, "No Images selected", Toast.LENGTH_SHORT).show();

            }
        });

        if(hasSelectedImage) {
            return imageAsString;
        } else {
            return null;
        }
    }


    private void pieChart() {
        ArrayList<PieEntry> entries = new ArrayList<>();


    }
}
