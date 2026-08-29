package com.example.wordme.data

enum class LearningGoal(
    val displayName: String,
    val subtitle: String,
    val emoji: String,
    val category: String = displayName
) {
    EVERYDAY_ENGLISH(
        displayName = "Everyday English",
        subtitle = "Speak naturally in daily situations",
        emoji = "💬",
        category = "Everyday English"
    ),

    TRAVEL_ENGLISH(
        displayName = "Travel English",
        subtitle = "Be confident wherever you go",
        emoji = "✈️",
        category = "Travel English"
    ),

    WORK_AND_BUSINESS(
        displayName = "Work & Business",
        subtitle = "Communicate confidently at work",
        emoji = "💼",
        category = "Work & Business"
    ),

    ACADEMIC_ENGLISH(
        displayName = "Academic English",
        subtitle = "Study, write and succeed in English",
        emoji = "🎓",
        category = "Academic English"
    ),

    MEDIA_AND_READING(
        displayName = "Media & Reading",
        subtitle = "Understand books, news, movies and shows",
        emoji = "📰",
        category = "Media & Reading"
    );

    val title: String
        get() = displayName

    companion object {
        fun fromDisplayName(name: String): LearningGoal? =
            entries.firstOrNull { it.displayName.equals(name, ignoreCase = true) }
    }
}