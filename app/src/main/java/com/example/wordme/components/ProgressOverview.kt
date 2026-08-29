package com.example.wordme.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.wordme.ui.WordMeIcons
import com.example.wordme.ui.theme.AccentBlue
import com.example.wordme.ui.theme.CardBackground
import com.example.wordme.ui.theme.MutedBlueGrey
import com.example.wordme.ui.theme.NavyPrimary
import com.example.wordme.ui.theme.SoftBlueBorder
import com.example.wordme.ui.theme.StreakAccent

@Composable
fun ProgressOverview(
    days: Int,
    words: Int,
    streak: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(), // Removed fixed height to let layout wrap compactly
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, SoftBlueBorder),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp), // Thinner vertical padding
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Days active
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(0.dp) // Tightest spacing
            ) {
                Icon(
                    imageVector = WordMeIcons.Calendar,
                    contentDescription = null,
                    tint = AccentBlue,
                    modifier = Modifier.size(16.dp) // Smaller icon size
                )
                Text(
                    text = "$days",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = NavyPrimary
                )
                Text(
                    text = "Days",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MutedBlueGrey
                )
            }

            // Divider 1
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(14.dp) // Thinner divider height
                    .background(SoftBlueBorder)
            )

            // Words learned
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                Icon(
                    imageVector = WordMeIcons.Book,
                    contentDescription = null,
                    tint = AccentBlue,
                    modifier = Modifier.size(16.dp) // Smaller icon size
                )
                Text(
                    text = "$words",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = NavyPrimary
                )
                Text(
                    text = "Words",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MutedBlueGrey
                )
            }

            // Divider 2
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(14.dp) // Thinner divider height
                    .background(SoftBlueBorder)
            )

            // Streak
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                Icon(
                    imageVector = WordMeIcons.Fire,
                    contentDescription = null,
                    tint = StreakAccent,
                    modifier = Modifier.size(16.dp) // Smaller icon size
                )
                Text(
                    text = "$streak",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = NavyPrimary
                )
                Text(
                    text = "Day Streak",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MutedBlueGrey
                )
            }
        }
    }
}
