package com.example.deadlockdetectionsimulator.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.deadlockdetectionsimulator.algorithms.DeadlockDetector
import com.example.deadlockdetectionsimulator.models.DeadlockResult
import com.example.deadlockdetectionsimulator.ui.components.MatrixInput
import com.example.deadlockdetectionsimulator.ui.components.VectorInput

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeadlockScreen(onBack: () -> Unit) {
    var pCount by remember { mutableStateOf(5) }
    var rCount by remember { mutableStateOf(3) }
    
    var allocation by remember(pCount, rCount) { 
        mutableStateOf(Array(pCount) { IntArray(rCount) }) 
    }
    var request by remember(pCount, rCount) { 
        mutableStateOf(Array(pCount) { IntArray(rCount) }) 
    }
    var maxMatrix by remember(pCount, rCount) { 
        mutableStateOf(Array(pCount) { IntArray(rCount) }) 
    }
    var available by remember(rCount) { 
        mutableStateOf(IntArray(rCount)) 
    }
    
    var result by remember { mutableStateOf<DeadlockResult?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Deadlock Detection") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = pCount.toString(),
                    onValueChange = { pCount = it.toIntOrNull() ?: 0 },
                    label = { Text("Processes") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = rCount.toString(),
                    onValueChange = { rCount = it.toIntOrNull() ?: 0 },
                    label = { Text("Resources") },
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (pCount > 0 && rCount > 0) {
                MatrixInput(
                    title = "Allocation Matrix",
                    rows = pCount,
                    cols = rCount,
                    data = allocation,
                    onValueChange = { r, c, v -> allocation[r][c] = v }
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                MatrixInput(
                    title = "Request Matrix",
                    rows = pCount,
                    cols = rCount,
                    data = request,
                    onValueChange = { r, c, v -> request[r][c] = v }
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                MatrixInput(
                    title = "Max Matrix",
                    rows = pCount,
                    cols = rCount,
                    data = maxMatrix,
                    onValueChange = { r, c, v -> maxMatrix[r][c] = v }
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                VectorInput(
                    title = "Available Resources",
                    size = rCount,
                    data = available,
                    onValueChange = { i, v -> available[i] = v }
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = {
                        // Validation: Allocation + Request <= Max
                        var valid = true
                        for (i in 0 until pCount) {
                            for (j in 0 until rCount) {
                                if (allocation[i][j] + request[i][j] > maxMatrix[i][j]) {
                                    valid = false
                                    break
                                }
                            }
                        }
                        
                        if (valid) {
                            errorMessage = null
                            result = DeadlockDetector.detect(allocation, request, maxMatrix, available)
                        } else {
                            errorMessage = "Error: Allocation + Request cannot exceed Max for any process/resource."
                            result = null
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Run Detection")
                }
                
                errorMessage?.let { msg ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = msg, color = MaterialTheme.colorScheme.error)
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedButton(
                    onClick = {
                        allocation = Array(pCount) { IntArray(rCount) }
                        request = Array(pCount) { IntArray(rCount) }
                        maxMatrix = Array(pCount) { IntArray(rCount) }
                        available = IntArray(rCount)
                        result = null
                        errorMessage = null
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Reset Inputs")
                }
                
                result?.let { res ->
                    Spacer(modifier = Modifier.height(24.dp))
                    Card {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = if (res.isDeadlocked) "DEADLOCK DETECTED" else "NO DEADLOCK",
                                style = MaterialTheme.typography.headlineSmall,
                                color = if (res.isDeadlocked) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                            )
                            if (res.isDeadlocked) {
                                Text("Deadlocked Processes: ${res.deadlockedProcesses.joinToString(", ") { "P$it" }}")
                            }
                            Text("Finished Processes: ${res.finishedProcesses.joinToString(", ") { "P$it" }}")
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Work Vector History:", style = MaterialTheme.typography.titleMedium)
                            res.workHistory.forEachIndexed { index, work ->
                                Text("Step $index: [${work.joinToString(", ")}]")
                            }
                        }
                    }
                }
            }
        }
    }
}
