package com.example.wordme.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.wordme.ui.WordMeIcons
import com.example.wordme.ui.theme.AccentBlue
import com.example.wordme.ui.theme.CardBackground
import com.example.wordme.ui.theme.LightBlue
import com.example.wordme.ui.theme.MutedBlueGrey
import com.example.wordme.ui.theme.NavyPrimary
import com.example.wordme.ui.theme.SoftBlueBorder
import com.example.wordme.ui.theme.StreakAccent

@Composable
fun StreakCelebrationDialog(
    streak: Int,
    userName: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (title, subtitle, body) = when (streak) {
        3 -> Triple(
            "3 DAYS!",
            "Your first streak milestone",
            "You've shown up for 3 days in a row. Great start, $userName!"
        )
        7 -> Triple(
            "7 DAYS!",
            "One full week",
            "Congratulations, $userName! You've kept your Word Me streak alive for 7 days."
        )
        30 -> Triple(
            "30 DAYS!",
            "Amazing consistency",
            "Outstanding work, $userName! You've learned with Word Me for 30 days in a row."
        )
        60 -> Triple(
            "60 DAYS! 🔥",
            "Two months and counting",
            "Two months and counting, $userName! Outstanding consistency!"
        )
        90 -> Triple(
            "90 DAYS! 🔥",
            "Three months of learning",
            "Three months of learning, $userName! You're making massive progress."
        )
        else -> {
            val months = streak / 30
            val monthLabel = if (months == 4) "Four months" else if (months == 5) "Five months" else "$months months"
            Triple(
                "$streak DAYS! 🔥",
                "$monthLabel of consistency",
                "You've learned with Word Me for $streak days in a row. Keep it up, $userName!"
            )
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, SoftBlueBorder),
            colors = CardDefaults.cardColors(containerColor = CardBackground),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Celebration Icon Emblem
                Box(
                    modifier = Modifier
                        .size(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Confetti/Sparkles in background
                    Icon(
                        imageVector = WordMeIcons.Sparkles,
                        contentDescription = null,
                        tint = AccentBlue.copy(alpha = 0.3f),
                        modifier = Modifier.size(90.dp)
                    )

                    // Circular container for flame
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFF2E6)), // Light orange background
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = WordMeIcons.Fire,
                            contentDescription = "Streak Flame",
                            tint = StreakAccent, // Orange flame
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }

                // Title
                Text(
                    text = title,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = NavyPrimary,
                    textAlign = TextAlign.Center
                )

                // Subtitle
                Text(
                    text = subtitle,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentBlue,
                    textAlign = TextAlign.Center
                )

                // Body description
                Text(
                    text = body,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = MutedBlueGrey,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Keep Going Button
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentBlue,
                        contentColor = CardBackground
                    )
                ) {
                    Text(
                        text = "KEEP GOING",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}

@Composable
fun WordMilestoneCelebrationDialog(
    count: Int,
    userName: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, SoftBlueBorder),
            colors = CardDefaults.cardColors(containerColor = CardBackground),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Celebration Icon Emblem
                Box(
                    modifier = Modifier.size(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Confetti/Sparkles in background
                    Icon(
                        imageVector = WordMeIcons.Sparkles,
                        contentDescription = null,
                        tint = AccentBlue.copy(alpha = 0.3f),
                        modifier = Modifier.size(90.dp)
                    )

                    // Circular container for trophy
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(LightBlue.copy(alpha = 0.6f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = WordMeIcons.Trophy,
                            contentDescription = "Trophy",
                            tint = AccentBlue,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }

                // Title
                Text(
                    text = "$count WORDS!",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = NavyPrimary,
                    textAlign = TextAlign.Center
                )

                // Subtitle
                Text(
                    text = "Vocabulary milestone",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentBlue,
                    textAlign = TextAlign.Center
                )

                // Body description
                Text(
                    text = "You've successfully completed the exercises and learned $count unique vocabulary words. Fantastic effort, $userName!",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = MutedBlueGrey,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Keep Going Button
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentBlue,
                        contentColor = CardBackground
                    )
                ) {
                    Text(
                        text = "KEEP IT UP",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}
