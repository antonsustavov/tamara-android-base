package com.tamara.care.watch.speech

import android.app.Notification
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.lifecycle.LifecycleService
import com.tamara.care.watch.R
import com.tamara.care.watch.call.CallService
import com.tamara.care.watch.manager.NotificationManager
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class SpeechListener : LifecycleService(), RecognitionListener {
    companion object {
        @JvmStatic
        val TAG = "Speech Listener"
        @JvmStatic
        val KEY = "help"
        @JvmStatic
        val ID = 2
    }

    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var notificationManager: NotificationManager
    private lateinit var currentNotification: Notification

    @Inject
    lateinit var callService: CallService

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "START SPEECH")

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        speechRecognizer.setRecognitionListener(this)

        startListening()

        return super.onStartCommand(intent, flags, startId)
    }

    private fun startListening() {
        val voice = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        voice.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        voice.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        speechRecognizer.startListening(voice)
    }

    override fun onReadyForSpeech(params: Bundle?) {
        Log.d(TAG, params.toString())
    }

    override fun onBeginningOfSpeech() {
        Log.d(TAG, "Beginning speech")
    }

    override fun onRmsChanged(rmsdB: Float) {
        Log.d(TAG, rmsdB.toString())
    }

    override fun onBufferReceived(buffer: ByteArray?) {
        Log.d(TAG, buffer.toString())
    }

    override fun onEndOfSpeech() {
        Log.d(TAG, "END")
    }

    override fun onError(error: Int) {
        val message: String = when (error) {
            SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
            SpeechRecognizer.ERROR_CLIENT -> "Client side error"
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions"
            SpeechRecognizer.ERROR_NETWORK -> "Network error"
            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
            SpeechRecognizer.ERROR_NO_MATCH -> "No match"
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "RecognitionService busy"
            SpeechRecognizer.ERROR_SERVER -> "error from server"
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input"
            else -> "Didn't understand, please try again."
        }
        Log.d(TAG, message)
        speechRecognizer.cancel()
        startListening()
    }

    override fun onResults(results: Bundle?) {
        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        if (matches != null) {
            Log.i(TAG, matches.toString())
            processVoiceRecognition(matches)
        } else {
            Log.d(TAG, "SPEECH Result null")
        }
        speechRecognizer.cancel()
        startListening()
    }

    private fun processVoiceRecognition(matches: ArrayList<String>) {
        Log.i(TAG, ">>>>>> SPEECH MATCHING $matches")
        matches.forEach {
            if (it.contains(KEY)) {
                callService.call(applicationContext)
            }
        }
    }

    override fun onPartialResults(partialResults: Bundle?) {
        Log.d(TAG, partialResults.toString())
    }

    override fun onEvent(eventType: Int, params: Bundle?) {
        Log.d(TAG, eventType.toString() + " " + params.toString())
    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer.destroy()
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "ON CREATE")
        notificationManager = NotificationManager(this)
        createNotification()
    }

    private fun createNotification() {
        notificationManager.createNotification(
            title = getString(R.string.service_state),
            description = "run",
        )
        currentNotification = notificationManager.getCurrentNotification()
        startForeground(ID, currentNotification)
    }
}