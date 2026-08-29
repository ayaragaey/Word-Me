package com.example.wordme.audio

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.wordme.data.Word
import java.io.File
import java.util.Locale

data class AudioDebugInfo(
    val wordId: Int,
    val displayedWord: String,
    val textSentToTTS: String,
    val selectedVoice: String,
    val audioFile: String,
    val cachedAudioKey: String
)

class VocabularyAudioManager(private val context: Context) {

    private var tts: TextToSpeech? = null
    var isTtsReady by mutableStateOf(false)
        private set

    var lastDebugInfo by mutableStateOf<AudioDebugInfo?>(null)
        private set

    private var mediaPlayer: MediaPlayer? = null

    val cacheDir: File by lazy {
        val dir = File(context.cacheDir, "vocab_audio")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        dir
    }

    init {
        initTTS()
    }

    private fun initTTS() {
        tts = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.US
                isTtsReady = true
                Log.d("VocabularyAudio", "TTS Initialized successfully with US English")
                cleanLegacyCache()
                tts?.voices?.forEach { v ->
                    if (v.locale.language == "en") {
                        Log.d("VocabularyAudio", "Voice: ${v.name}, locale=${v.locale}, features=${v.features}")
                    }
                }
            } else {
                Log.e("VocabularyAudio", "TTS Initialization failed with status: $status")
            }
        }

        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                Log.d("VocabularyAudio", "TTS started playback/synthesis: $utteranceId")
            }

            override fun onDone(utteranceId: String?) {
                Log.d("VocabularyAudio", "TTS finished playback/synthesis: $utteranceId")
            }

            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?) {
                Log.e("VocabularyAudio", "TTS error on utterance: $utteranceId")
            }

            override fun onError(utteranceId: String?, errorCode: Int) {
                Log.e("VocabularyAudio", "TTS error on utterance: $utteranceId, code: $errorCode")
            }
        })
    }

    /**
     * Cleans any legacy v1 cache files from before the male voice fix.
     */
    fun cleanLegacyCache() {
        try {
            val files = cacheDir.listFiles() ?: return
            for (file in files) {
                if (file.name.startsWith("vocab_") && !file.name.startsWith("vocab_v2_")) {
                    Log.d("VocabularyAudio", "Deleting legacy cache file: ${file.name}")
                    file.delete()
                }
            }
        } catch (e: Exception) {
            Log.w("VocabularyAudio", "Error cleaning legacy cache: ${e.message}")
        }
    }

    /**
     * Builds a unique cache key including word ID, voice gender, and sanitized word text.
     * Prevents male and female audio from ever overwriting or reusing each other's files.
     */
    fun getCacheKey(wordId: Int, female: Boolean, wordText: String): String {
        val gender = if (female) "female" else "male"
        val cleanWord = wordText.trim().lowercase().replace(Regex("[^a-z0-9]"), "_")
        return "vocab_v2_${wordId}_${gender}_${cleanWord}"
    }

    /**
     * Resolves the target cache file for this vocabulary item and voice.
     */
    fun getCacheFile(wordId: Int, female: Boolean, wordText: String): File {
        val key = getCacheKey(wordId, female, wordText)
        return File(cacheDir, "$key.wav")
    }

    /**
     * Invalidates any stale cache files for this wordId that do not match the expected word text.
     */
    fun invalidateStaleCacheForWord(wordId: Int, expectedWordText: String): Int {
        val cleanWord = expectedWordText.trim().lowercase().replace(Regex("[^a-z0-9]"), "_")
        val prefix = "vocab_v2_${wordId}_"
        var deletedCount = 0

        val existingFiles = cacheDir.listFiles() ?: return 0
        for (file in existingFiles) {
            // Delete legacy non-v2 files for this ID
            if (file.name.startsWith("vocab_${wordId}_") && !file.name.startsWith("vocab_v2_")) {
                if (file.delete()) deletedCount++
                continue
            }

            if (file.name.startsWith(prefix) && file.name.endsWith(".wav")) {
                // File format: vocab_v2_{id}_{gender}_{word}.wav
                val nameWithoutExt = file.name.removeSuffix(".wav")
                val parts = nameWithoutExt.split("_", limit = 5)
                if (parts.size >= 5) {
                    val cachedWord = parts[4]
                    if (cachedWord != cleanWord) {
                        Log.w("VocabularyAudio", "Stale cache detected for ID $wordId: ${file.name} (expected word: $cleanWord). Deleting.")
                        if (file.delete()) {
                            deletedCount++
                        }
                    }
                }
            }
        }
        return deletedCount
    }

    /**
     * Intelligently selects the best available female or male voice.
     * Strictly ensures male voice selection never picks female voices (like iob).
     * Configures pitch and rate as fallbacks to ensure distinct gender tones on all engines.
     */
    fun configureVoice(female: Boolean): Pair<Voice?, String> {
        val engine = tts ?: return Pair(null, "TTS Not Initialized")
        val voices = try {
            engine.voices?.filter { it.locale.language == Locale.ENGLISH.language } ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }

        var matchedVoice: Voice? = null

        if (female) {
            // Pink button: Pure female voice selection
            // Priority 1: High quality Google US female voices (tpf, sfg, iol, tpc)
            matchedVoice = voices.firstOrNull { it.name == "en-us-x-tpf-local" }
                ?: voices.firstOrNull { it.name == "en-us-x-sfg-local" }
                ?: voices.firstOrNull { it.name == "en-us-x-iol-local" }
                ?: voices.firstOrNull { it.name == "en-us-x-tpc-local" }
                ?: voices.firstOrNull { it.name.contains("tpf") || it.name.contains("sfg") || it.name.contains("iol") }
                // Priority 2: Generic female identifiers (Samsung, AOSP, etc.)
                ?: voices.firstOrNull { voice ->
                    val name = voice.name.lowercase()
                    (name.contains("female") || name.contains("_f00") || name.contains("-f00") ||
                            name.contains("-f-") || name.contains("_f_") || name.contains("en_us_f")) &&
                            !name.contains("male") && !name.contains("tpd") && !name.contains("iom") && !name.contains("iog")
                }
                // Priority 3: Fallback English voice excluding male patterns
                ?: voices.firstOrNull { voice ->
                    val name = voice.name.lowercase()
                    !name.contains("male") && !name.contains("tpd") && !name.contains("iom") &&
                            !name.contains("iog") && !name.contains("_m00") && !name.contains("-m-")
                }
        } else {
            // Blue button: Pure male voice selection
            // Priority 1: High quality Google US male voices (tpd is Google's deep male voice, iom, iog)
            // Note: iob is Google Voice 2 which is FEMALE - strictly avoid iob!
            matchedVoice = voices.firstOrNull { it.name == "en-us-x-tpd-local" }
                ?: voices.firstOrNull { it.name == "en-us-x-iom-local" }
                ?: voices.firstOrNull { it.name == "en-us-x-iog-local" }
                ?: voices.firstOrNull { it.name == "en-us-x-tpd-network" }
                ?: voices.firstOrNull { it.name == "en-us-x-iom-network" }
                ?: voices.firstOrNull { it.name == "en-us-x-iog-network" }
                ?: voices.firstOrNull { it.name.contains("tpd") || it.name.contains("iom") || it.name.contains("iog") }
                // Priority 2: Generic male identifiers (Samsung, AOSP, etc.)
                ?: voices.firstOrNull { voice ->
                    val name = voice.name.lowercase()
                    (name.contains("male") || name.contains("_m00") || name.contains("-m00") ||
                            name.contains("-m-") || name.contains("_m_") || name.contains("en_us_m")) &&
                            !name.contains("female") && !name.contains("tpf") && !name.contains("sfg") &&
                            !name.contains("iob") && !name.contains("iol") && !name.contains("tpc")
                }
                // Priority 3: Fallback English voice excluding female patterns
                ?: voices.firstOrNull { voice ->
                    val name = voice.name.lowercase()
                    !name.contains("female") && !name.contains("tpf") && !name.contains("sfg") &&
                            !name.contains("iob") && !name.contains("iol") && !name.contains("tpc") &&
                            !name.contains("_f00") && !name.contains("-f-")
                }
        }

        if (matchedVoice != null) {
            try {
                engine.voice = matchedVoice
            } catch (e: Exception) {
                Log.w("VocabularyAudio", "Could not set engine voice: ${e.message}")
            }
        }

        // Apply pitch and rate differences to guarantee distinct voices on any engine
        if (female) {
            engine.setPitch(1.20f) // Bright, clearly feminine pitch
            engine.setSpeechRate(0.92f)
        } else {
            engine.setPitch(0.75f) // Deep, clearly masculine pitch
            engine.setSpeechRate(0.88f)
        }

        val voiceDescription = matchedVoice?.name
            ?: if (female) "Default Female (Pitch: 1.20)" else "Default Male (Pitch: 0.75)"

        return Pair(matchedVoice, voiceDescription)
    }

    /**
     * Primary playback method strictly bound to a Word object.
     */
    fun playWord(word: Word, female: Boolean) {
        playWord(word.id, word.word, female)
    }

    /**
     * Plays a vocabulary word given its unique ID and displayed word text.
     * Enforces displayedWord == textSentToTTS.
     */
    fun playWord(wordId: Int, displayedWord: String, female: Boolean) {
        val engine = tts
        if (engine == null || !isTtsReady) {
            Log.w("VocabularyAudio", "Cannot play word - TTS engine not ready yet")
            return
        }

        // Exact text verification: TTS input strictly derived from displayed word
        val textSentToTTS = displayedWord.trim()
        val cacheKey = getCacheKey(wordId, female, textSentToTTS)
        val cacheFile = getCacheFile(wordId, female, textSentToTTS)

        // 1. Invalidate any stale audio for this ID that had a different word
        invalidateStaleCacheForWord(wordId, textSentToTTS)

        // 2. Select voice
        val (_, selectedVoice) = configureVoice(female)

        // 3. Print required temporary debugging BEFORE playback
        val debugInfo = AudioDebugInfo(
            wordId = wordId,
            displayedWord = displayedWord,
            textSentToTTS = textSentToTTS,
            selectedVoice = selectedVoice,
            audioFile = cacheFile.absolutePath,
            cachedAudioKey = cacheKey
        )
        lastDebugInfo = debugInfo

        Log.d("VocabularyAudio", """
            === VOCABULARY AUDIO PLAYBACK DEBUG ===
            wordId: ${debugInfo.wordId}
            displayedWord: ${debugInfo.displayedWord}
            textSentToTTS: ${debugInfo.textSentToTTS}
            selectedVoice: ${debugInfo.selectedVoice}
            audioURL/audioFile: ${debugInfo.audioFile}
            cachedAudioKey: ${debugInfo.cachedAudioKey}
            =======================================
        """.trimIndent())

        // 4. Playback from cache if valid, or synthesize and speak via TTS
        if (cacheFile.exists() && cacheFile.length() > 44) {
            // Play cached audio via MediaPlayer
            try {
                mediaPlayer?.stop()
                mediaPlayer?.release()
                mediaPlayer = MediaPlayer().apply {
                    setDataSource(cacheFile.absolutePath)
                    prepare()
                    start()
                }
                Log.d("VocabularyAudio", "Playing from cache file: ${cacheFile.name}")
                return
            } catch (e: Exception) {
                Log.w("VocabularyAudio", "Failed to play from cache file, falling back to TTS: ${e.message}")
                cacheFile.delete()
            }
        }

        // Speak directly via TTS
        val utteranceId = "${cacheKey}_${System.currentTimeMillis()}"
        val params = Bundle().apply {
            putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId)
        }

        engine.speak(textSentToTTS, TextToSpeech.QUEUE_FLUSH, params, utteranceId)

        // Also synthesize to cache file in background for future instant/offline playback
        try {
            engine.synthesizeToFile(textSentToTTS, params, cacheFile, "${utteranceId}_synth")
        } catch (e: Exception) {
            Log.w("VocabularyAudio", "synthesizeToFile failed: ${e.message}")
        }
    }

    fun stop() {
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
            tts?.stop()
        } catch (e: Exception) {
            Log.w("VocabularyAudio", "Error stopping audio: ${e.message}")
        }
    }

    fun shutdown() {
        stop()
        try {
            tts?.shutdown()
            tts = null
            isTtsReady = false
        } catch (e: Exception) {
            Log.w("VocabularyAudio", "Error shutting down TTS: ${e.message}")
        }
    }
}
