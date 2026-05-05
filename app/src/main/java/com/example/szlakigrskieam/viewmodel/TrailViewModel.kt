package com.example.szlakigrskieam.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.szlakigrskieam.database.AppDatabase
import com.example.szlakigrskieam.database.Trail
import com.example.szlakigrskieam.database.TrailTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TrailViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    private val trailDao = database.trailDao()

    private val _category = MutableLiveData("górski")

    val trails = _category.switchMap {
        trailDao.getTrailsByCategory(it)
    }

    fun setCategory(category: String) {
        _category.value = category
    }

    fun getTrail(id: Int): LiveData<Trail> {
        return trailDao.getTrailById(id)
    }

    fun observeTimes(trailId: Int): Flow<List<TrailTime>> {
        return database.trailTimeDao().getTimesForTrail(trailId)
    }

    fun saveTime(trailId: Int, time: Long) {
        viewModelScope.launch {
            database.trailTimeDao().insertTime(
                TrailTime(
                    trailId = trailId,
                    timeMillis = time,
                    date = System.currentTimeMillis()
                )
            )
        }
    }
}