package com.android.kotemanagement.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.kotemanagement.databinding.ViewSoldiersListItemBinding;
import com.android.kotemanagement.room.entities.Soldiers;
import com.android.kotemanagement.utilities.ConvertImage;

import java.util.ArrayList;
import java.util.List;

public class ViewSoldierAdapter extends RecyclerView.Adapter<ViewSoldierAdapter.ViewSoldierViewHolder> {

    List<Soldiers> soldiersList = new ArrayList<>();


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
    }

    @Override
    public int getItemCount() {
        return soldiersList.size();
    }

    public void setSoldiersList(List<Soldiers> soldiersList) {
        this.soldiersList = soldiersList;
    }

}
