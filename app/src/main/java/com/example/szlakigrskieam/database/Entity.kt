package com.example.szlakigrskieam.database
import androidx.room.*

@Entity(tableName = "trails")
data class Trail(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String,
    val name: String,
    val trailStart: String,
    val trailEnd: String,
    val distance: Double,
    val descRes: Int,
    val websiteUrl: String,
    val imageRes: Int,
)

@Entity(tableName = "trail_times")
data class TrailTime(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val trailId: Int,
    val timeMillis: Long,
    val date: Long
)