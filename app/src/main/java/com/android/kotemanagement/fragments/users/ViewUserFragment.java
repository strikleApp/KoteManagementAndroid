package com.android.kotemanagement.fragments.users;

import android.os.Bundle;
import android.util.Log;
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
    rvUsers = (RecyclerView) view.findViewById(R.id.rvUsers);
    viewUserAdapter = new ViewUserAdapter(viewUserModalList);
    rvUsers.setLayoutManager(new LinearLayoutManager(getContext()));
    rvUsers.setAdapter(viewUserAdapter);
    Log.d("TAG", "onCreateView: " + viewUserModalList.size());

    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
  }
}
