package com.android.kotemanagement.fragments.inventory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.kotemanagement.R;
import com.android.kotemanagement.activities.HomeActivity;
import com.android.kotemanagement.adapter.ItemOffsetDecoration;
import com.android.kotemanagement.adapter.ViewIssueWeaponsAdapter;
import com.android.kotemanagement.databinding.ReturnInventoryFragmentBinding;
import com.android.kotemanagement.room.viewmodel.IssueWeaponsViewModel;
import com.android.kotemanagement.room.viewmodel.RecordsViewModel;

import java.util.Objects;

public class ReturnInventoryFragment extends Fragment {

    private ReturnInventoryFragmentBinding binding;
    private IssueWeaponsViewModel issueWeaponsViewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ReturnInventoryFragmentBinding.inflate(inflater, container, false);

        binding.tilSearch.setEndIconOnClickListener(v -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            ReturnWeaponDialogFragment dialogFragment = new ReturnWeaponDialogFragment();
            Bundle bundle = new Bundle();

            String serialNumber = Objects.requireNonNull(binding.etSearch.getText()).toString();
            if (serialNumber != null) {
                bundle.putString("serial_number", serialNumber);
                dialogFragment.setArguments(bundle);
                dialogFragment.show(fragmentManager, "ReturnWeaponDialogFragment");
            }
        });

        ViewIssueWeaponsAdapter adapter = new ViewIssueWeaponsAdapter((HomeActivity) getActivity());
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(16);
        binding.rcvReturn.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rcvReturn.addItemDecoration(itemDecoration);
        binding.rcvReturn.setAdapter(adapter);

        issueWeaponsViewModel = new ViewModelProvider(this).get(IssueWeaponsViewModel.class);
        issueWeaponsViewModel.getAllIssuedWeaponsList().observe(getViewLifecycleOwner(), issueWeaponsList -> {
            adapter.setIssueWeaponsList(issueWeaponsList);
        });

        return binding.getRoot();
    }
}
