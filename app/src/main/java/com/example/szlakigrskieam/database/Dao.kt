package com.example.szlakigrskieam.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.szlakigrskieam.screens.TrailTimeWithName
import kotlinx.coroutines.flow.Flow

@Dao
interface TrailDao {
    @Query("SELECT * FROM trails WHERE category = :category")
    fun getTrailsByCategory(category: String): LiveData<List<Trail>>

    @Query("SELECT * FROM trails WHERE id = :id")
    fun getTrailById(id: Int): LiveData<Trail>

    @Insert
    suspend fun insertTrails(trails: List<Trail>)
}

@Dao
interface TrailTimeDao {

    @Insert
    suspend fun insertTime(time: TrailTime)

    @Query("SELECT * FROM trail_times WHERE trailId = :trailId ORDER BY date DESC")
    fun getTimesForTrail(trailId: Int): Flow<List<TrailTime>>

    @Query("""
    SELECT tt.id, tt.trailId, tt.timeMillis, tt.date, t.name AS trailName
    FROM trail_times tt INNER JOIN trails t ON tt.trailId = t.id
    ORDER BY tt.date DESC
    """)
    fun getAllTimesWithTrailName(): Flow<List<TrailTimeWithName>>
}