package com.android.kotemanagement.fragments.users;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.android.kotemanagement.R;
import com.android.kotemanagement.modals.ViewUserModal;

public class DeleteUserFragment extends Fragment {
  ConstraintLayout clBody;
  Button btnSearch;
  TextView tvName;
  TextView tvID;
  TextView tvRank;
  TextView tvDateOfJoining;
  EditText etSearchUser;
  Button btnDelete;

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_delete_user, container, false);
    clBody = view.findViewById(R.id.clBody);
    clBody.setVisibility(View.GONE);
    btnSearch = view.findViewById(R.id.btnSearch);
    tvName = view.findViewById(R.id.tvName);
    tvID = view.findViewById(R.id.tvID);
    tvRank = view.findViewById(R.id.tvRank);
    tvDateOfJoining = view.findViewById(R.id.tvDateOfJoining);
    etSearchUser = view.findViewById(R.id.etSearchUser);
    btnDelete = view.findViewById(R.id.btnDelete);

    btnSearch.setOnClickListener(
        v -> {
          String id = etSearchUser.getText().toString();
          boolean isFound = false;
          if (!id.isEmpty()) {
            for (ViewUserModal userModal : ViewUserFragment.viewUserModalList) {
              if (userModal.getId().equals(id)) {
                tvName.setText(userModal.getName());
                tvID.setText(userModal.getId());
                tvRank.setText(userModal.getRank());
                tvDateOfJoining.setText(userModal.getDateOfJoining());
                clBody.setVisibility(View.VISIBLE);
                isFound = true;
                etSearchUser.setText("");
              }
            }
            if (!isFound) {
              Toast.makeText(view.getContext(), "User was found!", Toast.LENGTH_SHORT).show();
            }
          }
        });

    btnDelete.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast.makeText(view.getContext(), "User Deleted!", Toast.LENGTH_SHORT).show();
        clBody.setVisibility(View.GONE);
      }
    });

    return view;
  }
}
