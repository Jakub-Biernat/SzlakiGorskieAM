package com.example.szlakigrskieam.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.szlakigrskieam.viewmodel.TrailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrailsListScreen(
    viewModel: TrailViewModel,
    onClick: (Int) -> Unit,
    onMenuClick: () -> Unit
) {

    val trails by viewModel.trails.observeAsState(emptyList())

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Lista szlaków") },
                navigationIcon = {
                    IconButton(onMenuClick) {
                        Icon(Icons.Default.Menu, contentDescription = null)
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
                Button({ viewModel.setCategory("górski") }) { Text("Górskie") }
                Button({ viewModel.setCategory("rowerowy") }) { Text("Rowerowe") }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {

                items(trails, key = { it.id }) { trail ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onClick(trail.id) }
                    ) {

                        Column {

                            Image(
                                painter = painterResource(trail.imageRes),
                                contentDescription = trail.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp),
                                contentScale = ContentScale.Crop
                            )

                            Column(Modifier.padding(8.dp)) {
                                Text(
                                    trail.name,
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Text(
                                    "${trail.distance} km",
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