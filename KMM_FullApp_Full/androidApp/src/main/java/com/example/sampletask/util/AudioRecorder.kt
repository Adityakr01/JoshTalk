package com.example.sampletask.util

import android.media.MediaRecorder
import java.io.File

class AudioRecorder(private val outFile: File) {
    private var recorder: MediaRecorder? = null

    fun start() {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(outFile.absolutePath)
            try {
                prepare()
                start()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun stop() {
        recorder?.run {
            try { stop() } catch (t: Throwable) { }
            release()
        }
        recorder = null
    }
}
