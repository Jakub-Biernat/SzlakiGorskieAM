package com.example.szlakigrskieam.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.szlakigrskieam.database.Trail
import com.example.szlakigrskieam.viewmodel.TrailViewModel
import kotlinx.coroutines.launch
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Stop
import com.example.szlakigrskieam.viewmodel.StopwatchViewModel
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import com.example.szlakigrskieam.viewmodel.StopwatchState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.input.pointer.pointerInput
import androidx.navigation.NavController
import kotlin.math.abs


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrailDetailScreen(
    viewModel: TrailViewModel,
    trailId: Int,
    stopwatchViewModel: StopwatchViewModel,
    navController: NavController,
    onMenuClick: () -> Unit
) {
    val trail by viewModel.getTrail(trailId).observeAsState()

    val trails by viewModel.trails.observeAsState(emptyList())
    val currentIndex = trails.indexOfFirst { it.id == trailId }

    val times by viewModel
        .observeTimes(trailId)
        .collectAsState(initial = emptyList())

    val context = LocalContext.current

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // zapis aktualnego czasu stopera
                    val state = stopwatchViewModel.stopwatches.value[trailId]
                    if (state != null) {
                        viewModel.saveTime(trailId, state.time)
                    }
                }
            ) {
                Icon(Icons.Default.Save, contentDescription = "Zapisz czas")
            }
        },
        modifier = Modifier.pointerInput(trailId) {
            var totalDrag = 0f

            detectHorizontalDragGestures(
                onHorizontalDrag = { change, dragAmount ->
                    change.consume()
                    totalDrag += dragAmount
                },
                onDragEnd = {
                    if (trails.isNotEmpty() && currentIndex != -1) {

                        // swipe w lewo → następny szlak
                        if (totalDrag < -150) {
                            val nextIndex = (currentIndex + 1) % trails.size
                            val nextId = trails[nextIndex].id

                            navController.navigate("TrailDetailScreen/$nextId") {
                                popUpTo("TrailDetailScreen/$trailId") { inclusive = true }
                            }
                        }

                        // swipe w prawo → poprzedni szlak
                        if (totalDrag > 150) {
                            val prevIndex = if (currentIndex - 1 < 0) {
                                trails.size - 1
                            } else {
                                currentIndex - 1
                            }

                            val prevId = trails[prevIndex].id

                            navController.navigate("TrailDetailScreen/$prevId") {
                                popUpTo("TrailDetailScreen/$trailId") { inclusive = true }
                            }
                        }
                    }

                    totalDrag = 0f
                }
            )
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(trail?.name ?: "Ładowanie...") },

                // 👈 LEWA STRONA — POWRÓT
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Powrót"
                        )
                    }
                },

                // 👉 PRAWA STRONA — MENU
                actions = {
                    IconButton(onClick = onMenuClick) {
                        Icon(
                            Icons.Default.Menu,
                            contentDescription = "Menu"
                        )
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            if (trail == null) {
                CircularProgressIndicator()
                return@Column
            }

            Image(
                painter = painterResource(id = trail!!.imageRes),
                contentDescription = trail!!.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(16.dp))

            Text(trail!!.name, style = MaterialTheme.typography.headlineMedium)

            Spacer(Modifier.height(8.dp))

            Text("Kategoria: ${trail!!.category}")
            Text("Długość: ${trail!!.distance} km")
            Text("Start: ${trail!!.trailStart}")
            Text("Koniec: ${trail!!.trailEnd}")

            Spacer(Modifier.height(16.dp))

            Text(text = stringResource(id = trail!!.descRes))

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Więcej szczegółów",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(trail!!.websiteUrl)
                    )
                    context.startActivity(intent)
                }
            )

            Spacer(Modifier.height(24.dp))

            StopwatchSection(
                trailId = trailId,
                trailViewModel = viewModel,
                viewModel = stopwatchViewModel
            )

            Spacer(Modifier.height(16.dp))

            Text("Zapisane czasy:")

            times.forEach {
                Text(
                    text = "${formatTime(it.timeMillis)} — ${formatDate(it.date)}"
                )
            }
        }
    }
}

@Composable
fun StopwatchSection(
    trailId: Int,
    trailViewModel: TrailViewModel,
    viewModel: StopwatchViewModel
) {

    val stopwatches by viewModel.stopwatches.collectAsState()
    val state = stopwatches[trailId] ?: StopwatchState()

    val seconds = (state.time / 1000) % 60
    val minutes = (state.time / (1000 * 60)) % 60
    val hours = (state.time / (1000 * 60 * 60))

    Column {
        Row{
            Text(
                text = String.format("%02d:%02d:%02d", hours, minutes, seconds),
                style = MaterialTheme.typography.headlineLarge
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            IconButton(onClick = { viewModel.start(trailId) }) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Start")
            }

            IconButton(onClick = {
                viewModel.stop(trailId)
            }) {
                Icon(Icons.Default.Stop, contentDescription = "Stop")
            }

            IconButton(onClick = { viewModel.reset(trailId) }) {
                Icon(Icons.Default.Refresh, contentDescription = "Reset")
            }
        }
    }
}

fun formatTime(millis: Long): String {
    val seconds = (millis / 1000) % 60
    val minutes = (millis / (1000 * 60)) % 60
    val hours = (millis / (1000 * 60 * 60))
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}

fun formatDate(time: Long): String {
    val sdf = java.text.SimpleDateFormat("dd.MM.yyyy HH:mm")
    return sdf.format(java.util.Date(time))
}
