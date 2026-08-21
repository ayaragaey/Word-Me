package com.example.wordme.ui
import android.util.Log
import android.speech.tts.TextToSpeech
import java.util.Locale
import androidx.compose.ui.platform.LocalContext
import com.example.wordme.data.Word
import com.example.wordme.data.WordRepository
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordme.ui.theme.WordMeTheme

// Theme color shortcuts (Updated to exact requested hex codes)
private val NavyPrimary = Color(0xFF0D2A59)      // #0D2A59 Primary dark text/headings
private val AccentBlue = Color(0xFF2784F5)
private val FemalePink = Color(0xFFE75480)// #2784F5 Primary accent blue
private val SoftSkyBlue = Color(0xFFEAF4FF)      // #EAF4FF Light blue/highlight background
private val PaleBlueBg = Color(0xFFF6FAFF)       // #F6FAFF Background
private val PureWhite = Color(0xFFFFFFFF)        // #FFFFFF Cards
private val PaleBlueBorder = Color(0xFFD8E9FC)   // #D8E9FC Border blue
private val MutedBlueGrey = Color(0xFF718096)    // #718096 Muted text
private val OrangeStreak = Color(0xFFF97316)     // Orange for streak icon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordLearningScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val words = remember {
        WordRepository.loadWords(context).shuffled()
    }
    // Tab Navigation State
    var selectedTab by rememberSaveable { mutableStateOf("home") }

    // App Data State
    var currentWordIndex by rememberSaveable { mutableStateOf(0) }
    var sentenceText by rememberSaveable { mutableStateOf("") }
    var isChecked by rememberSaveable { mutableStateOf(false) }

    // Default MVP Metrics (Updated to match mock specifications)
    var dayCount by rememberSaveable { mutableStateOf(12) }
    var wordsLearnedCount by rememberSaveable { mutableStateOf(27) }
    var sentencesWrittenCount by rememberSaveable { mutableStateOf(27) }
    var streakCount by rememberSaveable { mutableStateOf(5) }

    // Vocabulary notebook initialized with required mock words
    var learnedWords by rememberSaveable {
        mutableStateOf(listOf("Hesitant", "Reliable", "Overwhelmed", "Remarkable", "Awkward"))
    }

    val currentWord = words[currentWordIndex]
    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigationBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PaleBlueBg)
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                "home" -> HomeScreen(
                    word = currentWord,
                    dayIndex = dayCount,
                    wordsCount = wordsLearnedCount,
                    streakCount = streakCount,
                    sentenceText = sentenceText,
                    onSentenceChange = {
                        sentenceText = it
                        if (isChecked) isChecked = false
                    },
                    isChecked = isChecked,
                    onCheckSentence = {
                        if (sentenceText.isNotBlank()) {
                            isChecked = true
                            sentencesWrittenCount++
                            val formattedName = currentWord.word.lowercase().replaceFirstChar { it.uppercase() }
                            if (!learnedWords.contains(formattedName)) {
                                learnedWords = learnedWords + formattedName
                            }
                        }
                    },
                    onWordMe = {
                        // Cycle mock vocabulary words
                        currentWordIndex = (currentWordIndex + 1) % words.size
                        // Increment Words counter by 1 using local Compose state
                        wordsLearnedCount++
                        sentenceText = ""
                        isChecked = false
                    }
                )
                "words" -> MyWordsScreen(
                    learnedWords = learnedWords
                )
                "milestones" -> MyMilestonesScreen(
                    learnedWordsCount = wordsLearnedCount,
                    daysActiveCount = dayCount,
                    sentencesWrittenCount = sentencesWrittenCount,
                    streakCount = streakCount
                )
            }
        }
    }
}

