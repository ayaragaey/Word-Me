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
    modifier: Modifier = Modifier
) {
    val feedbackTitle = when {
        score >= 8 -> "Great job! 🎉"
        score >= 5 -> "Good effort! 👍"
        else -> "Keep practicing! 📝"
    }

    val feedbackDetails = when {
        score >= 8 -> "Your sentence is grammatically correct and you used '${word.lowercase()}' naturally."
        score >= 5 -> "You used '${word.lowercase()}' in the sentence. Try to make it a bit more natural or grammatically complete."
        else -> "Try to write a complete sentence using '${word.lowercase()}' to practice."
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, PositiveFeedbackText.copy(alpha = 0.15f)),
        colors = CardDefaults.cardColors(containerColor = PositiveFeedbackBg), // Pale green background
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Main Row: Circular progress score and text
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Score circle
                Box(
                    modifier = Modifier.size(64.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawCircle(
                            color = Color.White,
                            style = Stroke(width = 5.dp.toPx())
                        )
                        val sweep = (score.toFloat() / 10f) * 360f
                        drawArc(
                            color = PositiveFeedbackText,
                            startAngle = -90f,
                            sweepAngle = sweep,
                            useCenter = false,
                            style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$score",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = PositiveFeedbackText
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
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = PositiveFeedbackText
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = feedbackDetails,
                        fontSize = 13.sp,
                        color = NavyPrimary,
                        lineHeight = 17.sp
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
                            .background(if (filled) PositiveFeedbackText else Color.Transparent)
                            .border(
                                width = 1.dp,
                                color = if (filled) PositiveFeedbackText else PositiveFeedbackText.copy(alpha = 0.4f),
                                shape = CircleShape
                            )
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "$score/10",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = PositiveFeedbackText
                )
            }

            // Tip container box (Inner Card with white bg)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(CardBackground)
                    .padding(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = WordMeIcons.Lightbulb,
                        contentDescription = "Tip",
                        tint = StreakAccent,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Try to make your sentence as natural as possible.",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = NavyPrimary,
                        lineHeight = 17.sp
                    )
                }
            }
        }
    }
}
