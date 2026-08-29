package com.example.wordme.screens.milestones

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordme.components.AchievementCard
import com.example.wordme.components.LevelProgressCard
import com.example.wordme.components.NamePromptDialog
import com.example.wordme.ui.WordMeIcons
import com.example.wordme.ui.WordViewModel
import com.example.wordme.ui.theme.AccentBlue
import com.example.wordme.ui.theme.CardBackground
import com.example.wordme.ui.theme.LightBlue
import com.example.wordme.ui.theme.MutedBlueGrey
import com.example.wordme.ui.theme.NavyPrimary
import com.example.wordme.ui.theme.SoftBlueBorder
import com.example.wordme.ui.theme.StreakAccent
import com.example.wordme.utils.MilestoneStatus
import com.example.wordme.utils.MilestoneUtils

@Composable
fun MyMilestonesScreen(
    viewModel: WordViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    var showEditNameDialog by remember { mutableStateOf(false) }

    if (showEditNameDialog) {
        NamePromptDialog(
            title = "Edit your name",
            subtitle = "Update how you want us to address you:",
            initialName = viewModel.userName ?: "",
            buttonText = "SAVE",
            isDismissible = true,
            onDismiss = { showEditNameDialog = false },
            onNameSubmitted = { newName ->
                viewModel.updateUserName(newName)
                showEditNameDialog = false
            }
        )
    }

    // Retrieve word and streak milestones
    val wordMilestones = remember(viewModel.wordsLearnedCount) {
        MilestoneUtils.getWordMilestones(viewModel.wordsLearnedCount)
    }
    val streakMilestones = remember(viewModel.streakCount) {
        MilestoneUtils.getStreakMilestones(viewModel.streakCount)
    }

    // Next word milestone target
    val inProgressWordMilestone = wordMilestones.find { it.status == MilestoneStatus.IN_PROGRESS }
    val nextWordTarget = inProgressWordMilestone?.targetValue ?: 50
    val prevWordTarget = if (nextWordTarget > 50) nextWordTarget - 50 else 0
    val currentProgressInMilestone = (viewModel.wordsLearnedCount - prevWordTarget).coerceAtLeast(0)
    val totalRangeInMilestone = (nextWordTarget - prevWordTarget).coerceAtLeast(1)
    val wordProgressFraction = if (viewModel.wordsLearnedCount >= 500000) 1.0f else currentProgressInMilestone.toFloat() / totalRangeInMilestone.toFloat()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp) // Cohesive 12dp gap
    ) {
        // Back to Profile Button
        Row(
            modifier = Modifier
                .clickable { viewModel.selectTab(com.example.wordme.navigation.Screen.PROFILE) }
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back to My Profile",
                tint = AccentBlue,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = "Back to My Profile",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = AccentBlue
            )
        }



        // Header Area with Trophy
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color(0xFF0D2A59), fontWeight = FontWeight.Bold)) {
                            append("My ")
                        }
                        withStyle(style = SpanStyle(color = Color(0xFF2784F5), fontWeight = FontWeight.Bold)) {
                            append("Milestones")
                        }
                    },
                    fontSize = 32.sp,
                    fontFamily = FontFamily.Serif
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    viewModel.userName?.let { name ->
                        Text(
                            text = "Hi $name",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = AccentBlue,
                            modifier = Modifier.clickable { showEditNameDialog = true }
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "•",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MutedBlueGrey.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                    }
                    Text(
                        text = "Track your progress and celebrate your wins.",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = MutedBlueGrey
                    )
                }
            }
        }

        // Summary Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, SoftBlueBorder),
            colors = CardDefaults.cardColors(containerColor = CardBackground),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 14.dp, horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatCol(value = "${viewModel.dayCount}", label = "Days Active", icon = "days")
                DividerLine()
                StatCol(value = "${viewModel.wordsLearnedCount}", label = "Words Learned", icon = "words")
                DividerLine()
                StatCol(value = "🔥 ${viewModel.streakCount}", label = "Day Streak", icon = "streak")
                DividerLine()
                StatCol(value = "${viewModel.sentencesWrittenCount}", label = "Sentences", icon = "sentences")
                DividerLine()
                StatCol(value = viewModel.bestScore, label = "Best Score", icon = "score")
            }
        }

        // Level Section
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "Level Standing",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = NavyPrimary
            )
            LevelProgressCard(levelDetails = viewModel.levelDetails)
        }

        // Learning Progress Section
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "Learning Progress",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = NavyPrimary
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, SoftBlueBorder),
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Circular Progress on Left
                    Box(
                        modifier = Modifier.size(68.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            drawCircle(
                                color = LightBlue.copy(alpha = 0.5f),
                                style = Stroke(width = 5.dp.toPx())
                            )
                            drawArc(
                                color = AccentBlue,
                                startAngle = -90f,
                                sweepAngle = wordProgressFraction * 360f,
                                useCenter = false,
                                style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round)
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${viewModel.wordsLearnedCount}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = NavyPrimary
                            )
                            Text(
                                text = "/$nextWordTarget",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MutedBlueGrey
                            )
                        }
                    }

                    // Details on Right
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "${viewModel.wordsLearnedCount} Words Learned",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = NavyPrimary
                        )

                        // Progress Bar + Percentage text
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            LinearProgressIndicator(
                                progress = { wordProgressFraction },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = AccentBlue,
                                trackColor = LightBlue.copy(alpha = 0.5f),
                                strokeCap = StrokeCap.Round
                            )
                            val progressPercentage = (wordProgressFraction * 100).toInt().coerceIn(0, 100)
                            Text(
                                text = "$progressPercentage%",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MutedBlueGrey
                            )
                        }

                        // Next Milestone label
                        Text(
                            text = "Next milestone: $nextWordTarget Words",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = AccentBlue
                        )
                    }
                }
            }
        }

        // Achievements Section - Word Milestones
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Word Milestones",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = NavyPrimary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                wordMilestones.forEach { item ->
                    AchievementCard(
                        item = item,
                        modifier = Modifier.weight(1f)
                    )
                }
                // Fill space if less than 3
                if (wordMilestones.size < 3) {
                    repeat(3 - wordMilestones.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        // Achievements Section - Streak Milestones
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Streak Milestones",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = NavyPrimary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                streakMilestones.forEach { item ->
                    AchievementCard(
                        item = item,
                        modifier = Modifier.weight(1f)
                    )
                }
                // Fill space if less than 3
                if (streakMilestones.size < 3) {
                    repeat(3 - streakMilestones.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        // Motivational Banner at Bottom
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, SoftBlueBorder),
            colors = CardDefaults.cardColors(containerColor = CardBackground),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(LightBlue, CardBackground)
                        )
                    )
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "🎉",
                        fontSize = 28.sp
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Keep it up!",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = NavyPrimary
                        )
                        Text(
                            text = "Consistency is the key to mastering new words.",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = MutedBlueGrey,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun StatCol(
    value: String,
    label: String,
    icon: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        val color = when (icon) {
            "streak" -> StreakAccent
            else -> NavyPrimary
        }
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold,
            color = color,
            textAlign = TextAlign.Center
        )
        Text(
            text = label,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = MutedBlueGrey,
            textAlign = TextAlign.Center,
            lineHeight = 11.sp
        )
    }
}

@Composable
fun DividerLine() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(24.dp)
            .background(SoftBlueBorder)
    )
}
