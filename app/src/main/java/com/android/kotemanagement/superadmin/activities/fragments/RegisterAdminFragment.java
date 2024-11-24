package com.android.kotemanagement.superadmin.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.kotemanagement.databinding.FragmentRegisterSuperAdminBinding;

public class RegisterAdminFragment extends Fragment {
    private FragmentRegisterSuperAdminBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterSuperAdminBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}
