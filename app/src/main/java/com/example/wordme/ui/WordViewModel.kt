package com.example.wordme.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.wordme.data.WordRepository
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.wordme.data.Word
import com.example.wordme.data.WordData
import com.example.wordme.data.StreakManager
import com.example.wordme.data.RecoveryQuestions
import com.example.wordme.screens.recovery.RecoveryStep
import com.example.wordme.navigation.Screen
import com.example.wordme.utils.LevelDetails
import com.example.wordme.utils.LevelUtils
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Locale

sealed interface Celebration {
    data class LevelUp(val levelNumber: Int, val levelName: String, val wordsRequired: Int) : Celebration
    data class WordMilestone(val count: Int) : Celebration
    data class StreakMilestone(val days: Int) : Celebration
}

class WordViewModel(application: Application) : AndroidViewModel(application) {
    // Persistent streak manager
    private val streakManager = StreakManager(application.applicationContext)

    // User name
    var userName by mutableStateOf(streakManager.userName)
        private set

    fun updateUserName(name: String) {
        streakManager.userName = name
        userName = name
    }

    // Tab navigation state
    var selectedTab by mutableStateOf(Screen.HOME)
        private set

    // Vocabulary words
    val words: List<Word> = WordRepository
        .loadWords(application.applicationContext)
        .shuffled()
    var currentWordIndex by mutableStateOf(0)
        private set

    val currentWord: Word
        get() = words[currentWordIndex]

    // Vocab lookup map combining assets database and initial mock words
    private val allWordsLookup: Map<Int, Word> by lazy {
        (words + WordData.initialLearnedWords).associateBy { it.id }
    }

    // Practice sentence states
    var sentenceText by mutableStateOf("")
        private set

    var isChecked by mutableStateOf(false)
        private set

    var showExampleTranslations by mutableStateOf(false)
        private set

    // Progress metrics persistent values (recomposed on updates)
    var dayCount by mutableStateOf(streakManager.dayCount)
        private set

    var streakCount by mutableStateOf(streakManager.currentStreak)
        private set

    var sentencesWrittenCount by mutableStateOf(streakManager.sentencesWritten)
        private set

    var bestScore by mutableStateOf("${streakManager.bestScoreValue}/10")
        private set

    // Learned words tracking
    var learnedWords by mutableStateOf<List<Word>>(emptyList())
        private set

    val wordsLearnedCount: Int
        get() = learnedWords.size

    val levelDetails: LevelDetails
        get() = LevelUtils.calculateLevelDetails(wordsLearnedCount)

    // Recovery Screen States
    var isRecoveryActive by mutableStateOf(false)
        private set

    var recoveryStep by mutableStateOf(RecoveryStep.INTRO)
        private set

    var recoveryTimeLeftFormatted by mutableStateOf("10:00")
        private set

    var currentQuestionIndex by mutableStateOf(0)
        private set

    var selectedOptionIndex by mutableStateOf<Int?>(null)
        private set

    private var timerJob: Job? = null

    // Celebration states
    var pendingCelebrations by mutableStateOf<List<Celebration>>(emptyList())
        private set

    val currentCelebration: Celebration?
        get() = pendingCelebrations.firstOrNull()

    // Score of the currently graded sentence
    var currentSentenceScore by mutableStateOf(0)
        private set

    init {
        // Reconstruct learned words list on launch
        val savedIds = streakManager.learnedWordIds.mapNotNull { it.toIntOrNull() }
        learnedWords = savedIds.mapNotNull { allWordsLookup[it] }

        checkStreakOnLaunch()
        initializeAcknowledgedCelebrationsIfNeeded()
        checkAndQueueUnacknowledgedCelebrations()
    }

    private fun checkStreakOnLaunch() {
        val recoveryRequired = streakManager.checkStreakOnAppLaunch()
        dayCount = streakManager.dayCount
        streakCount = streakManager.currentStreak

        if (recoveryRequired) {
            isRecoveryActive = true
            if (streakManager.recoveryDeadline > 0L) {
                if (System.currentTimeMillis() >= streakManager.recoveryDeadline) {
                    streakManager.onRecoveryFailure()
                    streakCount = 0
                    dayCount = streakManager.dayCount
                    recoveryStep = RecoveryStep.FAILURE
                } else {
                    recoveryStep = RecoveryStep.CHALLENGE
                    currentQuestionIndex = streakManager.recoveryChallengeProgress
                    selectedOptionIndex = null
                    startTimerJob()
                }
            } else {
                recoveryStep = RecoveryStep.INTRO
            }
        } else {
            isRecoveryActive = false
        }
    }

