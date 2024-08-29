package com.android.kotemanagement.fragments.users;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.kotemanagement.R;
import com.android.kotemanagement.modals.ViewUserModal;

import java.util.ArrayList;
import java.util.List;

public class ViewUserFragment extends Fragment {
  static List<ViewUserModal> viewUserModalList = new ArrayList<>();
  RecyclerView rvUsers;
  ViewUserAdapter viewUserAdapter;

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_view_user, container, false);
    ViewUserModal user1 = new ViewUserModal("1", "Aashish", "Captain", "05/07/2020");
    ViewUserModal user2 = new ViewUserModal("2", "Abhijeet", "Major", "18/09/2024");
    ViewUserModal user3 = new ViewUserModal("3", "Vikash", "Sergeant", "09/10/2023");
    ViewUserModal user4 = new ViewUserModal("4", "Alina", "Corporal", "02/07/2022");
    ViewUserModal user5 = new ViewUserModal("5", "Ashutosh", "Major", "05/11/2024");
    viewUserModalList.add(user1);
    viewUserModalList.add(user2);
    viewUserModalList.add(user3);
    viewUserModalList.add(user4);
    viewUserModalList.add(user5);

    rvUsers = (RecyclerView) view.findViewById(R.id.rvUsers);
    viewUserAdapter = new ViewUserAdapter(viewUserModalList);
    rvUsers.setLayoutManager(new LinearLayoutManager(getContext()));
    rvUsers.setAdapter(viewUserAdapter);

    return view;
  }

}
