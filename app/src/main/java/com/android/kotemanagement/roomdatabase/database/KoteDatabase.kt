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
        private var INSTANCE : KoteDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope) : KoteDatabase{
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, KoteDatabase::class.java,
                    "kote_database")
                    .addCallback(KoteDatabaseCallback(scope))
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }

    private class KoteDatabaseCallback(private val coroutineScope: CoroutineScope) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            INSTANCE?. let { koteDatabase ->
                coroutineScope.launch {
                    val soldiersDAO : SoldiersDAO = koteDatabase.getSoldiersDao()
                    soldiersDAO.insert(Soldiers("Army1", "Major", "Abhijeet", "Mishra","03/03/1990", "Male"))
                }
            }
        }
    }



}
