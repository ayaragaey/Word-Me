package com.example.wordme.screens.mywords

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordme.data.Word
import com.example.wordme.ui.WordMeIcons
import com.example.wordme.ui.theme.AccentBlue
import com.example.wordme.ui.theme.CardBackground
import com.example.wordme.ui.theme.MutedBlueGrey
import com.example.wordme.ui.theme.NavyPrimary
import com.example.wordme.ui.theme.PositiveFeedbackText
import com.example.wordme.ui.theme.SoftBlueBorder

@Composable
fun MyWordsScreen(
    learnedWords: List<Word>,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Header Section (Visually matching HomeScreen)
        Column {
            Text(
                text = "My Words",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = NavyPrimary,
                fontFamily = FontFamily.SansSerif
            )
            Text(
                text = "Words you've learned along the way.",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = MutedBlueGrey
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Words list
        if (learnedWords.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, SoftBlueBorder),
                colors = CardDefaults.cardColors(containerColor = CardBackground)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "No words learned yet.",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = NavyPrimary
                    )
                    Text(
                        text = "Write your first sentence on the Home screen to start learning!",
                        fontSize = 13.sp,
                        color = MutedBlueGrey,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        } else {
            // Reverse list to show recently learned words first
            learnedWords.asReversed().forEach { word ->
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
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Word info
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Learned",
                                    tint = PositiveFeedbackText,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = word.word.lowercase().replaceFirstChar { it.uppercase() },
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NavyPrimary
                                )
                                Text(
                                    text = word.type.lowercase(),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontStyle = FontStyle.Italic,
                                    color = AccentBlue
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = word.translation,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = AccentBlue
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Chevron indicator
                        Icon(
                            imageVector = WordMeIcons.ChevronRight,
                            contentDescription = "Details",
                            tint = MutedBlueGrey.copy(alpha = 0.5f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
