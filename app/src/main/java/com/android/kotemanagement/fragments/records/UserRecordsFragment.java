package com.android.kotemanagement.fragments.records;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.kotemanagement.databinding.FragmentUserRecordsBinding;

public class UserRecordsFragment extends Fragment {
    private FragmentUserRecordsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUserRecordsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}