// Helper to highlight a specific word in examples using AccentBlue
@Composable
fun highlightWord(sentence: String, target: String): AnnotatedString {
    return remember(sentence, target) {
        buildAnnotatedString {
            val lowercaseSentence = sentence.lowercase()
            val lowercaseTarget = target.lowercase()
            var startIndex = 0
            while (true) {
                val index = lowercaseSentence.indexOf(lowercaseTarget, startIndex)
                if (index == -1) {
                    append(sentence.substring(startIndex))
                    break
                }
                append(sentence.substring(startIndex, index))
                withStyle(
                    SpanStyle(
                        color = AccentBlue,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append(sentence.substring(index, index + target.length))
                }
                startIndex = index + target.length
            }
        }
    }
}

// Composable for the Progress Overview (3 counters in a single horizontal container)
@Composable
fun ProgressOverview(
    days: Int,
    words: Int,
    streak: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, PaleBlueBorder),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Days Counter
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = WordMeIcons.Calendar,
                    contentDescription = "Calendar Icon",
                    tint = AccentBlue,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "$days",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = NavyPrimary
                )
                Text(
                    text = "Days",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MutedBlueGrey
                )
            }

            // Separator 1
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(44.dp)
                    .background(PaleBlueBorder)
            )

            // Words Counter
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = WordMeIcons.Book,
                    contentDescription = "Book Icon",
                    tint = AccentBlue,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "$words",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = NavyPrimary
                )
                Text(
                    text = "Words",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MutedBlueGrey
                )
            }

            // Separator 2
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(44.dp)
                    .background(PaleBlueBorder)
            )

            // Streak Counter
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = WordMeIcons.Fire,
                    contentDescription = "Streak Icon",
                    tint = OrangeStreak,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "$streak",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = NavyPrimary
                )
                Text(
                    text = "Day Streak",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MutedBlueGrey
                )
            }
        }
    }
}

