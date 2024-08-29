package com.android.kotemanagement.roomdatabase.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.android.kotemanagement.roomdatabase.entities.Soldiers
import com.android.kotemanagement.roomdatabase.repository.SoldiersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SoldiersEntityViewModel(application: Application) : AndroidViewModel(application) {
    var soldiersRepository : SoldiersRepository
    var allSoldiersList : LiveData<List<Soldiers>>

    init {
        soldiersRepository = SoldiersRepository(application)
        allSoldiersList = soldiersRepository.getAllSoldiers()
    }

    fun insert(soldiers: Soldiers) = viewModelScope.launch(Dispatchers.IO) {
        soldiersRepository.insert(soldiers)
    }

    fun delete(soldiers: Soldiers) = viewModelScope.launch(Dispatchers.IO) {
        soldiersRepository.delete(soldiers)
    }

    fun update(soldiers: Soldiers) = viewModelScope.launch(Dispatchers.IO) {
        soldiersRepository.update(soldiers)
    }

    suspend fun getSoldierByArmyNumber(armyNumber: String) : Soldiers {
        return soldiersRepository.getSoldierByArmyNumber(armyNumber)
    }

    fun getAllSoldiers() : LiveData<List<Soldiers>> {
        return allSoldiersList
    }

}