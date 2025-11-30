package com.example.sampletask.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TaskSelectionScreen(onText: () -> Unit, onImage: () -> Unit, onPhoto: () -> Unit, onHistory: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = onText, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) { Text("Text Reading Task") }
        Button(onClick = onImage, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) { Text("Image Description Task") }
        Button(onClick = onPhoto, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) { Text("Photo Capture Task") }
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onHistory) { Text("Task History") }
    }
}
