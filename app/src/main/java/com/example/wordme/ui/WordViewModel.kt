package com.example.wordme.ui

import com.example.wordme.data.UserLearningProfile
import com.example.wordme.data.LearningGoal
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
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
import com.example.wordme.widget.WidgetUpdater

sealed interface Celebration {
    data class LevelUp(val levelNumber: Int, val levelName: String, val wordsRequired: Int) : Celebration
    data class WordMilestone(val count: Int) : Celebration
    data class StreakMilestone(val days: Int) : Celebration
    data class DailyTargetReached(val target: Int, val date: String) : Celebration
}

class WordViewModel(application: Application) : AndroidViewModel(application) {
    // Persistent streak manager
    private val streakManager = StreakManager(application.applicationContext)

    var learningGoals by mutableStateOf(streakManager.learningGoals)
        private set

    var onboardingCompleted by mutableStateOf(streakManager.onboardingCompleted)
        private set

    // User name
    var userName by mutableStateOf(streakManager.userName)
        private set

    fun updateUserName(name: String) {
        streakManager.userName = name
        userName = name
        WidgetUpdater.updateWidget(getApplication())
    }

    // Tab navigation state
    var selectedTab by mutableStateOf(Screen.HOME)
        private set

    // Vocabulary words
    var userProfile by mutableStateOf(UserLearningProfile())
        private set


    var words by mutableStateOf<List<Word>>(emptyList())
        private set

    var currentWordIndex by mutableStateOf(0)
        private set

    val learnedWordIdsSet: Set<Int>
        get() = streakManager.learnedWordIds.mapNotNull {
            it.split(":").firstOrNull()?.toIntOrNull()
        }.toSet()

    private fun loadWordsForCurrentGoals() {
        val selectedGoalEntries = LearningGoal.entries.filter { goal ->
            learningGoals.contains(goal.displayName) || learningGoals.contains(goal.category)
        }
        val allGoalWords = WordRepository.getWordsForGoals(
            getApplication<Application>().applicationContext,
            selectedGoalEntries
        )
        val learnedIds = learnedWordIdsSet
        val unlearnedGoalWords = allGoalWords.filter { it.id !in learnedIds }

        words = if (unlearnedGoalWords.isNotEmpty()) {
            unlearnedGoalWords.shuffled()
        } else {
            // Fallback: If all words in the selected goal are completed, check unlearned words across all categories
            val allWords = WordRepository.loadWords(getApplication<Application>().applicationContext)
            val globalUnlearned = allWords.filter { it.id !in learnedIds }
            if (globalUnlearned.isNotEmpty()) {
                globalUnlearned.shuffled()
            } else if (allGoalWords.isNotEmpty()) {
                allGoalWords.shuffled()
            } else {
                allWords.shuffled()
            }
        }
    }

    fun toggleLearningGoal(goal: String) {
        val current = learningGoals.toMutableSet()
        if (current.contains(goal)) {
            if (current.size > 1) {
                current.remove(goal)
            }
        } else {
            current.add(goal)
        }
        streakManager.learningGoals = current
        learningGoals = current
        val selectedGoalEntries = current.mapNotNull { LearningGoal.fromDisplayName(it) }
        userProfile = userProfile.copy(
            selectedGoals = selectedGoalEntries
        )
        loadWordsForCurrentGoals()
        currentWordIndex = 0
    }

    fun completeOnboarding(name: String, goals: Set<String>) {
        streakManager.userName = name
        userName = name
        streakManager.learningGoals = goals
        learningGoals = goals
        streakManager.onboardingCompleted = true
        onboardingCompleted = true
        val selectedGoalEntries = goals.mapNotNull { LearningGoal.fromDisplayName(it) }
        userProfile = userProfile.copy(
            selectedGoals = selectedGoalEntries
        )
        loadWordsForCurrentGoals()
        currentWordIndex = 0
        WidgetUpdater.updateWidget(getApplication())
    }

