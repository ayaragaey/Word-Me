package com.example.wordme.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.wordme.data.WordRepository
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.wordme.data.Word
import com.example.wordme.data.WordData
import com.example.wordme.navigation.Screen
import com.example.wordme.utils.LevelDetails
import com.example.wordme.utils.LevelUtils

data class LevelUpState(
    val levelNumber: Int,
    val levelName: String,
    val wordsRequired: Int
)

class WordViewModel(application: Application) : AndroidViewModel(application) {
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

    // Practice sentence states
    var sentenceText by mutableStateOf("")
        private set

    var isChecked by mutableStateOf(false)
        private set

    var showExampleTranslations by mutableStateOf(false)
        private set

    // Progress metrics
    var dayCount by mutableStateOf(12)
        private set

    var streakCount by mutableStateOf(5)
        private set

    var sentencesWrittenCount by mutableStateOf(27)
        private set

    var bestScore by mutableStateOf("9/10")
        private set

    // Learned words tracking
    var learnedWords by mutableStateOf(WordData.initialLearnedWords)
        private set

    val wordsLearnedCount: Int
        get() = learnedWords.size

    // Level up event state
    var levelUpState by mutableStateOf<LevelUpState?>(null)
        private set

    val levelDetails: LevelDetails
        get() = LevelUtils.calculateLevelDetails(wordsLearnedCount)

    fun selectTab(screen: Screen) {
        selectedTab = screen
    }

    fun onSentenceTextChange(text: String) {
        sentenceText = text
    }

    fun toggleExampleTranslations() {
        showExampleTranslations = !showExampleTranslations
    }

    fun checkSentence() {
        if (sentenceText.isBlank() || isChecked) return

        isChecked = true
        sentencesWrittenCount++

        val wordToAdd = currentWord
        // Check if already completed (by checking if its word string is in learnedWords list)
        val exists = learnedWords.any { it.word.uppercase() == wordToAdd.word.uppercase() }

        if (!exists) {
            val oldDetails = levelDetails
            
            // Add to learned words
            learnedWords = learnedWords + wordToAdd
            
            val newDetails = levelDetails

            // Check if level upgraded
            if (newDetails.level > oldDetails.level) {
                levelUpState = LevelUpState(
                    levelNumber = newDetails.level,
                    levelName = newDetails.name,
                    wordsRequired = newDetails.currentThreshold
                )
            }
        }
    }

    fun nextWord() {
        // Go to next word
        currentWordIndex = (currentWordIndex + 1) % words.size
        
        // Reset states
        sentenceText = ""
        isChecked = false
        showExampleTranslations = false
    }

    fun dismissLevelUp() {
        levelUpState = null
    }
}
