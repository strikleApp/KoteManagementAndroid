package com.android.kotemanagement.fragments.users;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.kotemanagement.R;
import com.android.kotemanagement.activities.UpdateUsersActivity;
import com.android.kotemanagement.room.entities.Soldiers;

import java.util.List;

public class ViewUserAdapter extends RecyclerView.Adapter<ViewUserAdapter.MyViewHolder> {

    List<Soldiers> list;
    Context context;


    public ViewUserAdapter(Context context) {
        this.context = context;

    }

    public void setSoldiersList(List<Soldiers> soldiersList) {
        this.list = soldiersList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_user_list_item_layout, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Soldiers modal = list.get(position);
        holder.tvName.setText(modal.getFirstName() + " " + modal.getLastName());
        holder.tvID.setText(modal.getArmyNumber());
        holder.tvRank.setText(modal.getRank());
        holder.tvDOB.setText(modal.getDob());
        holder.tvShrinkID.setText(modal.getArmyNumber());
        holder.tvShrinkName.setText(modal.getFirstName());
        holder.clExpandedView.setVisibility(View.GONE);

        holder.clExpandedView.setOnClickListener(
                v -> {
                    holder.clExpandedView.setVisibility(View.GONE);
                    holder.clShrinkView.setVisibility(View.VISIBLE);
                });

        holder.clShrinkView.setOnClickListener(
                v -> {
                    holder.clExpandedView.setVisibility(View.VISIBLE);
                    holder.clShrinkView.setVisibility(View.GONE);
                });

        holder.btnUpdateUser.setOnClickListener(view ->
        {
            Intent intent = new Intent(context, UpdateUsersActivity.class);
            intent.putExtra("army_number", modal.armyNumber);
            context.startActivity(intent);


        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvID;
        TextView tvRank;
        TextView tvDOB;
        ConstraintLayout clExpandedView;
        ConstraintLayout clShrinkView;
        TextView tvShrinkID;
        TextView tvShrinkName;
        Button btnUpdateUser;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvID = itemView.findViewById(R.id.tvID);
            tvRank = itemView.findViewById(R.id.tvRank);
            tvDOB = itemView.findViewById(R.id.tvDOB);
            clExpandedView = itemView.findViewById(R.id.clExpandedView);
            clShrinkView = itemView.findViewById(R.id.clShrinkView);
            tvShrinkID = itemView.findViewById(R.id.tvShrinkID);
            tvShrinkName = itemView.findViewById(R.id.tvShrinkName);
            btnUpdateUser = itemView.findViewById(R.id.btnUpdateUser);
        }
    }
}
