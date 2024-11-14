package com.android.kotemanagement.fragments.users;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.kotemanagement.R;
import com.android.kotemanagement.activities.HomeActivity;
import com.android.kotemanagement.adapter.ItemOffsetDecoration;
import com.android.kotemanagement.adapter.ViewSoldierAdapter;
import com.android.kotemanagement.databinding.FragmentViewUserBinding;
import com.android.kotemanagement.fragments.soldiers.ViewSoldiersDialogFragment;
import com.android.kotemanagement.room.entities.Soldiers;
import com.android.kotemanagement.room.viewmodel.SoldiersViewModel;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

public class ViewUserFragment extends Fragment {
    FragmentViewUserBinding binding;

    Soldiers searchedSoldier;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentViewUserBinding.inflate(inflater, container, false);

        ViewSoldierAdapter adapter =  new ViewSoldierAdapter((HomeActivity) getActivity());
        binding.rvUsers.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvUsers.addItemDecoration(new ItemOffsetDecoration(16));
        binding.rvUsers.setAdapter(adapter);

        SoldiersViewModel soldiersViewModel = new ViewModelProvider(this).get(SoldiersViewModel.class);
        soldiersViewModel.getAllSoldiersList().observe(requireActivity(), soldiersList -> {
            adapter.setSoldiersList(soldiersList);
            adapter.notifyDataSetChanged();
        });


        //User search code

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

            //opening dialog fragment

            ViewSoldiersDialogFragment dialogFragment = new ViewSoldiersDialogFragment();
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

            Bundle bundle = new Bundle();
            bundle.putString("image", searchedSoldier.image);
            bundle.putString("firstName", searchedSoldier.firstName);
            bundle.putString("lastName", searchedSoldier.lastName);
            bundle.putString("rank", searchedSoldier.rank);
            bundle.putString("armyNumber", searchedSoldier.armyNumber);
            bundle.putString("dob", searchedSoldier.dob);

            dialogFragment.setArguments(bundle);
            dialogFragment.show(fragmentManager, "ViewSoldiersDialogFragment");

        });

        return binding.getRoot();
    }
}
