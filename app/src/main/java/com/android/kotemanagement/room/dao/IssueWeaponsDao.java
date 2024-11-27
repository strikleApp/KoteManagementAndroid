package com.android.kotemanagement.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.android.kotemanagement.room.entities.IssueWeapons;

import java.util.List;

@Dao
public interface IssueWeaponsDao {

    @Insert
    void insert(IssueWeapons issueWeapons);

    @Delete
    void delete(IssueWeapons issueWeapons);

    @Query("SELECT * FROM issue_weapons")
    LiveData<List<IssueWeapons>> getAllIssueWeaponsList();

    @Query("SELECT * FROM issue_weapons WHERE serialNumber = :serialNumber")
    IssueWeapons getWeaponBySerialNumber(String serialNumber);

    @Query("SELECT * FROM issue_weapons WHERE serialNumber = :sno LIMIT 1")
    LiveData<IssueWeapons> checkIfWeaponExists(String sno);

    @Query("SELECT * FROM issue_weapons WHERE armyNumber = :armyNumber LIMIT 1")
    IssueWeapons getWeaponByArmyNumber(String armyNumber);
}
