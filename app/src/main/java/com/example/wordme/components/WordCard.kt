package com.example.wordme.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordme.data.LearningGoal
import com.example.wordme.data.Word
import com.example.wordme.ui.WordMeIcons
import com.example.wordme.ui.theme.AccentBlue
import com.example.wordme.ui.theme.BrightBlue
import com.example.wordme.ui.theme.CardBackground
import com.example.wordme.ui.theme.FemalePronunciationBg
import com.example.wordme.ui.theme.FemalePronunciationText
import com.example.wordme.ui.theme.LightBlue
import com.example.wordme.ui.theme.MutedBlueGrey
import com.example.wordme.ui.theme.NavyPrimary
import com.example.wordme.ui.theme.SoftBlueBorder

@Composable
fun WordCard(
    word: Word,
    showExampleTranslations: Boolean,
    onToggleExampleTranslations: () -> Unit,
    onSpeakFemale: (Word) -> Unit,
    onSpeakMale: (Word) -> Unit,
    onMoreSentencesClick: () -> Unit,
    onAnotherWordClick: () -> Unit,
    learningGoal: String? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp), // Premium 24dp rounded corners
        border = BorderStroke(1.dp, SoftBlueBorder),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp) // Subtle shadow
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Top Row: Word status badge & speakers
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // WORD OF THE DAY badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(LightBlue)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "☆",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = AccentBlue
                        )
                        Text(
                            text = "WORD OF THE DAY",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = AccentBlue,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                // Compact speaker buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Female pronunciation button (pink circle)
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(FemalePronunciationBg)
                            .clickable { onSpeakFemale(word) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = WordMeIcons.Speaker,
                            contentDescription = "Female pronunciation",
                            tint = FemalePronunciationText,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    // Male pronunciation button (pale blue circle)
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(LightBlue)
                            .clickable { onSpeakMale(word) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = WordMeIcons.Speaker,
                            contentDescription = "Male pronunciation",
                            tint = AccentBlue,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Main Vocabulary Word & Learning Goal Tag
            val goalToDisplay = getWordLearningGoal(word, learningGoal)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Word and Change Word (Undo) icon inline
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Text(
                        text = word.word.uppercase(),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = NavyPrimary,
                        letterSpacing = 0.5.sp
                    )
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .clickable { onAnotherWordClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Another Word",
                            tint = AccentBlue,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                if (!goalToDisplay.isNullOrBlank()) {
                    Spacer(modifier = Modifier.width(16.dp))
                    LearningGoalTag(goal = goalToDisplay)
                }
            }

            // Arabic Translation & English Transliteration on a single clean line
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = word.translation,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentBlue
                )
                Text(
                    text = "/ ${word.pronunciation} /",
                    fontSize = 12.sp,
                    color = MutedBlueGrey
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Part of Speech in bright blue
            Text(
                text = word.type.lowercase(),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                color = AccentBlue
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Definition in slightly heavier typography
            Text(
                text = word.definition,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = NavyPrimary,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = SoftBlueBorder, thickness = 1.dp)
            Spacer(modifier = Modifier.height(14.dp))

            // Examples Header and toggle inline
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = WordMeIcons.SpeechBubble,
                        contentDescription = null,
                        tint = AccentBlue,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "IN A SENTENCE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentBlue,
                        letterSpacing = 0.5.sp
                    )
                }

                // Show/Hide examples translation toggle inside a premium rounded box/pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(LightBlue)
                        .clickable { onToggleExampleTranslations() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = if (showExampleTranslations) "Hide Translation" else "Show Translation",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = AccentBlue
                        )
                        Text(
                            text = if (showExampleTranslations) "⌃" else "⌄",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = AccentBlue
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Example list
            word.examples.forEachIndexed { index, sentence ->
                val translation = word.exampleTranslations.getOrNull(index)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Badge numbers circular light blue badge "01", "02", "03"
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(LightBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "%02d".format(index + 1),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = AccentBlue
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        // Sentence with highlighted word in bright blue
                        Text(
                            text = highlightSentenceWord(sentence, word.word),
                            fontSize = 14.sp,
                            color = NavyPrimary,
                            lineHeight = 18.sp,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Translation below if visible
                    if (showExampleTranslations && translation != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = translation,
                            fontSize = 13.sp,
                            color = MutedBlueGrey,
                            textAlign = TextAlign.Right,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 34.dp)
                        )
                    }

                    if (index < word.examples.size - 1) {
                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(color = SoftBlueBorder.copy(alpha = 0.5f), thickness = 0.5.dp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Bottom Actions: More Sentences (right aligned)
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(LightBlue)
                        .clickable { onMoreSentencesClick() }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "More Sentences",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentBlue
                    )
                }
            }
        }
    }
}

@Composable
fun highlightSentenceWord(sentence: String, target: String): AnnotatedString {
    return remember(sentence, target) {
        buildAnnotatedString {
            val lowercaseSentence = sentence.lowercase()
            val lowercaseTarget = target.lowercase()
            var startIndex = 0
            while (true) {
                val index = lowercaseSentence.indexOf(lowercaseTarget, startIndex)
                if (index == -1) {
                    append(sentence.substring(startIndex))
                    break
                }
                append(sentence.substring(startIndex, index))
                withStyle(
                    SpanStyle(
                        color = BrightBlue, // Bright blue highlighted target word
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append(sentence.substring(index, index + target.length))
                }
                startIndex = index + target.length
            }
        }
    }
}

/**
 * Helper to resolve the exact Learning Goal for a word.
 */
fun getWordLearningGoal(word: Word, preferredGoal: String? = null): String {
    return preferredGoal
        ?: word.goalTags.firstOrNull { it.isNotBlank() }
        ?: when (word.category.lowercase()) {
            "business", "finance" -> LearningGoal.WORK_AND_BUSINESS.displayName
            "exam", "exams", "study", "education" -> LearningGoal.ACADEMIC_ENGLISH.displayName
            "travel" -> LearningGoal.TRAVEL_ENGLISH.displayName
            "media", "reading", "movies" -> LearningGoal.MEDIA_AND_READING.displayName
            else -> LearningGoal.EVERYDAY_ENGLISH.displayName
        }
}

/**
 * Compact visual chip displaying a word's exact learning goal.
 */
@Composable
fun LearningGoalTag(
    goal: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 7.sp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 4.dp, vertical = 1.dp)
) {
    if (goal.isBlank()) return

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(LightBlue)
            .border(1.dp, SoftBlueBorder, RoundedCornerShape(6.dp))
            .padding(contentPadding),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = goal,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            color = AccentBlue,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/**
 * Backward compatibility alias for LearningGoalTag.
 */
@Composable
fun CategoryTag(
    category: String,
    modifier: Modifier = Modifier
) {
    LearningGoalTag(goal = category, modifier = modifier)
}

