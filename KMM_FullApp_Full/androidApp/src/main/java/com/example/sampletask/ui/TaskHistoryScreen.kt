package com.example.sampletask.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.sampletask.models.Task
import com.example.sampletask.repo.TaskRepositoryAndroidImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@Composable
fun TaskHistoryScreen(repo: TaskRepositoryAndroidImpl) {
    var tasks by remember { mutableStateOf<List<Task>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            tasks = withContext(Dispatchers.IO) { repo.allTasks() }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Total Tasks: ${tasks.size}")
        val totalDuration = tasks.sumOf { it.durationSec }
        Text("Total recording duration: ${totalDuration}s")
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn {
            items(tasks) { t ->
                Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    Text("${t.taskType} | ${t.durationSec}s | ${t.timestamp}")
                    Spacer(modifier = Modifier.height(4.dp))
                    when (t.taskType) {
                        com.example.sampletask.models.TaskType.TEXT_READING -> {
                            Text(t.text ?: "")
                        }
                        com.example.sampletask.models.TaskType.IMAGE_DESCRIPTION -> {
                            t.imageUrl?.let { AsyncImage(model = it, contentDescription = null, modifier = Modifier.height(120.dp).fillMaxWidth()) }
                        }
                        com.example.sampletask.models.TaskType.PHOTO_CAPTURE -> {
                            t.imagePath?.let {
                                val file = File(it)
                                if (file.exists()) {
                                    AsyncImage(model = it, contentDescription = null, modifier = Modifier.height(120.dp).fillMaxWidth())
                                } else {
                                    Text("Photo missing")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
