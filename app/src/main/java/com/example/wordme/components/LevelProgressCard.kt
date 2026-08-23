package com.example.wordme.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordme.ui.theme.BrightBlue
import com.example.wordme.ui.theme.DarkBlue
import com.example.wordme.ui.theme.NavyPrimary
import com.example.wordme.utils.LevelDetails

@Composable
fun LevelBadge(level: Int, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(30.dp) // Shrunk width
            .height(40.dp), // Shrunk height
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // Ribbon shape with a V-notch at the bottom
            val path = Path().apply {
                moveTo(0f, 0f)
                lineTo(width, 0f)
                lineTo(width, height * 0.85f)
                lineTo(width * 0.5f, height * 0.7f)
                lineTo(0f, height * 0.85f)
                close()
            }
            drawPath(
                path = path,
                color = Color(0xFF1684F8) // Primary blue accent for ribbon body
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(bottom = 3.dp)
        ) {
            Text(
                text = "👑",
                fontSize = 8.sp // Smaller crown
            )
            Spacer(modifier = Modifier.height(1.dp))
            Text(
                text = "$level",
                color = Color.White,
                fontSize = 12.sp, // Smaller font
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Composable
fun LevelProgressCard(
    levelDetails: LevelDetails,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(), // Removed fixed height so it wraps compactly
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkBlue), // Dark blue/navy card background
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp), // Reduced vertical padding
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Level badge on the left
            LevelBadge(level = levelDetails.level)

            // Details on the right
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp) // Tighter vertical spacing
            ) {
                // Header (Level number and Name closer to count)
                Text(
                    text = "LEVEL ${levelDetails.level} • ${levelDetails.name.uppercase()}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.8f),
                    letterSpacing = 0.5.sp
                )

                // Word count and percentage row closer to progress bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    val currentProgressValue = levelDetails.currentThreshold + (levelDetails.progressFraction * (levelDetails.nextThreshold - levelDetails.currentThreshold)).toInt()
                    Text(
                        text = "$currentProgressValue / ${levelDetails.nextThreshold} words",
                        fontSize = 15.sp, // Slightly smaller text size
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        text = "${levelDetails.progressPercentage}%",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }

                // Bright blue progress bar
                LinearProgressIndicator(
                    progress = { levelDetails.progressFraction },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp) // Reduced height from 6dp to 5dp
                        .clip(RoundedCornerShape(3.dp)),
                    color = BrightBlue, // Bright blue progress fill
                    trackColor = Color.White.copy(alpha = 0.15f),
                    strokeCap = StrokeCap.Round
                )

                // Words remaining close to progress bar with soft gold star accent
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "⭐",
                        fontSize = 9.sp // Smaller star icon
                    )
                    Text(
                        text = levelDetails.supportingMessage,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFEF08A) // Soft gold text
                    )
                }
            }
        }
    }
}
