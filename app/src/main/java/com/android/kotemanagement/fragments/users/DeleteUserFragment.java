package com.android.kotemanagement.fragments.users;

import android.opengl.Visibility;
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
import androidx.lifecycle.ViewModelProvider;

import com.android.kotemanagement.R;
import com.android.kotemanagement.databinding.FragmentDeleteUserBinding;
import com.android.kotemanagement.modals.ViewUserModal;
import com.android.kotemanagement.room.entities.Soldiers;
import com.android.kotemanagement.room.viewmodel.SoldiersViewModel;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

public class DeleteUserFragment extends Fragment {
  FragmentDeleteUserBinding binding;

  Soldiers searchedSoldier;
  SoldiersViewModel soldiersViewModel;

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentDeleteUserBinding.inflate(inflater, container, false);

    binding.clBody.setVisibility(View.GONE);

    soldiersViewModel = new ViewModelProvider(this).get(SoldiersViewModel.class);

    binding.tlSearchUser.setEndIconOnClickListener(v-> {
      String armyNumber = Objects.requireNonNull(binding.etSearchUser.getText()).toString();

      CountDownLatch latch = new CountDownLatch(1);
      Executors.newSingleThreadExecutor().execute(() -> {
        searchedSoldier = soldiersViewModel.getSoldierByArmyNumber(armyNumber);
        latch.countDown();
      });

      try {
        latch.await();
      } catch (InterruptedException e) {
        Log.e("Interrupted Exception", "Error occurred while searching for user in ViewUserFragment.java.");
      }

      if(searchedSoldier != null) {
        binding.clBody.setVisibility(View.VISIBLE);

        binding.tvName.setText(searchedSoldier.firstName + " " + searchedSoldier.lastName);
        binding.tvRank.setText(searchedSoldier.rank);
        binding.tvDateOfJoining.setText(searchedSoldier.dob);
        binding.tvID.setText(searchedSoldier.armyNumber);
      }
    });

    binding.btnDelete.setOnClickListener(v-> {
      CountDownLatch latch = new CountDownLatch(1);
      Executors.newSingleThreadExecutor().execute(() -> {
        soldiersViewModel.delete(searchedSoldier);
        latch.countDown();
      });
      try {
        latch.await();
        binding.clBody.setVisibility(View.GONE);
        Toast.makeText(requireContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
      } catch (InterruptedException e) {
        Log.e("Interrupted Exception", "Error occurred while deleting user in DeleteUserFragment.java.");
      }
    });

    return binding.getRoot();
  }
  
}
