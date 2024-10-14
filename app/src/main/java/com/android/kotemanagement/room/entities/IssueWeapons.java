package com.android.kotemanagement.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "issue_weapons")
public class IssueWeapons {
    @PrimaryKey @NotNull
    public String serialNumber;

    public String weaponType;
    public String weaponName;
    public String armyNumber;

    public IssueWeapons(String serialNumber, String weaponType, String weaponName, String armyNumber) {
        this.serialNumber = serialNumber;
        this.weaponType = weaponType;
        this.weaponName = weaponName;
        this.armyNumber = armyNumber;
    }

}
