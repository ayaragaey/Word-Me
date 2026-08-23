package com.example.wordme.data

data class RecoveryQuestion(
    val id: Int,
    val type: String, // "definition", "translation", "completion"
    val questionText: String,
    val options: List<String>,
    val correctOptionIndex: Int
)

object RecoveryQuestions {
    val questions = listOf(
        RecoveryQuestion(
            id = 1,
            type = "definition",
            questionText = "What is the definition of ABUNDANT?",
            options = listOf(
                "Existing or available in large quantities; plentiful.",
                "Well meaning and kindly.",
                "Fluent or persuasive in speaking or writing.",
                "No longer produced or used; out of date."
            ),
            correctOptionIndex = 0
        ),
        RecoveryQuestion(
            id = 2,
            type = "translation",
            questionText = "What is the Arabic translation of DILIGENT?",
            options = listOf(
                "صراحة",
                "مجتهد",
                "وفير",
                "حذر"
            ),
            correctOptionIndex = 1
        ),
        RecoveryQuestion(
            id = 3,
            type = "completion",
            questionText = "Complete the sentence: 'He made an _______ speech that moved the audience.'",
            options = listOf(
                "frugal",
                "obsolete",
                "eloquent",
                "wary"
            ),
            correctOptionIndex = 2
        ),
        RecoveryQuestion(
            id = 4,
            type = "definition",
            questionText = "What is the meaning of CANDOR?",
            options = listOf(
                "A sentimental longing or wistful affection for the past.",
                "Cheerful and friendly.",
                "Well meaning and kindly.",
                "The quality of being open and honest in expression; frankness."
            ),
            correctOptionIndex = 3
        ),
        RecoveryQuestion(
            id = 5,
            type = "translation",
            questionText = "What is the Arabic translation of WARY?",
            options = listOf(
                "حذر",
                "وفير",
                "خامل",
                "صراحة"
            ),
            correctOptionIndex = 0
        )
    )
}
