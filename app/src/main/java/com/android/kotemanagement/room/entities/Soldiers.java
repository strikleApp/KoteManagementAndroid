package com.android.kotemanagement.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "soldiers_table")
public class Soldiers {
    public String image;
    @PrimaryKey @NotNull
    public String armyNumber;

    public String firstName;
    public String lastName;
    public String rank;
    public String dob;

    public Soldiers(String image, @NotNull String armyNumber, String firstName, String lastName, String rank, String dob) {
        this.image = image;
        this.armyNumber = armyNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.rank = rank;
        this.dob = dob;
    }
}
