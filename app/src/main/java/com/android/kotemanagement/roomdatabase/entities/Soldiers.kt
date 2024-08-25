package com.android.kotemanagement.roomdatabase.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "soldiers_table")
class Soldiers internal constructor(
    @field:PrimaryKey var armyNumber: String,
    var rank: String,
    var firstName: String,
    var lastName: String,
    dob: String,
    var gender: String
) {

}
