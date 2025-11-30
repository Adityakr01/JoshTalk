package com.example.sampletask.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.sampletask.repo.TaskRepositoryAndroidImpl

@Composable
fun AppNavHost(navController: NavHostController, repo: TaskRepositoryAndroidImpl) {
    NavHost(navController = navController, startDestination = "start") {
        composable("start") { StartScreen(onStart = { navController.navigate("noise") }) }
        composable("noise") { NoiseTestScreen(onPass = { navController.navigate("tasks") }) }
        composable("tasks") {
            TaskSelectionScreen(
                onText = { navController.navigate("text") },
                onImage = { navController.navigate("image") },
                onPhoto = { navController.navigate("photo") },
                onHistory = { navController.navigate("history") }
            )
        }
        composable("text") { TextReadingScreen(repo = repo, onDone = { navController.popBackStack() }) }
        composable("image") { ImageDescriptionScreen(repo = repo, onDone = { navController.popBackStack() }) }
        composable("photo") { PhotoCaptureScreen(repo = repo, onDone = { navController.popBackStack() }) }
        composable("history") { TaskHistoryScreen(repo = repo) }
    }
}
