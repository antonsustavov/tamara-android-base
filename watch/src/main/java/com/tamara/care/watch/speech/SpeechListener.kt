package com.tamara.care.watch.speech

import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.lifecycle.LifecycleService
import com.tamara.care.watch.call.CallService
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class SpeechListener : LifecycleService(), RecognitionListener {
    companion object {
        const val TAG = "Speech Listener"
        const val KEY = "help"
    }
    private lateinit var speechRecognizer: SpeechRecognizer
    @Inject
    lateinit var callService: CallService

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, ">>>>>>>>>>>>>>>>>>>>> START")
        Log.i(TAG, SpeechRecognizer.isRecognitionAvailable(this).toString())
        Log.i(TAG, (Looper.myLooper() == Looper.getMainLooper()).toString())

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        speechRecognizer.setRecognitionListener(this)

        startListening()

        return super.onStartCommand(intent, flags, startId)
    }

    private fun startListening() {
        val voice = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        voice.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        voice.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        speechRecognizer.startListening(voice)
    }

    override fun onReadyForSpeech(params: Bundle?) {
        Log.i(TAG, params.toString())
    }

    override fun onBeginningOfSpeech() {
        Log.i(TAG, "Beginning speech")
    }

    override fun onRmsChanged(rmsdB: Float) {
        Log.i(TAG, rmsdB.toString())
    }

    override fun onBufferReceived(buffer: ByteArray?) {
        Log.i(TAG, buffer.toString())
    }

    override fun onEndOfSpeech() {
        Log.i(TAG, "END")
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
        Log.i(TAG, message)
        speechRecognizer.cancel()
        startListening()
    }

    override fun onResults(results: Bundle?) {
        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        if (matches != null) {
            Log.i(TAG, matches.toString())
            processVoiceRecognition(matches)
        } else {
            Log.i(TAG, "SPEECH Result null")
        }
        speechRecognizer.cancel()
        startListening()
    }

    private fun processVoiceRecognition(matches: ArrayList<String>) {
        if (matches.contains(KEY)) {
            callService.call()
        }
    }

    override fun onPartialResults(partialResults: Bundle?) {
        Log.i(TAG, partialResults.toString())
    }

    override fun onEvent(eventType: Int, params: Bundle?) {
        Log.i(TAG, eventType.toString() + " " + params.toString())
    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer.destroy()
    }
}