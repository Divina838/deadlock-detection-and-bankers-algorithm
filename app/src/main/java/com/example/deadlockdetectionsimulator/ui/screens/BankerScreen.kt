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
import com.example.deadlockdetectionsimulator.algorithms.BankersAlgorithm
import com.example.deadlockdetectionsimulator.models.BankerResult
import com.example.deadlockdetectionsimulator.ui.components.MatrixInput
import com.example.deadlockdetectionsimulator.ui.components.VectorInput

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BankerScreen(onBack: () -> Unit) {
    var pCount by remember { mutableStateOf(5) }
    var rCount by remember { mutableStateOf(3) }
    
    var allocation by remember(pCount, rCount) { 
        mutableStateOf(Array(pCount) { IntArray(rCount) }) 
    }
    var maxMatrix by remember(pCount, rCount) { 
        mutableStateOf(Array(pCount) { IntArray(rCount) }) 
    }
    var needMatrix by remember(pCount, rCount) {
        mutableStateOf(Array(pCount) { IntArray(rCount) })
    }
    var available by remember(rCount) { 
        mutableStateOf(IntArray(rCount)) 
    }
    
    var result by remember { mutableStateOf<BankerResult?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Banker's Algorithm") },
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
                    onValueChange = { r, c, v -> 
                        allocation[r][c] = v
                        // Sync Need: Need = Max - Allocation
                        needMatrix[r][c] = (maxMatrix[r][c] - v).coerceAtLeast(0)
                    }
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                MatrixInput(
                    title = "Max Matrix",
                    rows = pCount,
                    cols = rCount,
                    data = maxMatrix,
                    onValueChange = { r, c, v -> 
                        maxMatrix[r][c] = v
                        // Sync Need: Need = Max - Allocation
                        needMatrix[r][c] = (v - allocation[r][c]).coerceAtLeast(0)
                    }
                )
                
                Spacer(modifier = Modifier.height(16.dp))

                MatrixInput(
                    title = "Need Matrix",
                    rows = pCount,
                    cols = rCount,
                    data = needMatrix,
                    onValueChange = { r, c, v -> 
                        needMatrix[r][c] = v
                        // Sync Max: Max = Allocation + Need
                        maxMatrix[r][c] = allocation[r][c] + v
                    }
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
                        // Validation: Allocation <= Max
                        var valid = true
                        for (i in 0 until pCount) {
                            for (j in 0 until rCount) {
                                if (allocation[i][j] > maxMatrix[i][j]) {
                                    valid = false
                                    break
                                }
                            }
                        }
                        
                        if (valid) {
                            errorMessage = null
                            result = BankersAlgorithm.calculate(allocation, needMatrix, available)
                        } else {
                            errorMessage = "Error: Allocation cannot exceed Max for any process/resource."
                            result = null
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Run Safety Algorithm")
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedButton(
                    onClick = {
                        allocation = Array(pCount) { IntArray(rCount) }
                        maxMatrix = Array(pCount) { IntArray(rCount) }
                        needMatrix = Array(pCount) { IntArray(rCount) }
                        available = IntArray(rCount)
                        result = null
                        errorMessage = null
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Reset Inputs")
                }
                
                errorMessage?.let { msg ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = msg, color = MaterialTheme.colorScheme.error)
                }
                
                result?.let { res ->
                    Spacer(modifier = Modifier.height(24.dp))
                    Card {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = if (res.isSafe) "STATE IS SAFE" else "STATE IS UNSAFE",
                                style = MaterialTheme.typography.headlineSmall,
                                color = if (res.isSafe) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                            )
                            if (res.isSafe) {
                                Text("Safe Sequence: ${res.safeSequence.joinToString(" -> ") { "P$it" }}")
                            }
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Need Matrix:", style = MaterialTheme.typography.titleMedium)
                            res.needMatrix.forEachIndexed { i, row ->
                                Text("P$i: [${row.joinToString(", ")}]")
                            }

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
