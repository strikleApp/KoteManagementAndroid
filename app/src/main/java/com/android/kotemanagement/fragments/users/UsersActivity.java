package com.android.kotemanagement.fragments.users;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.kotemanagement.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UsersActivity extends Fragment
    implements BottomNavigationView.OnNavigationItemSelectedListener {

  BottomNavigationView bottomNavigationView;
  private final Fragment viewUserFragment = new ViewUserFragment ();
  private final Fragment addUserFragment = new AddUserFragment();
  private final Fragment deleteUserFragment = new DeleteUserFragment();

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
      getChildFragmentManager().beginTransaction()
              .add(R.id.flFragment, addUserFragment, "addUserFragment")
              .add(R.id.flFragment, viewUserFragment, "viewUserFragment")
              .add(R.id.flFragment, deleteUserFragment, "deleteUserFragment")
              .hide(viewUserFragment)
              .hide(deleteUserFragment)
              .commit();
    }

    return view;
  }

  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem item) {

    if (item.getItemId() == R.id.add_user) {

      showFragment(addUserFragment);
      return true;
    } else if (item.getItemId() == R.id.view_user) {
      showFragment(viewUserFragment);
      return true;
    } else if (item.getItemId() == R.id.delete_user) {
      showFragment(deleteUserFragment);
      return true;
    }



    return false;
  }

  private void showFragment(Fragment fragment) {
    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
    if (fragment == addUserFragment) {
      transaction.hide(viewUserFragment);
      transaction.hide(deleteUserFragment);
    } else if (fragment == viewUserFragment) {
      transaction.hide(addUserFragment);
      transaction.hide(deleteUserFragment);
    } else if (fragment == deleteUserFragment) {
      transaction.hide(addUserFragment);
      transaction.hide(viewUserFragment);
    }
    transaction.show(fragment).commit();
  }
}
