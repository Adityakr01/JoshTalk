package com.example.sampletask.models

import kotlinx.serialization.Serializable

@Serializable
data class Task(
    val id: String,
    val taskType: TaskType,
    val text: String? = null,
    val imageUrl: String? = null,
    val imagePath: String? = null,
    val audioPath: String? = null,
    val durationSec: Int,
    val timestamp: String // ISO-8601
)

@Serializable
enum class TaskType { TEXT_READING, IMAGE_DESCRIPTION, PHOTO_CAPTURE }
