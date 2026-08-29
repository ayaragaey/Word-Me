package com.example.wordme.data

import kotlin.collections.emptyList

data class UserLearningProfile(
    val selectedGoals: List<LearningGoal> = emptyList(),
    val dailyWordTarget: Int = 10,
    val currentLevel: String = "A1"
)