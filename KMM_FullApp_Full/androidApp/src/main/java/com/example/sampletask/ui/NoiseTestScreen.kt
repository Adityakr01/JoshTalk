package com.example.sampletask.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun NoiseTestScreen(onPass: () -> Unit) {
    var db by remember { mutableStateOf(0) }
    var status by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Decibel meter (0 - 60 dB): $db dB")
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = {
            status = "Testing..."
            LaunchedEffect(Unit) {
                val samples = mutableListOf<Int>()
                repeat(6) {
                    delay(300)
                    val s = Random.nextInt(20, 55)
                    samples.add(s)
                    db = s
                }
                val avg = samples.average()
                status = if (avg < 40) "Good to proceed" else "Please move to a quieter place"
                if (avg < 40) onPass()
            }
        }) {
            Text(text = "Start Test")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = status)
    }
}
