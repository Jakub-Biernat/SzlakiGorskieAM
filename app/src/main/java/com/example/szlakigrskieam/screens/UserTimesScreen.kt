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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.szlakigrskieam.database.AppDatabase

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
    navController: NavController,
    onMenuClick: () -> Unit
) {

    val context = LocalContext.current
    val db = remember { AppDatabase.getInstance(context) }

    val times by db.trailTimeDao()
        .getAllTimesWithTrailName()
        .collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Moje czasy") },
                navigationIcon = {
                    IconButton({ navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                },
                actions = {
                    IconButton(onMenuClick) {
                        Icon(Icons.Default.Menu, null)
                    }
                }
            )
        }
    ) { padding ->

        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.padding(16.dp)
        ) {

            items(times, key = { it.id }) { time ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Column(Modifier.padding(12.dp)) {

                        Text(
                            "Szlak: ${time.trailName}",
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