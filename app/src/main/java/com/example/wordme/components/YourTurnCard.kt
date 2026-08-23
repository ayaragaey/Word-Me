package com.example.wordme.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordme.ui.WordMeIcons
import com.example.wordme.ui.theme.AccentBlue
import com.example.wordme.ui.theme.BrightBlue
import com.example.wordme.ui.theme.CardBackground
import com.example.wordme.ui.theme.LightBlue
import com.example.wordme.ui.theme.MutedBlueGrey
import com.example.wordme.ui.theme.NavyPrimary
import com.example.wordme.ui.theme.SoftBlueBorder

@Composable
fun YourTurnCard(
    word: String,
    sentenceText: String,
    onSentenceChange: (String) -> Unit,
    onCheckSentence: () -> Unit,
    isCompleted: Boolean,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val isButtonEnabled = sentenceText.isNotBlank() && !isCompleted

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, SoftBlueBorder),
        colors = CardDefaults.cardColors(containerColor = LightBlue), // Soft blue-tinted card background
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
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

            Spacer(modifier = Modifier.height(8.dp))

            // Heading (word highlighted in bright blue)
            Text(
                text = buildAnnotatedString {
                    append("Write your own sentence using ")
                    withStyle(SpanStyle(color = AccentBlue, fontWeight = FontWeight.Bold)) {
                        append(word.lowercase())
                    }
                    append(".")
                },
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = NavyPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Text Input Field with White Background and 18dp Corner Radius
            OutlinedTextField(
                value = sentenceText,
                onValueChange = { if (!isCompleted) onSentenceChange(it) },
                placeholder = {
                    Text(
                        text = "Write your sentence here...",
                        color = MutedBlueGrey.copy(alpha = 0.5f),
                        fontSize = 14.sp
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 96.dp),
                shape = RoundedCornerShape(18.dp), // Premium 18dp radius
                enabled = !isCompleted,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AccentBlue,
                    unfocusedBorderColor = SoftBlueBorder,
                    disabledBorderColor = SoftBlueBorder.copy(alpha = 0.5f),
                    focusedContainerColor = CardBackground, // White input background
                    unfocusedContainerColor = CardBackground,
                    disabledContainerColor = CardBackground.copy(alpha = 0.8f),
                    focusedTextColor = NavyPrimary,
                    unfocusedTextColor = NavyPrimary,
                    disabledTextColor = MutedBlueGrey
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Check Sentence Button with 16dp rounded corners and distinct state transitions
            Button(
                onClick = {
                    focusManager.clearFocus()
                    onCheckSentence()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(16.dp), // Premium 16dp corner radius
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentBlue, // Strong bright blue
                    contentColor = CardBackground, // White text
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
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "→",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
