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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrailDetailScreen(
    viewModel: TrailViewModel,
    trailId: Int,
) {
    val trail by viewModel.getTrail(trailId).observeAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(trail?.name ?: "Ładowanie...") }
            )
        }
    ) { padding ->
        val context = LocalContext.current
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            if (trail == null) {

                CircularProgressIndicator()

            } else {

                Image(
                    painter = painterResource(id = trail!!.imageRes),
                    contentDescription = trail!!.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = trail!!.name,
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text("Kategoria: ${trail!!.category}")
                Text("Długość: ${trail!!.distance} km")
                Text("Start: ${trail!!.trailStart}")
                Text("Koniec: ${trail!!.trailEnd}")

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(id = trail!!.descRes)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Więcej szczegółów",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(trail!!.websiteUrl))
                        context.startActivity(intent)
                    }
                )
            }
        }
    }
}
