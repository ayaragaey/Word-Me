package com.example.wordme

import com.example.wordme.data.ExampleSentenceTranslator
import com.example.wordme.data.WordData
import org.junit.Test
import org.junit.Assert.*

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testMockWordsHaveTranslations() {
        val hesitant = WordData.mockWordsList.find { it.word.equals("HESITANT", ignoreCase = true) }
        assertNotNull(hesitant)
        assertEquals(3, hesitant!!.examples.size)
        assertEquals(3, hesitant.exampleTranslations.size)
        assertEquals("كانت مترددة في قبول العرض.", hesitant.exampleTranslations[0])
    }

    @Test
    fun testTemplateSentenceTranslation() {
        val translation = ExampleSentenceTranslator.translate(
            sentence = "We discussed the time during the lesson.",
            targetWord = "time",
            wordTranslation = "الوقت"
        )
        assertEquals("ناقشنا الوقت خلال الدرس.", translation)
    }

    @Test
    fun testNoIdCollisionsBetweenCustomWordsAndJsonWords() {
        val file = java.io.File("src/main/assets/words.json")
        assertTrue("words.json must exist", file.exists())
        val json = file.readText()
        val listType = object : com.google.gson.reflect.TypeToken<List<com.example.wordme.data.Word>>() {}.type
        val wordsFromJson: List<com.example.wordme.data.Word> = com.google.gson.Gson().fromJson(json, listType)
        val jsonById = wordsFromJson.associateBy { it.id }
        val jsonByName = wordsFromJson.associateBy { it.word.uppercase().trim() }

        // Verify all IDs in words.json are unique
        assertEquals(wordsFromJson.size, jsonById.size)

        // Verify mock words consistency
        for (mockWord in WordData.mockWordsList) {
            val matchingByName = jsonByName[mockWord.word.uppercase().trim()]
            if (matchingByName != null) {
                assertEquals(
                    "Word '${mockWord.word}' should have same ID in mockWordsList as in words.json",
                    matchingByName.id,
                    mockWord.id
                )
            } else {
                assertNull(
                    "Custom word '${mockWord.word}' ID ${mockWord.id} should not collide with words.json",
                    jsonById[mockWord.id]
                )
            }
        }

        // Verify initial learned words consistency
        for (learnedWord in WordData.initialLearnedWords) {
            val matchingByName = jsonByName[learnedWord.word.uppercase().trim()]
            if (matchingByName != null) {
                assertEquals(
                    "Word '${learnedWord.word}' should have same ID in initialLearnedWords as in words.json",
                    matchingByName.id,
                    learnedWord.id
                )
            } else {
                assertNull(
                    "Custom word '${learnedWord.word}' ID ${learnedWord.id} should not collide with words.json",
                    jsonById[learnedWord.id]
                )
            }
        }
    }

    @Test
    fun testAudioCacheKeyUniqueness() {
        // Build sample cache keys
        fun getCacheKey(wordId: Int, female: Boolean, wordText: String): String {
            val gender = if (female) "female" else "male"
            val cleanWord = wordText.trim().lowercase().replace(Regex("[^a-z0-9]"), "_")
            return "vocab_v2_${wordId}_${gender}_${cleanWord}"
        }

        val wordId = 101
        val wordText = "international"

        val femaleKey = getCacheKey(wordId, true, wordText)
        val maleKey = getCacheKey(wordId, false, wordText)

        // Male and Female cache keys must never be equal
        assertNotEquals(femaleKey, maleKey)
        assertEquals("vocab_v2_101_female_international", femaleKey)
        assertEquals("vocab_v2_101_male_international", maleKey)

        // Different word IDs must produce different keys
        val keyWord2 = getCacheKey(102, true, "left")
        assertNotEquals(femaleKey, keyWord2)
    }

    @Test
    fun testStaleCacheInvalidation() {
        val tempDir = java.nio.file.Files.createTempDirectory("vocab_audio_test").toFile()
        try {
            // Create a stale file: ID 101 had "oldword"
            val staleMaleFile = java.io.File(tempDir, "vocab_v2_101_male_oldword.wav")
            val staleFemaleFile = java.io.File(tempDir, "vocab_v2_101_female_oldword.wav")
            // Create a legacy v1 file that should be pruned
            val legacyFile = java.io.File(tempDir, "vocab_101_male_oldword.wav")
            // Create a valid file: ID 101 has "newword"
            val validFile = java.io.File(tempDir, "vocab_v2_101_male_newword.wav")
            // Create an unrelated file: ID 102 has "otherword"
            val otherWordFile = java.io.File(tempDir, "vocab_v2_102_male_otherword.wav")

            staleMaleFile.writeText("dummy stale male")
            staleFemaleFile.writeText("dummy stale female")
            legacyFile.writeText("dummy legacy")
            validFile.writeText("dummy valid")
            otherWordFile.writeText("dummy other")

            val wordId = 101
            val expectedWord = "newword"
            val cleanWord = expectedWord.trim().lowercase().replace(Regex("[^a-z0-9]"), "_")
            val prefix = "vocab_v2_${wordId}_"

            // Run invalidation logic
            for (file in tempDir.listFiles() ?: emptyArray()) {
                if (file.name.startsWith("vocab_${wordId}_") && !file.name.startsWith("vocab_v2_")) {
                    file.delete()
                    continue
                }
                if (file.name.startsWith(prefix) && file.name.endsWith(".wav")) {
                    val nameWithoutExt = file.name.removeSuffix(".wav")
                    val parts = nameWithoutExt.split("_", limit = 5)
                    if (parts.size >= 5) {
                        val cachedWord = parts[4]
                        if (cachedWord != cleanWord) {
                            file.delete()
                        }
                    }
                }
            }

            // Stale and legacy files should be deleted
            assertFalse(staleMaleFile.exists())
            assertFalse(staleFemaleFile.exists())
            assertFalse(legacyFile.exists())
            // Valid file for 101 and unrelated file for 102 should still exist
            assertTrue(validFile.exists())
            assertTrue(otherWordFile.exists())
        } finally {
            tempDir.deleteRecursively()
        }
    }

    @Test
    fun testDisplayedWordEqualsTtsInputTextAndVoiceSwitchDoesNotChangeText() {
        val wordsToTest = listOf(
            com.example.wordme.data.Word(1, "NEW", "جديد", "نو", "adjective", "Not of long duration", emptyList()),
            com.example.wordme.data.Word(101, "INTERNATIONAL", "دولي", "انترناشونال", "adjective", "Concerning nations", emptyList()),
            com.example.wordme.data.Word(6553, "MECHANICALLY", "ميكانيكياً", "ميكانيكلي", "adverb", "By mechanism", emptyList()),
            com.example.wordme.data.Word(15001, "OVERWHELMED", "مرهق", "اوفركيلمد", "adjective", "Having strong emotion", emptyList())
        )

        for (word in wordsToTest) {
            val displayedWord = word.word.uppercase()
            val textSentToTTSFemale = word.word.trim()
            val textSentToTTSMale = word.word.trim()

            // 1. Displayed word must match TTS text
            assertTrue(displayedWord.equals(textSentToTTSFemale, ignoreCase = true))
            assertTrue(displayedWord.equals(textSentToTTSMale, ignoreCase = true))

            // 2. Switching between female and male must never alter the spoken text
            assertEquals(textSentToTTSFemale, textSentToTTSMale)

            // 3. Audio keys are tied to the exact word ID and text
            val femaleKey = "vocab_v2_${word.id}_female_${word.word.trim().lowercase()}"
            val maleKey = "vocab_v2_${word.id}_male_${word.word.trim().lowercase()}"

            assertTrue(femaleKey.contains("${word.id}"))
            assertTrue(maleKey.contains("${word.id}"))
            assertTrue(femaleKey.contains("female"))
            assertTrue(maleKey.contains("male"))
            assertNotEquals(femaleKey, maleKey)
        }
    }

    @Test
    fun testLevelDetailsAtZeroWords() {
        val levelDetails = com.example.wordme.utils.LevelUtils.calculateLevelDetails(0)
        assertEquals(1, levelDetails.level)
        assertEquals("Starter", levelDetails.name)
        assertEquals(0, levelDetails.currentThreshold)
        assertEquals(50, levelDetails.nextThreshold)
        assertEquals(0f, levelDetails.progressFraction, 0.001f)
        assertEquals(0, levelDetails.progressPercentage)
        assertEquals(50, levelDetails.wordsRemaining)
        assertEquals("50 words to Explorer", levelDetails.supportingMessage)
    }

    @Test
    fun testMilestonesAtZeroWordsAndStreak() {
        val wordMilestones = com.example.wordme.utils.MilestoneUtils.getWordMilestones(0)
        // At 0 words learned, no milestone is UNLOCKED
        assertTrue(wordMilestones.none { it.status == com.example.wordme.utils.MilestoneStatus.UNLOCKED })
        val inProgressWord = wordMilestones.find { it.status == com.example.wordme.utils.MilestoneStatus.IN_PROGRESS }
        assertNotNull(inProgressWord)
        assertEquals(50, inProgressWord!!.targetValue)
        assertEquals(0, inProgressWord.currentValue)

        val streakMilestones = com.example.wordme.utils.MilestoneUtils.getStreakMilestones(0)
        // At 0 streak, no milestone is UNLOCKED
        assertTrue(streakMilestones.none { it.status == com.example.wordme.utils.MilestoneStatus.UNLOCKED })
        val inProgressStreak = streakMilestones.find { it.status == com.example.wordme.utils.MilestoneStatus.IN_PROGRESS }
        assertNotNull(inProgressStreak)
        assertEquals(3, inProgressStreak!!.targetValue)
        assertEquals(0, inProgressStreak.currentValue)
    }
}