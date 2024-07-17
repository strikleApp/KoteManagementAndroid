package com.android.kotemanagement.fragments.users;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.kotemanagement.R;
import com.android.kotemanagement.modals.ViewUserModal;

import java.util.List;

public class ViewUserAdapter extends RecyclerView.Adapter<ViewUserAdapter.MyViewHolder> {

  List<ViewUserModal> list;

  public ViewUserAdapter(List<ViewUserModal> list) {

    this.list = list;
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
    ViewUserModal modal = list.get(position);
    holder.tvName.setText(modal.getName());
    holder.tvID.setText(modal.getId());
    holder.tvRank.setText(modal.getRank());
    holder.tvDateOfJoin.setText(modal.getDateOfJoining());
  }

  @Override
  public int getItemCount() {
    return list.size();
  }

  public static class MyViewHolder extends RecyclerView.ViewHolder {
    TextView tvName;
    TextView tvID;
    TextView tvRank;
    TextView tvDateOfJoin;

    public MyViewHolder(@NonNull View itemView) {
      super(itemView);

      tvName = itemView.findViewById(R.id.tvName);
      tvID = itemView.findViewById(R.id.tvID);
      tvRank = itemView.findViewById(R.id.tvRank);
      tvDateOfJoin = itemView.findViewById(R.id.tvDateOfJoining);
    }
  }
}
