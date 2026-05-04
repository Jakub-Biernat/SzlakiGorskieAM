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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.VerticalDivider
import androidx.compose.ui.Alignment
import com.example.szlakigrskieam.viewmodel.StopwatchViewModel
import androidx.compose.material3.*
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.DrawerValue
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import com.example.szlakigrskieam.viewmodel.SettingsViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsViewModel: SettingsViewModel = viewModel()

            SzlakiGórskieAMTheme(darkTheme = settingsViewModel.darkMode) {
                Main(
                    darkMode = settingsViewModel.darkMode,
                    onToggleDarkMode = {
                        settingsViewModel.darkMode = !settingsViewModel.darkMode
                    }
                )
            }
        }
    }
}

@Composable
fun Main(
    darkMode: Boolean,
    onToggleDarkMode: () -> Unit
) {
    val navController = rememberNavController()
    val trailViewModel: TrailViewModel = viewModel()
    val stopwatchViewModel: StopwatchViewModel = viewModel()

    var selectedTrailId by remember { mutableStateOf<Int?>(null) }

    val configuration = LocalConfiguration.current
    val isTablet = remember(configuration) {
        configuration.smallestScreenWidthDp >= 600
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val openDrawer: () -> Unit = {
        scope.launch { drawerState.open() }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    "Menu",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )

                NavigationDrawerItem(
                    label = { Text("Lista szlaków") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("TrailsListScreen")
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Animacja") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("AnimationScreen")
                    }
                )

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                // 🔥 PRZEŁĄCZNIK DARK MODE
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Tryb ciemny",
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = darkMode,
                        onCheckedChange = { onToggleDarkMode() }
                    )
                }
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = "AnimationScreen"
        ) {

            composable("AnimationScreen") {
                AnimationScreen(
                    onAnimationEnd = {
                        navController.navigate("TrailsListScreen") {
                            popUpTo("AnimationScreen") { inclusive = true }
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
                                    navController.navigate("TrailDetailScreen/$id")
                                },
                                onMenuClick = openDrawer
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
                                    trailId = selectedTrailId!!,
                                    stopwatchViewModel = stopwatchViewModel,
                                    navController = navController,
                                    onMenuClick = openDrawer
                                )
                            }
                        }
                    }
                } else {
                    TrailsListScreen(
                        viewModel = trailViewModel,
                        onClick = { id ->
                            navController.navigate("TrailDetailScreen/$id")
                        },
                        onMenuClick = openDrawer
                    )
                }
            }

            composable("TrailDetailScreen/{trailId}") { backStackEntry ->
                val trailId = backStackEntry.arguments
                    ?.getString("trailId")
                    ?.toIntOrNull() ?: 0

                TrailDetailScreen(
                    viewModel = trailViewModel,
                    trailId = trailId,
                    stopwatchViewModel = stopwatchViewModel,
                    navController = navController,
                    onMenuClick = openDrawer
                )
            }
        }
    }
}