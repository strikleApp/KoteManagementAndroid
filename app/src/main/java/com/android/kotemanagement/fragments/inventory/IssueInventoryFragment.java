package com.android.kotemanagement.fragments.inventory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.kotemanagement.R;
import com.android.kotemanagement.databinding.IssueInventoryFragmentBinding;

public class IssueInventoryFragment extends Fragment {

    private IssueInventoryFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = IssueInventoryFragmentBinding.inflate(inflater, container, false);


        return binding.getRoot();
    }
}
