package com.android.kotemanagement.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.android.kotemanagement.room.entities.Soldiers;

import java.util.List;

@Dao
public interface SoldiersDao {
    @Insert
    void insert(Soldiers soldiers);

    @Update
    void update(Soldiers soldiers);

    @Delete
    void delete(Soldiers soldiers);

    @Query("SELECT * FROM soldiers_table")
    LiveData<List<Soldiers>> getAllSoldiersList();

    @Query("SELECT * FROM soldiers_table WHERE armyNumber = :armyNumber LIMIT 1")
    Soldiers getSoldierByArmyNumber(String armyNumber);
}