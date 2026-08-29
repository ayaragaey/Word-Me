package com.example.wordme.audio

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.util.Locale

class SentenceSpeechRecognizer(
    private val context: Context,
    private val onResult: (String) -> Unit,
    private val onPartialResult: ((String) -> Unit)? = null
) {
    private val mainHandler = Handler(Looper.getMainLooper())
    private var speechRecognizer: SpeechRecognizer? = null

    var isListening by mutableStateOf(false)
        private set

    var rmsLevel by mutableFloatStateOf(0f)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    val isAvailable: Boolean
        get() = SpeechRecognizer.isRecognitionAvailable(context)

    fun startListening() {
        mainHandler.post {
            errorMessage = null
            if (!isAvailable) {
                errorMessage = "Speech recognition is not available on this device."
                return@post
            }

            try {
                if (speechRecognizer == null) {
                    speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
                        setRecognitionListener(createRecognitionListener())
                    }
                }

                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                    )
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US.toLanguageTag())
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, Locale.US.toLanguageTag())
                    putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
                    putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
                }

                speechRecognizer?.startListening(intent)
                isListening = true
                rmsLevel = 0f
            } catch (e: Exception) {
                Log.e("SentenceSpeechRecognizer", "Failed to start listening", e)
                errorMessage = e.localizedMessage ?: "Failed to start recording."
                isListening = false
            }
        }
    }

    fun stopListening() {
        mainHandler.post {
            try {
                speechRecognizer?.stopListening()
            } catch (e: Exception) {
                Log.w("SentenceSpeechRecognizer", "Error stopping listening", e)
            }
            isListening = false
            rmsLevel = 0f
        }
    }

    fun cancel() {
        mainHandler.post {
            try {
                speechRecognizer?.cancel()
            } catch (e: Exception) {
                Log.w("SentenceSpeechRecognizer", "Error canceling listening", e)
            }
            isListening = false
            rmsLevel = 0f
        }
    }

    fun destroy() {
        mainHandler.post {
            try {
                speechRecognizer?.destroy()
                speechRecognizer = null
            } catch (e: Exception) {
                Log.w("SentenceSpeechRecognizer", "Error destroying SpeechRecognizer", e)
            }
            isListening = false
            rmsLevel = 0f
        }
    }

    fun clearError() {
        errorMessage = null
    }

    private fun createRecognitionListener(): RecognitionListener {
        return object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                isListening = true
                errorMessage = null
            }

            override fun onBeginningOfSpeech() {
                isListening = true
            }

            override fun onRmsChanged(rmsdB: Float) {
                rmsLevel = rmsdB.coerceIn(0f, 10f)
            }

            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onEndOfSpeech() {
                isListening = false
                rmsLevel = 0f
            }

            override fun onError(error: Int) {
                isListening = false
                rmsLevel = 0f
                val message = when (error) {
                    SpeechRecognizer.ERROR_AUDIO -> "Audio recording error. Please check your mic."
                    SpeechRecognizer.ERROR_CLIENT -> null // Can happen on cancel/timeout, do not alarm user
                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Microphone permission required."
                    SpeechRecognizer.ERROR_NETWORK -> "Network connection required for speech recognition."
                    SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timed out. Please try again."
                    SpeechRecognizer.ERROR_NO_MATCH -> "No speech recognized. Try speaking closer to the mic."
                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Speech recognizer is busy. Please try again."
                    SpeechRecognizer.ERROR_SERVER -> "Server error. Please try again later."
                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech heard. Please speak clearly into the mic."
                    else -> "Recognition failed ($error)."
                }
                if (message != null) {
                    errorMessage = message
                }
                Log.w("SentenceSpeechRecognizer", "SpeechRecognizer error: $error ($message)")
            }

            override fun onResults(results: Bundle?) {
                isListening = false
                rmsLevel = 0f
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                val recognizedText = matches?.firstOrNull()?.trim()
                if (!recognizedText.isNullOrBlank()) {
                    val formatted = recognizedText.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(Locale.US) else it.toString()
                    }
                    onResult(formatted)
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {
                val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                val partialText = matches?.firstOrNull()?.trim()
                if (!partialText.isNullOrBlank()) {
                    val formatted = partialText.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(Locale.US) else it.toString()
                    }
                    onPartialResult?.invoke(formatted)
                }
            }

            override fun onEvent(eventType: Int, params: Bundle?) {}
        }
    }
}
