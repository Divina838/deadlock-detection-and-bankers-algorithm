package com.example.deadlockdetectionsimulator.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun MatrixInput(
    title: String,
    rows: Int,
    cols: Int,
    data: Array<IntArray>,
    onValueChange: (Int, Int, Int) -> Unit
) {
    Column {
        Text(text = title, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        for (i in 0 until rows) {
            Row(modifier = Modifier.fillMaxWidth()) {
                for (j in 0 until cols) {
                    var text by remember(rows, cols, i, j, data[i][j]) { 
                        mutableStateOf(data[i][j].toString()) 
                    }
                    OutlinedTextField(
                        value = text,
                        onValueChange = { newValue ->
                            if (newValue.isEmpty() || newValue.all { it.isDigit() }) {
                                text = newValue
                                val intValue = newValue.toIntOrNull() ?: 0
                                onValueChange(i, j, intValue)
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(2.dp),
                        label = { Text("P$i,R$j") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                }
            }
        }
    }
}

@Composable
fun VectorInput(
    title: String,
    size: Int,
    data: IntArray,
    onValueChange: (Int, Int) -> Unit
) {
    Column {
        Text(text = title, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            for (i in 0 until size) {
                var text by remember(size, i, data[i]) { 
                    mutableStateOf(data[i].toString()) 
                }
                OutlinedTextField(
                    value = text,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.all { it.isDigit() }) {
                            text = newValue
                            val intValue = newValue.toIntOrNull() ?: 0
                            onValueChange(i, intValue)
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(2.dp),
                    label = { Text("R$i") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }
        }
    }
}
