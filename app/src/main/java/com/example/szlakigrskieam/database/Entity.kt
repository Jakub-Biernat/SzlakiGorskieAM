package com.example.szlakigrskieam.database
import androidx.room.*

@Entity(tableName = "trails")
data class Trail(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val category: String,
    val distance: Double,
    val color: String
)