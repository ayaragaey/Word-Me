package com.example.wordme.screens.mywords

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordme.data.Word
import com.example.wordme.ui.theme.AccentBlue
import com.example.wordme.ui.theme.CardBackground
import com.example.wordme.ui.theme.DarkBlue
import com.example.wordme.ui.theme.LightBlue
import com.example.wordme.ui.theme.MutedBlueGrey
import com.example.wordme.ui.theme.NavyPrimary
import com.example.wordme.ui.theme.SoftBlueBorder

enum class SortOption(val displayName: String) {
    DATE_LEARNED("Date Learned"),
    ALPHA_AZ("Alphabetical A-Z"),
    ALPHA_ZA("Alphabetical Z-A")
}

@Composable
fun MyWordsScreen(
    learnedWords: List<Word>,
    userName: String?,
    onNameChanged: (String) -> Unit,
    onRehearseWord: (Word) -> Unit,
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedSort by remember { mutableStateOf(SortOption.DATE_LEARNED) }
    var dropdownExpanded by remember { mutableStateOf(false) }

    // Filter words
    val filteredWords = remember(learnedWords, searchQuery) {
        if (searchQuery.isBlank()) {
            learnedWords
        } else {
            val q = searchQuery.trim().lowercase()
            learnedWords.filter { word ->
                word.word.contains(q, ignoreCase = true) ||
                word.translation.contains(q, ignoreCase = true) ||
                word.pronunciation.contains(q, ignoreCase = true)
            }
        }
    }

    // Sort words
    val sortedWords = remember(filteredWords, selectedSort) {
        when (selectedSort) {
            SortOption.DATE_LEARNED -> {
                // Sort by learnedDate descending, then by id descending
                filteredWords.sortedWith(
                    compareByDescending<Word> { it.learnedDate ?: "" }
                        .thenByDescending { it.id }
                )
            }
            SortOption.ALPHA_AZ -> {
                filteredWords.sortedBy { it.word.lowercase() }
            }
            SortOption.ALPHA_ZA -> {
                filteredWords.sortedByDescending { it.word.lowercase() }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Header Section
        Column {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color(0xFF0D2A59), fontWeight = FontWeight.Bold)) {
                        append("My ")
                    }
                    withStyle(style = SpanStyle(color = Color(0xFF2784F5), fontWeight = FontWeight.Bold)) {
                        append("Words")
                    }
                },
                fontSize = 32.sp,
                fontFamily = FontFamily.Serif
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Your learned vocabulary",
                fontSize = 15.sp,
                color = MutedBlueGrey
            )
            Spacer(modifier = Modifier.height(10.dp))
            
            // Badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(LightBlue)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "📚 ${learnedWords.size} words collected",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkBlue
                )
            }
        }

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = {
                Text(
                    text = "Search words...",
                    color = MutedBlueGrey.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = MutedBlueGrey,
                    modifier = Modifier.size(20.dp)
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear search",
                        tint = MutedBlueGrey,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { searchQuery = "" }
                    )
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AccentBlue,
                unfocusedBorderColor = SoftBlueBorder,
                focusedContainerColor = CardBackground,
                unfocusedContainerColor = CardBackground,
                focusedTextColor = NavyPrimary,
                unfocusedTextColor = NavyPrimary
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // Sort Dropdown button
        Box(
            modifier = Modifier.wrapContentSize()
        ) {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { dropdownExpanded = true }
                    .background(Color.White)
                    .border(1.dp, SoftBlueBorder, RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Sort",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1784F5)
                )
                Text(
                    text = "▼",
                    fontSize = 10.sp,
                    color = Color(0xFF1784F5)
                )
            }

            DropdownMenu(
                expanded = dropdownExpanded,
                onDismissRequest = { dropdownExpanded = false },
                modifier = Modifier.background(CardBackground)
            ) {
                SortOption.values().forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (selectedSort == option) {
                                    Text(text = "✓", color = AccentBlue, fontWeight = FontWeight.Bold)
                                } else {
                                    Spacer(modifier = Modifier.width(12.dp))
                                }
                                Text(
                                    text = option.displayName,
                                    color = if (selectedSort == option) AccentBlue else NavyPrimary,
                                    fontWeight = if (selectedSort == option) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        },
                        onClick = {
                            selectedSort = option
                            dropdownExpanded = false
                        }
                    )
                }
            }
        }

        // Vocabulary Entries List or Empty State
        if (learnedWords.isEmpty()) {
            // Overall empty state
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "📚",
                    fontSize = 48.sp
                )
                Text(
                    text = "Your word collection is empty.",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = NavyPrimary,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Start learning words and they\nwill appear here.",
                    fontSize = 14.sp,
                    color = MutedBlueGrey,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onNavigateToHome,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentBlue),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "WORD ME! ✨",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        } else if (sortedWords.isEmpty()) {
            // Search empty state
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "No results found for \"$searchQuery\"",
                    fontSize = 14.sp,
                    color = MutedBlueGrey,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            // Entries
            if (selectedSort == SortOption.DATE_LEARNED) {
                // Grouped by Date
                val grouped = sortedWords.groupBy { getGroupHeader(it.learnedDate ?: "") }
                grouped.forEach { (header, wordsInGroup) ->
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = header,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MutedBlueGrey,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        HorizontalDivider(color = SoftBlueBorder.copy(alpha = 0.5f), thickness = 1.dp)

                        wordsInGroup.forEach { word ->
                            WordEntryItem(
                                word = word,
                                onRehearseClick = onRehearseWord
                            )
                            HorizontalDivider(color = SoftBlueBorder.copy(alpha = 0.3f), thickness = 0.5.dp)
                        }
                    }
                }
            } else {
                // Alphabetical List (Not grouped)
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    sortedWords.forEach { word ->
                        WordEntryItem(
                            word = word,
                            onRehearseClick = onRehearseWord
                        )
                        HorizontalDivider(color = SoftBlueBorder.copy(alpha = 0.3f), thickness = 0.5.dp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun WordEntryItem(
    word: Word,
    onRehearseClick: (Word) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = word.word.uppercase(),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1784F5)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Learned on: ${formatLearnedDate(word.learnedDate ?: "")}",
                fontSize = 13.sp,
                color = MutedBlueGrey
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        OutlinedButton(
            onClick = { onRehearseClick(word) },
            border = BorderStroke(1.dp, Color(0xFF1784F5)),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF1784F5)),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
            modifier = Modifier.height(32.dp)
        ) {
            Text(
                text = "↻ Rehearse",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

fun formatLearnedDate(dateStr: String): String {
    return try {
        val date = java.time.LocalDate.parse(dateStr)
        val formatter = java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy", java.util.Locale.US)
        date.format(formatter)
    } catch (e: Exception) {
        dateStr
    }
}

fun getGroupHeader(dateStr: String): String {
    return try {
        val date = java.time.LocalDate.parse(dateStr)
        val today = java.time.LocalDate.now()
        when {
            date == today -> "TODAY"
            date == today.minusDays(1) -> "YESTERDAY"
            else -> {
                val formatter = java.time.format.DateTimeFormatter.ofPattern("MMMM yyyy", java.util.Locale.US)
                date.format(formatter).uppercase()
            }
        }
    } catch (e: Exception) {
        "OLDER"
    }
}
