package com.tamara.care.watch.speech

import android.Manifest
import android.app.Notification
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_MIN
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleService
import com.tamara.care.watch.R
import java.util.*

class SpeechListener : LifecycleService(), RecognitionListener {
    private lateinit var speechRecognizer: SpeechRecognizer
    private var isActivated: Boolean = false
    private val activationKeyword: String = "help"
    private val foregroundId: Int = 1000001

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED -> {
//            requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), 9379995)
//        }
//        if (ContextCompat.checkSelfPermission(this, "android.permission.RECORD_AUDIO") != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(intent., arrayOf(Manifest.permission.RECORD_AUDIO), 9379995))
//        }
        Log.i(">>>>>> START", ">>>>>>>>>>>>>>>>>>>>> START")
        Log.i(">>>>>> START", SpeechRecognizer.isRecognitionAvailable(this).toString())

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
//        speechRecognizer = SpeechRecognizer.createOnDeviceSpeechRecognizer(this)
        speechRecognizer.setRecognitionListener(this)

        val voice = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        voice.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        voice.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        voice.putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true)
//        voice.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10)
//        startActivityForResult(voice, 1111)

        speechRecognizer.startListening(voice)

//        startForeground(this)

        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForeground(service: Service) {
        val notificationBuilder = NotificationCompat.Builder(this, "channel id")
        val notification = notificationBuilder.setOngoing(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(PRIORITY_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        startForeground(foregroundId, notification)

//        val notification: Notification = Notification.Builder(service).notification
//        service.startForeground(foregroundId, notification)
    }


    override fun onReadyForSpeech(params: Bundle?) {
        Log.i(">>>>>> READY", ">>>>> READY")
    }

    override fun onBeginningOfSpeech() {
        TODO("Not yet implemented")
    }

    override fun onRmsChanged(rmsdB: Float) {
        TODO("Not yet implemented")
    }

    override fun onBufferReceived(buffer: ByteArray?) {
        TODO("Not yet implemented")
    }

    override fun onEndOfSpeech() {
        TODO("Not yet implemented")
    }

    override fun onError(error: Int) {
        val message: String
        message = when (error) {
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
        Log.i(">>>>>> ERROR", message)
    }

    override fun onResults(results: Bundle?) {
        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
//        val scores = results?.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES)
        Log.i(">>>>>>>>> SPOKEN TEXT ", matches!![0])
//        if (matches != null) {
//            if (isActivated) {
//                isActivated = false
////                stopRecognition()
//                Log.i("Matches", matches.toString())
//            } else {
//                matches.firstOrNull { it.contains(other = activationKeyword, ignoreCase = true) }
//                    ?.let {
//                        isActivated = true
//                    }
//                Log.i("Matches", matches.toString())
////                startRecognition()
//            }
//        }
    }

    override fun onPartialResults(partialResults: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun onEvent(eventType: Int, params: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
//        speechRecognizer.stopListening()
//        speechRecognizer.destroy()
        super.onDestroy()
        speechRecognizer.destroy()
    }
}