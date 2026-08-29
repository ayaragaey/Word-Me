package com.example.wordme.components

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.wordme.audio.SentenceSpeechRecognizer
import com.example.wordme.ui.WordMeIcons
import com.example.wordme.ui.theme.AccentBlue
import com.example.wordme.ui.theme.BrightBlue
import com.example.wordme.ui.theme.CardBackground
import com.example.wordme.ui.theme.LightBlue
import com.example.wordme.ui.theme.MutedBlueGrey
import com.example.wordme.ui.theme.NavyPrimary
import com.example.wordme.ui.theme.SoftBlueBorder
import java.util.Locale

@Composable
fun YourTurnCard(
    word: String,
    sentenceText: String,
    onSentenceChange: (String) -> Unit,
    onCheckSentence: () -> Unit,
    isCompleted: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val isButtonEnabled = sentenceText.isNotBlank() && !isCompleted

    var localError by remember { mutableStateOf<String?>(null) }

    val speechRecognizer = remember {
        SentenceSpeechRecognizer(
            context = context,
            onResult = { recognized ->
                onSentenceChange(recognized)
            },
            onPartialResult = { partial ->
                onSentenceChange(partial)
            }
        )
    }

    // Cancel recording if the current word changes
    LaunchedEffect(word) {
        if (speechRecognizer.isListening) {
            speechRecognizer.cancel()
        }
    }

    // Clean up recognizer resources on dispose
    DisposableEffect(Unit) {
        onDispose {
            speechRecognizer.destroy()
        }
    }

    // Fallback launcher for speech recognition intent
    val speechIntentLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val spokenText = result.data
                ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                ?.firstOrNull()
            if (!spokenText.isNullOrBlank()) {
                val formatted = spokenText.trim().replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.US) else it.toString()
                }
                onSentenceChange(formatted)
            }
        }
    }

    fun startListeningWithFallback() {
        localError = null
        if (speechRecognizer.isAvailable) {
            speechRecognizer.startListening()
        } else {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                )
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US.toLanguageTag())
                putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak your sentence using '$word'")
            }
            try {
                speechIntentLauncher.launch(intent)
            } catch (e: Exception) {
                localError = "Speech recognition is not available on this device."
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startListeningWithFallback()
        } else {
            localError = "Microphone permission is needed to record your sentence."
        }
    }

    fun onMicClicked() {
        focusManager.clearFocus()
        if (speechRecognizer.isListening) {
            speechRecognizer.stopListening()
            return
        }
        if (isCompleted) return

        val permissionCheck = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        )
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            startListeningWithFallback()
        } else {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    // Pulsing animation for active recording
    val infiniteTransition = rememberInfiniteTransition(label = "recording_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    val displayedError = localError ?: speechRecognizer.errorMessage

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, SoftBlueBorder),
        colors = CardDefaults.cardColors(containerColor = LightBlue),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            // Label
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = WordMeIcons.Pencil,
                    contentDescription = null,
                    tint = AccentBlue,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "YOUR TURN",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentBlue,
                    letterSpacing = 0.5.sp
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Heading (word highlighted in bright blue)
            Text(
                text = buildAnnotatedString {
                    append("Write or record your own sentence using ")
                    withStyle(SpanStyle(color = AccentBlue, fontWeight = FontWeight.Bold)) {
                        append(word.lowercase())
                    }
                    append(".")
                },
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = NavyPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Text Input Field with Mic Button in Trailing Icon
            OutlinedTextField(
                value = sentenceText,
                onValueChange = { if (!isCompleted && !speechRecognizer.isListening) onSentenceChange(it) },
                placeholder = {
                    Text(
                        text = if (speechRecognizer.isListening) "Listening... speak now" else "Write or record sentence here...",
                        color = MutedBlueGrey.copy(alpha = 0.5f),
                        fontSize = 14.sp
                    )
                },
                trailingIcon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        modifier = Modifier.padding(end = 4.dp)
                    ) {
                        // Clear button if there is text and not recording
                        if (sentenceText.isNotBlank() && !isCompleted && !speechRecognizer.isListening) {
                            IconButton(
                                onClick = { onSentenceChange("") },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Text(
                                    text = "✕",
                                    fontSize = 13.sp,
                                    color = MutedBlueGrey,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Microphone / Stop button
                        IconButton(
                            onClick = { onMicClicked() },
                            enabled = !isCompleted,
                            modifier = Modifier
                                .size(36.dp)
                                .background(
                                    color = if (speechRecognizer.isListening) Color(0xFFFFEBEE) else LightBlue,
                                    shape = CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = if (speechRecognizer.isListening) WordMeIcons.Stop else WordMeIcons.Microphone,
                                contentDescription = if (speechRecognizer.isListening) "Stop recording" else "Record sentence",
                                tint = if (speechRecognizer.isListening) Color(0xFFE53935) else AccentBlue,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp),
                shape = RoundedCornerShape(18.dp),
                enabled = !isCompleted,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (speechRecognizer.isListening) Color(0xFFE53935) else AccentBlue,
                    unfocusedBorderColor = if (speechRecognizer.isListening) Color(0xFFE53935) else SoftBlueBorder,
                    disabledBorderColor = SoftBlueBorder.copy(alpha = 0.5f),
                    focusedContainerColor = CardBackground,
                    unfocusedContainerColor = CardBackground,
                    disabledContainerColor = CardBackground.copy(alpha = 0.8f),
                    focusedTextColor = NavyPrimary,
                    unfocusedTextColor = NavyPrimary,
                    disabledTextColor = MutedBlueGrey
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
            )

            // Active Recording Status Bar
            AnimatedVisibility(visible = speechRecognizer.isListening) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF5F5)),
                    border = BorderStroke(1.dp, Color(0xFFFFCDD2))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .scale(pulseScale)
                                    .background(Color(0xFFE53935), CircleShape)
                            )
                            Text(
                                text = "Listening... Speak your sentence",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFC62828)
                            )
                        }
                        Text(
                            text = "Done",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE53935),
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .clickable { speechRecognizer.stopListening() }
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            // Error or Permission Prompt
            AnimatedVisibility(visible = displayedError != null && !speechRecognizer.isListening) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp, start = 4.dp, end = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = displayedError ?: "",
                        fontSize = 11.sp,
                        color = Color(0xFFD32F2F),
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "✕",
                        fontSize = 12.sp,
                        color = MutedBlueGrey,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable {
                                localError = null
                                speechRecognizer.clearError()
                            }
                            .padding(4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Check Sentence Button
            Button(
                onClick = {
                    focusManager.clearFocus()
                    if (speechRecognizer.isListening) {
                        speechRecognizer.stopListening()
                    }
                    onCheckSentence()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentBlue,
                    contentColor = CardBackground,
                    disabledContainerColor = SoftBlueBorder.copy(alpha = 0.6f),
                    disabledContentColor = MutedBlueGrey.copy(alpha = 0.6f)
                ),
                enabled = isButtonEnabled
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "CHECK MY SENTENCE",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "→",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
