package com.android.kotemanagement.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.android.kotemanagement.room.entities.Armory;

import java.util.List;

@Dao
public interface ArmoryDao {

    @Insert
    void insert(Armory weapon);

    @Delete
    void delete(Armory weapon);

    @Query("SELECT * FROM armory")
    LiveData<List<Armory>> getAllWeaponList();

    @Query("SELECT * FROM armory WHERE serialNumber = :serialNumber")
    Armory getWeaponBySerialNumber(String serialNumber);
}
