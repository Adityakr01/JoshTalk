package com.example.sampletask.repo

import com.example.sampletask.models.Task

interface TaskRepository {
    suspend fun saveTask(task: Task)
    suspend fun allTasks(): List<Task>
}
