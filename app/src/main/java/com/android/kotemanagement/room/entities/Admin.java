package com.android.kotemanagement.room.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "admin", primaryKeys = {"armyNumber", "username"})
public class Admin {

    @NotNull
    private String armyNumber;
    @NotNull
    private String password;
    @NotNull
    private String username;
    @NotNull
    private String name;
    @NotNull
    private String rank;
    @NotNull
    private String image;

    public @NotNull String getImage() {
        return image;
    }

    public void setImage(@NotNull String image) {
        this.image = image;
    }

    public @NotNull String getArmyNumber() {
        return armyNumber;
    }

    public void setArmyNumber(@NotNull String armyNumber) {
        this.armyNumber = armyNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    // Constructor
    public Admin(@NonNull String armyNumber, @NonNull String password, @NonNull String username, @NonNull String name, @NonNull String rank, @NonNull String image) {
        this.armyNumber = armyNumber;
        this.password = password;
        this.username = username;
        this.name = name;
        this.rank = rank;
        this.image = image;
    }
}
