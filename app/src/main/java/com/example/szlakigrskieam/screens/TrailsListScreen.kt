package com.example.szlakigrskieam.screens

import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrailsListScreen(
    viewModel: TrailViewModel,
    onClick: (Int) -> Unit,
    onMenuClick: () -> Unit   // 👈 NOWE
) {
    val trails by viewModel.trails.observeAsState(emptyList())

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Lista Szlaków") },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            Row(
                modifier = Modifier.padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = { viewModel.setCategory("górski") }) {
                    Text("Górskie")
                }
                Button(onClick = { viewModel.setCategory("rowerowy") }) {
                    Text("Rowerowe")
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(trails) { trail ->

                    Card(
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onClick(trail.id) }
                            .animateContentSize()
                    ) {

                        Column {

                            Image(
                                painter = painterResource(id = trail.imageRes),
                                contentDescription = trail.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp),
                                contentScale = ContentScale.Crop
                            )

                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(
                                    text = trail.name,
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Text(
                                    text = "${trail.distance} km",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}