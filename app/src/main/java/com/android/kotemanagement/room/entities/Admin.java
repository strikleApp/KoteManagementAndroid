package com.android.kotemanagement.room.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "admin")
public class Admin {

    @PrimaryKey @NotNull
    public String armyNumber;
    public String password;

    public Admin(@NonNull String armyNumber, String password) {
        this.armyNumber = armyNumber;
        this.password = password;
    }
}
