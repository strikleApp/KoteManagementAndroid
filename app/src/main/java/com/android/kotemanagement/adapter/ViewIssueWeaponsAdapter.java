package com.android.kotemanagement.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.kotemanagement.activities.HomeActivity;
import com.android.kotemanagement.databinding.LiveViewWeaponsBinding;
import com.android.kotemanagement.fragments.inventory.IssueWeaponDetailsFragment;
import com.android.kotemanagement.room.entities.IssueWeapons;

import java.util.ArrayList;
import java.util.List;

public class ViewIssueWeaponsAdapter extends RecyclerView.Adapter<ViewIssueWeaponsAdapter.MyViewHolder> {
    private List<IssueWeapons> issueWeaponsList = new ArrayList<>();
    private HomeActivity activity;

    public ViewIssueWeaponsAdapter(HomeActivity activity) {
        this.activity = activity;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LiveViewWeaponsBinding itemBinding;
        public MyViewHolder(@NonNull LiveViewWeaponsBinding binding) {
            super(binding.getRoot());
            this.itemBinding = binding;
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LiveViewWeaponsBinding binding = LiveViewWeaponsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        IssueWeapons issuedWeapon = issueWeaponsList.get(position);
        holder.itemBinding.tvWeaponName.setText(issuedWeapon.weaponName);
        holder.itemBinding.tvSerialNumber.setText(issuedWeapon.serialNumber);
        holder.itemBinding.cardView.setOnClickListener(v-> {
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            IssueWeaponDetailsFragment dialogFragment = new IssueWeaponDetailsFragment();
            Bundle args = new Bundle();
            args.putString("serialNumber", issuedWeapon.serialNumber);
            dialogFragment.setArguments(args);
            dialogFragment.show(fragmentManager, "IssueWeaponDetailsFragment");
        });
    }

    @Override
    public int getItemCount() {
        return issueWeaponsList.size();
    }

    public void setIssueWeaponsList(List<IssueWeapons> issueWeaponsList) {
        this.issueWeaponsList = issueWeaponsList;
        notifyDataSetChanged();
    }
}
