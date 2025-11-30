package com.example.sampletask.ui

import android.graphics.Bitmap
import android.os.SystemClock
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
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
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PhotoCaptureScreen(repo: TaskRepositoryAndroidImpl, onDone: () -> Unit) {
    val ctx = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var description by remember { mutableStateOf("") }
    var feedback by remember { mutableStateOf("") }
    var recordingFilePath by remember { mutableStateOf<String?>(null) }
    var startTime by remember { mutableStateOf(0L) }
    val coroutineScope = rememberCoroutineScope()

    val takePicture = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bmp ->
        bitmap = bmp
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Button(onClick = { takePicture.launch(null) }) { Text("Capture Image") }
        Spacer(modifier = Modifier.height(12.dp))

        bitmap?.let {
            Image(bitmap = it.asImageBitmap(), contentDescription = "Captured", modifier = Modifier.fillMaxWidth().height(240.dp))
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Describe the photo in your language.") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(12.dp))

            val file = File(ctx.filesDir, "photo_audio_${System.currentTimeMillis()}.mp4")
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
                                    recordingFilePath = file.absolutePath
                                    feedback = "Saved audio: ${file.absolutePath}"
                                }
                                true
                            }
                            else -> false
                        }
                    }
                ) { Text("Hold to record (optional)") }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(feedback)
            Spacer(modifier = Modifier.height(12.dp))

            Button(onClick = {
                val photoFile = File(ctx.filesDir, "photo_${System.currentTimeMillis()}.jpg")
                try {
                    FileOutputStream(photoFile).use { out ->
                        it.compress(android.graphics.Bitmap.CompressFormat.JPEG, 90, out)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                val task = Task(
                    id = UUID.randomUUID().toString(),
                    taskType = TaskType.PHOTO_CAPTURE,
                    imagePath = photoFile.absolutePath,
                    audioPath = recordingFilePath,
                    durationSec = 18,
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
}