    private fun initializeAcknowledgedCelebrationsIfNeeded() {
        if (!streakManager.isAcknowledgedCelebrationsInitialized) {
            val wordsCount = wordsLearnedCount
            val currentStreakVal = streakCount
            val currentLevelVal = levelDetails.level

            val initialAck = mutableSetOf<String>()
            
            // Level milestones: level_1, level_2, etc. up to currentLevelVal
            for (l in 1..currentLevelVal) {
                initialAck.add("level_$l")
            }
            
            // Word milestones: multiples of 50 <= wordsCount
            for (w in 50..wordsCount step 50) {
                initialAck.add("word_$w")
            }
            
            // Streak milestones: 3 (if currentStreakVal >= 3), 7 (if >= 7), 14, 21, etc. <= currentStreakVal
            if (currentStreakVal >= 3) initialAck.add("streak_3")
            if (currentStreakVal >= 7) initialAck.add("streak_7")
            for (s in 14..currentStreakVal step 7) {
                initialAck.add("streak_$s")
            }

            // Also preserve lastShownStreakCelebration by adding it
            val lastShown = streakManager.lastShownStreakCelebration
            if (lastShown > 0) {
                initialAck.add("streak_$lastShown")
                if (lastShown >= 3) initialAck.add("streak_3")
                if (lastShown >= 7) {
                    initialAck.add("streak_7")
                    for (s in 14..lastShown step 7) {
                        initialAck.add("streak_$s")
                    }
                }
            }

            streakManager.acknowledgedCelebrations = initialAck
            streakManager.isAcknowledgedCelebrationsInitialized = true
        }
    }

    private fun checkAndQueueUnacknowledgedCelebrations() {
        val lvl = levelDetails.level
        val wordsCount = wordsLearnedCount
        val streakVal = streakCount
        val ack = streakManager.acknowledgedCelebrations
        
        val newCelebrations = mutableListOf<Celebration>()
        
        // 1. Level Ups
        val levelThresholds = listOf(0, 50, 200, 500, 1000, 2500, 5000, 10000, 25000, 50000, 100000, 250000)
        for (l in 1..lvl) {
            val key = "level_$l"
            if (!ack.contains(key)) {
                val threshold = levelThresholds.getOrNull(l - 1) ?: 0
                val details = LevelUtils.calculateLevelDetails(threshold)
                newCelebrations.add(Celebration.LevelUp(l, details.name, threshold))
            }
        }
        
        // 2. Word Milestones
        for (w in 50..wordsCount step 50) {
            val key = "word_$w"
            if (!ack.contains(key)) {
                newCelebrations.add(Celebration.WordMilestone(w))
            }
        }
        
        // 3. Streak Milestones
        if (streakVal >= 3) {
            val key = "streak_3"
            if (!ack.contains(key)) {
                newCelebrations.add(Celebration.StreakMilestone(3))
            }
        }
        if (streakVal >= 7) {
            val key = "streak_7"
            if (!ack.contains(key)) {
                newCelebrations.add(Celebration.StreakMilestone(7))
            }
        }
        for (s in 14..streakVal step 7) {
            val key = "streak_$s"
            if (!ack.contains(key)) {
                newCelebrations.add(Celebration.StreakMilestone(s))
            }
        }

        // Sort: LevelUp -> WordMilestone -> StreakMilestone
        val sorted = newCelebrations.sortedWith(compareBy(
            { when(it) {
                is Celebration.LevelUp -> 1
                is Celebration.WordMilestone -> 2
                is Celebration.StreakMilestone -> 3
            }},
            { when(it) {
                is Celebration.LevelUp -> it.levelNumber
                is Celebration.WordMilestone -> it.count
                is Celebration.StreakMilestone -> it.days
            }}
        ))
        
        pendingCelebrations = pendingCelebrations + sorted
    }

    fun selectTab(screen: Screen) {
        selectedTab = screen
    }

    fun onSentenceTextChange(text: String) {
        sentenceText = text
    }

    fun toggleExampleTranslations() {
        showExampleTranslations = !showExampleTranslations
    }

    fun evaluateSentence(sentence: String, word: String): Int {
        val trimmed = sentence.trim()
        if (trimmed.isEmpty()) return 0
        val containsWord = trimmed.contains(word, ignoreCase = true)
        if (!containsWord) {
            return 1 + (trimmed.length % 5)
        }
        
        var score = 7
        if (trimmed[0].isUpperCase()) score += 1
        if (trimmed.endsWith(".") || trimmed.endsWith("!") || trimmed.endsWith("?")) score += 1
        if (trimmed.split("\\s+".toRegex()).size >= 5) score += 1
        
        return score.coerceIn(1, 10)
    }

