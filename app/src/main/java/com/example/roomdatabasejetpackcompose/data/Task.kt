package com.example.roomdatabasejetpackcompose.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// TODO 2: create entity object, which will be a table in the database
@Entity(tableName = "TaskTable")
data class Task(
    @ColumnInfo(name = "name")
    val name: String,
//    @ColumnInfo(name = "rank")
//    var rank: Int,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
