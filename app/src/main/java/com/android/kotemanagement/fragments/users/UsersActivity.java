package com.android.kotemanagement.fragments.users;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class UsersActivity extends Fragment
        implements BottomNavigationView.OnNavigationItemSelectedListener {

  BottomNavigationView bottomNavigationView;
  private final Fragment viewUserFragment = new ViewUserFragment ();
  private final Fragment addUserFragment = new AddUserFragment();
  private final Fragment deleteUserFragment = new DeleteUserFragment();
  private static final int MENU_ADD_USER = R.id.add_user;
  private static final int MENU_VIEW_USER = R.id.view_user;
  private static final int MENU_DELETE_USER = R.id.delete_user;

  @Nullable
  @Override
  public View onCreateView(
          @NonNull LayoutInflater inflater,
          @Nullable ViewGroup container,
          @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.activity_user, container, false);

    bottomNavigationView = view.findViewById(R.id.bnvBottomNavBar);
    bottomNavigationView.setOnNavigationItemSelectedListener(this);

    if (savedInstanceState == null) {
      // Initialize all fragments and hide the others
      FragmentManager fm = getChildFragmentManager();
      FragmentTransaction ft = fm.beginTransaction();
      ft.add(R.id.flFragment, viewUserFragment, "viewUserFragment")
              .add(R.id.flFragment, addUserFragment, "addUserFragment")
              .add(R.id.flFragment, deleteUserFragment, "deleteUserFragment")
              .hide(viewUserFragment)
              .hide(deleteUserFragment)
              .commit();

      // Set the default fragment (e.g., AddUserFragment)
      bottomNavigationView.setSelectedItemId(R.id.add_user);
      showFragment(addUserFragment);
    }

    return view;
  }

  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    int itemId = item.getItemId();

    if (itemId == MENU_ADD_USER) {
      showFragment(addUserFragment);
      return true;
    } else if (itemId == MENU_VIEW_USER) {
      showFragment(viewUserFragment);
      return true;
    } else if (itemId == MENU_DELETE_USER) {
      showFragment(deleteUserFragment);
      return true;
    }

    return false;
  }

  private void showFragment(Fragment fragment) {
    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

    // Hide all fragments first
    transaction.hide(addUserFragment);
    transaction.hide(viewUserFragment);
    transaction.hide(deleteUserFragment);

    // Show the selected fragment
    transaction.show(fragment).commit();
  }
}
