package com.example.wordme.screens.profile

import android.app.TimePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.wordme.components.LevelProgressCard
import com.example.wordme.navigation.Screen
import com.example.wordme.ui.WordMeIcons
import com.example.wordme.ui.WordViewModel
import com.example.wordme.ui.theme.AccentBlue
import com.example.wordme.ui.theme.CardBackground
import com.example.wordme.ui.theme.LightBlue
import com.example.wordme.ui.theme.MutedBlueGrey
import com.example.wordme.ui.theme.NavyPrimary
import com.example.wordme.ui.theme.SoftBlueBorder

@Composable
fun ProfileScreen(
    viewModel: WordViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    var showResetDialog by remember { mutableStateOf(false) }

    // Dialog for resetting data progress
    if (showResetDialog) {
        ResetConfirmationDialog(
            onDismiss = { showResetDialog = false },
            onConfirm = {
                viewModel.resetProgress()
                showResetDialog = false
            }
        )
    }

    // Calculate total achievements unlocked
    val unlockedMilestonesCount = remember(viewModel.wordsLearnedCount, viewModel.streakCount) {
        val levelMilestones = viewModel.levelDetails.level
        val wordMilestones = viewModel.wordsLearnedCount / 50
        val streakMilestones = when {
            viewModel.streakCount < 3 -> 0
            viewModel.streakCount in 3..6 -> 1
            else -> 2 + (viewModel.streakCount - 7) / 7
        }
        levelMilestones + wordMilestones + streakMilestones
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Header Section
        Column {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color(0xFF0D2A59), fontWeight = FontWeight.Bold)) {
                        append("My ")
                    }
                    withStyle(style = SpanStyle(color = Color(0xFF2784F5), fontWeight = FontWeight.Bold)) {
                        append("Profile")
                    }
                },
                fontSize = 32.sp,
                fontFamily = FontFamily.Serif
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Your Word Me journey",
                fontSize = 15.sp,
                color = MutedBlueGrey
            )
        }

        // Profile Identity Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, SoftBlueBorder),
            colors = CardDefaults.cardColors(containerColor = CardBackground),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = viewModel.userName ?: "Explorer",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = NavyPrimary
                    )
                    Text(
                        text = "Vocabulary ${viewModel.levelDetails.name}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentBlue
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = viewModel.joinedDate,
                        fontSize = 12.sp,
                        color = MutedBlueGrey,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }


        // Level standing progression card
        LevelProgressCard(levelDetails = viewModel.levelDetails)

        // Learning Goals Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, SoftBlueBorder),
            colors = CardDefaults.cardColors(containerColor = CardBackground),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "🎯", fontSize = 18.sp)
                    Text(
                        text = "Learning Goals",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = NavyPrimary
                    )
                }

                var dropdownExpanded by remember { mutableStateOf(false) }
                val possibleGoals = listOf(
                    "Improve daily communication",
                    "Prepare for travel",
                    "Advance career/business",
                    "Pass English exams",
                    "Read books & news",
                    "Watch movies & shows"
                )

                Box(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { dropdownExpanded = true }
                            .border(1.dp, SoftBlueBorder.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                            .background(LightBlue.copy(alpha = 0.3f))
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = "My Goal",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = MutedBlueGrey
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = viewModel.learningGoals.joinToString(", "),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = NavyPrimary,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Expand Goals",
                                tint = MutedBlueGrey,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .background(CardBackground)
                    ) {
                        possibleGoals.forEach { goal ->
                            val isSelected = viewModel.learningGoals.contains(goal)
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Checkbox(
                                            checked = isSelected,
                                            onCheckedChange = { viewModel.toggleLearningGoal(goal) },
                                            colors = CheckboxDefaults.colors(
                                                checkedColor = AccentBlue
                                            )
                                        )
                                        Text(
                                            text = goal,
                                            color = NavyPrimary,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }
                                },
                                onClick = {
                                    viewModel.toggleLearningGoal(goal)
                                }
                            )
                        }
                    }
                }

                HorizontalDivider(color = SoftBlueBorder.copy(alpha = 0.5f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Daily target",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MutedBlueGrey
                        )
                        Text(
                            text = "${viewModel.dailyTarget} word${if (viewModel.dailyTarget > 1) "s" else ""} per day",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = NavyPrimary
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        listOf(1, 2, 3, 5).forEach { target ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (viewModel.dailyTarget == target) AccentBlue else LightBlue)
                                    .clickable { viewModel.updateDailyTarget(target) }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "$target",
                                    color = if (viewModel.dailyTarget == target) Color.White else NavyPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // My Milestones Card
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
                    .clickable { viewModel.selectTab(Screen.MY_MILESTONES) }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(text = "🏆", fontSize = 24.sp)
                    Column {
                        Text(
                            text = "My Milestones",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = NavyPrimary
                        )
                        Text(
                            text = "$unlockedMilestonesCount milestones unlocked",
                            fontSize = 13.sp,
                            color = MutedBlueGrey
                        )
                    }
                }
            }
        }

        // Settings Settings Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, SoftBlueBorder),
            colors = CardDefaults.cardColors(containerColor = CardBackground),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "⚙", fontSize = 18.sp)
                    Text(
                        text = "Settings",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = NavyPrimary
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Notifications",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = NavyPrimary
                        )
                        Text(
                            text = if (viewModel.notificationsEnabled) "Receive daily reminders" else "Daily reminders disabled",
                            fontSize = 12.sp,
                            color = MutedBlueGrey
                        )
                    }
                    Switch(
                        checked = viewModel.notificationsEnabled,
                        onCheckedChange = { viewModel.toggleNotifications(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = AccentBlue,
                            uncheckedThumbColor = MutedBlueGrey,
                            uncheckedTrackColor = LightBlue
                        )
                    )
                }

                HorizontalDivider(color = SoftBlueBorder.copy(alpha = 0.5f))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val calendar = java.util.Calendar.getInstance()
                            val currentHour = calendar.get(java.util.Calendar.HOUR_OF_DAY)
                            val currentMinute = calendar.get(java.util.Calendar.MINUTE)
                            
                            val picker = TimePickerDialog(
                                context,
                                { _, selectedHour, selectedMinute ->
                                    val amPm = if (selectedHour < 12) "AM" else "PM"
                                    val displayHour = when {
                                        selectedHour == 0 -> 12
                                        selectedHour > 12 -> selectedHour - 12
                                        else -> selectedHour
                                    }
                                    val formattedTime = String.format(java.util.Locale.US, "%d:%02d%s", displayHour, selectedMinute, amPm)
                                    viewModel.updateReminderTime(formattedTime)
                                },
                                currentHour,
                                currentMinute,
                                false
                            )
                            picker.show()
                        }
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Reminder time",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = NavyPrimary
                        )
                        Text(
                            text = "Set daily learning reminder time",
                            fontSize = 12.sp,
                            color = MutedBlueGrey
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = viewModel.reminderTime,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = AccentBlue
                        )
                        Icon(
                            imageVector = WordMeIcons.ChevronRight,
                            contentDescription = null,
                            tint = AccentBlue,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        // Reset progress Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showResetDialog = true },
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color(0xFFFCA5A5)),
            colors = CardDefaults.cardColors(containerColor = CardBackground),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(text = "❌", fontSize = 24.sp)
                Column {
                    Text(
                        text = "Reset Progress",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFDC2626)
                    )
                    Text(
                        text = "Delete all learning data",
                        fontSize = 12.sp,
                        color = MutedBlueGrey
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}




@Composable
fun ResetConfirmationDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, Color(0xFFFCA5A5)),
            colors = CardDefaults.cardColors(containerColor = CardBackground),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFEF2F2)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("⚠️", fontSize = 28.sp)
                }

                Text(
                    text = "Reset Progress?",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "This will permanently delete all your learned words, streaks, and progress records. This action cannot be undone.",
                    fontSize = 13.sp,
                    color = MutedBlueGrey,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LightBlue,
                            contentColor = NavyPrimary
                        )
                    ) {
                        Text("CANCEL", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onConfirm,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFDC2626),
                            contentColor = Color.White
                        )
                    ) {
                        Text("RESET PROGRESS", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}
