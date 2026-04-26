package com.example.szlakigrskieam.database
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TrailDao {
    @Query("SELECT * FROM trails WHERE category = :category")
    fun getTrailsByCategory(category: String): LiveData<List<Trail>>

    @Query("SELECT * FROM trails WHERE id = :id")
    fun getTrailById(id: Int): LiveData<Trail>

    @Insert
    suspend fun insertTrails(trails: List<Trail>)
}