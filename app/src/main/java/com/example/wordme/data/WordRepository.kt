package com.example.wordme.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object WordRepository {

    fun loadWords(context: Context): List<Word> {

        val json = context.assets
            .open("words.json")
            .bufferedReader()
            .use { it.readText() }

        val wordListType = object : TypeToken<List<Word>>() {}.type

        return Gson().fromJson(json, wordListType)
    }
}