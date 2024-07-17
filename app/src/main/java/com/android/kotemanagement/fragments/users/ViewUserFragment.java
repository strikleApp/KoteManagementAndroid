package com.android.kotemanagement.fragments.users;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.kotemanagement.R;
import com.android.kotemanagement.modals.ViewUserModal;

import java.util.ArrayList;
import java.util.List;

public class ViewUserFragment extends Fragment {
  List<ViewUserModal> viewUserModalList = new ArrayList<>();
  RecyclerView rvUsers;

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_view_user, container, false);
    rvUsers = (RecyclerView) view.findViewById(R.id.rvUsers);

    ViewUserModal viewUserModal = new ViewUserModal("1", "Aashish", "Corporal", "12-07-2021");
    viewUserModalList.add(viewUserModal);
    ViewUserModal viewUserModal1 = new ViewUserModal("2", "Name", "Corporal", "12-07-2021");
    viewUserModalList.add(viewUserModal1);

    ViewUserAdapter viewUserAdapter = new ViewUserAdapter(viewUserModalList);
    rvUsers.setLayoutManager(new LinearLayoutManager(getContext()));
    rvUsers.setAdapter(viewUserAdapter);

    return view;
  }
}
