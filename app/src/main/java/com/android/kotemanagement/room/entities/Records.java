package com.android.kotemanagement.room.entities;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "records")
public class Records {
    @NonNull
    @PrimaryKey
    private String date;
    private RecordType type;
    private String armyNumber;
    private RecordAction action;

    public Records(@NonNull String date, RecordType type, String armyNumber, RecordAction action) {
        this.date = date;
        this.type = type;
        this.armyNumber = armyNumber;
        this.action = action;
    }

    //USER
    private String name;
    private String rank;
    //INVENTORY
    private String sno;
    private String weaponName;


    public RecordType getType() {
        return type;
    }

    public void setType(RecordType type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }


    public void setDate(String date) {
        this.date = date;
    }

    public String getArmyNumber() {
        return armyNumber;
    }

    public void setArmyNumber(String armyNumber) {
        this.armyNumber = armyNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getWeaponName() {
        return weaponName;
    }

    public void setWeaponName(String weaponName) {
        this.weaponName = weaponName;
    }

    public RecordAction getAction() {
        return action;
    }

    public void setAction(RecordAction action) {
        this.action = action;
    }
}
