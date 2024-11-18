package com.android.kotemanagement.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.kotemanagement.databinding.ListViewAllRecordsBinding;
import com.android.kotemanagement.databinding.ListViewInventoryRecordsBinding;
import com.android.kotemanagement.room.entities.RecordAction;
import com.android.kotemanagement.room.entities.Records;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class InventoryRecordsAdapter extends RecyclerView.Adapter<InventoryRecordsAdapter.MyViewHolder> {
    public InventoryRecordsAdapter(Context context) {
        this.context = context;
    }

    public void setRecordsList(List<Records> recordsList) {
        this.recordsList = recordsList;
    }

    private List<Records> recordsList;
    private Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ListViewInventoryRecordsBinding binding;

        public MyViewHolder(@NonNull ListViewInventoryRecordsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListViewInventoryRecordsBinding binding = ListViewInventoryRecordsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new InventoryRecordsAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Records record = recordsList.get(position);
        LocalDateTime localDateTime = LocalDateTime.parse(record.getDate());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String formattedDateTime = localDateTime.format(formatter);
        holder.binding.tvTypeInventory.setText("Inventory");
        holder.binding.tvArmyNumberInventory.setText(record.getArmyNumber());
        holder.binding.tvSerialNumber.setText(record.getSno());
        holder.binding.tvWeapon.setText(record.getWeaponName());
        holder.binding.tvDateInventory.setText(formattedDateTime);
        String action = "";
        if (record.getAction() == RecordAction.INVENTORY_ADDED) {
            action = "Inventory Issued";
        } else if (record.getAction() == RecordAction.INVENTORY_RETURNED) {
            Log.e("Inventory Returned", "YES");
            action = "Inventory Returned";
        }
        holder.binding.tvActionInventory.setText(action);
    }

    @Override
    public int getItemCount() {
        return recordsList.size();
    }
}
