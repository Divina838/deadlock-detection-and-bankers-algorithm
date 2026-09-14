package com.example.deadlockdetectionsimulator.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onNavigateToDeadlock: () -> Unit,
    onNavigateToBanker: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "OS Deadlock & Resource Allocation Simulator",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Explore Operating Systems concepts through interactive simulations.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(48.dp))
        Button(
            onClick = onNavigateToDeadlock,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Deadlock Detection")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onNavigateToBanker,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Banker's Algorithm")
        }
    }
}
