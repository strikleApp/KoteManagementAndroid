package com.android.kotemanagement.roomdatabase.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.android.kotemanagement.roomdatabase.dao.SoldiersDAO
import com.android.kotemanagement.roomdatabase.entities.Soldiers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Soldiers::class], version = 1)
abstract class KoteDatabase : RoomDatabase() {

    abstract fun getSoldiersDao() : SoldiersDAO

    //Singleton making
    companion object {
        @Volatile
        private var instance : KoteDatabase? = null

        fun getDatabase(context: Context) : KoteDatabase{
            synchronized(this) {
                if(instance == null) {
                    instance = Room.databaseBuilder(context.applicationContext, KoteDatabase::class.java,
                        "kote_database").build()
                }
                return instance as KoteDatabase
            }
        }
    }



}
