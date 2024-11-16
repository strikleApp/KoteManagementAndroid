package com.android.kotemanagement.fragments.records;

import android.util.Log;

import com.android.kotemanagement.room.entities.RecordAction;
import com.android.kotemanagement.room.entities.RecordType;
import com.android.kotemanagement.room.entities.Records;
import com.android.kotemanagement.room.viewmodel.RecordsViewModel;

import java.time.LocalDateTime;

public class RecordFunctions {

    static public void addUserRecord(
            String armyNumber,
            String name,
            String rank, RecordsViewModel recordsViewModel) {
        Records record = new Records(LocalDateTime.now().toString(), RecordType.USERS_RECORDS, armyNumber, RecordAction.USER_ADDED);
        record.setName(name);
        record.setRank(rank);
        recordsViewModel.insert(record);
    }

    static public void addInventoryRecord(
            String armyNumber,
            String sno,
            String weaponName, RecordsViewModel recordsViewModel) {
        Records record = new Records(LocalDateTime.now().toString(), RecordType.INVENTORY_RECORDS, armyNumber, RecordAction.INVENTORY_ADDED);
        record.setSno(sno);
        record.setWeaponName(weaponName);
        recordsViewModel.insert(record);
    }

    static public void removeInventoryRecord(
            String armyNumber,
            String sno,
            String weaponName, RecordsViewModel recordsViewModel) {
        Records record = new Records(LocalDateTime.now().toString(), RecordType.INVENTORY_RECORDS, armyNumber, RecordAction.INVENTORY_RETURNED);
        Log.e("Inventory Returned", "ISSUED CALLED");
        record.setSno(sno);
        record.setWeaponName(weaponName);
        recordsViewModel.insert(record);
    }
}
