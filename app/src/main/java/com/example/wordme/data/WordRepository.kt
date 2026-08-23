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

        // Map mock words by uppercase string for easy translation lookup
        val mockLookup = WordData.mockWordsList.associateBy { it.word.uppercase() }

        return jsonWords.map { item ->
            val matchedMockWord = mockLookup[item.word.uppercase()]
            
            // Generate fallback translations if the word does not have hardcoded ones in assets or mocks
            val translations = matchedMockWord?.exampleTranslations ?: run {
                if (item.exampleTranslations.isNullOrEmpty()) {
                    item.examples.mapIndexed { idx, _ ->
                        when (idx) {
                            0 -> "هذه جملة توضيحية لاستخدام كلمة (${item.translation})."
                            1 -> "مثال آخر يوضح كيفية استعمال (${item.translation}) في سياق مفيد."
                            else -> "نموذج يبين صياغة كلمة (${item.translation}) بشكل صحيح."
                        }
                    }
                } else {
                    item.exampleTranslations
                }
            }

            Word(
                id = item.id,
                word = item.word,
                translation = item.translation,
                pronunciation = item.pronunciation,
                type = item.type,
                definition = item.definition,
                examples = item.examples,
                exampleTranslations = translations,
                level = item.level,
                category = item.category
            )
        }
    }
}