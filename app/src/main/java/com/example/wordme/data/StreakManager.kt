package com.example.wordme.data

import android.content.Context
import android.content.SharedPreferences
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class StreakManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("wordme_streak_prefs", Context.MODE_PRIVATE)

    // User name
    var userName: String?
        get() = prefs.getString("user_name", null)
        set(value) = prefs.edit().putString("user_name", value).apply()

    // Current streak
    var currentStreak: Int
        get() = prefs.getInt("current_streak", 5)
        set(value) = prefs.edit().putInt("current_streak", value).apply()

    // Day count (Total active days)
    var dayCount: Int
        get() = prefs.getInt("day_count", 12)
        set(value) = prefs.edit().putInt("day_count", value).apply()

    // Last active date (yyyy-MM-dd)
    var lastActiveDate: String?
        get() = prefs.getString("last_active_date", LocalDate.now().minusDays(1).toString())
        set(value) = prefs.edit().putString("last_active_date", value).apply()

    // Recovery deadline timestamp (epoch milliseconds)
    var recoveryDeadline: Long
        get() = prefs.getLong("recovery_deadline", 0L)
        set(value) = prefs.edit().putLong("recovery_deadline", value).apply()

    // Date when recovery was successfully completed (yyyy-MM-dd)
    var recoveryCompletedDate: String?
        get() = prefs.getString("recovery_completed_date", null)
        set(value) = prefs.edit().putString("recovery_completed_date", value).apply()

    // Date when recovery failed (yyyy-MM-dd)
    var recoveryFailedDate: String?
        get() = prefs.getString("recovery_failed_date", null)
        set(value) = prefs.edit().putString("recovery_failed_date", value).apply()

    // Last shown streak celebration milestone
    var lastShownStreakCelebration: Int
        get() = prefs.getInt("last_shown_streak_celebration", 3)
        set(value) = prefs.edit().putInt("last_shown_streak_celebration", value).apply()

    // Celebration pending state
    var celebrationPending: Boolean
        get() = prefs.getBoolean("celebration_pending", false)
        set(value) = prefs.edit().putBoolean("celebration_pending", value).apply()

    var pendingCelebrationStreak: Int
        get() = prefs.getInt("pending_celebration_streak", 0)
        set(value) = prefs.edit().putInt("pending_celebration_streak", value).apply()

    // Learned word IDs
    var learnedWordIds: Set<String>
        get() = prefs.getStringSet("learned_word_ids", null) ?: (101..127).map { it.toString() }.toSet()
        set(value) = prefs.edit().putStringSet("learned_word_ids", value).apply()

    // Sentences Written
    var sentencesWritten: Int
        get() = prefs.getInt("sentences_written", 27)
        set(value) = prefs.edit().putInt("sentences_written", value).apply()

    // Best Score (stored as an Int out of 10)
    var bestScoreValue: Int
        get() = prefs.getInt("best_score_value", 9)
        set(value) = prefs.edit().putInt("best_score_value", value).apply()

    // Acknowledged celebrations to prevent retroactive popups
    var acknowledgedCelebrations: Set<String>
        get() = prefs.getStringSet("acknowledged_celebrations", null) ?: emptySet()
        set(value) = prefs.edit().putStringSet("acknowledged_celebrations", value).apply()

    // Migration flag for initializing acknowledged celebrations
    var isAcknowledgedCelebrationsInitialized: Boolean
        get() = prefs.getBoolean("acknowledged_celebrations_initialized", false)
        set(value) = prefs.edit().putBoolean("acknowledged_celebrations_initialized", value).apply()

    // Current recovery challenge question index (0 to 4)
    var recoveryChallengeProgress: Int
        get() = prefs.getInt("recovery_challenge_progress", 0)
        set(value) = prefs.edit().putInt("recovery_challenge_progress", value).apply()

    /**
     * Determines the streak status and recovery availability on app launch.
     * Returns true if recovery is required.
     */
    fun checkStreakOnAppLaunch(): Boolean {
        val today = LocalDate.now()
        val lastActiveStr = lastActiveDate

        // If there's an active timer, check if it has expired
        if (recoveryDeadline > 0L) {
            if (System.currentTimeMillis() >= recoveryDeadline) {
                // Timer expired while app was closed
                return true // Go to recovery screen which will show failure state
            }
            return true // Resume active challenge screen
        }

        if (lastActiveStr == null) {
            // No previous activity, everything is normal (streak 0)
            return false
        }

        val lastActive = try {
            LocalDate.parse(lastActiveStr)
        } catch (e: Exception) {
            today.minusDays(1)
        }

        val daysDiff = ChronoUnit.DAYS.between(lastActive, today)

        when {
            daysDiff <= 1 -> {
                // Normal behavior:
                // 0: completed a word today
                // 1: completed a word yesterday, still has today to complete a word
                return false
            }
            daysDiff == 2L -> {
                // Missed exactly one day (yesterday)
                // Check if already resolved today
                val todayStr = today.toString()
                if (recoveryCompletedDate == todayStr || recoveryFailedDate == todayStr) {
                    return false
                }
                return true // Show recovery intro screen
            }
            else -> {
                // Missed 2 or more days. Reset streak immediately.
                currentStreak = 0
                recoveryDeadline = 0L
                recoveryChallengeProgress = 0
                return false
            }
        }
    }

    /**
     * Call this when a normal Word Me word is successfully completed.
     */
    fun onWordCompleted() {
        val todayStr = LocalDate.now().toString()
        if (lastActiveDate == todayStr) {
            // Already completed a word today. No extra streak or day count.
            return
        }

        // Increment active day count
        dayCount++

        if (currentStreak == 0) {
            currentStreak = 1
        } else {
            // Since they completed the word today and were active,
            // they increment the streak.
            currentStreak++
        }

        lastActiveDate = todayStr

        // Check for celebrations
        checkCelebrationUnlocked(currentStreak)
    }

    /**
     * Call this when the recovery challenge is successfully completed.
     */
    fun onRecoverySuccess() {
        recoveryCompletedDate = LocalDate.now().toString()
        recoveryDeadline = 0L
        recoveryChallengeProgress = 0
        // Previous streak remains intact!
        // No streak increment happens automatically - user must still complete a normal word today.
    }

    /**
     * Call this when the recovery challenge fails (timer expires).
     */
    fun onRecoveryFailure() {
        recoveryFailedDate = LocalDate.now().toString()
        recoveryDeadline = 0L
        recoveryChallengeProgress = 0
        currentStreak = 0 // Reset streak to 0
    }

    /**
     * Checks if a streak value qualifies for a celebration and sets it as pending.
     */
    private fun checkCelebrationUnlocked(streak: Int) {
        if (streak > lastShownStreakCelebration && isStreakMilestone(streak)) {
            celebrationPending = true
            pendingCelebrationStreak = streak
        }
    }

    fun isStreakMilestone(streak: Int): Boolean {
        return streak == 3 || streak == 7 || (streak >= 14 && streak % 7 == 0)
    }
}
