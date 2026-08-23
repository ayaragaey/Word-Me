package com.example.wordme.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordme.ui.WordMeIcons
import com.example.wordme.ui.theme.AccentBlue
import com.example.wordme.ui.theme.CardBackground
import com.example.wordme.ui.theme.LightBlue
import com.example.wordme.ui.theme.MutedBlueGrey
import com.example.wordme.ui.theme.NavyPrimary
import com.example.wordme.ui.theme.PositiveFeedbackBg
import com.example.wordme.ui.theme.PositiveFeedbackText
import com.example.wordme.ui.theme.SoftBlueBorder
import com.example.wordme.ui.theme.StreakAccent
import com.example.wordme.utils.MilestoneItem
import com.example.wordme.utils.MilestoneStatus

@Composable
fun AchievementCard(
    item: MilestoneItem,
    modifier: Modifier = Modifier
) {
    val isUnlocked = item.status == MilestoneStatus.UNLOCKED
    val isInProgress = item.status == MilestoneStatus.IN_PROGRESS
    val isLocked = item.status == MilestoneStatus.LOCKED

    Card(
        modifier = modifier.height(136.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = 1.dp,
            color = when {
                isUnlocked -> PositiveFeedbackText.copy(alpha = 0.2f)
                isInProgress -> SoftBlueBorder
                else -> SoftBlueBorder.copy(alpha = 0.4f)
            }
        ),
        colors = CardDefaults.cardColors(
            containerColor = when {
                isUnlocked -> PositiveFeedbackBg.copy(alpha = 0.3f)
                else -> CardBackground
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Row: Icon badge & status indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Circular icon/badge
                val badgeBgColor = when {
                    isUnlocked -> {
                        if (item.category == "streak") Color(0xFFFFF3E0) else Color(0xFFE8F5E9)
                    }
                    isInProgress -> LightBlue.copy(alpha = 0.6f)
                    else -> LightBlue.copy(alpha = 0.2f)
                }

                val badgeTint = when {
                    isUnlocked -> {
                        if (item.category == "streak") StreakAccent else PositiveFeedbackText
                    }
                    isInProgress -> AccentBlue
                    else -> MutedBlueGrey.copy(alpha = 0.5f)
                }

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(badgeBgColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (item.category == "streak") WordMeIcons.Fire else WordMeIcons.Trophy,
                        contentDescription = null,
                        tint = badgeTint,
                        modifier = Modifier.size(16.dp)
                    )
                }

                // Checkmark / Lock status indicator
                when {
                    isUnlocked -> {
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .clip(CircleShape)
                                .background(PositiveFeedbackBg)
                                .border(1.dp, PositiveFeedbackText.copy(alpha = 0.3f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Unlocked",
                                tint = PositiveFeedbackText,
                                modifier = Modifier.size(10.dp)
                            )
                        }
                    }
                    isLocked -> {
                        Icon(
                            imageVector = WordMeIcons.Lock,
                            contentDescription = "Locked",
                            tint = MutedBlueGrey.copy(alpha = 0.4f),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    else -> {
                        // In Progress: no status icon needed, progress text suffices
                    }
                }
            }

            // Middle section: Title & Description
            Column {
                Text(
                    text = item.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isLocked) NavyPrimary.copy(alpha = 0.5f) else NavyPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.description,
                    fontSize = 11.sp,
                    color = MutedBlueGrey,
                    lineHeight = 14.sp,
                    maxLines = 2
                )
            }

            // Bottom section: Progress / Label tag
            when {
                isUnlocked -> {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(PositiveFeedbackText.copy(alpha = 0.1f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "Unlocked",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = PositiveFeedbackText
                        )
                    }
                }
                isInProgress -> {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(LightBlue.copy(alpha = 0.8f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${item.currentValue} / ${item.targetValue}",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = AccentBlue
                        )
                    }
                }
                isLocked -> {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(LightBlue.copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "Up next",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Medium,
                            color = MutedBlueGrey.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }
    }
}
