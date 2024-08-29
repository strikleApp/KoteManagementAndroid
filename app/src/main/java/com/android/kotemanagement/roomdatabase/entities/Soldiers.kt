package com.android.kotemanagement.roomdatabase.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "soldiers_table")
data class Soldiers(@PrimaryKey var armyNumber: String, var rank: String,
                    var firstName: String, var lastName: String, var dob: String, var gender: String) {

}
