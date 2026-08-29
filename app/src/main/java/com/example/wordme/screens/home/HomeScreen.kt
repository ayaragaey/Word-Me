package com.example.wordme.screens.home

import com.example.wordme.audio.VocabularyAudioManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import com.example.wordme.R
import com.example.wordme.components.FeedbackCard
import com.example.wordme.components.LevelProgressCard
import com.example.wordme.components.NamePromptDialog
import com.example.wordme.components.ProgressOverview
import com.example.wordme.components.WordCard
import com.example.wordme.components.YourTurnCard
import com.example.wordme.ui.WordMeIcons
import com.example.wordme.ui.WordViewModel
import com.example.wordme.navigation.Screen
import com.example.wordme.ui.theme.AccentBlue
import com.example.wordme.ui.theme.CardBackground
import com.example.wordme.ui.theme.LightBlue
import com.example.wordme.ui.theme.MutedBlueGrey
import com.example.wordme.ui.theme.NavyPrimary
import com.example.wordme.ui.theme.SoftBlueBorder
import com.example.wordme.ui.theme.StreakAccent
import java.util.Locale
import com.example.wordme.data.LearningGoal

@Composable
fun HomeScreen(
    viewModel: WordViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    var showEditNameDialog by remember { mutableStateOf(false) }
    var showFeedbackDialog by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel.isChecked) {
        if (viewModel.isChecked) {
            showFeedbackDialog = true
        }
    }

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

    // Vocabulary Audio Manager initialization
    val audioManager = remember(context) { VocabularyAudioManager(context) }
    DisposableEffect(audioManager) {
        onDispose {
            audioManager.shutdown()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp) // Reduced vertical gap between cards to move everything up
    ) {
        Spacer(modifier = Modifier.height(6.dp)) // Shrunk top spacing to bring everything up

        // Header Section (Row containing logo/title on the left, and Hi Name on the right)
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp
        val isSmallScreen = screenWidth < 360

        val logoSize = if (isSmallScreen) 34.dp else 40.dp
        val horizontalGap = if (isSmallScreen) 6.dp else 10.dp
        val titleSize = if (isSmallScreen) 26.sp else 30.sp

        val nameToDisplay = if (viewModel.userName.isNullOrBlank()) "Explorer" else viewModel.userName

        Spacer(modifier = Modifier.height(8.dp))

        // Header Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left part: Logo and Title text in a horizontal row
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_logo),
                    contentDescription = "Word Me Logo",
                    modifier = Modifier.size(logoSize)
                )

                Spacer(modifier = Modifier.width(horizontalGap))

                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color(0xFF0D2A59), fontWeight = FontWeight.Bold)) {
                            append("Word ")
                        }
                        withStyle(style = SpanStyle(color = Color(0xFF2784F5), fontWeight = FontWeight.Bold)) {
                            append("Me!")
                        }
                    },
                    fontSize = titleSize,
                    fontFamily = FontFamily.Serif
                )
            }

            // Right part: Hi (Name)
            Text(
                text = "Hi $nameToDisplay",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = AccentBlue,
                modifier = Modifier.clickable { showEditNameDialog = true }
            )
        }

        // Progress Overview
        ProgressOverview(
            days = viewModel.dayCount,
            words = viewModel.wordsLearnedCount,
            streak = viewModel.streakCount
        )



        if (viewModel.rehearsalWord != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, SoftBlueBorder),
                colors = CardDefaults.cardColors(containerColor = LightBlue),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "🔄",
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Rehearsal Mode: Refreshing memory",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = NavyPrimary
                        )
                    }
                    Text(
                        text = "Exit",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentBlue,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .clickable { viewModel.exitRehearsal() }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Word Card
        WordCard(
            word = viewModel.currentWord,
            showExampleTranslations = viewModel.showExampleTranslations,
            onToggleExampleTranslations = { viewModel.toggleExampleTranslations() },
            onSpeakFemale = { wordToSpeak -> audioManager.playWord(wordToSpeak, female = true) },
            onSpeakMale = { wordToSpeak -> audioManager.playWord(wordToSpeak, female = false) },
            onMoreSentencesClick = { viewModel.generateMoreSentences() },
            onAnotherWordClick = { viewModel.nextWord() },
            learningGoal = viewModel.currentWord.goalTags.firstOrNull { tag ->
                viewModel.learningGoals.any { goalName ->
                    val goalEntry = LearningGoal.fromDisplayName(goalName)
                    tag.equals(goalEntry?.displayName, ignoreCase = true) || tag.equals(goalEntry?.category, ignoreCase = true)
                }
            }
                ?: viewModel.currentWord.goalTags.firstOrNull()
                ?: viewModel.learningGoals.firstOrNull()
                ?: ""
        )

        // Your Turn Card
        YourTurnCard(
            word = viewModel.currentWord.word,
            sentenceText = viewModel.sentenceText,
            onSentenceChange = { viewModel.onSentenceTextChange(it) },
            onCheckSentence = { viewModel.checkSentence() },
            isCompleted = viewModel.isChecked
        )

        // Feedback Result popup dialog (shown only after checking)
        if (showFeedbackDialog) {
            Dialog(onDismissRequest = { showFeedbackDialog = false }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, SoftBlueBorder),
                    colors = CardDefaults.cardColors(containerColor = CardBackground),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        FeedbackCard(
                            word = viewModel.currentWord.word,
                            score = viewModel.currentSentenceScore
                        )

                        Text(
                            text = if (viewModel.rehearsalWord != null) "Exit rehearsal and learn a new word?" else "Ready for another word?",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = NavyPrimary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Later button (closes dialog)
                            Button(
                                onClick = { showFeedbackDialog = false },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = LightBlue,
                                    contentColor = AccentBlue
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(40.dp)
                            ) {
                                Text(
                                    text = "Later",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            // Word Me! button (closes dialog and triggers nextWord)
                            Button(
                                onClick = {
                                    showFeedbackDialog = false
                                    if (viewModel.rehearsalWord != null) {
                                        viewModel.exitRehearsal()
                                    } else {
                                        viewModel.nextWord()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AccentBlue,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(40.dp)
                            ) {
                                Text(
                                    text = "Word Me! ✨",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
