package com.example.wordme.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object WordRepository {

    private data class WordJson(
        val id: Int,
        val word: String,
        val translation: String,
        val pronunciation: String,
        val type: String,
        val definition: String,
        val examples: List<String>,
        val exampleTranslations: List<String>? = null,
        val level: String = "B1",
        val category: String = "general"
    )

    fun loadWords(context: Context): List<Word> {

        val json = context.assets
            .open("words.json")
            .bufferedReader()
            .use { it.readText() }

        val listType = object : TypeToken<List<WordJson>>() {}.type

        val jsonWords: List<WordJson> =
            Gson().fromJson(json, listType)

        return jsonWords.map { item ->
            Word(
                id = item.id,
                word = item.word,
                translation = item.translation,
                pronunciation = item.pronunciation,
                type = item.type,
                definition = item.definition,
                examples = item.examples,
                exampleTranslations = item.exampleTranslations.orEmpty(),
                level = item.level,
                category = item.category
            )
        }
    }
}