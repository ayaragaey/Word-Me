package com.example.wordme.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordme.ui.WordMeIcons
import com.example.wordme.ui.theme.CardBackground
import com.example.wordme.ui.theme.MutedBlueGrey
import com.example.wordme.ui.theme.NavyPrimary
import com.example.wordme.ui.theme.PositiveFeedbackBg
import com.example.wordme.ui.theme.PositiveFeedbackText
import com.example.wordme.ui.theme.StreakAccent

@Composable
fun FeedbackCard(
    word: String,
    score: Int,
    recommendations: List<String> = emptyList(),
    exampleSentence: String = "",
    modifier: Modifier = Modifier
) {
    val isPassed = score >= 7

    val themeColor = if (isPassed) PositiveFeedbackText else Color(0xFFEA580C)
    val bgColor = if (isPassed) PositiveFeedbackBg else Color(0xFFFFF7ED)
    val borderColor = if (isPassed) PositiveFeedbackText.copy(alpha = 0.15f) else Color(0xFFFED7AA)

    val feedbackTitle = when {
        score >= 8 -> "Great job! 🎉"
        score == 7 -> "Well done! 👍"
        score >= 5 -> "Almost there! ✍️"
        else -> "Needs Improvement 📝"
    }

    val feedbackDetails = when {
        score >= 8 -> "Your sentence is grammatically correct and you used '${word.lowercase()}' naturally."
        score == 7 -> "Good sentence! You used '${word.lowercase()}' correctly."
        else -> "You need at least 7/10 to pass. Review the recommendations below and try again!"
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, borderColor),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Main Row: Circular progress score and text
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Score circle
                Box(
                    modifier = Modifier.size(60.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawCircle(
                            color = Color.White,
                            style = Stroke(width = 5.dp.toPx())
                        )
                        val sweep = (score.toFloat() / 10f) * 360f
                        drawArc(
                            color = themeColor,
                            startAngle = -90f,
                            sweepAngle = sweep,
                            useCenter = false,
                            style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$score",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = themeColor
                        )
                        Text(
                            text = "/10",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = MutedBlueGrey
                        )
                    }
                }

                // Feedback message
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = feedbackTitle,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = themeColor
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = feedbackDetails,
                        fontSize = 12.5.sp,
                        color = NavyPrimary,
                        lineHeight = 16.5.sp
                    )
                }
            }

            // Subtle 10 dots indicator
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 1..10) {
                    val filled = i <= score
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(if (filled) themeColor else Color.Transparent)
                            .border(
                                width = 1.dp,
                                color = if (filled) themeColor else themeColor.copy(alpha = 0.3f),
                                shape = CircleShape
                            )
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (isPassed) "$score/10 (Passed)" else "$score/10 (7/10 required)",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = themeColor
                )
            }

            // Recommendations List if score < 7
            if (!isPassed && recommendations.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(CardBackground)
                        .padding(12.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(text = "💡", fontSize = 14.sp)
                            Text(
                                text = "How to improve:",
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = NavyPrimary
                            )
                        }
                        recommendations.forEach { rec ->
                            Row(
                                modifier = Modifier.padding(start = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    text = "•",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = themeColor
                                )
                                Text(
                                    text = rec,
                                    fontSize = 12.sp,
                                    color = NavyPrimary,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }
            }

            // Example Sentence Tip Box (if available and score < 7)
            if (!isPassed && exampleSentence.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(CardBackground.copy(alpha = 0.8f))
                        .padding(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(text = "✨", fontSize = 13.sp)
                        Column {
                            Text(
                                text = "Example for inspiration:",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = MutedBlueGrey
                            )
                            Text(
                                text = "\"$exampleSentence\"",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = NavyPrimary,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            } else if (isPassed) {
                // Tip container box for passing sentence
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(CardBackground)
                        .padding(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = WordMeIcons.Lightbulb,
                            contentDescription = "Tip",
                            tint = StreakAccent,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Great formulation! Keep applying new words in your daily thoughts.",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = NavyPrimary,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    }
}
