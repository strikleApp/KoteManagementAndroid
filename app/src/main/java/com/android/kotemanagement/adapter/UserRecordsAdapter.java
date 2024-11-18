package com.android.kotemanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.kotemanagement.databinding.ListViewUserRecordsBinding;
import com.android.kotemanagement.room.entities.RecordAction;
import com.android.kotemanagement.room.entities.Records;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class UserRecordsAdapter extends RecyclerView.Adapter<UserRecordsAdapter.MyViewHolder> {
    public void setRecordsList(List<Records> recordsList) {
        this.recordsList = recordsList;
    }

    List<Records> recordsList;

    public UserRecordsAdapter(Context context) {
        this.context = context;
    }

    Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ListViewUserRecordsBinding binding;

        public MyViewHolder(@NonNull ListViewUserRecordsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListViewUserRecordsBinding binding = ListViewUserRecordsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new UserRecordsAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Records record = recordsList.get(position);
        LocalDateTime localDateTime = LocalDateTime.parse(record.getDate());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String formattedDateTime = localDateTime.format(formatter);
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

    }


    @Override
    public int getItemCount() {
        return recordsList.size();
    }
}
