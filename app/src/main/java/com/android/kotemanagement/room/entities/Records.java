package com.android.kotemanagement.room.entities;

import android.icu.util.LocaleData;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;
import java.util.Date;

@Entity(tableName = "records")
public class Records {

    private String type;

    @NonNull
    @PrimaryKey
    private String date;
    private String armyNumber;
    //USER
    private String name;
    private String rank;
    //INVENTORY
    private String sno;
    private String weaponName;


    public String getType() {
        return type;
    }

    public void setType(String type) {
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
}
