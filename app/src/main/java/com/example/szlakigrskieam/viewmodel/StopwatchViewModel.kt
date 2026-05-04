package com.example.szlakigrskieam.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class StopwatchState(
    val time: Long = 0L,
    val isMeasuring: Boolean = false
)
class StopwatchViewModel : ViewModel() {
    private val _stopwatches = MutableStateFlow<Map<Int, StopwatchState>>(emptyMap())
    val stopwatches: StateFlow<Map<Int, StopwatchState>> = _stopwatches.asStateFlow()
    private val stopwatchesJobs = mutableMapOf<Int, Job>()

    fun start(trailId: Int) {
        _stopwatches.update { map ->
            val state = map[trailId] ?: StopwatchState()
            map + (trailId to state.copy(isMeasuring = true))
        }

        stopwatchesJobs[trailId]?.cancel()
        stopwatchesJobs[trailId] = viewModelScope.launch {
            while(isActive){
                delay(1000L)
                _stopwatches.update { map ->
                    val state = map[trailId] ?: StopwatchState()
                    map + (trailId to state.copy(time = state.time + 1000L))
                }
            }
        }
    }

    fun stop(trailId: Int) {
        _stopwatches.update { map ->
            val state = map[trailId] ?: StopwatchState()
            map + (trailId to state.copy(isMeasuring = false))
        }
        stopwatchesJobs[trailId]?.cancel()
    }

    fun reset(trailId: Int) {
        stop(trailId)
        _stopwatches.update { map ->
            map + (trailId to StopwatchState())
        }
    }
}