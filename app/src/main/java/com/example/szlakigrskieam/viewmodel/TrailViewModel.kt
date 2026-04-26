package com.example.szlakigrskieam.viewmodel
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.szlakigrskieam.database.Trail
import com.example.szlakigrskieam.database.TrailDao
import androidx.lifecycle.switchMap
import com.example.szlakigrskieam.database.AppDatabase


class TrailViewModel(application: Application) : AndroidViewModel(application) {
    private val trailDao = AppDatabase.getInstance(application).trailDao()
    private val _category = MutableLiveData("górski")

    val trails: LiveData<List<Trail>> = _category.switchMap { trailDao.getTrailsByCategory(it) }

    fun setCategory(category: String) {
        _category.value = category
    }

    fun getTrail(id: Int): LiveData<Trail> {
        return trailDao.getTrailById(id)
    }
}