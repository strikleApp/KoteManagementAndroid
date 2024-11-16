package com.android.kotemanagement.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.kotemanagement.databinding.ListViewAllRecordsBinding;
import com.android.kotemanagement.room.entities.RecordAction;
import com.android.kotemanagement.room.entities.RecordType;
import com.android.kotemanagement.room.entities.Records;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class RecordsAdapter extends RecyclerView.Adapter<RecordsAdapter.MyViewHolder> {

    public void setRecordsList(List<Records> recordsList) {
        this.recordsList = recordsList;
    }

    private List<Records> recordsList;
    private final Context context;

    public RecordsAdapter(Context context) {
        this.context = context;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ListViewAllRecordsBinding binding;

        public MyViewHolder(@NonNull ListViewAllRecordsBinding listViewAllRecordsBinding) {
            super(listViewAllRecordsBinding.getRoot());
            this.binding = listViewAllRecordsBinding;
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListViewAllRecordsBinding binding = ListViewAllRecordsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Records record = recordsList.get(position);
        LocalDateTime localDateTime = LocalDateTime.parse(record.getDate());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String formattedDateTime = localDateTime.format(formatter);
        if (record.getType() == RecordType.USERS_RECORDS) {
            // For Users
            holder.binding.inventoryRecords.setVisibility(View.GONE);
            holder.binding.usersRecords.setVisibility(View.VISIBLE);
            holder.binding.tvType.setText("User");
            holder.binding.tvArmyNumber.setText(record.getArmyNumber());
            holder.binding.tvName.setText(record.getName());
            holder.binding.tvRank.setText(record.getRank());
            holder.binding.tvDate.setText(formattedDateTime);
            String action = "";
            if (record.getAction() == RecordAction.USER_ADDED) {
                action = "User Added";
            } else if (record.getAction() == RecordAction.USER_REMOVED) {
                action = "User Removed";
            }
            holder.binding.tvAction.setText(action);

        } else if (record.getType() == RecordType.INVENTORY_RECORDS) {
            // For Inventory
            holder.binding.inventoryRecords.setVisibility(View.VISIBLE);
            holder.binding.usersRecords.setVisibility(View.GONE);
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
    }


    @Override
    public int getItemCount() {
        return recordsList.size();
    }
}

