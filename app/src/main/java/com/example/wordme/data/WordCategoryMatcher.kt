package com.example.wordme.data

object WordCategoryMatcher {

    fun matches(
        word: Word,
        goals: List<LearningGoal>
    ): Boolean {

        if (goals.isEmpty()) {
            return true
        }

        return goals.any { goal ->
            word.category.equals(
                goal.category,
                ignoreCase = true
            )
        }
    }
}