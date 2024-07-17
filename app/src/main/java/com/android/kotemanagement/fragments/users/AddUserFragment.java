package com.android.kotemanagement.fragments.users;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

import com.android.kotemanagement.R;
import com.android.kotemanagement.modals.ViewUserModal;

import java.util.ArrayList;
import java.util.List;

public class AddUserFragment extends Fragment {
  EditText etUserID;
  EditText etUserName;
  EditText etDateOfJoining;
  AppCompatSpinner spinnerRank;
  Button btnAddUser;

  List<String> ranks = new ArrayList<>();

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_add_user, container, false);
    etUserID = view.findViewById(R.id.teUserID);
    etUserName = view.findViewById(R.id.teUserName);
    etDateOfJoining = view.findViewById(R.id.teDateOfJoining);
    spinnerRank = view.findViewById(R.id.spinnerRank);
    btnAddUser = view.findViewById(R.id.btnAddUser);

    ArrayAdapter<CharSequence> arrayAdapter =
        ArrayAdapter.createFromResource(
            view.getContext(), R.array.ranks_array, android.R.layout.simple_spinner_item);
    spinnerRank.setAdapter(arrayAdapter);
    btnAddUser.setOnClickListener(
            v -> {
              String id = etUserID.getText().toString();
              String name = etUserName.getText().toString();
              String rank = spinnerRank.getSelectedItem().toString();
              String dateOfJoining = etDateOfJoining.getText().toString();

              if (!id.isEmpty() && !name.isEmpty() && !rank.isEmpty() && !dateOfJoining.isEmpty()) {

                ViewUserModal viewUserModal = new ViewUserModal(id, name, rank, dateOfJoining);
                ViewUserFragment.viewUserModalList.add(viewUserModal);
                etUserID.setText("");
                etUserName.setText("");
                etDateOfJoining.setText("");
                spinnerRank.setSelection(0);
              }
            });
    return view;
  }
}
