package com.android.kotemanagement.roomdatabase.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.android.kotemanagement.roomdatabase.dao.SoldiersDAO
import com.android.kotemanagement.roomdatabase.database.KoteDatabase
import com.android.kotemanagement.roomdatabase.entities.Soldiers

class SoldiersRepository(application: Application) {
    var soldiersDAO : SoldiersDAO
    var allSoldiersList : LiveData<List<Soldiers>>

    init {
        val database = KoteDatabase.getDatabase(application)
        soldiersDAO = database.getSoldiersDao()
        allSoldiersList = soldiersDAO.getAllSoldiers()
    }

    suspend fun insert(soldier: Soldiers) {
        soldiersDAO.insert(soldier)
    }

    suspend fun delete(soldier: Soldiers) {
        soldiersDAO.delete(soldier)
    }

    suspend fun update(soldier: Soldiers) {
        soldiersDAO.delete(soldier)
    }

    suspend fun getSoldierByArmyNumber(armyNumber: String) : Soldiers {
        return soldiersDAO.getSoldierByArmyNumber(armyNumber)
    }

    fun getAllSoldiers() : LiveData<List<Soldiers>> {
        return allSoldiersList
    }

}