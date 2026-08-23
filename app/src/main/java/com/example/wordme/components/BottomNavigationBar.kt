package com.example.wordme.components

import androidx.compose.foundation.border
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordme.ui.WordMeIcons
import com.example.wordme.ui.theme.AccentBlue
import com.example.wordme.ui.theme.CardBackground
import com.example.wordme.ui.theme.LightBlue
import com.example.wordme.ui.theme.MutedBlueGrey
import com.example.wordme.ui.theme.SoftBlueBorder

@Composable
fun BottomNavigationBar(
    selectedTab: String,
    onTabSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        containerColor = CardBackground,
        tonalElevation = 0.dp,
        modifier = modifier
            .border(width = 1.dp, color = SoftBlueBorder)
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
                    fontSize = 11.sp,
                    fontWeight = if (selectedTab == "home") FontWeight.Bold else FontWeight.Medium
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AccentBlue,
                unselectedIconColor = MutedBlueGrey,
                selectedTextColor = AccentBlue,
                unselectedTextColor = MutedBlueGrey,
                indicatorColor = LightBlue // Pale blue active background/pill
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
                    fontSize = 11.sp,
                    fontWeight = if (selectedTab == "words") FontWeight.Bold else FontWeight.Medium
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AccentBlue,
                unselectedIconColor = MutedBlueGrey,
                selectedTextColor = AccentBlue,
                unselectedTextColor = MutedBlueGrey,
                indicatorColor = LightBlue // Pale blue active background/pill
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
                    fontSize = 11.sp,
                    fontWeight = if (selectedTab == "milestones") FontWeight.Bold else FontWeight.Medium
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AccentBlue,
                unselectedIconColor = MutedBlueGrey,
                selectedTextColor = AccentBlue,
                unselectedTextColor = MutedBlueGrey,
                indicatorColor = LightBlue // Pale blue active background/pill
            )
        )
    }
}
