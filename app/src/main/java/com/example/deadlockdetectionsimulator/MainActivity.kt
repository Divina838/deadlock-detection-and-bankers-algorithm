package com.example.deadlockdetectionsimulator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.deadlockdetectionsimulator.ui.screens.BankerScreen
import com.example.deadlockdetectionsimulator.ui.screens.DeadlockScreen
import com.example.deadlockdetectionsimulator.ui.screens.HomeScreen
import com.example.deadlockdetectionsimulator.ui.theme.DeadlockDetectionSimulatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DeadlockDetectionSimulatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onNavigateToDeadlock = { navController.navigate("deadlock") },
                onNavigateToBanker = { navController.navigate("banker") }
            )
        }
        composable("deadlock") {
            DeadlockScreen(onBack = { navController.popBackStack() })
        }
        composable("banker") {
            BankerScreen(onBack = { navController.popBackStack() })
        }
    }
}
