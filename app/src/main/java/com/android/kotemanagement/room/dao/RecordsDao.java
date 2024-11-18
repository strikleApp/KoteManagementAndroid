package com.android.kotemanagement.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.android.kotemanagement.room.entities.Records;

import java.util.List;

@Dao
public interface RecordsDao {

    @Insert
    void insert(Records record);

    @Update
    void update(Records record);

    @Delete
    void delete(Records record);

    @Query("SELECT * FROM records")
        // Ensure your table name is correct
    LiveData<List<Records>> getAllRecords();

    @Query("SELECT * FROM records WHERE armyNumber = :armyNumber LIMIT 1")
    Records getRecordByArmyNumber(String armyNumber);
}