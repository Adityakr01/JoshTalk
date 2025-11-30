package com.example.sampletask.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StartScreen(onStart: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Let's start with a Sample Task for practice.")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Pehele hum ek sample task karte hain.")
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onStart) {
            Text(text = "Start Sample Task")
        }
    }
}
