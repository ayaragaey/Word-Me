package com.example.wordme.data

data class Word(
    val id: Int,
    val word: String,
    val translation: String,
    val pronunciation: String,
    val type: String,
    val definition: String,
    val examples: List<String>,
    val exampleTranslations: List<String> = emptyList(),
    val level: String = "B1",
    val category: String = "general",
    val learnedDate: String? = null
)