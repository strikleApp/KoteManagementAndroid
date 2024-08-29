package com.android.kotemanagement.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.kotemanagement.databinding.ViewUserListItemLayoutBinding
import com.android.kotemanagement.roomdatabase.entities.Soldiers

class SoldiersAdapter(activity: Activity) : RecyclerView.Adapter<SoldiersAdapter.SoldiersAdapterViewHolder>(){

    var soldiersList : List<Soldiers> = ArrayList()

    fun setSoldiersList(soldiersList : List<Soldiers>){
        this.soldiersList = soldiersList
    }

    class SoldiersAdapterViewHolder(val itemBinding : ViewUserListItemLayoutBinding) : RecyclerView.ViewHolder(itemBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoldiersAdapterViewHolder {
        val view = ViewUserListItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SoldiersAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return soldiersList.size
    }

    override fun onBindViewHolder(holder: SoldiersAdapterViewHolder, position: Int) {
        var soldier = soldiersList[position]
        with(holder) {

        }
    }
}