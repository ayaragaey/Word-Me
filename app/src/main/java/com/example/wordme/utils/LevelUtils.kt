package com.example.wordme.utils

data class LevelDetails(
    val level: Int,
    val name: String,
    val currentThreshold: Int,
    val nextThreshold: Int,
    val progressFraction: Float,
    val progressPercentage: Int,
    val wordsRemaining: Int,
    val supportingMessage: String
)

object LevelUtils {
    private val LEVEL_THRESHOLDS = listOf(
        0 to "Starter",
        50 to "Explorer",
        200 to "Builder",
        500 to "Speaker",
        1000 to "Conversationalist",
        2500 to "Fluent",
        5000 to "Wordsmith",
        10000 to "Advanced",
        25000 to "Expert",
        50000 to "Master",
        100000 to "Scholar",
        250000 to "Lexicon Legend"
    )

    fun calculateLevelDetails(wordsLearned: Int): LevelDetails {
        val totalLibrarySize = 500000

        // If user mastered everything
        if (wordsLearned >= totalLibrarySize) {
            return LevelDetails(
                level = 12,
                name = "Lexicon Legend",
                currentThreshold = 250000,
                nextThreshold = totalLibrarySize,
                progressFraction = 1.0f,
                progressPercentage = 100,
                wordsRemaining = 0,
                supportingMessage = "500,000 Words Completed"
            )
        }

        // Determine current level index
        var currentLevelIndex = 0
        for (i in LEVEL_THRESHOLDS.indices) {
            if (wordsLearned >= LEVEL_THRESHOLDS[i].first) {
                currentLevelIndex = i
            } else {
                break
            }
        }

        val levelNumber = currentLevelIndex + 1
        val levelName = LEVEL_THRESHOLDS[currentLevelIndex].second
        val currentThreshold = LEVEL_THRESHOLDS[currentLevelIndex].first

        if (levelNumber == 12) {
            // Lexicon Legend goes from 250,000 to 500,000
            val nextThreshold = totalLibrarySize
            val diff = nextThreshold - currentThreshold
            val progress = (wordsLearned - currentThreshold).toFloat() / diff.toFloat()
            val progressPercentage = (progress * 100).toInt().coerceIn(0, 100)
            val remaining = nextThreshold - wordsLearned

            return LevelDetails(
                level = 12,
                name = "Lexicon Legend",
                currentThreshold = currentThreshold,
                nextThreshold = nextThreshold,
                progressFraction = progress.coerceIn(0f, 1f),
                progressPercentage = progressPercentage,
                wordsRemaining = remaining,
                supportingMessage = "$progressPercentage% of the Word Me vocabulary library"
            )
        } else {
            val nextLevelIndex = currentLevelIndex + 1
            val nextThreshold = LEVEL_THRESHOLDS[nextLevelIndex].first
            val nextLevelName = LEVEL_THRESHOLDS[nextLevelIndex].second

            val diff = nextThreshold - currentThreshold
            val progress = (wordsLearned - currentThreshold).toFloat() / diff.toFloat()
            val progressPercentage = (progress * 100).toInt().coerceIn(0, 100)
            val remaining = nextThreshold - wordsLearned

            return LevelDetails(
                level = levelNumber,
                name = levelName,
                currentThreshold = currentThreshold,
                nextThreshold = nextThreshold,
                progressFraction = progress.coerceIn(0f, 1f),
                progressPercentage = progressPercentage,
                wordsRemaining = remaining,
                supportingMessage = "$remaining words to $nextLevelName"
            )
        }
    }
}
