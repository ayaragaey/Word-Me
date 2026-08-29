package com.example.wordme.screens.profile

import android.app.TimePickerDialog
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.wordme.components.LevelProgressCard
import com.example.wordme.components.NamePromptDialog
import com.example.wordme.data.LearningGoal
import com.example.wordme.navigation.Screen
import com.example.wordme.ui.WordMeIcons
import com.example.wordme.ui.WordViewModel
import com.example.wordme.ui.theme.AccentBlue
import com.example.wordme.ui.theme.CardBackground
import com.example.wordme.ui.theme.LightBlue
import com.example.wordme.ui.theme.MutedBlueGrey
import com.example.wordme.ui.theme.NavyPrimary
import com.example.wordme.ui.theme.SoftBlueBorder
import com.example.wordme.ui.theme.StreakAccent

@Composable
fun ProfileScreen(
    viewModel: WordViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    var showResetDialog by remember { mutableStateOf(false) }
    var showEditNameDialog by remember { mutableStateOf(false) }

    // Intercept back button to return to Home screen
    BackHandler {
        viewModel.selectTab(Screen.HOME)
    }

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

    // Dialog for editing user name
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

    // Calculate total achievements unlocked
    val unlockedMilestonesCount = remember(viewModel.wordsLearnedCount, viewModel.streakCount) {
        val levelMilestones = (viewModel.levelDetails.level - 1).coerceAtLeast(0)
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

        // Back to Home Button
        Row(
            modifier = Modifier
                .clickable { viewModel.selectTab(Screen.HOME) }
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back to Home",
                tint = AccentBlue,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = "Back to Home",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = AccentBlue
            )
        }

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
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, SoftBlueBorder),
            colors = CardDefaults.cardColors(containerColor = CardBackground),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showEditNameDialog = true }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = viewModel.userName ?: "Explorer",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = NavyPrimary
                        )
                        Icon(
                            imageVector = WordMeIcons.Pencil,
                            contentDescription = "Edit Name",
                            tint = MutedBlueGrey,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Text(
                        text = "Vocabulary ${viewModel.levelDetails.name} (Level ${viewModel.levelDetails.level})",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentBlue
                    )
                }
                Text(
                    text = viewModel.joinedDate,
                    fontSize = 11.sp,
                    color = MutedBlueGrey,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Level Progress Card
        LevelProgressCard(levelDetails = viewModel.levelDetails)

        // Learning Goals Card
        var dropdownExpanded by remember { mutableStateOf(false) }
        Box(modifier = Modifier.fillMaxWidth()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { dropdownExpanded = true },
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, SoftBlueBorder),
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(text = "🎯", fontSize = 16.sp)
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Learning Goals",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = NavyPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            viewModel.learningGoals.forEach { goalName ->
                                val goalEntry = LearningGoal.fromDisplayName(goalName)
                                val emoji = goalEntry?.emoji ?: ""
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .border(1.dp, SoftBlueBorder, RoundedCornerShape(6.dp))
                                        .background(LightBlue.copy(alpha = 0.4f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = if (emoji.isNotEmpty()) "$emoji  $goalName" else goalName,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = NavyPrimary
                                    )
                                }
                            }
                        }
                    }
                    Icon(
                        imageVector = WordMeIcons.ChevronRight,
                        contentDescription = null,
                        tint = MutedBlueGrey,
                        modifier = Modifier.size(16.dp)
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
                val possibleGoals = LearningGoal.entries
                possibleGoals.forEach { goal ->
                    val isSelected = viewModel.learningGoals.contains(goal.displayName)
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Checkbox(
                                    checked = isSelected,
                                    onCheckedChange = { checked ->
                                        viewModel.toggleLearningGoal(goal.displayName)
                                    },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = AccentBlue
                                    )
                                )
                                val emoji = goal.emoji
                                Text(
                                    text = if (emoji.isNotEmpty()) "$emoji  ${goal.displayName}" else goal.displayName,
                                    color = if (isSelected) AccentBlue else NavyPrimary,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 14.sp
                                )
                            }
                        },
                        onClick = {
                            viewModel.toggleLearningGoal(goal.displayName)
                        }
                    )
                }
            }
        }

        // Achievements Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { viewModel.selectTab(Screen.MY_MILESTONES) },
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, SoftBlueBorder),
            colors = CardDefaults.cardColors(containerColor = CardBackground),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFF9C4)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = WordMeIcons.Trophy,
                        contentDescription = null,
                        tint = Color(0xFFFBC02D),
                        modifier = Modifier.size(22.dp)
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "My Milestones",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = NavyPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "$unlockedMilestonesCount milestones unlocked",
                        fontSize = 13.sp,
                        color = MutedBlueGrey
                    )
                }
                Icon(
                    imageVector = WordMeIcons.ChevronRight,
                    contentDescription = null,
                    tint = MutedBlueGrey,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Settings Card
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
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(LightBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "⚙️", fontSize = 20.sp)
                    }
                    Text(
                        text = "Settings",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = NavyPrimary
                    )
                }

                HorizontalDivider(color = SoftBlueBorder.copy(alpha = 0.5f))

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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFEF2F2)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🗑️", fontSize = 20.sp)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Reset Progress",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFDC2626)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Delete all learning progress and start fresh",
                        fontSize = 12.sp,
                        color = MutedBlueGrey
                    )
                }
                Icon(
                    imageVector = WordMeIcons.ChevronRight,
                    contentDescription = null,
                    tint = Color(0xFFDC2626),
                    modifier = Modifier.size(20.dp)
                )
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
                    text = buildAnnotatedString {
                        append("Are you sure you want to reset\nyour Word Me journey?\n\nThis will permanently remove:\n\n• Learned words\n• Day streak\n• Milestones\n• Levels\n• Learning progress\n• Sentence history\n\nThis action cannot be undone.")
                    },
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
                        Text("Cancel", fontWeight = FontWeight.Bold)
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
                        Text("Reset", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
