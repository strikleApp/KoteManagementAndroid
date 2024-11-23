package com.android.kotemanagement.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "armory")
public class Armory {
    @PrimaryKey @NotNull
    public String serialNumber;

    public String weaponType;
    public String weaponName;

    public Armory(@NotNull String serialNumber, String weaponType, String weaponName) {
        this.serialNumber = serialNumber;
        this.weaponType = weaponType;
        this.weaponName = weaponName;
    }
}
