package com.example.szlakigrskieam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.szlakigrskieam.screens.*
import com.example.szlakigrskieam.ui.theme.SzlakiGórskieAMTheme
import com.example.szlakigrskieam.viewmodel.SettingsViewModel
import com.example.szlakigrskieam.viewmodel.StopwatchViewModel
import com.example.szlakigrskieam.viewmodel.TrailViewModel
import kotlinx.coroutines.launch

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

    val configuration = LocalConfiguration.current
    val isTablet = remember(configuration) {
        configuration.smallestScreenWidthDp >= 600
    }

    var selectedTrailId by remember { mutableStateOf<Int?>(null) }

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
                    label = { Text("Moje czasy") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("UserTimesScreen")
                    }
                )

                Divider(modifier = Modifier.padding(vertical = 8.dp))

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
                                    selectedTrailId = id
                                },
                                onMenuClick = openDrawer
                            )
                        }

                        VerticalDivider()

                        Box(modifier = Modifier.weight(1.5f)) {
                            if (selectedTrailId == null) {
                                Box(
                                    Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
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

            composable("UserTimesScreen") {
                UserTimesScreen(
                    navController = navController,
                    onMenuClick = openDrawer
                )
            }
        }
    }
}