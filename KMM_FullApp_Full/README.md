# KMM Sample Task App (Full)

This is a ready-to-run Android-first Kotlin project implementing the Sample Task prototype:
- Start Screen
- Noise Test (simulated)
- Task selection
- Text Reading recording (press-and-hold)
- Image Description (record)
- Photo Capture + optional audio
- Local Task History (JSON file storage)
- Networking helper to fetch product description (dummyjson)

## Build the APK (locally)
1. Install JDK 17 and Android SDK.
2. From project root run:
   ```
   ./gradlew :androidApp:assembleDebug
   ```
3. APK output:
   ```
   androidApp/build/outputs/apk/debug/androidApp-debug.apk
   ```

## Notes
- This project uses MediaRecorder for audio and TakePicturePreview for camera.
- Permissions: RECORD_AUDIO and CAMERA are requested; handle denials in production.
- Storage: tasks are saved to `filesDir/tasks.json` for simplicity. Consider Room/SQLDelight for production.
