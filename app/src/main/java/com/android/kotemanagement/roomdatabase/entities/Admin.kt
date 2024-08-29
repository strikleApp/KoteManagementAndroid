package com.android.kotemanagement.roomdatabase.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "admin")
data class Admin(@PrimaryKey var armyNumber : String,
    var name : String, var password : String, var confirmPassword : String)
{
}