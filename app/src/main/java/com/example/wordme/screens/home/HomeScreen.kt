package com.example.wordme.screens.home

import android.speech.tts.TextToSpeech
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import com.example.wordme.R
import com.example.wordme.components.FeedbackCard
import com.example.wordme.components.LevelProgressCard
import com.example.wordme.components.ProgressOverview
import com.example.wordme.components.WordCard
import com.example.wordme.components.YourTurnCard
import com.example.wordme.ui.WordMeIcons
import com.example.wordme.ui.WordViewModel
import com.example.wordme.ui.theme.AccentBlue
import com.example.wordme.ui.theme.CardBackground
import com.example.wordme.ui.theme.LightBlue
import com.example.wordme.ui.theme.MutedBlueGrey
import com.example.wordme.ui.theme.NavyPrimary
import com.example.wordme.ui.theme.SoftBlueBorder
import com.example.wordme.ui.theme.StreakAccent
import java.util.Locale

@Composable
fun HomeScreen(
    viewModel: WordViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    // TextToSpeech engine initialization
    var textToSpeech by remember { mutableStateOf<TextToSpeech?>(null) }
    var ttsReady by remember { mutableStateOf(false) }

    DisposableEffect(context) {
        val engine = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech?.language = Locale.US
                ttsReady = true
            }
        }
        textToSpeech = engine
        onDispose {
            engine.stop()
            engine.shutdown()
        }
    }

    // Function to speak word in English with voice gender configuration
    val speakWord = { female: Boolean ->
        if (ttsReady) {
            val engine = textToSpeech
            if (engine != null) {
                val targetVoiceName = if (female) {
                    "en-us-x-tpf-local"
                } else {
                    "en-us-x-tpd-local"
                }
                val selectedVoice = engine.voices?.firstOrNull {
                    it.name == targetVoiceName
                }
                if (selectedVoice != null) {
                    engine.voice = selectedVoice
                }
                engine.setSpeechRate(0.85f)
                engine.setPitch(1.0f)
                engine.speak(
                    viewModel.currentWord.word,
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    if (female) "female_${viewModel.currentWord.id}" else "male_${viewModel.currentWord.id}"
                )
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp) // Gap between major cards is 12dp
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Header Section (Row containing logo and text)
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp
        val isSmallScreen = screenWidth < 360

        val logoSize = if (isSmallScreen) 38.dp else 44.dp
        val horizontalGap = if (isSmallScreen) 8.dp else 12.dp
        val titleSize = if (isSmallScreen) 30.sp else 36.sp
        val subtitleSize = if (isSmallScreen) 13.sp else 15.sp
        val subtitleGap = if (isSmallScreen) 2.dp else 4.dp

        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_logo),
                    contentDescription = "Word Me Logo",
                    modifier = Modifier.size(logoSize)
                )

                Spacer(modifier = Modifier.width(horizontalGap))

                Column(
                    verticalArrangement = Arrangement.spacedBy(subtitleGap)
                ) {
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
                    Text(
                        text = "One word. One sentence. Every day.",
                        fontSize = subtitleSize,
                        fontWeight = FontWeight.Normal,
                        color = MutedBlueGrey
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
        }

        // Progress Overview
        ProgressOverview(
            days = viewModel.dayCount,
            words = viewModel.wordsLearnedCount,
            streak = viewModel.streakCount
        )

        // Level Progress Card
        LevelProgressCard(levelDetails = viewModel.levelDetails)

        // Word Card
        WordCard(
            word = viewModel.currentWord,
            showExampleTranslations = viewModel.showExampleTranslations,
            onToggleExampleTranslations = { viewModel.toggleExampleTranslations() },
            onSpeakFemale = { speakWord(true) },
            onSpeakMale = { speakWord(false) }
        )

        // Your Turn Card
        YourTurnCard(
            word = viewModel.currentWord.word,
            sentenceText = viewModel.sentenceText,
            onSentenceChange = { viewModel.onSentenceTextChange(it) },
            onCheckSentence = { viewModel.checkSentence() },
            isCompleted = viewModel.isChecked
        )

        // Feedback Result panel (shown only after checking)
        AnimatedVisibility(
            visible = viewModel.isChecked,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
            exit = fadeOut()
        ) {
            FeedbackCard(
                word = viewModel.currentWord.word,
                score = viewModel.currentSentenceScore
            )
        }

        // Ready for another word CTA (always visible so users can cycle words at any time)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, SoftBlueBorder),
            colors = CardDefaults.cardColors(containerColor = LightBlue), // Soft pale blue background
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Sparkles icon inside a white circle
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(CardBackground), // White circular background
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = WordMeIcons.Sparkles,
                            contentDescription = null,
                            tint = AccentBlue,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Text(
                        text = "Ready for another word?",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = NavyPrimary
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Word me action button (Always active as users can cycle words at will)
                Button(
                    onClick = { viewModel.nextWord() },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentBlue, // Bright blue button
                        contentColor = CardBackground
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "WORD ME! ✨",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}
