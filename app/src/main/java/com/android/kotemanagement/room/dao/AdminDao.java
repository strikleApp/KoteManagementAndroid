package com.android.kotemanagement.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.android.kotemanagement.room.entities.Admin;

@Dao
public interface AdminDao {
    @Insert
    void insert(Admin admin);

    @Delete
    void remove(Admin admin);

    // Query to check if an admin with the same army number or username exists (case-insensitive)
    @Query("SELECT * FROM admin WHERE LOWER(username) = LOWER(:username) OR LOWER(armyNumber) = LOWER(:armyNumber) LIMIT 1")
    LiveData<Admin> getAdminByUsernameOrArmyNumber(String username, String armyNumber);

    // Query to fetch admin based on username, armyNumber, and password (case-insensitive)
    @Query("SELECT * FROM admin WHERE LOWER(username) = LOWER(:username) AND LOWER(armyNumber) = LOWER(:armyNumber) AND LOWER(password) = LOWER(:password) LIMIT 1")
    Admin getAdmin(String username, String armyNumber, String password);
}
