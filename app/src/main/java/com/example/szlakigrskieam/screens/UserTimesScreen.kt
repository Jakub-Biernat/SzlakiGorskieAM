package com.example.szlakigrskieam.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.szlakigrskieam.database.AppDatabase
import com.example.szlakigrskieam.database.TrailTime
import com.example.szlakigrskieam.viewmodel.TrailViewModel
import kotlinx.coroutines.flow.collectLatest

data class TrailTimeWithName(
    val id: Int,
    val trailId: Int,
    val timeMillis: Long,
    val date: Long,
    val trailName: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserTimesScreen(
    viewModel: TrailViewModel,
    navController: NavController,
    onMenuClick: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val db = remember { AppDatabase.getInstance(context) }

    val times by produceState(initialValue = emptyList<TrailTimeWithName>()) {
        db.trailTimeDao().getAllTimesWithTrailName().collectLatest {
            value = it
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Moje czasy") },

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

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            items(times) { time ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Column(Modifier.padding(12.dp)) {

                        Text(
                            text = "Nazwa szlaku: ${time.trailName}",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text("Czas: ${formatTime(time.timeMillis)}")
                        Text("Data: ${formatDate(time.date)}")
                    }
                }
            }
        }
    }
}