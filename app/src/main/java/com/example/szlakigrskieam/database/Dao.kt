package com.example.szlakigrskieam.database
import androidx.lifecycle.LiveData
import androidx.room.*
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
}