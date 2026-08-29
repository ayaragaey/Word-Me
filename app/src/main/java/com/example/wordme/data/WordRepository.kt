package com.example.wordme.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object WordRepository {

    private var cachedWords: List<Word>? = null

    fun loadWords(context: Context): List<Word> {

        if (cachedWords != null) {
            return cachedWords!!
        }

        val json = context.assets
            .open("words.json")
            .bufferedReader()
            .use { it.readText() }

        val type = object : TypeToken<List<Word>>() {}.type

        val mockLookup = (WordData.mockWordsList + WordData.initialLearnedWords)
            .associateBy { it.word.uppercase().trim() }

        cachedWords = Gson().fromJson<List<Word>>(json, type).map { word ->
            val matchedMock = mockLookup[word.word.uppercase().trim()]

            val translations = when {
                !matchedMock?.exampleTranslations.isNullOrEmpty() -> {
                    matchedMock!!.exampleTranslations
                }
                !word.exampleTranslations.isNullOrEmpty() -> {
                    word.exampleTranslations
                }
                else -> {
                    word.examples.map { sentence ->
                        ExampleSentenceTranslator.translate(
                            sentence = sentence,
                            targetWord = word.word,
                            wordTranslation = word.translation
                        )
                    }
                }
            }

            word.copy(
                exampleTranslations = translations,
                goalTags = word.goalTags ?: emptyList()
            )
        }

        return cachedWords!!
    }

    fun getWordsForGoals(
        context: Context,
        goals: List<LearningGoal>
    ): List<Word> {
        val allWords = loadWords(context)

        return allWords.filter { word ->
            goals.isEmpty() || goals.any { goal ->
                word.goalTags.contains(goal.displayName)
            }
        }
    }

    fun getAllWordsMap(context: Context): Map<Int, Word> {
        val loaded = loadWords(context)
        return (loaded + WordData.mockWordsList + WordData.initialLearnedWords).associateBy { it.id }
    }

    fun getWordById(context: Context, id: Int): Word? {
        return getAllWordsMap(context)[id]
    }
}