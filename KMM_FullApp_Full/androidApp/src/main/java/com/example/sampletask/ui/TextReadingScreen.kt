package com.example.sampletask.ui

import android.os.SystemClock
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.sampletask.models.Task
import com.example.sampletask.models.TaskType
import com.example.sampletask.repo.TaskRepositoryAndroidImpl
import com.example.sampletask.util.AudioRecorder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TextReadingScreen(repo: TaskRepositoryAndroidImpl, onDone: () -> Unit) {
    val ctx = LocalContext.current
    var sampleText by remember { mutableStateOf("Mega long lasting fragrance... (placeholder description)") }
    var feedback by remember { mutableStateOf("") }
    var recordingFilePath by remember { mutableStateOf<String?>(null) }
    var startTime by remember { mutableStateOf(0L) }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(sampleText)
        Spacer(modifier = Modifier.height(12.dp))
        Text("Instruction: Read the passage aloud in your native language.")
        Spacer(modifier = Modifier.height(12.dp))

        val file = File(ctx.filesDir, "record_${System.currentTimeMillis()}.mp4")
        val recorder = remember { AudioRecorder(file) }

        Box(modifier = Modifier.fillMaxWidth().height(80.dp)) {
            Button(
                onClick = { /* handled by pointer */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInteropFilter { motionEvent ->
                        when (motionEvent.action) {
                            android.view.MotionEvent.ACTION_DOWN -> {
                                recorder.start()
                                startTime = SystemClock.elapsedRealtime()
                                feedback = "Recording..."
                                true
                            }
                            android.view.MotionEvent.ACTION_UP, android.view.MotionEvent.ACTION_CANCEL -> {
                                recorder.stop()
                                val durationMs = SystemClock.elapsedRealtime() - startTime
                                val durationSec = (durationMs / 1000).toInt()
                                if (durationSec < 10) {
                                    feedback = "Recording too short (min 10 s)."
                                    try { if (file.exists()) file.delete() } catch (_: Exception) {}
                                } else if (durationSec > 20) {
                                    feedback = "Recording too long (max 20 s)."
                                    try { if (file.exists()) file.delete() } catch (_: Exception) {}
                                } else {
                                    feedback = "Saved: ${file.absolutePath}, duration: ${durationSec}s"
                                    recordingFilePath = file.absolutePath
                                }
                                true
                            }
                            else -> false
                        }
                    }
            ) {
                Text(text = "Hold to record")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(text = feedback)
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val path = recordingFilePath ?: run {
                feedback = "No valid recording to submit."
                return@Button
            }
            val duration = 12
            val task = Task(
                id = UUID.randomUUID().toString(),
                taskType = TaskType.TEXT_READING,
                text = sampleText,
                audioPath = path,
                durationSec = duration,
                timestamp = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(Date())
            )

            coroutineScope.launch {
                withContext(Dispatchers.IO) { repo.saveTask(task) }
                onDone()
            }
        }) { Text("Submit") }
    }
}
