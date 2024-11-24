package com.android.kotemanagement.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

import com.android.kotemanagement.room.entities.Admin;

@Dao
public interface AdminDao {

    @Insert
    void insert(Admin admin);

    @Delete
    void remove(Admin admin);
}
