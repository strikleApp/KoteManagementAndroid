package com.android.kotemanagement.fragments.users;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.kotemanagement.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UsersActivity extends Fragment
    implements BottomNavigationView.OnNavigationItemSelectedListener {

  BottomNavigationView bottomNavigationView;

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.activity_user, container, false);

    bottomNavigationView = view.findViewById(R.id.bnvBottomNavBar);
    bottomNavigationView.setOnNavigationItemSelectedListener(this);
    bottomNavigationView.setSelectedItemId(R.id.add_user);
    if (savedInstanceState == null) {
      getChildFragmentManager()
          .beginTransaction()
          .replace(R.id.flFragment, AddUserFragment.class, null)
          .commit();
    }
    return view;
  }

  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    Fragment selectedFragment = null;

    if (item.getItemId() == R.id.add_user) {

      selectedFragment = new AddUserFragment();
    } else if (item.getItemId() == R.id.view_user) {
      selectedFragment = new ViewUserFragment();
    } else if (item.getItemId() == R.id.delete_user) {
      selectedFragment = new DeleteUserFragment();
    }

    if (selectedFragment != null) {
      getChildFragmentManager()
          .beginTransaction()
          .replace(R.id.flFragment, selectedFragment)
          .commit();
      return true;
    }

    return false;
  }
}
