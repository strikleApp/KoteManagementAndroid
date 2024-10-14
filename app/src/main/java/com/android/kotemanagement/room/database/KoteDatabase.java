package com.android.kotemanagement.room.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.android.kotemanagement.room.dao.IssueWeaponsDao;
import com.android.kotemanagement.room.dao.SoldiersDao;
import com.android.kotemanagement.room.entities.IssueWeapons;
import com.android.kotemanagement.room.entities.Soldiers;

@Database(entities = {Soldiers.class, IssueWeapons.class}, version = 1)
public abstract class KoteDatabase extends RoomDatabase {

    abstract public SoldiersDao getSoldiersDao();
    abstract public IssueWeaponsDao getIssueWeaponsDao();

    private static volatile KoteDatabase instance = null;
    public static KoteDatabase getDatabaseInstance(Context context) {
        synchronized (KoteDatabase.class) {
            if(instance == null) {
                instance = Room.databaseBuilder(context.getApplicationContext(), KoteDatabase.class, "kote_database")
                        .build();
            }
            return instance;
        }
    }
}