    val currentWord: Word
        get() = rehearsalWord ?: words.getOrElse(currentWordIndex) {
            words.firstOrNull() ?: WordData.mockWordsList.first()
        }

    // Vocab lookup map combining assets database and initial mock words
    private val allWordsLookup: Map<Int, Word> by lazy {
        WordRepository.getAllWordsMap(getApplication<Application>().applicationContext)
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

    // Settings & goal states
    val joinedDate: String
        get() = streakManager.joinedDate

    var notificationsEnabled by mutableStateOf(streakManager.notificationsEnabled)
        private set

    fun toggleNotifications(enabled: Boolean) {
        streakManager.notificationsEnabled = enabled
        notificationsEnabled = enabled
    }

    var reminderTime by mutableStateOf(streakManager.reminderTime)
        private set

    fun updateReminderTime(time: String) {
        streakManager.reminderTime = time
        reminderTime = time
    }



    var dailyTarget by mutableStateOf(streakManager.dailyTarget)
        private set

    fun updateDailyTarget(target: Int) {
        streakManager.dailyTarget = target
        dailyTarget = target
    }

    // Learned words tracking
    var learnedWords by mutableStateOf<List<Word>>(emptyList())
        private set

    val wordsLearnedCount: Int
        get() = learnedWords.size

    val wordsLearnedTodayCount: Int
        get() {
            val todayStr = java.time.LocalDate.now().toString()
            return streakManager.learnedWordIds.count { it.endsWith(":$todayStr") }
        }

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

    // Recommendations for the evaluated sentence
    var currentSentenceRecommendations by mutableStateOf<List<String>>(emptyList())
        private set

    init {
        // Reconstruct learned words list on launch
        reconstructLearnedWords()

        loadWordsForCurrentGoals()
        android.util.Log.d("WORD_COUNT", "Loaded unlearned words: ${words.size}")

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
        WidgetUpdater.updateWidget(getApplication())
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

        // Sort: LevelUp -> WordMilestone -> StreakMilestone -> DailyTargetReached
        val sorted = newCelebrations.sortedWith(compareBy(
            { when(it) {
                is Celebration.LevelUp -> 1
                is Celebration.WordMilestone -> 2
                is Celebration.StreakMilestone -> 3
                is Celebration.DailyTargetReached -> 4
            }},
            { when(it) {
                is Celebration.LevelUp -> it.levelNumber
                is Celebration.WordMilestone -> it.count
                is Celebration.StreakMilestone -> it.days
                is Celebration.DailyTargetReached -> it.target
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

    fun generateMoreSentences() {
        val word = currentWord
        val (newExamples, newTranslations) = com.example.wordme.utils.WordSentenceGenerator.generateNewSentences(word)
        val updatedWord = word.copy(
            examples = newExamples,
            exampleTranslations = newTranslations
        )
        if (rehearsalWord != null) {
            rehearsalWord = updatedWord
        } else {
            val updatedList = words.toMutableList()
            if (currentWordIndex in updatedList.indices) {
                updatedList[currentWordIndex] = updatedWord
                words = updatedList
            }
        }
        showExampleTranslations = false
    }

    data class EvaluationResult(
        val score: Int,
        val recommendations: List<String>
    )

    fun evaluateSentenceDetailed(sentence: String, word: String): EvaluationResult {
        val trimmed = sentence.trim()
        if (trimmed.isEmpty()) {
            return EvaluationResult(
                score = 0,
                recommendations = listOf("Please write a sentence using '${word.lowercase()}'.")
            )
        }

        val recs = mutableListOf<String>()
        val wordsList = trimmed.split("\\s+".toRegex()).filter { it.isNotBlank() }
        val containsWord = trimmed.contains(word, ignoreCase = true)

        if (!containsWord) {
            recs.add("Include the target word '${word.lowercase()}' in your sentence.")
        }

        if (wordsList.size < 4) {
            recs.add("Write a more complete sentence with at least 4 to 5 words.")
        }

        val startsWithCapital = trimmed.firstOrNull()?.isUpperCase() == true
        if (!startsWithCapital) {
            recs.add("Start your sentence with a capital letter.")
        }

        val endsWithPunctuation = trimmed.endsWith(".") || trimmed.endsWith("!") || trimmed.endsWith("?")
        if (!endsWithPunctuation) {
            recs.add("End your sentence with punctuation (e.g. . or ! or ?).")
        }

        // Calculate score
        val score: Int = if (!containsWord) {
            var s = 2
            if (wordsList.size >= 4) s += 1
            s
        } else if (wordsList.size < 3) {
            4
        } else {
            var s = 7 // Base passing score when target word is present in a decent sentence
            if (startsWithCapital) s += 1
            if (endsWithPunctuation) s += 1
            if (wordsList.size >= 6) s += 1
            if (wordsList.size == 3 && s > 6) s = 6
            s.coerceIn(1, 10)
        }

        return EvaluationResult(score = score, recommendations = recs)
    }

    fun evaluateSentence(sentence: String, word: String): Int {
        return evaluateSentenceDetailed(sentence, word).score
    }

    fun checkSentence() {
        if (sentenceText.isBlank() || isChecked) return

        val eval = evaluateSentenceDetailed(sentenceText, currentWord.word)
        val score = eval.score
        currentSentenceScore = score
        currentSentenceRecommendations = eval.recommendations
        isChecked = true

        // If score is less than 7, user must retry; do NOT record as completed yet
        if (score < 7) {
            return
        }

        // Update persistent sentences written
        streakManager.sentencesWritten = streakManager.sentencesWritten + 1
        sentencesWrittenCount = streakManager.sentencesWritten

        // Update persistent best score if new score is higher
        if (score > streakManager.bestScoreValue) {
            streakManager.bestScoreValue = score
            bestScore = "${score}/10"
        }

        if (rehearsalWord != null) {
            WidgetUpdater.updateWidget(getApplication())
            return
        }

        // Update streak persistently
        streakManager.onWordCompleted()
        dayCount = streakManager.dayCount
        streakCount = streakManager.currentStreak
        WidgetUpdater.updateWidget(getApplication())

        val wordToAdd = currentWord
        val isNewWord = streakManager.learnedWordIds.none { it.startsWith("${wordToAdd.id}:") || it == wordToAdd.id.toString() }

        if (isNewWord) {
            val oldWordsCount = wordsLearnedCount
            val oldLevel = levelDetails.level

            // Save word persistently with today's date
            val todayStr = java.time.LocalDate.now().toString()
            val newEntry = "${wordToAdd.id}:$todayStr"
            val updatedWordIds = streakManager.learnedWordIds + newEntry
            streakManager.learnedWordIds = updatedWordIds
            
            // Reconstruct learned words list
            reconstructLearnedWords()

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

            // Daily Target Reached Celebration
            val dailyKey = "daily_target_$todayStr"
            if (wordsLearnedTodayCount >= dailyTarget && !streakManager.acknowledgedCelebrations.contains(dailyKey)) {
                newCelebrations.add(Celebration.DailyTargetReached(dailyTarget, todayStr))
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

    fun retrySentence() {
        isChecked = false
        currentSentenceScore = 0
        currentSentenceRecommendations = emptyList()
    }

    fun nextWord() {
        if (rehearsalWord != null) {
            rehearsalWord = null
        }

        val learnedIds = learnedWordIdsSet
        val remainingUnlearned = words.filter { it.id !in learnedIds }

        if (remainingUnlearned.isNotEmpty()) {
            val wasFiltered = remainingUnlearned.size < words.size
            words = remainingUnlearned
            currentWordIndex = if (wasFiltered) {
                currentWordIndex % words.size
            } else {
                (currentWordIndex + 1) % words.size
            }
        } else {
            loadWordsForCurrentGoals()
            currentWordIndex = if (words.isNotEmpty()) (currentWordIndex + 1) % words.size else 0
        }

        sentenceText = ""
        isChecked = false
        currentSentenceScore = 0
        currentSentenceRecommendations = emptyList()
        showExampleTranslations = false
    }

    fun dismissCurrentCelebration() {
        val current = currentCelebration ?: return
        val stableId = when (current) {
            is Celebration.LevelUp -> "level_${current.levelNumber}"
            is Celebration.WordMilestone -> "word_${current.count}"
            is Celebration.StreakMilestone -> "streak_${current.days}"
            is Celebration.DailyTargetReached -> "daily_target_${current.date}"
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
            WidgetUpdater.updateWidget(getApplication())
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
        WidgetUpdater.updateWidget(getApplication())
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
                    WidgetUpdater.updateWidget(getApplication())
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

    // --- Rehearsal Mode & Helpers ---

    var rehearsalWord by mutableStateOf<Word?>(null)
        private set

    fun startRehearsal(word: Word) {
        rehearsalWord = word
        sentenceText = ""
        isChecked = false
        showExampleTranslations = false
        selectedTab = Screen.HOME
    }

    fun exitRehearsal() {
        rehearsalWord = null
        sentenceText = ""
        isChecked = false
        showExampleTranslations = false
    }

    private fun reconstructLearnedWords() {
        learnedWords = streakManager.learnedWordIds.mapNotNull { entry ->
            val parts = entry.split(":")
            val id = parts[0].toIntOrNull() ?: return@mapNotNull null
            val date = parts.getOrNull(1) ?: getFallbackDateForId(id)
            val baseWord = allWordsLookup[id]
            baseWord?.copy(learnedDate = date)
        }
    }

    private fun getFallbackDateForId(id: Int?): String {
        if (id == null) return java.time.LocalDate.now().toString()
        return when {
            id >= 120 -> java.time.LocalDate.now().toString()
            id >= 110 -> java.time.LocalDate.now().minusDays(1).toString()
            else -> java.time.LocalDate.now().minusDays(2).toString()
        }
    }

    fun resetProgress() {
        streakManager.resetAllData()

        // Reload name & states from streakManager
        userName = streakManager.userName
        dayCount = streakManager.dayCount
        streakCount = streakManager.currentStreak
        sentencesWrittenCount = streakManager.sentencesWritten
        bestScore = "${streakManager.bestScoreValue}/10"

        // Settings/goals values
        notificationsEnabled = streakManager.notificationsEnabled
        reminderTime = streakManager.reminderTime
        learningGoals = streakManager.learningGoals
        onboardingCompleted = streakManager.onboardingCompleted
        dailyTarget = streakManager.dailyTarget

        // Celebrations & learned words
        pendingCelebrations = emptyList()
        reconstructLearnedWords()

        // Re-sync user learning profile and word deck for default goals
        val selectedGoalEntries = learningGoals.mapNotNull { LearningGoal.fromDisplayName(it) }
        userProfile = UserLearningProfile(
            selectedGoals = selectedGoalEntries,
            dailyWordTarget = streakManager.dailyTarget
        )
        loadWordsForCurrentGoals()

        // Reset celebrations tracking
        streakManager.acknowledgedCelebrations = emptySet()
        streakManager.isAcknowledgedCelebrationsInitialized = false
        streakManager.lastShownStreakCelebration = 0
        streakManager.celebrationPending = false
        streakManager.pendingCelebrationStreak = 0

        // Set tab back to Home
        selectedTab = Screen.HOME

        // Reset practice / rehearsal states
        rehearsalWord = null
        currentWordIndex = 0
        sentenceText = ""
        isChecked = false
        showExampleTranslations = false
        currentSentenceScore = 0

        // Reset recovery states
        stopTimerJob()
        isRecoveryActive = false
        recoveryStep = RecoveryStep.INTRO
        currentQuestionIndex = 0
        selectedOptionIndex = null

        WidgetUpdater.updateWidget(getApplication())
    }

    override fun onCleared() {
        super.onCleared()
        stopTimerJob()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                // Get the Application object from extras
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])

                return WordViewModel(application) as T
            }
        }
    }
}

