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
import coil.compose.AsyncImage
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
fun ImageDescriptionScreen(repo: TaskRepositoryAndroidImpl, onDone: () -> Unit) {
    val ctx = LocalContext.current
    val sampleImage = "https://cdn.dummyjson.com/product-images/14/2.jpg"
    var feedback by remember { mutableStateOf("") }
    var recordingFilePath by remember { mutableStateOf<String?>(null) }
    var startTime by remember { mutableStateOf(0L) }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        AsyncImage(model = sampleImage, contentDescription = "Sample image", modifier = Modifier.fillMaxWidth().height(220.dp))
        Spacer(modifier = Modifier.height(12.dp))
        Text("Instruction: Describe what you see in your native language.")
        Spacer(modifier = Modifier.height(12.dp))

        val file = File(ctx.filesDir, "desc_${System.currentTimeMillis()}.mp4")
        val recorder = remember { AudioRecorder(file) }

        Box(modifier = Modifier.fillMaxWidth().height(80.dp)) {
            Button(onClick = {}, modifier = Modifier
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
                Text("Hold to record")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(feedback)
        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = {
            val path = recordingFilePath ?: run {
                feedback = "No valid recording to submit."
                return@Button
            }

            val task = Task(
                id = UUID.randomUUID().toString(),
                taskType = TaskType.IMAGE_DESCRIPTION,
                imageUrl = sampleImage,
                audioPath = path,
                durationSec = 12,
                timestamp = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(Date())
            )

            coroutineScope.launch {
                withContext(Dispatchers.IO) { repo.saveTask(task) }
                onDone()
            }
        }) {
            Text("Submit")
        }
    }
}