// 1. Home Tab Screen
@Composable
fun HomeScreen(
    word: Word,
    dayIndex: Int,
    wordsCount: Int,
    streakCount: Int,
    sentenceText: String,
    onSentenceChange: (String) -> Unit,
    isChecked: Boolean,
    onCheckSentence: () -> Unit,
    onWordMe: () -> Unit
) {
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    var textToSpeech by remember {
        mutableStateOf<TextToSpeech?>(null)
    }

    var ttsReady by remember {
        mutableStateOf(false)
    }

    DisposableEffect(context) {

        lateinit var engine: TextToSpeech

        engine = TextToSpeech(context) { status ->

            if (status == TextToSpeech.SUCCESS) {

                engine.language = Locale.US
                engine.setSpeechRate(0.85f)

                engine.voices
                    ?.filter { it.locale.language == "en" }
                    ?.forEach { voice ->
                        Log.d(
                            "WORDME_TTS",
                            "NAME=${voice.name} | LOCALE=${voice.locale} | " +
                                    "QUALITY=${voice.quality} | " +
                                    "NETWORK=${voice.isNetworkConnectionRequired}"
                        )
                    }

                ttsReady = true
            }
        }
        fun speakWord(female: Boolean) {
            val engine = textToSpeech ?: return

            val targetVoiceName = if (female) {
                "en-us-x-tpf-network"
            } else {
                "en-us-x-iol-local"
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
                word.word,
                TextToSpeech.QUEUE_FLUSH,
                null,
                if (female) {
                    "female_${word.id}"
                } else {
                    "male_${word.id}"
                }
            )
        }
        textToSpeech = engine

        onDispose {
            engine.stop()
            engine.shutdown()
            textToSpeech = null
        }
    }
    fun speakWord(female: Boolean) {
        val engine = textToSpeech ?: return

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

            Log.d(
                "WORDME_SELECTED_VOICE",
                "Using voice: ${selectedVoice.name}"
            )
        }

        engine.setSpeechRate(0.85f)

        // Keep natural pitch.
        // We do NOT want to fake gender using pitch.
        engine.setPitch(1.0f)

        engine.speak(
            word.word,
            TextToSpeech.QUEUE_FLUSH,
            null,
            if (female) {
                "female_${word.id}"
            } else {
                "male_${word.id}"
            }
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Header Area
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Word Me",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 32.sp,
                    color = NavyPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "One word. One sentence. Every day.",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MutedBlueGrey
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Progress Overview Row
        ProgressOverview(
            days = dayIndex,
            words = wordsCount,
            streak = streakCount
        )

        Spacer(modifier = Modifier.height(20.dp))

        // MAIN WORD CARD
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, PaleBlueBorder),
            colors = CardDefaults.cardColors(containerColor = PureWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                // Word Card Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // WORD OF THE DAY Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(SoftSkyBlue.copy(alpha = 0.6f))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = WordMeIcons.Ribbon,
                                contentDescription = "Ribbon Icon",
                                tint = AccentBlue,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "WORD OF THE DAY",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = AccentBlue,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }

                    // Speaker Circle Button
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(FemalePink.copy(alpha = 0.12f))
                                .clickable(enabled = ttsReady) {
                                    speakWord(female = true)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = WordMeIcons.Speaker,
                                contentDescription = "Female pronunciation",
                                tint = FemalePink,
                                modifier = Modifier.size(17.dp)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(AccentBlue.copy(alpha = 0.12f))
                                .clickable(enabled = ttsReady) {
                                    speakWord(female = false)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = WordMeIcons.Speaker,
                                contentDescription = "Male pronunciation",
                                tint = AccentBlue,
                                modifier = Modifier.size(17.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Word Header & Translation Section
                Text(
                    text = word.word.uppercase(),
                    fontSize = 38.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.SansSerif,
                    color = NavyPrimary,
                    letterSpacing = 1.sp
                )
                
                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = word.translation,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentBlue
                )

                Text(
                    text = word.pronunciation,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    color = MutedBlueGrey
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Word Type & Definition
                Text(
                    text = word.type,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    color = AccentBlue
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = word.definition,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 22.sp,
                    color = NavyPrimary
                )

                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(color = PaleBlueBorder, thickness = 1.dp)
                Spacer(modifier = Modifier.height(16.dp))

                // Examples Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = WordMeIcons.SpeechBubble,
                        contentDescription = "Speech Bubble Icon",
                        tint = AccentBlue,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "IN A SENTENCE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentBlue,
                        letterSpacing = 0.5.sp
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Examples List
                word.examples.forEachIndexed { index, sentence ->
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Circular Badge Number
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(SoftSkyBlue.copy(alpha = 0.6f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "%02d".format(index + 1),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AccentBlue
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            // Highlighted Sentence text
                            Text(
                                text = highlightWord(sentence = sentence, target = word.word),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Normal,
                                color = NavyPrimary,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        if (index < word.examples.size - 1) {
                            HorizontalDivider(color = PaleBlueBorder.copy(alpha = 0.4f), thickness = 0.8.dp)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // YOUR TURN CARD
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, PaleBlueBorder),
            colors = CardDefaults.cardColors(containerColor = PureWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = WordMeIcons.Pencil,
                        contentDescription = "Pencil Icon",
                        tint = AccentBlue,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "YOUR TURN",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentBlue,
                        letterSpacing = 0.5.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = buildAnnotatedString {
                        append("Write your own sentence using ")
                        withStyle(SpanStyle(color = AccentBlue, fontWeight = FontWeight.Bold)) {
                            append(word.word.lowercase())
                        }
                        append(".")
                    },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = NavyPrimary
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Input Field
                OutlinedTextField(
                    value = sentenceText,
                    onValueChange = onSentenceChange,
                    placeholder = {
                        Text(
                            text = "Write your sentence here...",
                            color = MutedBlueGrey.copy(alpha = 0.6f),
                            fontSize = 15.sp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentBlue,
                        unfocusedBorderColor = PaleBlueBorder,
                        focusedContainerColor = PureWhite,
                        unfocusedContainerColor = PureWhite,
                        focusedTextColor = NavyPrimary,
                        unfocusedTextColor = NavyPrimary
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Check Button
                Button(
                    onClick = {
                        focusManager.clearFocus()
                        onCheckSentence()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentBlue,
                        contentColor = PureWhite
                    ),
                    enabled = sentenceText.isNotBlank()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "CHECK MY SENTENCE",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "→",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // FEEDBACK RESULT PANEL (9/10 Score)
        AnimatedVisibility(
            visible = isChecked,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
            exit = fadeOut()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, PaleBlueBorder),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Custom Circle Progress Arc
                        Box(
                            modifier = Modifier.size(72.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                drawCircle(
                                    color = SoftSkyBlue.copy(alpha = 0.6f),
                                    style = Stroke(width = 6.dp.toPx())
                                )
                                drawArc(
                                    color = AccentBlue,
                                    startAngle = -90f,
                                    sweepAngle = 324f, // 9/10 = 90% progress
                                    useCenter = false,
                                    style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
                                )
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "9",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NavyPrimary
                                )
                                Text(
                                    text = "/10",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MutedBlueGrey
                                )
                            }
                        }

                        // Texts
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Great job! 🎉",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = NavyPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Your sentence is grammatically correct and you used '${word.word.lowercase()}' naturally.",
                                fontSize = 14.sp,
                                color = MutedBlueGrey,
                                lineHeight = 18.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 10 dots indicator
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (i in 1..10) {
                            val filled = i <= 9
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(if (filled) AccentBlue else Color.Transparent)
                                    .border(
                                        width = 1.dp,
                                        color = AccentBlue,
                                        shape = CircleShape
                                    )
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "9/10",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MutedBlueGrey
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Tip container card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(SoftSkyBlue.copy(alpha = 0.5f))
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = WordMeIcons.Lightbulb,
                                contentDescription = "Lightbulb Icon",
                                tint = AccentBlue,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Try to make your sentence as natural as possible.",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = NavyPrimary
                            )
                        }
                    }
                }
            }
        }

        // WORD ME! AGAIN CTA CARD
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, PaleBlueBorder),
            colors = CardDefaults.cardColors(containerColor = PureWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Sparkles badge
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(SoftSkyBlue.copy(alpha = 0.6f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = WordMeIcons.Sparkles,
                            contentDescription = "Sparkles Icon",
                            tint = AccentBlue,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Texts
                    Column {
                        Text(
                            text = "Ready for another word?",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = NavyPrimary
                        )
                        Spacer(modifier = Modifier.height(1.dp))
                        Text(
                            text = "There's always another word to learn.",
                            fontSize = 12.sp,
                            color = MutedBlueGrey
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // WORD ME! CTA Button
                Button(
                    onClick = onWordMe,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentBlue,
                        contentColor = PureWhite
                    ),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = "WORD ME! ✨",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// 2. My Words Tab Screen (Static and dynamic list of words learned)
@Composable
fun MyWordsScreen(learnedWords: List<String>) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "My Words",
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 32.sp,
            color = NavyPrimary
        )
        Text(
            text = "Keep track of all the words you have learned.",
            fontSize = 14.sp,
            color = MutedBlueGrey
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (learnedWords.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No words learned yet. Write a sentence on Home to start!",
                    fontSize = 14.sp,
                    color = MutedBlueGrey,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            learnedWords.forEachIndexed { index, wordText ->
                val matchedWord = mockWordsList.firstOrNull { it.word.lowercase() == wordText.lowercase() }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, PaleBlueBorder),
                    colors = CardDefaults.cardColors(containerColor = PureWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = wordText.uppercase(),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NavyPrimary
                                )
                                if (matchedWord != null) {
                                    Text(
                                        text = matchedWord.type,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontStyle = FontStyle.Italic,
                                        color = AccentBlue
                                    )
                                }
                            }
                            if (matchedWord != null) {
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${matchedWord.translation} • ${matchedWord.pronunciation}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = AccentBlue
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = matchedWord.definition,
                                    fontSize = 13.sp,
                                    color = MutedBlueGrey,
                                    lineHeight = 16.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(SoftSkyBlue)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "#%02d".format(index + 1),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AccentBlue
                                )
                            }
                            Text(
                                text = "Learned",
                                fontSize = 11.sp,
                                color = MutedBlueGrey
                            )
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

// 3. My Milestones Tab Screen (Progress counters + Achievements locked & unlocked)

enum class AchievementStatus {
    UNLOCKED,
    IN_PROGRESS,
    UP_NEXT
}

data class DynamicAchievement(
    val id: String,
    val title: String,
    val description: String,
    val targetValue: Int,
    val currentValue: Int,
    val category: String,
    val status: AchievementStatus
)

fun getDisplayAchievements(wordsCount: Int, streakCount: Int, showAll: Boolean = false): List<DynamicAchievement> {
    val unlockedWords = mutableListOf<DynamicAchievement>()
    if (wordsCount >= 1) {
        unlockedWords.add(DynamicAchievement("w_001_first", "First Word", "Learn your first word.", 1, 1, "words", AchievementStatus.UNLOCKED))
    }
    if (wordsCount >= 10) {
        unlockedWords.add(DynamicAchievement("w_010", "10 Words Learned", "Learn 10 words.", 10, 10, "words", AchievementStatus.UNLOCKED))
    }
    if (wordsCount >= 25) {
        unlockedWords.add(DynamicAchievement("w_025", "25 Words Learned", "Learn 25 words.", 25, 25, "words", AchievementStatus.UNLOCKED))
    }
    
    // Multiples of 50 <= wordsCount
    var w = 50
    while (w <= wordsCount && w <= 500000) {
        unlockedWords.add(DynamicAchievement("w_$w", "$w Words Learned", "Learn $w words.", w, w, "words", AchievementStatus.UNLOCKED))
        w += 50
    }
    
    // In-progress word milestone:
    val nextWordVal = when {
        wordsCount < 1 -> 1
        wordsCount < 10 -> 10
        wordsCount < 25 -> 25
        else -> if (wordsCount % 50 == 0) wordsCount + 50 else ((wordsCount / 50) + 1) * 50
    }
    val inProgressWord = if (nextWordVal <= 500000) {
        val title = when (nextWordVal) {
            1 -> "First Word"
            else -> "$nextWordVal Words Learned"
        }
        val desc = when (nextWordVal) {
            1 -> "Learn your first word."
            else -> "Learn $nextWordVal words."
        }
        DynamicAchievement("w_$nextWordVal", title, desc, nextWordVal, wordsCount, "words", AchievementStatus.IN_PROGRESS)
    } else null
    
    // Up next word milestone:
    val upNextWordVal = when (nextWordVal) {
        1 -> 10
        10 -> 25
        25 -> 50
        else -> nextWordVal + 50
    }
    val upNextWord = if (upNextWordVal <= 500000) {
        DynamicAchievement("w_$upNextWordVal", "$upNextWordVal Words Learned", "Learn $upNextWordVal words.", upNextWordVal, wordsCount, "words", AchievementStatus.UP_NEXT)
    } else null

    // --- STREAK ACHIEVEMENTS ---
    val unlockedStreaks = mutableListOf<DynamicAchievement>()
    if (streakCount >= 3) {
        unlockedStreaks.add(DynamicAchievement("s_003", "3-Day Streak", "Maintain a 3-day learning streak.", 3, 3, "streak", AchievementStatus.UNLOCKED))
    }
    
    // Multiples of 7 <= streakCount
    var s = 7
    while (s <= streakCount) {
        unlockedStreaks.add(DynamicAchievement("s_$s", "$s-Day Streak", "Maintain a $s-day learning streak.", s, s, "streak", AchievementStatus.UNLOCKED))
        s += 7
    }
    
    // In-progress streak milestone:
    val nextStreakVal = when {
        streakCount < 3 -> 3
        streakCount < 7 -> 7
        else -> if (streakCount % 7 == 0) streakCount + 7 else ((streakCount / 7) + 1) * 7
    }
    val inProgressStreak = DynamicAchievement(
        "s_$nextStreakVal", 
        "$nextStreakVal-Day Streak", 
        "Maintain a $nextStreakVal-day streak.", 
        nextStreakVal, 
        streakCount, 
        "streak", 
        AchievementStatus.IN_PROGRESS
    )
    
    // Up next streak milestone:
    val upNextStreakVal = when (nextStreakVal) {
        3 -> 7
        else -> nextStreakVal + 7
    }
    val upNextStreak = DynamicAchievement(
        "s_$upNextStreakVal", 
        "$upNextStreakVal-Day Streak", 
        "Maintain a $upNextStreakVal-day streak.", 
        upNextStreakVal, 
        streakCount, 
        "streak", 
        AchievementStatus.UP_NEXT
    )

    val displayList = mutableListOf<DynamicAchievement>()
    
    // Unlocked achievements:
    val allUnlocked = unlockedWords + unlockedStreaks
    val recentlyUnlocked = if (allUnlocked.size > 4 && !showAll) {
        // Take last 2 word achievements and last 2 streak achievements
        val lastWords = unlockedWords.takeLast(2)
        val lastStreaks = unlockedStreaks.takeLast(2)
        lastWords + lastStreaks
    } else {
        allUnlocked
    }
    
    displayList.addAll(recentlyUnlocked)
    if (inProgressWord != null) displayList.add(inProgressWord)
    displayList.add(inProgressStreak)
    if (upNextWord != null) displayList.add(upNextWord)
    displayList.add(upNextStreak)
    
    return displayList
}

@Composable
fun CircularProgressIndicatorWithText(
    current: Int,
    target: Int,
    fraction: Float,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(72.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = SoftSkyBlue.copy(alpha = 0.6f),
                style = Stroke(width = 6.dp.toPx())
            )
            drawArc(
                color = AccentBlue,
                startAngle = -90f,
                sweepAngle = fraction * 360f,
                useCenter = false,
                style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$current",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = NavyPrimary
            )
            Text(
                text = "/$target",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = MutedBlueGrey
            )
        }
    }
}

@Composable
fun RowScope.StatItem(
    icon: ImageVector,
    value: String,
    label: String,
    iconBgColor: Color,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(iconBgColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
        }
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold,
            color = NavyPrimary,
            textAlign = TextAlign.Center
        )
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = MutedBlueGrey,
            textAlign = TextAlign.Center,
            lineHeight = 12.sp
        )
    }
}

@Composable
fun AchievementCard(
    item: DynamicAchievement,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(160.dp),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, PaleBlueBorder),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Row: Badge/Icon (left) and Status (right)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Badge circle
                val badgeBgColor = when {
                    item.status != AchievementStatus.UNLOCKED -> PaleBlueBg
                    item.title == "First Word" -> SoftSkyBlue.copy(alpha = 0.8f)
                    item.title.contains("10 Words") -> Color(0xFFE8F5E9) // soft green
                    item.title.contains("25 Words") -> Color(0xFFFFF3E0) // soft orange
                    item.category == "streak" -> Color(0xFFFFF3E0) // soft orange
                    else -> SoftSkyBlue.copy(alpha = 0.8f) // soft blue
                }
                
                val iconTint = when {
                    item.status != AchievementStatus.UNLOCKED -> MutedBlueGrey
                    item.title == "First Word" -> AccentBlue
                    item.title.contains("10 Words") -> Color(0xFF4CAF50)
                    item.title.contains("25 Words") -> Color(0xFFF97316)
                    item.category == "streak" -> OrangeStreak
                    else -> AccentBlue
                }
                
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(badgeBgColor),
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        item.title.contains("10 Words") && item.status == AchievementStatus.UNLOCKED -> {
                            Text(
                                text = "10",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = iconTint
                            )
                        }
                        item.title.contains("25 Words") && item.status == AchievementStatus.UNLOCKED -> {
                            Text(
                                text = "25",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = iconTint
                            )
                        }
                        item.title == "First Word" -> {
                            Icon(
                                imageVector = WordMeIcons.Star,
                                contentDescription = null,
                                tint = iconTint,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        item.category == "streak" -> {
                            Icon(
                                imageVector = WordMeIcons.Fire,
                                contentDescription = null,
                                tint = iconTint,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        else -> {
                            Icon(
                                imageVector = WordMeIcons.Trophy,
                                contentDescription = null,
                                tint = iconTint,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
                
                // Status Icon
                if (item.status == AchievementStatus.UNLOCKED) {
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .clip(CircleShape)
                            .background(SoftSkyBlue)
                            .border(1.dp, PaleBlueBorder, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Unlocked",
                            tint = AccentBlue,
                            modifier = Modifier.size(10.dp)
                        )
                    }
                } else {
                    Icon(
                        imageVector = WordMeIcons.Lock,
                        contentDescription = "Locked",
                        tint = MutedBlueGrey.copy(alpha = 0.5f),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            
            // Middle section: Title & Description
            Column {
                Text(
                    text = item.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (item.status == AchievementStatus.UNLOCKED) NavyPrimary else NavyPrimary.copy(alpha = 0.6f)
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
            
            // Bottom status label/progress
            if (item.status == AchievementStatus.UNLOCKED) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(SoftSkyBlue.copy(alpha = 0.5f))
                        .border(0.5.dp, PaleBlueBorder.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "Unlocked",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentBlue
                    )
                }
            } else if (item.status == AchievementStatus.IN_PROGRESS) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(PaleBlueBg)
                        .border(0.5.dp, PaleBlueBorder.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "${item.currentValue} / ${item.targetValue}",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = NavyPrimary
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(PaleBlueBg)
                        .border(0.5.dp, PaleBlueBorder.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "Up next",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Medium,
                        color = MutedBlueGrey
                    )
                }
            }
        }
    }
}

@Composable
fun MyMilestonesScreen(
    learnedWordsCount: Int,
    daysActiveCount: Int,
    sentencesWrittenCount: Int,
    streakCount: Int
) {
    val scrollState = rememberScrollState()
    var showAllAchievements by rememberSaveable { mutableStateOf(false) }

    // Dynamic achievement calculations
    val achievements = remember(learnedWordsCount, streakCount, showAllAchievements) {
        getDisplayAchievements(learnedWordsCount, streakCount, showAllAchievements)
    }

    // Next milestone values for visual progress indicators
    val nextWordVal = remember(learnedWordsCount) {
        when {
            learnedWordsCount < 1 -> 1
            learnedWordsCount < 10 -> 10
            learnedWordsCount < 25 -> 25
            else -> if (learnedWordsCount % 50 == 0) learnedWordsCount + 50 else ((learnedWordsCount / 50) + 1) * 50
        }
    }

    val prevWordVal = remember(nextWordVal) {
        when (nextWordVal) {
            1 -> 0
            10 -> 1
            25 -> 10
            50 -> 25
            else -> nextWordVal - 50
        }
    }

    val wordProgressRange = nextWordVal - prevWordVal
    val wordProgressCurrent = learnedWordsCount - prevWordVal
    val wordProgressFraction = remember(wordProgressCurrent, wordProgressRange) {
        if (wordProgressRange > 0) {
            (wordProgressCurrent.toFloat() / wordProgressRange.toFloat()).coerceIn(0f, 1f)
        } else {
            0f
        }
    }

    // Dynamic counts
    val totalUnlockedCount = remember(learnedWordsCount, streakCount) {
        (if (learnedWordsCount >= 1) 1 else 0) +
        (if (learnedWordsCount >= 10) 1 else 0) +
        (if (learnedWordsCount >= 25) 1 else 0) +
        (learnedWordsCount / 50) +
        (if (streakCount >= 3) 1 else 0) +
        (streakCount / 7)
    }
    
    // We hardcode total pool capacity to 10 for display purposes in the MVP
    val totalPoolSize = 10

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // HEADER
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "My Milestones",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 32.sp,
                    color = NavyPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Track your progress and celebrate your wins.",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MutedBlueGrey
                )
            }
            
            // Trophy badge circle (top right)
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(SoftSkyBlue.copy(alpha = 0.6f))
                    .border(1.dp, PaleBlueBorder, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = WordMeIcons.Trophy,
                    contentDescription = "Trophy icon",
                    tint = AccentBlue,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // PROGRESS SUMMARY CARD (5 horizontal stats)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, PaleBlueBorder),
            colors = CardDefaults.cardColors(containerColor = PureWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatItem(
                    icon = WordMeIcons.Calendar,
                    value = "$daysActiveCount",
                    label = "Days Active",
                    iconBgColor = SoftSkyBlue.copy(alpha = 0.6f),
                    iconTint = AccentBlue
                )
                StatItem(
                    icon = WordMeIcons.Book,
                    value = "$learnedWordsCount",
                    label = "Words Learned",
                    iconBgColor = SoftSkyBlue.copy(alpha = 0.6f),
                    iconTint = AccentBlue
                )
                StatItem(
                    icon = WordMeIcons.Fire,
                    value = "🔥 $streakCount",
                    label = "Day Streak",
                    iconBgColor = Color(0xFFFFF3E0),
                    iconTint = OrangeStreak
                )
                StatItem(
                    icon = WordMeIcons.Pencil,
                    value = "$sentencesWrittenCount",
                    label = "Sentences",
                    iconBgColor = Color(0xFFF3E8FF),
                    iconTint = Color(0xFF8B5CF6)
                )
                StatItem(
                    icon = WordMeIcons.Star,
                    value = "9/10",
                    label = "Best Score",
                    iconBgColor = Color(0xFFFEF3C7),
                    iconTint = Color(0xFFF59E0B)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // LEARNING PROGRESS SECTION
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Learning Progress",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = NavyPrimary
            )
            Text(
                text = "See details >",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = AccentBlue,
                modifier = Modifier.clickable { /* interaction placeholder */ }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, PaleBlueBorder),
            colors = CardDefaults.cardColors(containerColor = PureWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Circular progress on left
                CircularProgressIndicatorWithText(
                    current = learnedWordsCount,
                    target = nextWordVal,
                    fraction = wordProgressFraction
                )
                
                // Details on right
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "$learnedWordsCount Words Learned",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = NavyPrimary
                    )
                    
                    Spacer(modifier = Modifier.height(2.dp))
                    
                    Text(
                        text = "You're on your way!",
                        fontSize = 13.sp,
                        color = MutedBlueGrey
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Horizontal progress bar + percentage text on right
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val pctInt = (wordProgressFraction * 100).toInt()
                        LinearProgressIndicator(
                            progress = { wordProgressFraction },
                            modifier = Modifier
                                .weight(1f)
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = AccentBlue,
                            trackColor = SoftSkyBlue.copy(alpha = 0.6f),
                            strokeCap = StrokeCap.Round
                        )
                        
                        Text(
                            text = "$pctInt%",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MutedBlueGrey
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Bullet item: Next milestone
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(AccentBlue)
                        )
                        Text(
                            text = "Next milestone: $nextWordVal Words",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = MutedBlueGrey
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ACHIEVEMENTS GRID SECTION
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Achievements",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = NavyPrimary
            )
            Text(
                text = "$totalUnlockedCount / $totalPoolSize Unlocked",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = AccentBlue
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Achievements 2-column grid
        val chunks = achievements.chunked(2)
        chunks.forEach { rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowItems.forEach { item ->
                    AchievementCard(
                        item = item,
                        modifier = Modifier.weight(1f)
                    )
                }
                if (rowItems.size == 1) {
                    Box(modifier = Modifier.weight(1f))
                }
            }
        }

        // View All Achievements Toggle Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            TextButton(
                onClick = { showAllAchievements = !showAllAchievements }
            ) {
                Text(
                    text = if (showAllAchievements) "Show Less" else "View All Achievements",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentBlue
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // MOTIVATIONAL BANNER AT BOTTOM
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, PaleBlueBorder),
            colors = CardDefaults.cardColors(containerColor = PureWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                            colors = listOf(SoftSkyBlue, PaleBlueBg)
                        )
                    )
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "🎉",
                        fontSize = 32.sp
                    )
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Keep it up!",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = NavyPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Consistency is the key to mastering new words.",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = MutedBlueGrey,
                            lineHeight = 16.sp
                        )
                    }
                    
                    Icon(
                        imageVector = WordMeIcons.Trophy,
                        contentDescription = null,
                        tint = AccentBlue.copy(alpha = 0.15f),
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}


// Bottom Navigation Bar Custom design using theme mappings
@Composable
fun BottomNavigationBar(
    selectedTab: String,
    onTabSelected: (String) -> Unit
) {
    NavigationBar(
        containerColor = PureWhite,
        tonalElevation = 0.dp,
        modifier = Modifier.border(width = 1.dp, color = PaleBlueBorder)
    ) {
        NavigationBarItem(
            selected = selectedTab == "home",
            onClick = { onTabSelected("home") },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home"
                )
            },
            label = {
                Text(
                    text = "Home",
                    fontWeight = if (selectedTab == "home") FontWeight.Bold else FontWeight.Medium
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AccentBlue,
                unselectedIconColor = MutedBlueGrey,
                selectedTextColor = AccentBlue,
                unselectedTextColor = MutedBlueGrey,
                indicatorColor = SoftSkyBlue.copy(alpha = 0.5f)
            )
        )

        NavigationBarItem(
            selected = selectedTab == "words",
            onClick = { onTabSelected("words") },
            icon = {
                Icon(
                    imageVector = WordMeIcons.Book,
                    contentDescription = "My Words"
                )
            },
            label = {
                Text(
                    text = "My Words",
                    fontWeight = if (selectedTab == "words") FontWeight.Bold else FontWeight.Medium
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AccentBlue,
                unselectedIconColor = MutedBlueGrey,
                selectedTextColor = AccentBlue,
                unselectedTextColor = MutedBlueGrey,
                indicatorColor = SoftSkyBlue.copy(alpha = 0.5f)
            )
        )

        NavigationBarItem(
            selected = selectedTab == "milestones",
            onClick = { onTabSelected("milestones") },
            icon = {
                Icon(
                    imageVector = WordMeIcons.Trophy,
                    contentDescription = "My Milestones"
                )
            },
            label = {
                Text(
                    text = "My Milestones",
                    fontWeight = if (selectedTab == "milestones") FontWeight.Bold else FontWeight.Medium
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AccentBlue,
                unselectedIconColor = MutedBlueGrey,
                selectedTextColor = AccentBlue,
                unselectedTextColor = MutedBlueGrey,
                indicatorColor = SoftSkyBlue.copy(alpha = 0.5f)
            )
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun WordLearningScreenPreview() {
    WordMeTheme {
        WordLearningScreen()
    }
}
