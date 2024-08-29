package com.android.kotemanagement.roomdatabase.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.android.kotemanagement.roomdatabase.entities.Soldiers
import kotlinx.coroutines.flow.Flow

@Dao
interface SoldiersDAO {

    @Insert
    fun insert(soldier: Soldiers)

    @Delete
    fun delete(soldier: Soldiers)

    @Update
    fun update(soldier: Soldiers)

    @Query("SELECT * FROM soldiers_table")
    fun getAllSoldiers() : LiveData<List<Soldiers>>

    @Query("SELECT * FROM soldiers_table WHERE armyNumber = :armyNumber")
    fun getSoldierByArmyNumber(armyNumber: String) : Soldiers

}