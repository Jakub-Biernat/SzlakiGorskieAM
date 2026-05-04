package com.example.szlakigrskieam

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.szlakigrskieam.screens.*
import com.example.szlakigrskieam.ui.theme.SzlakiGórskieAMTheme
import com.example.szlakigrskieam.viewmodel.TrailViewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.VerticalDivider
import androidx.compose.ui.Alignment


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Main()
        }
    }
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun Main(){
    val navController = rememberNavController()
    val trailViewModel: TrailViewModel = viewModel()

    var selectedTrailId by remember { mutableStateOf<Int?>(null) }
    val isTablet = LocalConfiguration.current.screenWidthDp >= 600

    LaunchedEffect(isTablet) {
        if (isTablet && navController.currentDestination?.route?.startsWith("TrailDetailScreen") == true) {
            navController.popBackStack("TrailsListScreen", inclusive = false)
        }
    }

    NavHost(navController = navController, startDestination = "AnimationScreen"){
        composable("AnimationScreen") {
            AnimationScreen(
                onAnimationEnd = {
                    navController.navigate("TrailsListScreen") {
                        popUpTo("SplashScreen") { inclusive = true }
                    }
                }
            )
        }

        composable("TrailsListScreen") {
            if (isTablet) {
                Row(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier.weight(1f)) {
                        TrailsListScreen(
                            viewModel = trailViewModel,
                            onClick = { id ->
                                selectedTrailId = id
                            }
                        )
                    }

                    VerticalDivider()

                    Box(modifier = Modifier.weight(1.5f)) {
                        if (selectedTrailId == null) {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text("Wybierz szlak z listy po lewej")
                            }
                        } else {
                            TrailDetailScreen(
                                viewModel = trailViewModel,
                                trailId = selectedTrailId!!
                            )
                        }
                    }
                }
            } else {
                TrailsListScreen(
                    viewModel = trailViewModel,
                    onClick = { id ->
                        selectedTrailId = id
                        navController.navigate("TrailDetailScreen/$id")
                    }
                )
            }
        }

        composable("TrailDetailScreen/{trailId}") { backStackEntry ->
            val trailId = backStackEntry.arguments
                ?.getString("trailId")
                ?.toIntOrNull() ?: 0

            TrailDetailScreen(
                viewModel = trailViewModel,
                trailId = trailId
            )
        }
    }
}