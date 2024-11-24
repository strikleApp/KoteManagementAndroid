package com.android.kotemanagement.room.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.android.kotemanagement.room.dao.AdminDao;
import com.android.kotemanagement.room.dao.IssueWeaponsDao;
import com.android.kotemanagement.room.dao.RecordsDao; // Import the new DAO
import com.android.kotemanagement.room.dao.SoldiersDao;
import com.android.kotemanagement.room.entities.Admin;
import com.android.kotemanagement.room.entities.IssueWeapons;
import com.android.kotemanagement.room.entities.Records; // Import the new entity
import com.android.kotemanagement.room.entities.Soldiers;

@Database(entities = {Soldiers.class, IssueWeapons.class, Records.class, Admin.class}, version = 4)
public abstract class KoteDatabase extends RoomDatabase {

    abstract public SoldiersDao getSoldiersDao();
    abstract public IssueWeaponsDao getIssueWeaponsDao();
    abstract public RecordsDao getRecordsDao();
    abstract public AdminDao getAdminDao();

    private static volatile KoteDatabase instance = null;

    public static KoteDatabase getDatabaseInstance(Context context) {
        synchronized (KoteDatabase.class) {
            if (instance == null) {
                instance = Room.databaseBuilder(context.getApplicationContext(), KoteDatabase.class, "kote_database")
                        .fallbackToDestructiveMigration()
                        .build();
            }
            return instance;
        }
    }
}