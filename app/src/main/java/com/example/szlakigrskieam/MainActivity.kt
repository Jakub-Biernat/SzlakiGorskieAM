package com.example.szlakigrskieam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.szlakigrskieam.screens.*
import com.example.szlakigrskieam.ui.theme.SzlakiGórskieAMTheme
import com.example.szlakigrskieam.viewmodel.TrailViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Main()
        }
    }
}

@Composable
fun Main(){
    val navController = rememberNavController()
    val trailViewModel: TrailViewModel = viewModel()

    NavHost(navController = navController, startDestination = "TrailsListScreen"){
        composable("TrailsListScreen") {
            TrailsListScreen(
                viewModel = trailViewModel,
                onClick = {id ->
                    navController.navigate("TrailDetailScreen/$id")
                }
            )
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