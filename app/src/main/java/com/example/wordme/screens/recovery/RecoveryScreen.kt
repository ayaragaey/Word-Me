package com.example.wordme.screens.recovery

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordme.data.RecoveryQuestions
import com.example.wordme.ui.WordMeIcons
import com.example.wordme.ui.WordViewModel
import com.example.wordme.ui.theme.*

enum class RecoveryStep {
    INTRO,
    CHALLENGE,
    SUCCESS,
    FAILURE
}

@Composable
fun RecoveryScreen(
    viewModel: WordViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MainBackground)
            .verticalScroll(scrollState)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (viewModel.recoveryStep) {
            RecoveryStep.INTRO -> IntroState(viewModel)
            RecoveryStep.CHALLENGE -> ChallengeState(viewModel)
            RecoveryStep.SUCCESS -> SuccessState(viewModel)
            RecoveryStep.FAILURE -> FailureState(viewModel)
        }
    }
}

@Composable
private fun IntroState(viewModel: WordViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        // Flame emblem
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(Color(0xFFFFF2E6)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = WordMeIcons.Fire,
                contentDescription = null,
                tint = StreakAccent,
                modifier = Modifier.size(48.dp)
            )
        }

        // Title
        Text(
            text = "Save Your Streak 🔥",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = NavyPrimary,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily.Serif
        )

        // Subtitle
        Text(
            text = "You missed yesterday — but your streak isn't gone yet.",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = NavyPrimary,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )

        Text(
            text = "Complete a 5-word challenge within 10 minutes to save it.",
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = MutedBlueGrey,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        // Streak Card
        Card(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(vertical = 12.dp),
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
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = WordMeIcons.Fire,
                    contentDescription = null,
                    tint = StreakAccent,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${viewModel.streakCount} Day Streak",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = NavyPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Start Button
        Button(
            onClick = { viewModel.startRecoveryChallenge() },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AccentBlue,
                contentColor = CardBackground
            )
        ) {
            Text(
                text = "START 5-WORD CHALLENGE",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Composable
private fun ChallengeState(viewModel: WordViewModel) {
    val questionIndex = viewModel.currentQuestionIndex
    val totalQuestions = RecoveryQuestions.questions.size
    val currentQuestion = RecoveryQuestions.questions[questionIndex]

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        // Screen Header
        Text(
            text = "Save Your Streak 🔥",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = NavyPrimary,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily.Serif
        )
        Text(
            text = "Complete all 5 words before time runs out.",
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = MutedBlueGrey,
            textAlign = TextAlign.Center
        )

        // Timer and Progress Card
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
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Countdown Timer
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFF2E6)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = WordMeIcons.Calendar, // Standard Calendar (representing time/deadline)
                            contentDescription = "Clock",
                            tint = StreakAccent,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Text(
                        text = viewModel.recoveryTimeLeftFormatted,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = StreakAccent
                    )
                }

                // Word progress text
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Word ${questionIndex + 1} of $totalQuestions",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = NavyPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    // Progress dots indicator
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (i in 0 until totalQuestions) {
                            val filled = i <= questionIndex
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(if (filled) AccentBlue else SoftBlueBorder)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Question Container Card
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Category/Type badge
                val typeLabel = when (currentQuestion.type) {
                    "definition" -> "VOCABULARY MEANING"
                    "translation" -> "ARABIC TRANSLATION"
                    "completion" -> "FILL IN THE BLANK"
                    else -> "QUESTION"
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(LightBlue)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = typeLabel,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentBlue,
                        letterSpacing = 0.5.sp
                    )
                }

                // Question Text
                Text(
                    text = currentQuestion.questionText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = NavyPrimary,
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Options list
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    currentQuestion.options.forEachIndexed { index, option ->
                        val isSelected = viewModel.selectedOptionIndex == index
                        val borderStroke = if (isSelected) {
                            BorderStroke(2.dp, AccentBlue)
                        } else {
                            BorderStroke(1.dp, SoftBlueBorder)
                        }
                        val bgColor = if (isSelected) LightBlue else CardBackground

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(bgColor)
                                .border(borderStroke, RoundedCornerShape(12.dp))
                                .clickable { viewModel.selectOption(index) }
                                .padding(16.dp)
                        ) {
                            Text(
                                text = option,
                                fontSize = 14.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = NavyPrimary,
                                textAlign = if (currentQuestion.type == "translation" && index < currentQuestion.options.size) TextAlign.Right else TextAlign.Left,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Next or Finish Button
        val buttonText = if (questionIndex == totalQuestions - 1) "FINISH CHALLENGE" else "NEXT"
        val isOptionSelected = viewModel.selectedOptionIndex != null

        Button(
            onClick = { viewModel.nextRecoveryQuestion() },
            enabled = isOptionSelected,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AccentBlue,
                contentColor = CardBackground,
                disabledContainerColor = SoftBlueBorder,
                disabledContentColor = MutedBlueGrey
            )
        ) {
            Text(
                text = buttonText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Composable
private fun SuccessState(viewModel: WordViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        // Success checkmark badge
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(PositiveFeedbackBg),
            contentAlignment = Alignment.Center
        ) {
            // A green background checkmark circle
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(PositiveFeedbackText),
                contentAlignment = Alignment.Center
            ) {
                // Standard checkmark - we use text for simple MVP or draw path. Let's use a nice bold text checkmark!
                Text(
                    text = "✓",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = CardBackground
                )
            }
        }

        // Title
        Text(
            text = "Streak Saved! 🔥",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = NavyPrimary,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily.Serif
        )

        // Subtitle
        Text(
            text = "You saved your ${viewModel.streakCount}-day streak.",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = NavyPrimary,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Nice save. Keep it going today.",
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = MutedBlueGrey,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Continue Button
        Button(
            onClick = { viewModel.dismissRecoverySuccess() },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AccentBlue,
                contentColor = CardBackground
            )
        ) {
            Text(
                text = "CONTINUE",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Composable
private fun FailureState(viewModel: WordViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        // Red X badge
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(Color(0xFFFFEBEB)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE53935)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "✕",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = CardBackground
                )
            }
        }

        // Title
        Text(
            text = "Streak Lost",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = NavyPrimary,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily.Serif
        )

        // Subtitle
        Text(
            text = "Your recovery window has ended.",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = NavyPrimary,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Start a new streak today by learning a word.",
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = MutedBlueGrey,
            textAlign = TextAlign.Center
        )

        // Streak 0 box
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(LightBlue)
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Text(
                text = "Current streak: 0",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = AccentBlue
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Start Again Button
        Button(
            onClick = { viewModel.restartStreakAfterFailure() },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AccentBlue,
                contentColor = CardBackground
            )
        ) {
            Text(
                text = "START AGAIN",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }
    }
}
