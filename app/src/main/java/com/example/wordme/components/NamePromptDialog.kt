package com.example.wordme.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import com.example.wordme.data.LearningGoal
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.wordme.ui.WordMeIcons
import com.example.wordme.ui.theme.AccentBlue
import com.example.wordme.ui.theme.CardBackground
import com.example.wordme.ui.theme.LightBlue
import com.example.wordme.ui.theme.MutedBlueGrey
import com.example.wordme.ui.theme.NavyPrimary
import com.example.wordme.ui.theme.SoftBlueBorder
import androidx.compose.ui.res.painterResource
import com.example.wordme.R
import androidx.compose.ui.graphics.Color

@Composable
fun NamePromptDialog(
    modifier: Modifier = Modifier,
    onOnboardingCompleted: ((String, Set<String>) -> Unit)? = null,
    onNameSubmitted: ((String) -> Unit)? = null,
    title: String = "Welcome to Word Me! 👋",
    subtitle: String = "Let's personalize your learning journey.",
    initialName: String = "",
    buttonText: String = "Let's Start! ✨",
    isDismissible: Boolean = false,
    onDismiss: () -> Unit = {}
) {
    val isOnboarding = onOnboardingCompleted != null && !isDismissible
    var nameText by remember(initialName) { mutableStateOf(initialName) }
    var selectedGoals by remember { mutableStateOf(emptySet<String>()) }
    var dropdownExpanded by remember { mutableStateOf(false) }

    // Validation errors
    var nameError by remember { mutableStateOf<String?>(null) }
    var goalError by remember { mutableStateOf<String?>(null) }

    val possibleGoals = LearningGoal.entries

    fun getGoalEmoji(goal: String): String {
        return LearningGoal.fromDisplayName(goal)?.emoji ?: ""
    }

    Dialog(
        onDismissRequest = {
            if (isDismissible) onDismiss()
        },
        properties = DialogProperties(
            dismissOnBackPress = isDismissible,
            dismissOnClickOutside = isDismissible
        )
    ) {
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
                // Illustration (Book logo)
                Image(
                    painter = painterResource(id = R.drawable.ic_logo),
                    contentDescription = "Word Me Logo",
                    modifier = Modifier
                        .size(100.dp)
                        .padding(bottom = 4.dp)
                )

                // Title
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = NavyPrimary,
                    textAlign = TextAlign.Center
                )

                // Subtitle / Description
                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    color = MutedBlueGrey,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp
                )

                // Name Input Label
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "What should we call you?",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = NavyPrimary
                    )
                    OutlinedTextField(
                        value = nameText,
                        onValueChange = {
                            nameText = it
                            nameError = null
                        },
                        placeholder = {
                            Text(
                                text = "Enter your name",
                                color = MutedBlueGrey.copy(alpha = 0.5f),
                                fontSize = 14.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = MutedBlueGrey
                            )
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AccentBlue,
                            unfocusedBorderColor = if (nameError != null) Color(0xFFDC2626) else SoftBlueBorder,
                            focusedContainerColor = CardBackground,
                            unfocusedContainerColor = CardBackground,
                            focusedTextColor = NavyPrimary,
                            unfocusedTextColor = NavyPrimary
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = if (isOnboarding) ImeAction.Next else ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            if (!isOnboarding) {
                                val trimmed = nameText.trim()
                                if (trimmed.isNotEmpty()) {
                                    onNameSubmitted?.invoke(trimmed)
                                } else {
                                    nameError = "Please enter your name."
                                }
                            }
                        })
                    )
                    nameError?.let {
                        Text(
                            text = it,
                            color = Color(0xFFDC2626),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }

                // Goal Input Label (Only in Onboarding Mode)
                if (isOnboarding) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "What is your learning goal?",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = NavyPrimary
                        )

                        Box(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(CardBackground)
                                    .border(
                                        1.dp,
                                        if (goalError != null) Color(0xFFDC2626) else SoftBlueBorder,
                                        RoundedCornerShape(16.dp)
                                    )
                                    .clickable { dropdownExpanded = true }
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val displayText = if (selectedGoals.isEmpty()) "Select your goal" else {
                                    selectedGoals.joinToString(", ") { goal ->
                                        val emoji = getGoalEmoji(goal)
                                        if (emoji.isNotEmpty()) "$emoji  $goal" else goal
                                    }
                                }
                                Text(
                                    text = displayText,
                                    color = if (selectedGoals.isEmpty()) MutedBlueGrey.copy(alpha = 0.5f) else NavyPrimary,
                                    fontSize = 14.sp,
                                    maxLines = 1
                                )
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = null,
                                    tint = MutedBlueGrey
                                )
                            }

                            DropdownMenu(
                                expanded = dropdownExpanded,
                                onDismissRequest = { dropdownExpanded = false },
                                modifier = Modifier
                                    .fillMaxWidth(0.9f)
                                    .background(CardBackground)
                            ) {
                                possibleGoals.forEach { goal ->
                                    val isSelected = selectedGoals.contains(goal.displayName)
                                    DropdownMenuItem(
                                        text = {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                Checkbox(
                                                    checked = isSelected,
                                                    onCheckedChange = { checked ->
                                                        selectedGoals = if (checked) {
                                                            selectedGoals + goal.displayName
                                                        } else {
                                                            selectedGoals - goal.displayName
                                                        }
                                                        goalError = null
                                                    },
                                                    colors = CheckboxDefaults.colors(
                                                        checkedColor = AccentBlue
                                                    )
                                                )
                                                val emoji = goal.emoji
                                                Text(
                                                    text = if (emoji.isNotEmpty()) "$emoji  ${goal.displayName}" else goal.displayName,
                                                    color = NavyPrimary,
                                                    fontSize = 14.sp
                                                )
                                            }
                                        },
                                        onClick = {
                                            val isChecked = selectedGoals.contains(goal.displayName)
                                            selectedGoals = if (isChecked) {
                                                selectedGoals - goal.displayName
                                            } else {
                                                selectedGoals + goal.displayName
                                            }
                                            goalError = null
                                        }
                                    )
                                }
                            }
                        }
                        goalError?.let {
                            Text(
                                text = it,
                                color = Color(0xFFDC2626),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Submit Buttons
                if (isOnboarding) {
                    Button(
                        onClick = {
                            val trimmedName = nameText.trim()
                            nameError = if (trimmedName.isEmpty()) "Please enter your name." else null
                            goalError = if (selectedGoals.isEmpty()) "Please select at least one learning goal." else null

                            if (nameError == null && goalError == null) {
                                onOnboardingCompleted?.invoke(trimmedName, selectedGoals)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AccentBlue,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = buttonText,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Cancel
                        Button(
                            onClick = onDismiss,
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = LightBlue,
                                contentColor = AccentBlue
                            )
                        ) {
                            Text(
                                text = "CANCEL",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                              )
                          }

                          // Save
                          Button(
                              onClick = {
                                  val trimmed = nameText.trim()
                                  if (trimmed.isNotEmpty()) {
                                      onNameSubmitted?.invoke(trimmed)
                                  } else {
                                      nameError = "Please enter your name."
                                  }
                              },
                              modifier = Modifier
                                  .weight(1f)
                                  .height(48.dp),
                              shape = RoundedCornerShape(12.dp),
                              colors = ButtonDefaults.buttonColors(
                                  containerColor = AccentBlue,
                                  contentColor = Color.White
                              )
                          ) {
                              Text(
                                  text = "SAVE",
                                  fontSize = 13.sp,
                                  fontWeight = FontWeight.Bold
                              )
                          }
                      }
                  }
              }
          }
      }
}
