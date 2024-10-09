package com.android.kotemanagement.fragments.users;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.kotemanagement.R;
import com.android.kotemanagement.room.viewmodel.SoldiersViewModel;

public class ViewUserFragment extends Fragment {

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
        viewUserAdapter = new ViewUserAdapter(getContext());
        rvUsers.setLayoutManager(new LinearLayoutManager(getContext()));

        SoldiersViewModel soldiersViewModel = new ViewModelProvider(this).get(SoldiersViewModel.class);
        soldiersViewModel.getAllSoldiersList().observe(requireActivity(), soldiersList -> {
            viewUserAdapter.setSoldiersList(soldiersList);
            rvUsers.setAdapter(viewUserAdapter);
            viewUserAdapter.notifyDataSetChanged();
        });

        return view;
    }
}
