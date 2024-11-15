package com.android.kotemanagement.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "soldiers_table")
public class Soldiers {
    public String image;
    @PrimaryKey
    @NotNull
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public @NotNull String getArmyNumber() {
        return armyNumber;
    }

    public void setArmyNumber(@NotNull String armyNumber) {
        this.armyNumber = armyNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}
