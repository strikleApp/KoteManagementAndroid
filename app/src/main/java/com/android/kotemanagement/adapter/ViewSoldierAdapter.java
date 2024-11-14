package com.android.kotemanagement.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.kotemanagement.activities.HomeActivity;
import com.android.kotemanagement.activities.ViewSoldiersActivity;
import com.android.kotemanagement.databinding.ViewSoldiersListItemBinding;
import com.android.kotemanagement.fragments.soldiers.ViewSoldiersDialogFragment;
import com.android.kotemanagement.room.entities.Soldiers;
import com.android.kotemanagement.utilities.ConvertImage;

import java.util.ArrayList;
import java.util.List;

public class ViewSoldierAdapter extends RecyclerView.Adapter<ViewSoldierAdapter.ViewSoldierViewHolder> {

    List<Soldiers> soldiersList = new ArrayList<>();
    HomeActivity activity;

    public ViewSoldierAdapter(HomeActivity activity) {
        this.activity = activity;
    }


    static class ViewSoldierViewHolder extends RecyclerView.ViewHolder {
        ViewSoldiersListItemBinding itemBinding;
        public ViewSoldierViewHolder(@NonNull ViewSoldiersListItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }
    }

    @NonNull
    @Override
    public ViewSoldierViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ViewSoldiersListItemBinding viewSoldiersListItemBinding = ViewSoldiersListItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );

        return new ViewSoldierViewHolder(viewSoldiersListItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewSoldierViewHolder holder, int position) {
        Soldiers soldier = soldiersList.get(position);
        holder.itemBinding.civSoldiers.setImageBitmap(ConvertImage.convertToBitmap(soldier.image));
        holder.itemBinding.tvArmyNumber.setText(soldier.armyNumber);
        holder.itemBinding.tvRank.setText(soldier.rank);
        holder.itemBinding.tvSoldierName.setText(soldier.firstName + " " + soldier.lastName);

        holder.itemBinding.materialCardView.setOnClickListener(v -> {
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            ViewSoldiersDialogFragment dialogFragment = new ViewSoldiersDialogFragment();

            Bundle bundle = new Bundle();
            bundle.putString("image", soldier.image);
            bundle.putString("firstName", soldier.firstName);
            bundle.putString("lastName", soldier.lastName);
            bundle.putString("rank", soldier.rank);
            bundle.putString("armyNumber", soldier.armyNumber);
            bundle.putString("dob", soldier.dob);

            dialogFragment.setArguments(bundle);
            dialogFragment.show(fragmentManager, "ViewSoldiersDialogFragment");
        });

    }

    @Override
    public int getItemCount() {
        return soldiersList.size();
    }

    public void setSoldiersList(List<Soldiers> soldiersList) {
        this.soldiersList = soldiersList;
    }

}
