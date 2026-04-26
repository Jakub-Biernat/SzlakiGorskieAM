package com.example.szlakigrskieam.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.szlakigrskieam.viewmodel.TrailViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrailsListScreen(viewModel: TrailViewModel, onClick: (Int) -> Unit) {
    val trails by viewModel.trails.observeAsState(emptyList())

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Szlaki") }
            )
        }
    ) {
        padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {
            Row {
                Button(onClick = {viewModel.setCategory("górski")}) {
                    Text("Górskie")
                }
                Button(onClick = {viewModel.setCategory("rowerowy")}) {
                    Text("Rowerowe")
                }
            }

            LazyColumn {
                items(trails) { trail ->
                    Text(
                        text = trail.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onClick(trail.id) }
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}