    fun checkSentence() {
        if (sentenceText.isBlank() || isChecked) return

        val score = evaluateSentence(sentenceText, currentWord.word)
        currentSentenceScore = score
        isChecked = true

        // Update persistent sentences written
        streakManager.sentencesWritten = streakManager.sentencesWritten + 1
        sentencesWrittenCount = streakManager.sentencesWritten

        // Update persistent best score if new score is higher
        if (score > streakManager.bestScoreValue) {
            streakManager.bestScoreValue = score
            bestScore = "${score}/10"
        }

        // Update streak persistently
        streakManager.onWordCompleted()
        dayCount = streakManager.dayCount
        streakCount = streakManager.currentStreak

        val wordToAdd = currentWord
        val isNewWord = !streakManager.learnedWordIds.contains(wordToAdd.id.toString())

        if (isNewWord) {
            val oldWordsCount = wordsLearnedCount
            val oldLevel = levelDetails.level

            // Save word persistently
            val updatedWordIds = streakManager.learnedWordIds + wordToAdd.id.toString()
            streakManager.learnedWordIds = updatedWordIds
            
            // Reconstruct learned words list
            learnedWords = updatedWordIds.mapNotNull { allWordsLookup[it.toIntOrNull()] }

            val newWordsCount = wordsLearnedCount
            val newLevel = levelDetails.level

            val newCelebrations = mutableListOf<Celebration>()

            // Level Up
            val levelThresholds = listOf(0, 50, 200, 500, 1000, 2500, 5000, 10000, 25000, 50000, 100000, 250000)
            if (newLevel > oldLevel) {
                for (l in (oldLevel + 1)..newLevel) {
                    val threshold = levelThresholds.getOrNull(l - 1) ?: 0
                    val details = LevelUtils.calculateLevelDetails(threshold)
                    newCelebrations.add(Celebration.LevelUp(l, details.name, threshold))
                }
            }

            // Word Milestones
            for (w in (oldWordsCount + 1)..newWordsCount) {
                if (w % 50 == 0 && w <= 500000) {
                    newCelebrations.add(Celebration.WordMilestone(w))
                }
            }

            if (newCelebrations.isNotEmpty()) {
                pendingCelebrations = pendingCelebrations + newCelebrations
            }
        }

        // Check if streak milestone was unlocked
        if (streakManager.celebrationPending) {
            val streakVal = streakManager.pendingCelebrationStreak
            val key = "streak_$streakVal"
            if (!streakManager.acknowledgedCelebrations.contains(key)) {
                pendingCelebrations = pendingCelebrations + Celebration.StreakMilestone(streakVal)
            }
            streakManager.celebrationPending = false
        }
    }

    fun nextWord() {
        currentWordIndex = (currentWordIndex + 1) % words.size
        sentenceText = ""
        isChecked = false
        showExampleTranslations = false
    }

    fun dismissCurrentCelebration() {
        val current = currentCelebration ?: return
        val stableId = when (current) {
            is Celebration.LevelUp -> "level_${current.levelNumber}"
            is Celebration.WordMilestone -> "word_${current.count}"
            is Celebration.StreakMilestone -> "streak_${current.days}"
        }
        streakManager.acknowledgedCelebrations = streakManager.acknowledgedCelebrations + stableId
        pendingCelebrations = pendingCelebrations.drop(1)
    }

    // --- Recovery Actions ---

    fun startRecoveryChallenge() {
        val deadline = System.currentTimeMillis() + 10 * 60 * 1000 // 10 minutes
        streakManager.recoveryDeadline = deadline
        streakManager.recoveryChallengeProgress = 0
        currentQuestionIndex = 0
        selectedOptionIndex = null
        recoveryStep = RecoveryStep.CHALLENGE
        startTimerJob()
    }

    fun selectOption(index: Int) {
        selectedOptionIndex = index
    }

    fun nextRecoveryQuestion() {
        if (selectedOptionIndex == null) return

        val totalQuestions = RecoveryQuestions.questions.size
        if (currentQuestionIndex < totalQuestions - 1) {
            currentQuestionIndex++
            streakManager.recoveryChallengeProgress = currentQuestionIndex
            selectedOptionIndex = null
        } else {
            // Finished challenge successfully!
            stopTimerJob()
            streakManager.onRecoverySuccess()
            dayCount = streakManager.dayCount
            streakCount = streakManager.currentStreak
            recoveryStep = RecoveryStep.SUCCESS
        }
    }

    fun dismissRecoverySuccess() {
        isRecoveryActive = false
        if (streakManager.celebrationPending) {
            val streakVal = streakManager.pendingCelebrationStreak
            val key = "streak_$streakVal"
            if (!streakManager.acknowledgedCelebrations.contains(key)) {
                pendingCelebrations = pendingCelebrations + Celebration.StreakMilestone(streakVal)
            }
            streakManager.celebrationPending = false
        }
    }

    fun restartStreakAfterFailure() {
        streakManager.onRecoveryFailure()
        dayCount = streakManager.dayCount
        streakCount = 0
        isRecoveryActive = false
    }

    private fun startTimerJob() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (isActive) {
                val timeLeftMs = streakManager.recoveryDeadline - System.currentTimeMillis()
                if (timeLeftMs <= 0) {
                    streakManager.onRecoveryFailure()
                    streakCount = 0
                    dayCount = streakManager.dayCount
                    recoveryStep = RecoveryStep.FAILURE
                    stopTimerJob()
                    break
                } else {
                    val minutes = (timeLeftMs / 1000) / 60
                    val seconds = (timeLeftMs / 1000) % 60
                    recoveryTimeLeftFormatted = String.format(Locale.US, "%02d:%02d", minutes, seconds)
                }
                delay(500)
            }
        }
    }

    private fun stopTimerJob() {
        timerJob?.cancel()
        timerJob = null
    }

    override fun onCleared() {
        super.onCleared()
        stopTimerJob()
    }
}

