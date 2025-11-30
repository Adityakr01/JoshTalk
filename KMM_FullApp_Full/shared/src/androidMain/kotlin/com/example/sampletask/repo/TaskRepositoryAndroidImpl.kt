package com.example.sampletask.repo

import android.content.Context
import com.example.sampletask.models.Task
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class TaskRepositoryAndroidImpl(private val context: Context) : TaskRepository {
    private val file: File get() = File(context.filesDir, "tasks.json")
    private val json = Json { prettyPrint = true; ignoreUnknownKeys = true }

    override suspend fun saveTask(task: Task) {
        val items = allTasks().toMutableList()
        items.add(task)
        file.writeText(json.encodeToString(items))
    }

    override suspend fun allTasks(): List<Task> {
        if (!file.exists()) return emptyList()
        return try {
            json.decodeFromString(file.readText())
        } catch (e: Exception) {
            emptyList()
        }
    }
}
