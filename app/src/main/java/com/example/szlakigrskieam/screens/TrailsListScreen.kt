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
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale


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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onClick(trail.id) }
                            .padding(8.dp)
                    ) {

                        Image(
                            painter = painterResource(id = trail.imageRes),
                            contentDescription = trail.name,
                            modifier = Modifier
                                .size(64.dp)
                                .padding(end = 8.dp),
                            contentScale = ContentScale.Crop
                        )

                        Column {
                            Text(text = trail.name)
                            Text(text = "${trail.distance} km")
                        }
                    }
                }
            }
        }
    }
}